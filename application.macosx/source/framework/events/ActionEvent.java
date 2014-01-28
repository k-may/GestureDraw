package framework.events;

public class ActionEvent extends Event {

	private int _time;
	
	public ActionEvent(int time) {
		super(EventType.Action);
		_time = time;
	}

	public int get_time() {
		return _time;
	}

}
