package framework.events;

import processing.core.PApplet;

public class LogEvent extends Event {

	private String _msg;
	private String _time;

	public LogEvent(String msg) {
		this(EventType.Log, msg);
	}

	public LogEvent(EventType type, String msg){
		super(type);
		
		_msg = msg;
		
		String minute = String.valueOf(PApplet.minute());
		minute = minute.length() == 1 ? "0" + minute : minute;
		
		String second = String.valueOf(PApplet.second());
		second = second.length() == 1 ? "0" + second : second;
		
		_time = PApplet.month() + "/" + PApplet.day() + " " + PApplet.hour()
				+ ":" + minute + ":" + second;
	}

	public String get_msg() {
		return _msg;
	}

	public String get_time() {
		return _time;
	}
}
