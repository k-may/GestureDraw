package framework.events;

import framework.data.UserData;

public class UpdateColorEvent extends Event {

	private UserData _user;
	private int _color;

	public UpdateColorEvent(UserData user, int color) {
		super(EventType.UpdateColor);
		_user = user;
		_color = color;
	}

	public UserData get_user() {
		return _user;
	}

	public int get_color() {
		return _color;
	}

}
