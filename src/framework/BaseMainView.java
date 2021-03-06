package framework;

import static processing.core.PApplet.println;

import java.util.ArrayList;

import framework.data.UserData;
import framework.events.TouchEvent;
import framework.interaction.IHoverTarget;
import framework.interaction.IInteractionRegion;
import framework.interaction.IInteractionView;
import framework.interaction.InteractionDispatcher;
import framework.interaction.Types.InteractionEventType;
import framework.scenes.SceneManager;
import framework.scenes.SceneType;
import framework.view.IUserMenuView;
import framework.view.IView;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class BaseMainView implements IMainView {
	protected InteractionDispatcher _dispatcher;
	protected ArrayList<IView> _childs;
	//protected PApplet _parent;
	
	public static SceneState CurrentState;
	

	
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	public static int SRC_WIDTH = 640;
	public static int SRC_HEIGHT = 480;
	
	public static int HOVER_ELAPSE = 1000;
	
	public static final float PRESS_POS_EXTENTS = 0.7F;
	public static final float PRESS_MAX_PRESSURE = 0.7f;
	
	protected IInteractionRegion _region;
	protected IInteractionView _interactionView;
	protected IUserMenuView _userMenuView;

	public BaseMainView() {
		init();
	}

	@Override
	public IUserMenuView get_userMenuView() {
		return _userMenuView;
	}

	private void init() {
		_dispatcher = new InteractionDispatcher(this);
		_childs = new ArrayList<IView>();
	}

	@Override
	public ArrayList<IView> getTargetsAtLocation(float x, float y) {
		return null;
	}

	@Override
	public void addPressDownEvent(IView target, float x, float y, int id) {
		addInteractionEvent(InteractionEventType.PressDown, target, x, y,  id);
	}

	@Override
	public void addPressReleaseEvent(IView target, float x, float y,int id) {
		addInteractionEvent(InteractionEventType.PressUp, target, x, y, id);
	}

	@Override
	public void addRollOverEvent(IView target, float x, float y,int id) {
		addInteractionEvent(InteractionEventType.RollOver, target, x, y, id);
	}

	@Override
	public void addCancelEvent(IView target, float x, float y,int id) {
		addInteractionEvent(InteractionEventType.Cancel, target, x, y, id);
		endHover(id);
	}

	@Override
	public void addMoveEvent(IView target, float x, float y,int id) {
		addInteractionEvent(InteractionEventType.Move, target, x, y,id);
	}

	@Override
	public void addHoverStartEvent(IView target, float x, float y, int id) {
		addInteractionEvent(InteractionEventType.HoverStart, target, x, y, id);
		int hoverInterval = ((IHoverTarget) target).get_hoverInterval();
		startHover(id, hoverInterval, target);
	}

	@Override
	public void addHoverEndEvent(IView target, float x, float y, int id) {
		addInteractionEvent(InteractionEventType.HoverEnd, target, x, y,id);
		endHover(id);
	}

	protected abstract void addInteractionEvent(InteractionEventType type, IView target,
			float x, float y, int id);

	@Override
	public Boolean isTouchEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public IView get_parent() {
		return null;
	}

	@Override
	public void addChild(IView child) {
		_childs.add(child);
	}

	@Override
	public void removeChild(IView child) {
		_childs.remove(child);
	}

	@Override
	public void set_parent(IView view) {
	}

	@Override
	public Rectangle get_rect() {
		return null;
	}

	@Override
	public abstract float get_width();
	
	@Override
	public void showMenu() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hideMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public int get_numChildren() {
		// TODO Auto-generated method stub
		return _childs.size() + 1;
	}

	@Override
	public IView get_childAt(int index) {
		// TODO Auto-generated method stub
		if (index == 0)
			return SceneManager.getScene();
		else
			return _childs.get(index - 1);
	}

	@Override
	public float get_x() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float get_y() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean isPressTarget() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void set_region(IInteractionRegion _region) {
		this._region = _region;
	}

	@Override
	public void addInteractionView(IInteractionView view) {
		_interactionView = view;
		addChild(_interactionView);
	}

	@Override
	public Object get_absPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleInteraction(TouchEvent event) {
	}

	@Override
	public IInteractionRegion get_region() {
		// TODO Auto-generated method stub
		return _region;
	}

	@Override
	public Boolean isHoverTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public abstract void startHover(int userID, int interval, IView target);

	public abstract void endHover(int userID);
	
	@Override
	public void draw() {
	}
	
	@Override
	public void removeUser(int id) {
		_region.removeUser(id);
		_dispatcher.removeUser(id);
		_userMenuView.removeDomain(id);
		
	}

}
