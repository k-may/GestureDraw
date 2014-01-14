package framework.events;

import framework.Controller;

public class Event {

	private EventType _type;

	public Event(EventType type) {
		set_type(type);
	}

	public void dispatch() {
		Controller.getInstance().addEvent(this);
	}

	public EventType get_type() {
		return _type;
	}

	public void set_type(EventType _type) {
		this._type = _type;
	}
}
