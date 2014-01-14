package framework.events;

import framework.data.UserData;

public class UserAddedEvent extends Event {

	private float _horRatio;
	private UserData _user;

	public UserAddedEvent(UserData user, float horRatio) {
		super(EventType.UserAdded);
		_horRatio = horRatio;
		_user = user;
	}
	
	public UserData get_user(){
		return _user;
	}

	public float get_horRatio() {
		return _horRatio;
	}

}
