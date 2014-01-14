package application.view;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PVector;
import framework.view.IUserMenuView;
import framework.view.View;

public class UserMenuView extends View implements IUserMenuView {

	private HashMap<Integer, RegionData> _regions;
	private int _numRegions = 0;

	public UserMenuView() {
		_width = 200;
		_regions = new HashMap<Integer, RegionData>();
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
		p.ellipseMode(PApplet.CORNER);

		RegionData[] regions = getOrderedRegions();
		for (int i = 0; i < _numRegions; i++) {
			p.fill(regions[i].color);
			p.ellipse(x, y, radius, radius);
			x += horSpacing;
		}

		super.draw(p);
	}

	private RegionData[] getOrderedRegions(){
		RegionData[] regions = (RegionData[]) _regions.values().toArray();
		
		Arrays.sort(regions, new Comparator<RegionData>(){

			@Override
			public int compare(RegionData arg0, RegionData arg1) {
				if(arg0.position < arg1.position)
					return 1;
				else
					return -1;	
			}
		});
		return regions;
	}
	
	@Override
	public void updateRegion(int id, float position, int color) {
		if (_regions.containsKey(id)) {
			RegionData region = _regions.get(id);
			region.color = color;
			region.position = position;
		} else
			_regions.put(id, new RegionData(color, position));

		_numRegions = _regions.values().size();
	}

	@Override
	public void removeRegion(int id) {
		if (_regions.containsKey(id))
			_regions.remove(id);

		_numRegions = _regions.values().size();
	}
}
