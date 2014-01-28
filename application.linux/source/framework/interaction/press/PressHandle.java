package framework.interaction.press;

import framework.BaseMainView;
import sun.misc.Perf.GetPerfAction;

public class PressHandle {

	private int _targetId;
	private float _position;
	private float _startPos;
	private Boolean _isPressing = false;

	public PressHandle(int targetId, float startPos) {
		_targetId = targetId;
		_startPos = _position = startPos;
	}

	public float getPressure() {
		float pressure = (_position - _startPos) / (BaseMainView.PRESS_POS_EXTENTS - _startPos);
		return Math.min(1.0f, Math.max(0.0f, pressure));
	}

	public Boolean update(float position) {

		_position = position;
		if (_position < _startPos)
			_startPos = _position;

		_isPressing = getPressure() > BaseMainView.PRESS_MAX_PRESSURE;
		// update scale

		return _isPressing;
	}

	public void setPressTendency(Boolean value) {
		_isPressing = value;
	}

	public Boolean getPressTendency() {
		return _isPressing;
	}

	public int get_targetID() {
		return _targetId;
	}
}
