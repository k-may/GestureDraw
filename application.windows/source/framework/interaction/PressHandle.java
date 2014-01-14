package framework.interaction;

import processing.core.PVector;

public class PressHandle {

	public static float PRESS_EXTENTS = 0.7F;
	private int _targetId;
	private PVector _targetPos;
	private PVector _tendency;
	private PVector _position;
	private PVector _startPos;
	private Boolean _isPressing = false;

	public PressHandle(int targetId, PVector targetPos, PVector startPos) {
		_targetId = targetId;
		_targetPos = targetPos;
		_startPos = startPos;
		_position = new PVector(_startPos.x, _startPos.y);
	}

	public float getPressure() {
		float pressure = (_position.z - _startPos.z)
				/ (PRESS_EXTENTS - _startPos.z);
		return Math.min(1.0f, Math.max(0.0f, pressure));
	}

	public void update(PVector tendency, PVector position) {
		_tendency = tendency;
		_position = PVector.lerp(_position, position, 0.1f);// position;

		// update scale
		if (_position.z < _startPos.z)
			_startPos.z = _position.z;

	}

	public void setPressTendency(Boolean value) {
		_isPressing = value;
	}

	public Boolean getPressTendency() {
		return _isPressing;
	}

	public PVector getUpdatedPosition() {
		float range = getPressure();
		_position = PVector.lerp(_position, _targetPos, 0.5f * range);// range);
		return _position;
	}

	public int get_targetID() {
		return _targetId;
	}
}
