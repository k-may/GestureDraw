package application.view;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PVector;
import framework.view.IDomainView;
import framework.view.View;

public class DomainView extends View implements IDomainView {

	private HashMap<Integer, DomainData> _domains;
	private int _numRegions = 0;

	public DomainView() {
		_width = 200;
		_domains = new HashMap<Integer, DomainData>();
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

		DomainData[] regions = getOrderedRegions();
		for (int i = 0; i < _numRegions; i++) {
			p.fill(regions[i].color);
			p.ellipse(x, y, radius, radius);
			x += horSpacing;
		}

		super.draw(p);
	}

	private DomainData[] getOrderedRegions() {

		DomainData[] regions = new DomainData[_numRegions];
		int count = 0;
		for (DomainData data : _domains.values()) {
			regions[count] = data;
			count++;
		}

		Arrays.sort(regions, new Comparator<DomainData>() {
			@Override
			public int compare(DomainData arg0, DomainData arg1) {
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
			DomainData region = _domains.get(id);
			region.color = color;
			region.position = position;
		} else
			_domains.put(id, new DomainData(color, position));

		_numRegions = _domains.values().size();
	}

	@Override
	public void removeDomain(int id) {
		if (_domains.containsKey(id))
			_domains.remove(id);

		_numRegions = _domains.values().size();
	}
}
