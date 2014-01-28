package application.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import application.interaction.RegionType;
import application.view.avatar.AvatarView;
import application.view.avatar.AvatarsView;
import application.view.canvas.CanvasScene;
import application.view.user.UserMenuView;
import framework.BaseMainView;
import framework.Controller;
import framework.Rectangle;
import framework.SceneState;
import framework.scenes.SceneManager;
import framework.scenes.SceneType;
import framework.view.IView;

public class MainView extends BaseMainView implements Observer {
	private SceneType DefaultScene = SceneType.Home;
	private Controller _controller;

	public static RegionType REGION_TYPE;

	public static int WHITE = 0XFFFFFFFF;
	public static int LIGHT_GREY = 0xBADBD7D7;
	public static int GREY = 0xBA6E6F72;
	public static int ICON_COLOR = 0Xff333333;
	public static int TEXT_COLOR = 0xff808080;

	public static int MESSAGE_WIDTH = 607;
	public static int MESSAGE_HEIGHT = 492;

	public static int BUTTON_TEXT_TOP = 134;

	public static int BG_COLOR = 0xff111011;

	public static int ICON_MAX_RADIUS = 30;
	public static int ICON_MIN_RADIUS = 4;

	public static int COLORWHEEL_RADIUS = 120;

	public static int BUTTON_WIDTH = 208;
	public static int BUTTON_HEIGHT = 208;

	public static final int DividorWidth = 6;

	// menu states
	public static Boolean CLEARABLE = true;
	public static Boolean TRACKS = true;

	// region input
	public static int ZRANGE = 300;
	public static int XRANGE = 200;
	public static int YRANGE = 200;

	public static float DRAW_MASS;
	public static float DRAW_LERP = 0.5f;
	public static float CURSOR_LERP = 0.05F;
	public static float TARGET_MASS = 0.005F;

	public MainView(PApplet parent) {
		super(parent);
		init();
		createChilds();
	}

	private void init() {
		_parent.smooth();
		//_currentScene = DefaultScene;
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
		// x = x * SCREEN_WIDTH;
		// y = y * SCREEN_HEIGHT;

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
					if (child.isPressTarget() || child.isHoverTarget())
						break; //

					x -= child.get_x();
					y -= child.get_y();

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
		SceneType sceneType = SceneManager.GetSceneType();
		
		if(sceneType == SceneType.Canvas){
			CanvasScene canvas = (CanvasScene) SceneManager.getScene();
			if(canvas.get_isSaving())
				CurrentState = SceneState.Saving;
			else
				CurrentState = SceneState.Canvas;
		}else
			CurrentState = SceneState.Home;
	}

	@Override
	public void setVisible(Boolean isVisible) {
	}

	@Override
	public int get_id() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public Boolean isDrawTarget() {
		return false;
	}

}