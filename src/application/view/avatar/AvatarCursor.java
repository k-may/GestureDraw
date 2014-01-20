package application.view.avatar;

import framework.interaction.Types.HandType;
import framework.view.View;
import application.content.ContentManager;
import processing.core.PApplet;
import processing.core.PImage;
import static processing.core.PApplet.println;

public class AvatarCursor extends View {

	private PImage _hand;
	private PImage _highlight;

	public float loadRatio = 0.0f;
	private int _outerRingWeight = 6;
	private int _midRingWeight = 2;
	private int _innerRingWeight = 2;

	private int _greyColor = 0xBA6E6F72;
	private int _lightGreyColor = 0xBADBD7D7;
	private int _color = 0xffffffff;
	private float _pressPressure = 0.0f;
	private float _strokePressure = 0.0f;
	private float _navPressure = 0.0f;

	public static final int MAX_RADIUS = 25;
	public static final int MIN_RADIUS = 4;
	private Boolean _isPressing = false;
	private HandType _handType;

	private Boolean _isOverPressTarget = false;

	private CursorMode _mode;

	public AvatarCursor() {

		_hand = ContentManager.GetIcon("handRight");
		_highlight = ContentManager.GetIcon("cursorHighlight");
	}

	@Override
	public void draw(PApplet p) {

		int baseColor = _pressPressure > 0.0f ? _lightGreyColor : _greyColor;

		p.ellipseMode(PApplet.CENTER);

		if (_pressPressure > 0.0f) {
			p.image(_highlight, _x - _highlight.width / 2, _y
					- _highlight.height / 2);
			if (!_isPressing) {
				p.tint(_lightGreyColor, _pressPressure * 255);
			} else
				baseColor = 0xffffffff;

			p.image(_hand, _x - _hand.width / 2, _y - _hand.height / 2);
		} else {
			p.noStroke();
			p.fill(baseColor);
			p.rect(_x - 2, _y - MAX_RADIUS - 8, 4, 6);
		}

		// outer ring
		p.noFill();
		p.stroke(baseColor);
		p.strokeCap(PApplet.SQUARE);
		p.strokeWeight(_outerRingWeight);
		p.arc(_x, _y, MAX_RADIUS * 2 + 10, MAX_RADIUS * 2 + 10, -(float) Math.PI / 2, get_loadAngle(), PApplet.OPEN);

		// mid ring
		p.stroke(baseColor);
		p.strokeWeight(_midRingWeight);
		p.ellipse(_x, _y, MAX_RADIUS * 2 + 3, MAX_RADIUS * 2 + 3);

		// inner/color ring
		p.strokeWeight(_innerRingWeight);
		p.stroke(_color);
		p.ellipse(_x, _y, MAX_RADIUS * 2 - 1, MAX_RADIUS * 2 - 1);

		// stroke
		switch (_mode) {
			case Drawing:
				drawDrawingEllipse(p, _x, _y);
				break;
			case Navigating:
				drawNavigationEllipse(p, _x, _y);
				break;
		}

		if (loadRatio == 1.0f)
			loadRatio = 0.0f;

		p.noTint();

	}

	private void drawNavigationEllipse(PApplet p, float x, float y) {
		p.strokeWeight(1);
		p.stroke(_color);
		p.noFill();
		float cRadius = GetRadiusForPressure(1 - _navPressure);
		p.ellipse(x, y, cRadius * 2, cRadius * 2);
	}

	private void drawDrawingEllipse(PApplet p, float x, float y) {
		p.noStroke();
		p.fill(_color);
		float cRadius = GetRadiusForPressure(_strokePressure);
		p.ellipse(x, y, cRadius * 2, cRadius * 2);
	}

	private float get_loadAngle() {
		// TODO Auto-generated method stub
		return (float) (loadRatio * Math.PI * 2 - Math.PI / 2);
	}

	public void setColor(int color) {
		_color = color;
	}

	public static float GetRadiusForPressure(float pressure) {
		return (MAX_RADIUS - MIN_RADIUS) * pressure + MIN_RADIUS;
	}

	public void set_pressing(Boolean pressing, float pressure) {
		_isPressing = pressing;
		_pressPressure = pressure;
	}

	public void setStrokePressure(float pressure) {
		_strokePressure = pressure;
	}

	public void set_isOverPressTarget(Boolean isOverPressTarget) {
		_isOverPressTarget = isOverPressTarget;
	}

	public void set_mode(CursorMode _mode) {
		this._mode = _mode;
	}

	public CursorMode get_mode() {
		return _mode;
	}

	public void set_strokePressure(float _strokePressure) {
		this._strokePressure = _strokePressure;
	}

	public void set_navPressure(float navigationPressure) {
		_navPressure = navigationPressure;
	}

	public void setHandType(HandType handType) {
		if (_handType != handType) {
			_handType = handType;
			_hand = _handType == HandType.Right ? ContentManager.GetIcon("handRight")
					: ContentManager.GetIcon("handLeft");
		}
	}

}
