package framework.data;

import framework.interaction.InteractionStreamData;
import framework.interaction.Types.HandType;
import framework.pressing.PressState;
import framework.pressing.PressStateData;

public class UserData {
	private int _id;
	private int _color = 0xffffffff;
	private int _previousColor;
	private HandType _primaryHand;
	private Boolean _updated = false;
	private float _localX;
	private float _localY;

	private PressStateData _pressStateData;
	private InteractionStreamData _streamData;

	public UserData(int id) {
		_id = id;
	}

	public int get_id() {
		return _id;
	}

	public void setColor(int color) {
		_previousColor = _color;
		_color = color;
	}

	public int getColor() {
		return _color;
	}

	public void setPrimaryHand(HandType type) {
		_primaryHand = type;
	}

	public Boolean isUpdated() {
		return _updated;
	}

	public void set_updated(Boolean _updated) {
		this._updated = _updated;
	}

	public float get_localX() {
		return _localX;
	}

	public void set_localX(float _localX) {
		this._localX = _localX;
	}

	public float get_localY() {
		return _localY;
	}

	public void set_localY(float _localY) {
		this._localY = _localY;
	}

	public Boolean isPressing() {
		return _streamData.isPressing(); //_isPressing;
	}

	public Boolean isOverPressTarget() {
		return _streamData.isOverPressTarget();//_isOverPressTarget;
	}

	public float getPressPressure(){
		return _pressStateData.get_pressure();
	}
	
	public PressState getPressState(){
		return _pressStateData.get_state();
	}

	public void set_pressStateData(PressStateData _pressStateData) {
		this._pressStateData = _pressStateData;
	}
	
	public float get_pressPressure(){
		return isOverPressTarget() ? _streamData.get_pressPressure() : 0.0f;//_pressure : 0.0f;
	}
	
	public float get_strokePressure(){
		return _pressStateData.get_state() == PressState.Drawing ? 
				_pressStateData.get_pressure() : 0.0f;
	}
	
	public float get_navigationPressure(){
		return _pressStateData.get_state() == PressState.Start ? 
				_pressStateData.get_pressure() : 0.0f;
	}

	public Boolean isOverButton(){
		return isOverHoverTarget() || isOverPressTarget();
	}
	
	public Boolean isOverHoverTarget() {
		return _streamData.isOverHoverTarget();//_isOverHoverTarget;
	}

	public void setStreamData(InteractionStreamData streamData) {
		_streamData = streamData;
	}
}
