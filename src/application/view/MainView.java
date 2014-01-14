package application.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import application.view.avatar.AvatarView;
import application.view.avatar.AvatarsView;
import framework.BaseMainView;
import framework.Controller;
import framework.Rectangle;
import framework.scenes.SceneManager;
import framework.scenes.SceneType;
import framework.view.IView;

public class MainView extends BaseMainView implements Observer {
	private SceneType DefaultScene = SceneType.Home;
	private Controller _controller;

	public static int LightGreyColor = 0xBADBD7D7;
	public static int ICON_COLOR = 0Xff333333;
	public static int TEXT_COLOR = 0xff808080;
	
	public static int MESSAGE_WIDTH = 607;
	public static int MESSAGE_HEIGHT = 492;
	
	public static int BUTTON_TEXT_TOP = 134;

	public MainView(PApplet parent) {
		super(parent);
		init();
		createChilds();
	} 

	private void init() {
		_parent.smooth();
		_currentScene = DefaultScene;
		_controller = Controller.getInstance();

		SCREEN_WIDTH = _parent.width;
		SCREEN_HEIGHT = _parent.height;
	}

	private void createChilds() {
		_userMenuView = new UserMenuView();
		int width = (int) ((UserMenuView) _userMenuView).get_width();
		((UserMenuView) _userMenuView).set_x((SCREEN_WIDTH - width) / 2);
		addChild((UserMenuView) _userMenuView);
	}

	public void start() {
		_parent.loop();
	}

	@Override
	public void draw(PApplet p) {
		// gets inputs from region and process
		_region.runInteractions();
		// send processed inputs to dispatcher
		_dispatcher.setStream(_region.getStream());

		int time = p.millis();
		_dispatcher.process(time);

		_controller.update(time);

		SceneManager.getScene().draw(p);

		for (IView child : _childs)
			child.draw(p);

	}

	@Override
	public ArrayList<IView> getTargetsAtLocation(float x, float y) {
		// map values
		x = x * SCREEN_WIDTH;
		y = y * SCREEN_HEIGHT;

		ArrayList<IView> elements = new ArrayList<IView>();

		IView element = this;

		try {
			getChildsAtLocation(element, x, y, elements);
		} catch (Exception e) {
			// exception to exit recursion
		}

		if (elements.contains(this))
			elements.remove(this);

		return elements;
	}

	private void getChildsAtLocation(IView parent, float x, float y,
			ArrayList<IView> elements) throws Exception {
		for (int i = parent.get_numChildren() - 1; i >= 0; i--) {
			IView child = parent.get_childAt(i);

			Rectangle rect = child.get_rect();
			if (rect.contains(x, y)) {
				if (child.get_numChildren() == 0) {
					if (child.isTouchEnabled())
						elements.add(child);

					break;
				} else {
					if (child.isTouchEnabled())
						elements.add(child);

					// exit recursion if press target found
					if (child.isPressTarget())
						throw new Exception();

					x -= parent.get_x();
					y -= parent.get_y();

					getChildsAtLocation(child, x, y, elements);
				}
			}
		}
	}

	@Override
	public void startHover(int userID, int interval, IView target) {
		AvatarView avatar = ((AvatarsView) _interactionView).getAvatarById(userID);

		if (avatar != null)
			avatar.startLoad(interval, 1.0f, target);
	}

	@Override
	public void endHover(int userID) {
		AvatarView avatar = ((AvatarsView) _interactionView).getAvatarById(userID);

		if (avatar != null)
			avatar.cancelHover();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		((AvatarsView) _interactionView).setScene(SceneManager.GetSceneType());
	}

	@Override
	public void setVisible(Boolean isVisible) {
		// TODO Auto-generated method stub

	}

	@Override
	public int get_id() {
		// TODO Auto-generated method stub
		return -1;
	}

}
