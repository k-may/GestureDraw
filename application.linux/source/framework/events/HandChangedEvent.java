package framework.events;

import framework.data.UserData;

public class HandChangedEvent extends Event {

	private UserData _user;

	public HandChangedEvent(UserData user) {
		super(EventType.HandChanged);
		_user = user;
	}

	public UserData getUser() {
		return _user;
	}

}
