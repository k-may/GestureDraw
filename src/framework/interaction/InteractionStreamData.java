package framework.interaction;

import framework.pressing.PressStateData;

public class InteractionStreamData {

	private float _z;
	private float _x;
	private float _y;
	private int _userId;
	private HandType _handType;
	private InteractionType _type;
	private Boolean _isOverPressTarget = false;
	private Boolean _isPressing = false;
	private float _pressPressure = 0.0f;
	private Boolean _isOverHoverTarget = false;
	
	public InteractionStreamData(float x, float y, float z, int userId,
			InteractionType type) {
		_userId = userId;
		_z = (z);
		_x = (x);
		_y = (y);
		_type = (type);
	}

	public InteractionStreamData(float x, float y, float z, int userId,
			InteractionType type, Boolean isOverHoverTarget, Boolean isOverPressTarget,Boolean isPressing,float pressPressure) {
		this(x, y, z, userId, type);
		_isOverHoverTarget = isOverHoverTarget;
		_isOverPressTarget = isOverPressTarget;
		_isPressing = isPressing;
		_pressPressure = pressPressure;
	}

	public float get_pressPressure() {
		return _pressPressure;
	}

	public Boolean isPressing() {
		return _isPressing;
	}

	public float get_z() {
		return _z;
	}

	public float get_x() {
		return _x;
	}

	public float get_y() {
		return _y;
	}

	public int get_userId() {
		return _userId;
	}

	public InteractionType get_type() {
		return _type;
	}

	public Boolean isOverPressTarget() {
		return _isOverPressTarget;
	}

	public Boolean isOverHoverTarget() {
		return _isOverHoverTarget ;
	}

	public Boolean isOverTarget() {
		// TODO Auto-generated method stub
		return _isOverHoverTarget || _isOverPressTarget;
	}

}
