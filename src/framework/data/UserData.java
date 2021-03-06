package framework.data;

import framework.cursor.CursorState;
import framework.depth.DepthState;
import framework.depth.DepthStateData;
import framework.events.HandChangedEvent;
import framework.interaction.Types.HandType;
import framework.interaction.data.InteractionStreamData;

public class UserData {
	public static int START_COLOR = 0xffffffff;
	private int _id;
	private int _color = START_COLOR;
	private int _previousColor;
	private HandType _primaryHand;
	private Boolean _updated = false;
	private int _unavailableCount;
	
	private float _localX;
	private float _localY;
	private int _region;

	private DepthStateData _depthStateData;
	private InteractionStreamData _streamData;
	private CursorState _cursorState;

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


	public Boolean isUpdated() {
		return _updated;
	}

	public void set_updated(Boolean updated) {
		this._updated = updated;		
		if(_updated){
			_unavailableCount = 0;
		}else{
			_unavailableCount ++;
		}
	}
	
	public int getUnavailableCount(){
		return _unavailableCount;
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

	public Boolean isOverPressTarget() {
		return _streamData.isOverPressTarget();//_isOverPressTarget;
	}
	
	public float getPressPressure(){
		return _depthStateData.get_pressure();
	}
	
	public DepthState getDepthState(){
		return _depthStateData.get_state();
	}

	public void set_depthStateData(DepthStateData _depthStateData) {
		this._depthStateData = _depthStateData;
	}
	
	public float get_strokePressure(){
		return _depthStateData.get_state() == DepthState.Drawing ? 
				_depthStateData.get_pressure() : 0.0f;
	}
	
	public float get_navigationPressure(){
		return _depthStateData.get_state() == DepthState.Start ? 
				_depthStateData.get_pressure() : 0.0f;
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

	public int get_region() {
		return _region;
	}

	public void set_region(int _region) {
		this._region = _region;
	}

	public HandType getHandType() {
		return _primaryHand;
	}

	public void setHandType(HandType type) {
		if(_primaryHand != null && _primaryHand != type)
			new HandChangedEvent(this).dispatch();
		
		_primaryHand = type;
	}

	public void setCursorState(CursorState cursorState) {
		_cursorState = cursorState;
	}
	
	public CursorState getCursorState(){
		return _cursorState;
	}
}
