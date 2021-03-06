package application.view.avatar;

import processing.core.PApplet;
import application.view.MainView;
import application.view.PView;
import application.view.colorwheel.ColorWheel;
import de.looksgood.ani.Ani;
import de.looksgood.ani.easing.Easing;
import framework.data.UserData;
import framework.depth.DepthState;
import framework.events.UpdateColorEvent;
import framework.view.IView;

public class AvatarView extends PView implements Comparable<AvatarView> {

	public int colorWheelAlpha = 255;
	private IView _hoverTarget;
	private UserData _user;
	private Boolean _isColorWheelVisible = false;
	private float _colorWheelX;
	private float _colorWheelY;
	private ColorWheel _colorWheel;
	private Ani _hoverAnimation;
	private Ani _colorWheelAnimation;

	private AvatarCursor _cursor;

	public AvatarView(UserData user) {
		// println("new AvatarView : " + user.get_id());
		_user = user;

		createChilds();
	}

	private void createChilds() {
		_colorWheel = new ColorWheel();
		_cursor = new AvatarCursor();
		addChild(_cursor);
	}

	@Override
	public void draw(PApplet p) {

		if (!_user.isOverButton())
			drawPressState(p);
		else {
			_isColorWheelVisible = false;

		}
		updateCursor();

		super.draw(p);
	}

	private void updateCursor() {
		_cursor.setState(_user.getCursorState());// getCursorMode());
		_cursor.set_x(_user.get_localX());
		_cursor.set_y(_user.get_localY());
	}

	private void drawPressState(PApplet p) {
		float x = _user.get_localX();
		float y = _user.get_localY();
		DepthState state = _user.getDepthState();

		if (state == DepthState.ColorSelection) {
			if (!_isColorWheelVisible)
				animateColorWheel(x, y);

			drawColorWheel(p);
		}

		_isColorWheelVisible = state == DepthState.ColorSelection;

	}

	private void drawColorWheel(PApplet p) {
		// hide color wheel slightly if over press target
		float localX = _user.get_localX();
		float localY = _user.get_localY();
		Boolean isOverWheel = isOverWheel(localX, localY);

		// alpha animates in (except if not over wheel)
		if (_user.isOverButton() || !isOverWheel)
			p.tint(255, 100);
		else
			p.tint(255, colorWheelAlpha);

		// draw wheel in static position
		p.image(_colorWheel.getImage(), _colorWheelX, _colorWheelY);

		if (isOverWheel && _colorWheelAnimation.isEnded())
			updateColor();

	}

	private void updateColor() {
		int x = (int) (_user.get_localX() - _colorWheelX);
		int y = (int) (_user.get_localY() - _colorWheelY);
		int color = _colorWheel.getColor(x, y); // GetColor((int) x, (int) y);
		_user.setColor(color);

		new UpdateColorEvent(_user, color).dispatch();
	}

	private Boolean isOverWheel(float x, float y) {
		float radius = MainView.COLORWHEEL_RADIUS;
		float cX = _colorWheelX + radius;
		float cY = _colorWheelY + radius;

		double dist = Math.sqrt(Math.pow(cX - x, 2) + Math.pow(cY - y, 2));

		return dist <= radius;
	}

	public void animateColorWheel(float x, float y) {
		_isColorWheelVisible = true;
		_colorWheelX = Math.max(x - MainView.COLORWHEEL_RADIUS, 0);
		_colorWheelY = Math.max(y - MainView.COLORWHEEL_RADIUS, 0);
		colorWheelAlpha = 0;
		_colorWheelAnimation = Ani.to(this, 0.4f, "colorWheelAlpha", 255, Easing.EXPO_IN);
	}

	public int get_userId() {
		return _user.get_id();
	}

	public void startLoad(int interval, float value, IView target) {
		_hoverTarget = target;
		_hoverAnimation = _cursor.animateLoad(interval, value);//Ani.to(_cursor, interval / 1000, "loadRatio", value, Ani.EXPO_OUT);
	}

	public void cancelHover() {
		if (_hoverTarget != null)
			_hoverTarget = null;

		if (_hoverAnimation != null) {
			_hoverAnimation.end();
			_hoverAnimation = null;
		}

		_cursor.loadRatio = 0.0f;
	}

	@Override
	public int compareTo(AvatarView o) {
		return get_userId() - o.get_userId();
	}
}
