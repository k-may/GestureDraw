package application.view.avatar;

import static processing.core.PApplet.println;
import processing.core.PApplet;
import processing.core.PImage;
import application.content.ContentManager;
import application.view.colorwheel.ColorWheel;
import de.looksgood.ani.Ani;
import de.looksgood.ani.easing.Easing;
import framework.cursor.CursorMode;
import framework.data.UserData;
import framework.events.UpdateColorEvent;
import framework.pressing.PressState;
import framework.view.IView;
import framework.view.View;

public class AvatarView extends View implements Comparable<AvatarView> {

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
		else
			_isColorWheelVisible = false;

		updateCursor();

		super.draw(p);
	}

	private void updateCursor() {
		_cursor.setState(_user.getCursorState());//getCursorMode());
		_cursor.set_x(_user.get_localX());
		_cursor.set_y(_user.get_localY());
	}

	private void drawPressState(PApplet p) {
		float x = _user.get_localX();
		float y = _user.get_localY();
		PressState state = _user.getPressState();
		
		if (state == PressState.ColorSelection) {
			if (!_isColorWheelVisible)
				animateColorWheel(x, y);

			drawColorWheel(p);
		}

		_isColorWheelVisible = state == PressState.ColorSelection;

	}

	public CursorMode getCursorMode() {
		PressState state = _user.getPressState();
		CursorMode mode = CursorMode.Navigating;

		switch (state) {
			case Drawing:
				mode = CursorMode.Drawing;
				break;
		}

		mode = _user.isOverPressTarget() ? mode.Pressing : mode;

		return mode;
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
		float radius = ColorWheel.WHEEL_RADIUS;
		float cX = _colorWheelX + radius;
		float cY = _colorWheelY + radius;

		double dist = Math.sqrt(Math.pow(cX - x, 2) + Math.pow(cY - y, 2));

		return dist <= radius;
	}

	public void animateColorWheel(float x, float y) {
		_isColorWheelVisible = true;
		_colorWheelX = x - ColorWheel.WHEEL_RADIUS;
		_colorWheelY = y - ColorWheel.WHEEL_RADIUS;
		colorWheelAlpha = 0;
		_colorWheelAnimation = Ani.to(this, 0.5f, "colorWheelAlpha", 255, Easing.EXPO_IN);
	}

	public int get_userId() {
		return _user.get_id();
	}

	public void startLoad(int interval, float value, IView target) {
		_hoverTarget = target;
		_hoverAnimation = new Ani(_cursor, interval / 1000, "loadRatio", value, Ani.EXPO_OUT);
	}

	public void cancelHover() {
		if (_hoverTarget != null)
			_hoverTarget = null;

		if (_hoverAnimation != null)
			_hoverAnimation.end();

		_cursor.loadRatio = 0.0f;
	}

	@Override
	public int compareTo(AvatarView o) {
		return get_userId() - o.get_userId();
	}
}
