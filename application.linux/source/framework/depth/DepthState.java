package framework.depth;

public enum DepthState {
	None("none"), Start("start"), ColorSelection("colorSelection"), PreDrawing("preDraw"), Drawing(
			"draw");

	private String _name;

	private DepthState(String name) {
		_name = name;
	}

	@Override
	public String toString() {
		return _name;
	}
	

	public Boolean isDrawing() {
		return this == DepthState.Drawing
				|| this == DepthState.PreDrawing;
	}

}
