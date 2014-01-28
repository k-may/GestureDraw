package framework.depth;

public class DepthStateData {
	private DepthState _state;

	public DepthState get_state() {
		return _state;
	}

	public float get_pressure() {
		return _pressure;
	}

	private float _pressure;
	

	public DepthStateData(DepthState state, float pressure) {
		_state = state;
		_pressure = pressure;
	}

}
