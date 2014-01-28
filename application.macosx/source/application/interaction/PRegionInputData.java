package application.interaction;

import processing.core.PVector;
import application.interaction.processing.RegionInputData;
import application.view.MainView;

public class PRegionInputData extends RegionInputData {

	@Override
	protected void resetRange() {
		minX = 0;// / 2;
		maxX = MainView.XRANGE;

		minY = 0; // / 2;
		maxY = MainView.YRANGE;

	}

	@Override
	protected void addPosition(PVector pos) {
		_rawPosition.lerp(pos, 0.1f);
		_positions.add(_rawPosition);
		_sampleCount++;
	}


}
