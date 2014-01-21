package application.view.avatar;

import framework.cursor.CursorMode;
import framework.cursor.CursorState;
import framework.interaction.Types.HandType;
import framework.view.View;
import application.content.ContentManager;
import application.view.MainView;
import processing.core.PApplet;
import processing.core.PImage;
import static processing.core.PApplet.println;

public class AvatarCursor extends View {

	private PImage _highlight;

	public float loadRatio = 0.0f;
	private int _outerRingWeight = 6;
	private int _midRingWeight = 2;
	private int _innerRingWeight = 2;

	public static final int MAX_RADIUS = 25;
	public static final int MIN_RADIUS = 4;

	private int _color = MainView.WHITE;
	private float _pressure = 0.0f;
	private HandType _handType;
	private CursorMode _mode;

	// private Boolean _isOverTarget = false;

	public AvatarCursor() {
		_highlight = ContentManager.GetIcon("cursorHighlight");
	}

	@Override
	public void draw(PApplet p) {

		int baseColor = getBaseColor();

		p.ellipseMode(PApplet.CENTER);
		drawOuter(p, baseColor);
		drawMiddle(p, baseColor);
		drawInner(p);
		drawMode(p);

		if (loadRatio == 1.0f)
			loadRatio = 0.0f;

		p.noTint();

	}

	private void drawMode(PApplet p) {
		switch (_mode) {
			case Navigating:
			case PreDrawing:
				drawNavigationEllipse(p);
			case Drawing:
				drawSightRect(p);
				break;
			case PrePressing:
				drawPressHand(p, false);
				break;
			case Pressing:
				drawPressHand(p, true);
				break;
		}
	}

	private void drawSightRect(PApplet p) {
		p.noStroke();
		p.fill(getBaseColor());
		p.rect(_x - 2, _y - MAX_RADIUS - 8, 4, 6);
	}

	private void drawPressHand(PApplet p, Boolean isPressing) {
		p.tint(getBaseColor(), 200);
		p.image(_highlight, _x - _highlight.width / 2, _y - _highlight.height
				/ 2);

		PImage hand = getHand();
		p.image(hand, _x - hand.width / 2, _y - hand.height / 2);
	}

	private void drawInner(PApplet p) {
		// inner/color ring
		p.strokeWeight(_innerRingWeight);
		p.stroke(_color);
		p.ellipse(_x, _y, MAX_RADIUS * 2 - 1, MAX_RADIUS * 2 - 1);

	}

	private void drawMiddle(PApplet p, int baseColor) {
		// mid ring
		p.stroke(baseColor);
		p.strokeWeight(_midRingWeight);
		p.ellipse(_x, _y, MAX_RADIUS * 2 + 3, MAX_RADIUS * 2 + 3);
	}

	private void drawOuter(PApplet p, int color) {
		// outer ring
		p.noFill();
		p.stroke(color, 100);
		p.strokeCap(PApplet.SQUARE);
		p.strokeWeight(_outerRingWeight);
		p.arc(_x, _y, MAX_RADIUS * 2 + 10, MAX_RADIUS * 2 + 10, -(float) Math.PI / 2, get_loadAngle(), PApplet.OPEN);

	}

	private void drawNavigationEllipse(PApplet p) {
		p.strokeWeight(1);
		p.stroke(_color);
		p.noFill();
		float cRadius = GetRadiusForPressure(1 - _pressure);
		p.ellipse(_x, _y, cRadius * 2, cRadius * 2);
	}

	private void drawDrawingEllipse(PApplet p, float x, float y) {
		p.noStroke();
		p.fill(_color);
		float cRadius = GetRadiusForPressure(_pressure);
		p.ellipse(x, y, cRadius * 2, cRadius * 2);
	}

	public static float GetRadiusForPressure(float pressure) {
		return (MAX_RADIUS - MIN_RADIUS) * pressure + MIN_RADIUS;
	}

	public void setState(CursorState _state) {
		this._mode = _state.get_mode();
		_handType = _state.get_handType();
		_color = _state.get_color();
		_pressure = _state.get_pressure();
	}

	private PImage getHand() {
		PImage _hand = _handType == HandType.Right ? ContentManager.GetIcon("handRight")
				: ContentManager.GetIcon("handLeft");

		return _hand;
	}

	private float get_loadAngle() {
		return (float) (loadRatio * Math.PI * 2 - Math.PI / 2);
	}

	private int getBaseColor() {
		if (_mode == CursorMode.Pressing)
			return MainView.WHITE;
		else if (_mode == CursorMode.PrePressing) {
			if (_pressure > 0.0f) {
				return MainView.LIGHT_GREY;
			} else
				return MainView.GREY;
		}else
			return MainView.WHITE;
	}

}
