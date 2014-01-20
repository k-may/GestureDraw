package framework.stroke;

import processing.core.PVector;

public class StrokeFragment {
	private PVector _startPt;
	private PVector _endPt;
	private PVector _ctrlPt;
	private float _pressure;
	private int _color;
	private int _id;
	private StrokeType _type;

	public StrokeFragment(PVector startPt, PVector ctrlPt, PVector endPt,
			float pressure, int color, int id, StrokeType type) {
		_startPt = startPt;
		_endPt = endPt;
		_ctrlPt = ctrlPt;

		_pressure = pressure;
		_color = color;
		_id = id;
		
		_type = type;
	}

	public StrokeType get_type(){
		return _type;
	}
	
	public int get_id() {
		return _id;
	}

	public PVector get_startPt() {
		return _startPt;
	}

	public PVector get_endPt() {
		return _endPt;
	}

	public PVector get_ctrlPt() {
		return _ctrlPt;
	}

	public float get_pressure() {
		return _pressure;
	}

	public int get_color() {
		return _color;
	}
}
