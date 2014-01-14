package framework.pressing;

public enum PressState {
	Start("start"), ColorSelection("colorSelection"), PreDrawing("preDraw"), Drawing(
			"draw");

	private String _name;

	private PressState(String name) {
		_name = name;
	}

	@Override
	public String toString() {
		return _name;
	}

}
