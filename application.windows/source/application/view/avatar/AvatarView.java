package application.view.avatar;

import static processing.core.PApplet.println;
import processing.core.PApplet;
import processing.core.PImage;
import application.content.ContentManager;
import application.view.colorwheel.ColorWheel;
import de.looksgood.ani.Ani;
import de.looksgood.ani.easing.Easing;
import framework.data.UserData;
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
	// private PImage _colorWheel;
	private ColorWheel _colorWheel;
	private Ani _hoverAnimation;

	public static int LightGreyColor = 0xBADBD7D7;

	private AvatarCursor _cursor;

	public AvatarView(UserData user) {
		//println("new AvatarView : " + user.get_id());
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
		_cursor.set_pressing(_user.isPressing(), _user.get_pressPressure());
		_cursor.set_mode(getCursorMode());
		_cursor.set_navPressure(_user.get_navigationPressure());
		_cursor.set_strokePressure(_user.get_strokePressure());
		_cursor.set_x(_user.get_localX());
		_cursor.set_y(_user.get_localY());
		_cursor.setColor(_user.getColor());

	}

	private void drawPressState(PApplet p) {
		float x = _user.get_localX();
		float y = _user.get_localY();
		PressState state = _user.getPressState();
		float pressure = _user.getPressPressure();
		switch (state) {
			case Start:
				drawStart(p);
				break;
			case ColorSelection:
				if (!_isColorWheelVisible)
					showColorWheel(x, y);

				drawColorSelection(p, pressure);
				break;
			case PreDrawing:
				drawPreDrawing(p, pressure, x, y);
				break;
			case Drawing:
				drawDrawing(p);
				break;
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

	private void drawDrawing(PApplet p) {
	}

	private void drawPreDrawing(PApplet p, float pressure, float x, float y) {
		p.strokeWeight(1);
		p.stroke(_user.getColor());
		pressure = 1 - pressure;
		float size = pressure * AvatarCursor.MAX_RADIUS * 2;
		float localX = _user.get_localX();
		float localY = _user.get_localY();
		p.ellipse(localX, localY, size, size);
	}

	private void drawColorSelection(PApplet p, float pressure) {
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

		if (isOverWheel)
			updateColor();

		p.strokeWeight(4);
		p.stroke(_user.getColor());
		p.noFill();
		float size = AvatarCursor.MAX_RADIUS * 2;
		p.ellipse(localX, localY, size, size);
	}

	private void drawStart(PApplet p) {
	}

	private void updateColor() {
		int x = (int) (_user.get_localX() - _colorWheelX);
		int y = (int) (_user.get_localY() - _colorWheelY);
		int color = _colorWheel.getColor(x, y); // GetColor((int) x, (int) y);
		_user.setColor(color);
	}

	private Boolean isOverWheel(float x, float y) {
		float radius = ColorWheel.WHEEL_RADIUS;
		float cX = _colorWheelX + radius;
		float cY = _colorWheelY + radius;

		double dist = Math.sqrt(Math.pow(cX - x, 2) + Math.pow(cY - y, 2));

		return dist <= radius;
	}

	public void showColorWheel(float x, float y) {
		_isColorWheelVisible = true;
		_colorWheelX = x - ColorWheel.WHEEL_RADIUS;
		_colorWheelY = y - ColorWheel.WHEEL_RADIUS;

		colorWheelAlpha = 0;
		Ani.to(this, 1, "colorWheelAlpha", 255, Easing.EXPO_OUT);
	}

	public int get_userId() {
		return _user.get_id();
	}

	public void startLoad(int interval, float value, IView target) {
		_hoverTarget = target;
		_hoverAnimation = new Ani(_cursor, interval / 1000, "loadRatio", value, Ani.EXPO_OUT, "onEnd:onHoverEnd");
	}

	public void cancelHover() {
		if (_hoverTarget != null)
			_hoverTarget = null;

		if (_hoverAnimation != null)
			_hoverAnimation.end();

		_cursor.loadRatio = 0.0f;
	}
	
	public void onHoverEnd(){
		//println("hoverend");
	}

	@Override
	public int compareTo(AvatarView o) {
		return get_userId() - o.get_userId();
	}
}
