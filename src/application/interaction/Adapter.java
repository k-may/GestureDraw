package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PVector;
import application.view.avatar.AvatarsView;
import framework.IMainView;
import framework.cursor.CursorMode;
import framework.cursor.CursorState;
import framework.data.UserData;
import framework.interaction.IAdapter;
import framework.interaction.InteractionStreamData;
import framework.interaction.InteractionTargetInfo;
import framework.interaction.InteractionType;
import framework.interaction.Types.HandType;
import framework.pressing.PressStateData;
import framework.pressing.PressStateFactory;
import framework.view.IView;

import framework.scenes.SceneManager;
import framework.scenes.SceneType;

import static processing.core.PApplet.println;

public class Adapter implements IAdapter {

	private final int MAX_UNAVAILABLE = 5;
	protected IMainView _canvas;
	protected AvatarsView _avatarsView;
	private PressStateFactory _pressStateFactory;
	private HashMap<Integer, PressStateData> _pressStateMap;

	public Adapter() {
		_avatarsView = new AvatarsView();
		_pressStateFactory = new PressStateFactory(0.1f, 0.3f, 0.5f);
		_pressStateMap = new HashMap<Integer, PressStateData>();
	}

	@Override
	public InteractionTargetInfo getInteractionInfoAtLocation(float x, float y,
			int userId, InteractionType type) {
		// TODO Auto-generated method stub
		InteractionTargetInfo info = new InteractionTargetInfo();
		ArrayList<IView> targets = _canvas.getTargetsAtLocation(x, y);

		Boolean overPressTarget = false;
		Boolean overHoverTarget = false;
		float attrX = 0.0f;
		float attrY = 0.0f;
		// IView
		IView pressTarget = null;

		// int pressTargetCount = 0;

		for (IView target : targets) {

			// if (target.isPressTarget())
			// pressTargetCount++;

			if (!overPressTarget && target.isPressTarget()) {

				// take first one!
				pressTarget = target;
				overPressTarget = true;
				PVector targetAbsPos = target.get_absPos();
				float targetWidth = target.get_width();
				float targetHeight = target.get_height();
				attrX = (targetAbsPos.x + targetWidth / 2)
						/ _canvas.get_width();
				attrY = (targetAbsPos.y + targetHeight / 2)
						/ _canvas.get_height();

			}
			overHoverTarget = overHoverTarget || target.isHoverTarget();
		}
		
		info.set_targetID(pressTarget != null ? pressTarget.get_id() : -1);
		info.set_isPressTarget(overPressTarget);
		info.set_isHoverTarget(overHoverTarget);
		info.set_pressAttractionX(attrX);
		info.set_pressAttractionY(attrY);

		return info;
	}

	@Override
	public void set_canvas(IMainView canvas) {
		_canvas = canvas;
		_canvas.addInteractionView(_avatarsView);
	}

	@Override
	public void beginInteractionFrame() {
		for (UserData data : _avatarsView.get_users()) {
			data.set_updated(false);
		}
	}

	@Override
	public void endInteractionFrame() {
		ArrayList<UserData> staleUsers = new ArrayList<UserData>();
		for (UserData user : _avatarsView.get_users()) {
			if (user.getUnavailableCount() > MAX_UNAVAILABLE)
				staleUsers.add(user);
		}

		for (UserData user : staleUsers) {
			_avatarsView.removeUser(user);
		}
	}

	@Override
	public void handleStreamData(ArrayList<InteractionStreamData> data) {

		SceneType sceneType = SceneManager.GetSceneType();
		
		for (InteractionStreamData streamData : data) {

			UserData user = _avatarsView.getUser(streamData.get_userId());

			if (user == null)
				user = _avatarsView.addUser(streamData.get_userId());

			float localX = streamData.get_x() * _canvas.get_width();
			float localY = streamData.get_y() * _canvas.get_height();

			user.setStreamData(streamData);

			PressStateData pressStateData = _pressStateFactory.getStateData(streamData.get_z(), sceneType, streamData.isOverTarget());
			_pressStateMap.put(streamData.get_userId(), pressStateData);
			user.set_pressStateData(pressStateData);
			
			user.set_localX(localX);
			user.set_localY(localY);
			user.setHandType(streamData.getHandType());
			
			CursorState cursorState = _pressStateFactory.getCursorState(sceneType, pressStateData, streamData.isPressing(), streamData.isOverTarget(), user.getHandType(), user.getColor());
			/*cursorState.set_color(user.getColor());
			cursorState.set_handType(HandType.Left);
			cursorState.set_pressure(0.5f);
			*/
			user.setCursorState(cursorState);
			
			user.set_updated(true);

		}
	}

	@Override
	public UserData getUserForDomain(int id) {
		return _avatarsView.getUser(id);
	}


}
