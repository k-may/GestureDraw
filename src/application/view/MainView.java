package application.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;
import processing.core.PVector;
import application.interaction.RegionType;
import application.view.avatar.AvatarView;
import application.view.avatar.AvatarsView;
import application.view.canvas.CanvasScene;
import application.view.user.UserMenuView;
import framework.BaseMainView;
import framework.Controller;
import framework.Rectangle;
import framework.SceneState;
import framework.data.UserData;
import framework.events.TouchEvent;
import framework.interaction.Types.InteractionEventType;
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
	public static float START_TO = 0.15f;
	public static float PREDRAW_TO = 0.4f;
	public static float COLOR_TO = 0.6f;

	// menu states
	public static Boolean CLEARABLE = true;
	public static Boolean TRACKS = true;

	// region input
	public static int ZRANGE = 300;
	public static int XRANGE = 200;
	public static int YRANGE = 200;
	public static final float RANGE_DAMPENING = 0.1f;
	
	public static final int TENDENCY_SAMPLES = 7;
	

	public static final int MAX_UNAVAILABLE = 5;
	public static final int MAX_INVALID = 15;
	public static final int MIN_SAMPLES = 25;
	public static float INVALID_DISTANCE = 0.5F;

	public static float DRAW_MASS;
	public static float DRAW_LERP = 0.5f;
	public static float CURSOR_LERP = 0.05F;
	public static float TARGET_MASS = 0.005F;
	public static float CENTER_SCREEN_MASS = 0.0005F;
	
	public static float DOMAIN_1 = 0.2f;
	public static float DOMAIN_2 = 0.8f;

	private PApplet _parent;

	public MainView(PApplet parent) {
		super();
		_parent = parent;
		init();
		createChilds();
	}

	private void init() {
		_parent.smooth();
		// _currentScene = DefaultScene;
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

	public void draw(PApplet p) {
		// gets inputs from region and process
		_region.runInteractions();
		// send processed inputs to dispatcher
		_dispatcher.setStream(_region.getStream());

		int time = p.millis();
		_dispatcher.process(time);

		_controller.update(time);

		((PIView) SceneManager.getScene()).draw(p);

		for (IView child : _childs)
			((PIView) child).draw(p);

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

		if (sceneType == SceneType.Canvas) {
			CanvasScene canvas = (CanvasScene) SceneManager.getScene();
			if (canvas.get_isSaving())
				CurrentState = SceneState.Saving;
			else
				CurrentState = SceneState.Canvas;
		} else
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

	@Override
	protected void addInteractionEvent(InteractionEventType type, IView target,
			float x, float y, int id) {
		PVector pos = ((PIView) target).get_absPos();

		UserData user = _interactionView.getUser(id);

		if (user != null) {
			float localX = x * SCREEN_WIDTH - pos.x;
			float localY = y * SCREEN_HEIGHT - pos.y;
			new TouchEvent(type, target, localX, localY, _interactionView.getUser(id), _parent.millis()).dispatch();
		}
	}

	public float get_width() {
		return _parent.width;
	}

	public float get_height() {
		return _parent.height;
	}


}
