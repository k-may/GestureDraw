package application.interaction;

import application.interaction.processing.RegionInputData;

import processing.core.PVector;

public class KinectHandData extends RegionInputData {

	private static final int MAX_INVALID = 15;
	private int _invalidDataCount = 0;
	private PVector lastPosition;
	private static final int MAX_SAMPLES = 5;

	public KinectHandData(int id) {
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
	protected void init(PVector pos) {
		super.init(pos);

		minX = pos.x - XRANGE / 2;
		maxX = minX + XRANGE;

		minY = pos.y - YRANGE / 2;
		maxY = minY + YRANGE;

		minZ = pos.z - ZRANGE / 2;
		maxZ = minZ + XRANGE;

		_sampleCount++;
	}

	@Override
	protected void addPosition(PVector pos) {
		//check for corrupt data
		if (isPositionBad(pos)) {
			_invalidDataCount++;
			return;
		} else {
			_invalidDataCount = 0;
			_position = pos;
			_positions.add(_position);
			updateRanges(_position);
		}

		_sampleCount++;
	}

	protected PVector getMappedPosition() {
		float x = getMapped(_position.x, minX, XRANGE);
		float y = getMapped(_position.y, minY, YRANGE);
		float z = 1 - getMapped(_position.z, minZ, ZRANGE);
		return new PVector(x, y, z);
	}

	private float getMapped(float val, float min, float range) {
		return Math.max(0.0f, Math.min(1.0f, Math.abs(val - min) / range));
	}

	private Boolean isPositionBad(PVector pos) {
		double distance = 0;
		if (lastPosition != null)
			distance = Math.hypot(pos.x - lastPosition.x, pos.y
					- lastPosition.y);

		lastPosition = pos;
		return distance > 10;
	}

	@Override
	public PVector getPosition() {
		return getMappedPosition();
	}

}
