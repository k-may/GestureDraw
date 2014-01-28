package application.interaction;

import application.interaction.processing.RegionInputData;
import application.view.MainView;

import processing.core.PVector;

public class KinectInputData extends RegionInputData {

	private static final int MAX_INVALID = 15;
	private int _invalidDataCount = 0;
	private PVector lastPosition;
	private static final int MAX_SAMPLES = 25;

	public KinectInputData(int id) {
		_id = id;
	}

	public Boolean isUpdated() {
		_invalidDataCount = _isUpdated ? _invalidDataCount
				: _invalidDataCount + 1;
		Boolean updated = _invalidDataCount < MAX_INVALID;
		_isUpdated = false;
		return updated;
	}

	public Boolean isReady() {
		return _sampleCount > MAX_SAMPLES;
	}

	@Override
	protected void addPosition(PVector pos) {
		// check for corrupt data
		if (isPositionBad(pos)) {
			_invalidDataCount++;
			return;
		} else {
			_invalidDataCount = 0;
			_rawPosition = pos;
			_positions.add(_rawPosition);
			updateRanges(_rawPosition);
		}

		_sampleCount++;
	}

	protected void updateRanges(PVector pos) {

		if (minX > pos.x) {
			minX = ease(minX, pos.x, _dampening);// minX + (pos.x - minX)*0.1f;
			maxX = minX + MainView.XRANGE;
		}

		if (maxX < pos.x) {
			maxX = ease(maxX, pos.x, _dampening);// maxX + (pos.x - maxX)*0.1f;
			minX = maxX - MainView.XRANGE;
		}

		if (minY > pos.y) {
			minY = ease(minY, pos.y, _dampening);// minY pos.y;
			maxY = minY + MainView.YRANGE;
		}

		if (maxY < pos.y) {
			maxY = ease(maxY, pos.y, _dampening);
			minY = maxY - MainView.YRANGE;
		}
		if (minZ > pos.z) {
			//System.out.println("--->>>update minz!");
			minZ = (int) ease(minZ, pos.z, _dampening);
			maxZ = minZ + MainView.ZRANGE;
		}

		if (maxZ < pos.z) {
			maxZ = (int) ease(maxZ, pos.z, _dampening);
			minZ = maxZ - MainView.ZRANGE;
		}
	}

	@Override
	protected void resetRange() {

		minX = _rawPosition.x - MainView.XRANGE * _cursorPos.x;// / 2;
		maxX = minX + MainView.XRANGE;

		minY = _rawPosition.y - MainView.YRANGE * _cursorPos.y; // / 2;
		maxY = minY + MainView.YRANGE;
	}


	private Boolean isPositionBad(PVector pos) {
		double distance = 0;
		if (lastPosition != null)
			distance = Math.hypot(pos.x - lastPosition.x, pos.y
					- lastPosition.y);

		lastPosition = pos;
		return distance > 10;
	}

}
