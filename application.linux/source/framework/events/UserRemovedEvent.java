package framework.events;

import framework.data.UserData;

public class UserRemovedEvent extends Event {

	private UserData _user;
	
	public UserRemovedEvent(UserData user) {
		super(EventType.UserRemoved);
		
		_user= user;
	}

	public UserData get_user() {
		return _user;
	}

}
