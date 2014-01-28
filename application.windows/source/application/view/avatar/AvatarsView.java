package application.view.avatar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.cursor.CursorMode;
import framework.data.UserData;
import framework.events.InActionEvent;
import framework.events.UserAddedEvent;
import framework.events.UserRemovedEvent;
import framework.interaction.IInteractionView;
import framework.scenes.SceneManager;
import framework.scenes.SceneType;
import framework.view.View;

import processing.core.PApplet;
import static processing.core.PApplet.println;

public class AvatarsView extends View implements IInteractionView {

	private ArrayList<UserData> _users;
	private Map<Integer, AvatarView> _avatarViews;

	private Boolean _updated = false;
	private int _lastUpdate;

	public AvatarsView() {
		_users = new ArrayList<UserData>();
		_avatarViews = new HashMap<Integer, AvatarView>();
	}

	@Override
	public void draw(PApplet p) {
		// arrange views by user pressure
		if (_avatarViews.values() != null) {
			List<AvatarView> views = new ArrayList<AvatarView>(_avatarViews.values());
			Collections.sort(views);

			for (AvatarView view : views) {
				view.draw(p);
			}
		}

		int time = p.millis();

		if (_updated)
			_lastUpdate = p.millis();

		if (time - _lastUpdate > 60000) {
			new InActionEvent().dispatch();
			_lastUpdate = time;
		}

		_updated = false;
	}

	@Override
	public void removeUser(UserData user) {
		if (_users.contains(user))
			_users.remove(user);

		if (_avatarViews.containsKey(user.get_id())) {
			AvatarView view = _avatarViews.get(user.get_id());
			_avatarViews.remove(user.get_id());
			removeChild(view);
			_updated = true;
		}

		new UserRemovedEvent(user).dispatch();
	}

	@Override
	public UserData addUser(int id) {

		UserData user = new UserData(id);
		_users.add(user);

		AvatarView view = new AvatarView(user);
		_avatarViews.put(user.get_id(), view);
		addChild(view);

		_updated = true;
		
		new UserAddedEvent(user).dispatch();

		return user;
	}

	@Override
	public UserData getUser(int id) {
		for (UserData u : _users) {
			if (u.get_id() == id) {
				_updated = true;
				return u;
			}
		}

		return null;
	}

	public ArrayList<UserData> get_users() {
		return _users;
	}

	@Override
	public Boolean isTouchEnabled() {
		return false;
	}

	public AvatarView getAvatarById(int id) {
		if (_avatarViews.containsKey(id))
			return _avatarViews.get(id);

		return null;
	}

}
