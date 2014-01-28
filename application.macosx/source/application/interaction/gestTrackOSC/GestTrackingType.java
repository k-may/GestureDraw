package application.interaction.gestTrackOSC;

public enum GestTrackingType {
	Normalized("/normalized"), Absolute("/absolute");

	private String _text;

	GestTrackingType(String text) {
		_text = text;
	}

	public String toString() {
		return _text;
	}
}
