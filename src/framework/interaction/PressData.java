package framework.interaction;

public class PressData {

	private Boolean _isPressing = false;
	private float _x;
	private float _y;
	private float _pressPressure = 0.0f;
	private Boolean _isPressTarget = false;

	public PressData(float x, float y, float pressPressure, Boolean isPressTarget,Boolean isPressing) {
		_isPressing = isPressing;
		_x = x;
		_y = y;
		_pressPressure = pressPressure;
		_isPressTarget = isPressTarget;
	}

	public float getPressPressure() {
		return _pressPressure;
	}

	public Boolean get_isPressing() {
		return _isPressing;
	}

	public float get_x() {
		return _x;
	}

	public float get_y() {
		return _y;
	}

	public Boolean get_isPressTarget() {
		return _isPressTarget;
	}
}
