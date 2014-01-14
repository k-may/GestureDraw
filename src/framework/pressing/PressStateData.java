package framework.pressing;

public class PressStateData {
	private PressState _state;

	public PressState get_state() {
		return _state;
	}

	public float get_pressure() {
		return _pressure;
	}

	private float _pressure;

	public PressStateData(PressState state, float pressure) {
		_state = state;
		_pressure = pressure;
	}

}
