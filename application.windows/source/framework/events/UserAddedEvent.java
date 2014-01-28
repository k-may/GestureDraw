package framework.events;

import framework.data.UserData;

public class UserAddedEvent extends Event {
	private UserData _user;

	public UserAddedEvent(UserData user) {
		super(EventType.UserAdded);
		_user = user;
	}
	
	public UserData get_user(){
		return _user;
	}
}
