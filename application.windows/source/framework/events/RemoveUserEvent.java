package framework.events;

import framework.data.UserData;

public class RemoveUserEvent extends Event {

	private UserData _user;
	
	public RemoveUserEvent(UserData user) {
		super(EventType.UserRemoved);
		
		_user= user;
	}

	public UserData get_user() {
		return _user;
	}

}
