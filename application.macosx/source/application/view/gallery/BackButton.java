package application.view.gallery;

import framework.events.BackEvent;
import framework.events.TouchEvent;
import framework.view.View;
import application.view.PView;
import application.view.image.Image;
import processing.core.PApplet;
import processing.core.PVector;

public class BackButton extends PView {

	private Image _bg;
	private Image _text;
	private Image _icon;
	private Boolean _isOver = false;
	private int _color = 0xff000000;

	public BackButton() {

		_width = 194;
		_height = 81;

		_isTouchEnabled = true;
		_isPressTarget = true;
		_isHoverEnabled = false;
		
		createChilds();
	}

	private void createChilds() {
		_bg = new Image("shadowLeft");
		_bg.set_width(_width);
		_bg.set_height(_height);
		addChild(_bg);

		_text = new Image("backText");
		_text.set_color(0xff000000);
		addChild(_text);

		_icon = new Image("backIcon");
		_icon.set_x(140);
		_icon.set_color(0xff000000);
		addChild(_icon);

	}

	@Override
	public void draw(PApplet p) {
		// TODO Auto-generated method stub
		super.draw(p);

		if (_isOver) {
			PVector absPos = get_absPos();
			p.rect(absPos.x, absPos.y, _width, _height);
		}
	}

	@Override
	protected void onPress(TouchEvent event) {
		new BackEvent().dispatch();
	}

	@Override
	protected void onHover(TouchEvent event) {
		_isOver = true;
		_color = event.getUser().getColor();
		_icon.set_color(event.getUser().getColor());
		_icon.fadeIn();
	}

	@Override
	protected void onHoverOut(TouchEvent event) {
		_isOver = false;

		_icon.set_color(0xff000000);
	}
}
