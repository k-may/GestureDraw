package framework.events;

public class LabelButtonPressed extends Event {

	private String _text;
	
	public LabelButtonPressed(String text){
		super(EventType.LabelButtonPressed);
	
		_text = text;
	}

	public String get_text() {
		return _text;
	}

}
