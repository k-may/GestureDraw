package application.view;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import application.content.ContentManager;
import application.view.image.BlurImage;
import application.view.image.GlowingImage;
import application.view.image.Image;
import framework.view.View;
import gesturedraw.GestureDraw;

public class ButtonBorder extends View {

	private  Image _bottom;
	private  GlowingImage _top;
	private  PGraphics _pg;
	private int _color = MainView.WHITE;
	private Boolean _isOver = false;
	private final int BLUR_PADDING = 20;

	public ButtonBorder() {
		_width = MainView.BUTTON_WIDTH;
		_height = MainView.BUTTON_HEIGHT;
	}

	@Override
	public void draw(PApplet p) {
		if (_bottom == null)
			createTop(p);

		if (_invalidated) {
			_invalidated = false;

			if (_isOver) {
				addChild(_top);
				removeChild(_bottom);
			} else {
				addChild(_bottom);
				removeChild(_top);
			}

			_bottom.set_x(-BLUR_PADDING);
			_bottom.set_y(-BLUR_PADDING);

			_top.set_x(-BLUR_PADDING);
			_top.set_y(-BLUR_PADDING);
			_top.set_color(_color);

		}
		super.draw(p);
	}

	private void createTop(PApplet p) {

		if (_pg == null) {
			_pg = getRect(p);
			ContentManager.SetIcon("buttonBottom", _pg.get());
		}

		_bottom = new Image("buttonBottom");
		_bottom.set_color(MainView.ICON_COLOR);
		addChild(_bottom);

		_top = new GlowingImage("buttonBottom");
	}

	private PGraphics getRect(PApplet p) {
		// add padding for blur
		PGraphics pg = GestureDraw.instance.createGraphics(MainView.BUTTON_WIDTH
				+ BLUR_PADDING * 2, MainView.BUTTON_HEIGHT + BLUR_PADDING * 2);
		pg.beginDraw();
		pg.stroke(MainView.WHITE);
		pg.strokeWeight(1);
		pg.noFill();
		pg.rect(20, 20, _width, _height);
		pg.endDraw();
		return pg;
	}

	public void hoverOver() {
		_isOver = true;
		_invalidated = true;
	}

	public void hoverOut() {
		// _color = MainView.ICON_COLOR;
		_isOver = false;
		_invalidated = true;
	}

	public void set_color(int color) {
		_color = color;
		_invalidated = true;

	}
}
