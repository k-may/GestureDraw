package application.interaction;

import static application.interaction.InteractionHelper.ease;
import processing.core.PVector;
import application.interaction.processing.RegionInputData;
import application.view.MainView;

public abstract class KinectInputData extends RegionInputData {

	public KinectInputData(ICursorHandler cursorHandler){
		this(cursorHandler, null);
	}
	public KinectInputData(ICursorHandler cursorHandler,
			IRangeHandler rangeHandler) {
		super(cursorHandler, rangeHandler);
	}

	@Override
	protected void addPosition(PVector pos) {
		// check for corrupt data
		if (pos == null || isPositionBad(pos)) {
			invalidDataCount++;
			return;
		} else {
			//System.out.println("position good : " + pos);
			invalidDataCount = 0;
			_rawPosition = pos;
			_positions.add(_rawPosition);
			updateRanges(_rawPosition);
		}

		_sampleCount++;
	}

	@Override
	public PVector getPosition() {
		return _cursorPos;
	}

	private Boolean isPositionBad(PVector pos) {
		double distance = 0;
		if (_rawPosition != null)
			distance = Math.hypot(pos.x - _rawPosition.x, pos.y
					- _rawPosition.y);

		//System.out.println("distance : " + distance);
		
		Boolean isBad = distance == 0.0 || distance > 150;
		
		return isBad;// || distance > 150;//MainView.INVALID_DISTANCE;
	}

	public Boolean isDataValid() {

		if (!updated)
			invalidDataCount++;

		Boolean isValid = invalidDataCount < MainView.MAX_INVALID;
		return isValid;
	}

	public Boolean isReady() {
		return _sampleCount > MainView.MIN_SAMPLES;
	}

	protected void updateRanges(PVector pos) {

		if (minX > pos.x) {
			minX = ease(minX, pos.x, MainView.RANGE_DAMPENING);//_dampening);// minX + (pos.x - minX)*0.1f;
			maxX = minX + MainView.XRANGE;
		}

		if (maxX < pos.x) {
			maxX = ease(maxX, pos.x, MainView.RANGE_DAMPENING);// maxX + (pos.x - maxX)*0.1f;
			minX = maxX - MainView.XRANGE;
		}

		if (minY > pos.y) {
			minY = ease(minY, pos.y, MainView.RANGE_DAMPENING);// minY pos.y;
			maxY = minY + MainView.YRANGE;
		}

		if (maxY < pos.y) {
			maxY = ease(maxY, pos.y, MainView.RANGE_DAMPENING);
			minY = maxY - MainView.YRANGE;
		}
		if (minZ > pos.z) {
			// System.out.println("--->>>update minz!");
			minZ = (int) ease(minZ, pos.z, MainView.RANGE_DAMPENING);
			maxZ = minZ + MainView.ZRANGE;
		}

		if (maxZ < pos.z) {
			maxZ = (int) ease(maxZ, pos.z, MainView.RANGE_DAMPENING);
			minZ = maxZ - MainView.ZRANGE;
		}
	}

	@Override
	protected void resetRange(PVector rawPos, PVector cursorPos) {

		minX = rawPos.x - MainView.XRANGE * cursorPos.x;// / 2;
		maxX = minX + MainView.XRANGE;

		minY = rawPos.y - MainView.YRANGE * cursorPos.y; // / 2;
		maxY = minY + MainView.YRANGE;
	}

}
