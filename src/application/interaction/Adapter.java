package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PVector;
import application.view.MainView;
import application.view.avatar.AvatarsView;
import framework.Controller;
import framework.IMainView;
import framework.SceneState;
import framework.cursor.CursorMode;
import framework.cursor.CursorState;
import framework.data.UserData;
import framework.depth.DepthStateData;
import framework.depth.DepthStateFactory;
import framework.interaction.IAdapter;
import framework.interaction.Types.HandType;
import framework.interaction.Types.InteractionType;
import framework.interaction.data.InteractionStreamData;
import framework.interaction.data.InteractionTargetInfo;
import framework.view.IView;

import framework.scenes.SceneManager;
import framework.scenes.SceneType;

import static processing.core.PApplet.println;

public class Adapter implements IAdapter {

	private final int MAX_UNAVAILABLE = 5;
	protected IMainView _canvas;
	protected AvatarsView _avatarsView;
	private DepthStateFactory _depthStateFactory;

	public Adapter() {
		_avatarsView = new AvatarsView();
		_depthStateFactory = new DepthStateFactory(0.15f, 0.4f, 0.6f);
	}

	@Override
	public InteractionTargetInfo getInteractionInfoAtLocation(float x, float y, InteractionType type) {
		// TODO Auto-generated method stub
		ArrayList<IView> targets = _canvas.getTargetsAtLocation(x
				* MainView.SCREEN_WIDTH, y * MainView.SCREEN_HEIGHT);

		InteractionTargetInfo info = new InteractionTargetInfo(targets);

		Boolean overPressTarget = false;
		Boolean overHoverTarget = false;

		float attrX = 0.0f;
		float attrY = 0.0f;
		// IView
		IView target = null;
		IView canvas = null;
		// take last one
		for (IView view : targets) {
			if (view.isHoverTarget() || view.isPressTarget()) {
				target = view;
			}
			if (view.isDrawTarget())
				canvas = view;
		}

		if (target != null) {
			overPressTarget = target.isPressTarget();
			// println("over press target : " + overPressTarget);
			overHoverTarget = target.isHoverTarget();
			PVector targetAbsPos = target.get_absPos();
			float targetWidth = target.get_width();
			float targetHeight = target.get_height();
			attrX = (targetAbsPos.x + targetWidth / 2) / _canvas.get_width();
			attrY = (targetAbsPos.y + targetHeight / 2) / _canvas.get_height();
		}

		info.set_canvas(canvas);
		info.set_targetID(target != null ? target.get_id() : -1);
		info.set_isPressTarget(overPressTarget);
		info.set_isHoverTarget(overHoverTarget);
		info.set_pressAttractionX(attrX);
		info.set_pressAttractionY(attrY);

		return info;
	}

	@Override
	public DepthStateData getInteractionInfoAtDepth(float z) {
		return _depthStateFactory.getStateData(z, MainView.CurrentState);
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
		for (InteractionStreamData streamData : data)
			digestStream(streamData);
	}

	private void digestStream(InteractionStreamData data) {

		UserData user = _avatarsView.getUser(data.get_userId());

		float localX = data.get_x() * _canvas.get_width();
		float localY = data.get_y() * _canvas.get_height();

		user.setStreamData(data);

		DepthStateData depthStateData = data.get_depthState();

		user.set_depthStateData(depthStateData);

		user.set_localX(localX);
		user.set_localY(localY);
		user.setHandType(data.getHandType());

		CursorState cursorState = _depthStateFactory.getCursorState(MainView.CurrentState, depthStateData, data.isPressing(), data.isOverTarget(), user.getHandType(), user.getColor());

		user.setCursorState(cursorState);
		user.set_updated(true);
	}

	@Override
	public UserData getUserForDomain(int id) {
		UserData user = _avatarsView.getUser(id);
		if (user == null) {
			user = _avatarsView.addUser(id);
		}
		return user;
	}

}
