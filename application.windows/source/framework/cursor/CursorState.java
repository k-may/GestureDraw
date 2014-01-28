package framework.cursor;

import framework.interaction.Types.HandType;


public class CursorState {

	private CursorMode _mode;
	private float _pressure;
	private HandType _handType;
	private int _color;

	public CursorState(CursorMode mode){
		_mode = mode;

	}
	
	public void set_mode(CursorMode _mode) {
		this._mode = _mode;
	}

	public void set_handType(HandType _handType) {
		this._handType = _handType;
	}

	public void set_color(int _color) {
		this._color = _color;
	}

	
	public HandType get_handType() {
		return _handType;
	}

	public int get_color() {
		return _color;
	}

	public CursorMode get_mode() {
		return _mode;
	}

	public float get_pressure() {
		return _pressure;
	}
	
	public void set_pressure(float pressure){
		_pressure = pressure;
	}

}
