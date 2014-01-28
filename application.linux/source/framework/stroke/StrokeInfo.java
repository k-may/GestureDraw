package framework.stroke;

import processing.core.PVector;

public class StrokeInfo {
	private StrokeType _type;
	private PVector _pos;
	private float _pressure;

	public StrokeInfo(PVector pos, StrokeType type, float pressure) {
		_pos = pos;
		_type = type;
		_pressure = pressure;
	}

	public StrokeType get_type() {
		return _type;
	}

	public PVector get_pos() {
		return _pos;
	}

	public float get_pressure() {
		return _pressure;
	}
}
