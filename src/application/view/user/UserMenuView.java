package application.view.user;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;


import processing.core.PApplet;
import processing.core.PVector;
import framework.view.IUserMenuView;
import framework.view.View;

public class UserMenuView extends View implements IUserMenuView {

	private HashMap<Integer, UserMenuData> _domains;
	private int _numRegions = 0;

	public UserMenuView() {
		_width = 200;
		_domains = new HashMap<Integer, UserMenuData>();
	}

	@Override
	public void draw(PApplet p) {

		PVector pos = this.get_absPos();
		int horSpacing = 20;
		int radius = 15;

		float x = pos.x + _width / 2 - (_numRegions * horSpacing) / 2 - radius
				/ 2;
		int y = (int) (pos.y + 20);

		p.stroke(150);
		p.strokeWeight(1);
		//p.ellipseMode(PApplet.CORNER);

		UserMenuData[] regions = getOrderedRegions();
		for (int i = 0; i < _numRegions; i++) {
			p.fill(regions[i].color);
			p.ellipse(x, y, radius, radius);
			x += horSpacing;
		}

		super.draw(p);
	}

	private UserMenuData[] getOrderedRegions() {

		UserMenuData[] regions = new UserMenuData[_numRegions];
		int count = 0;
		for (UserMenuData data : _domains.values()) {
			regions[count] = data;
			count++;
		}

		Arrays.sort(regions, new Comparator<UserMenuData>() {
			@Override
			public int compare(UserMenuData arg0, UserMenuData arg1) {
				if (arg0.position < arg1.position)
					return 1;
				else
					return -1;
			}
		});
		return regions;
	}

	@Override
	public void updateDomain(int id, float position, int color) {
		if (_domains.containsKey(id)) {
			UserMenuData region = _domains.get(id);
			region.color = color;
			region.position = position;
		} else
			_domains.put(id, new UserMenuData(color, position));

		_numRegions = _domains.values().size();
	}

	@Override
	public void removeDomain(int id) {
		if (_domains.containsKey(id))
			_domains.remove(id);

		_numRegions = _domains.values().size();
	}
}
