package application.interaction;

import processing.core.PVector;
import application.interaction.gestTrackOSC.domain.DomainCursorHandler;
import application.interaction.processing.RegionInputData;
import application.view.MainView;

public class PRegionInputData extends RegionInputData {

	public PRegionInputData() {
		super(new DomainCursorHandler());
	}

	@Override
	protected void resetRange(PVector rawPos, PVector cursorPos){
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

	@Override
	public PVector getPosition() {
		return _rawPosition;
	}

}
