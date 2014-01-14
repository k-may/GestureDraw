package application.view.gallery;

import framework.events.GalleryNavigationEvent;
import framework.events.TouchEvent;
import framework.view.View;
import application.view.Image;
import application.view.MainView;
import processing.core.PApplet;
import processing.core.PVector;

public class NavButton extends View {

	private Boolean _isOver = false;

	private Image _arrow;
	private Image _bg;
	private int _color = 0xff000000;
	private String _direction;

	public NavButton(String direction) {
		_direction = direction;
		_width = 82;
		_height = 197;
		
		_isTouchEnabled = true;
		_isPressTarget = true;
		_isHoverEnabled = false;
		
		createChilds();
	}

	private void createChilds() {
		if (_direction == "right") {
			_arrow = new Image("navigationRightIcon");
			_bg = new Image("shadowRight");
		} else {
			_arrow = new Image("navigationLeftIcon");
			_bg = new Image("shadowLeft");
		}

		_arrow.set_color(_color);

		addChild(_bg);
		addChild(_arrow);

	}

	@Override
	public void draw(PApplet p) {
		super.draw(p);

		if (_isOver) {
			PVector absPos = get_absPos();
			p.noFill();
			p.stroke(_color);
			p.rect(absPos.x, absPos.y, _width, _height);
		}
	}

	@Override
	protected void onPress(TouchEvent event) {
		new GalleryNavigationEvent(_direction).dispatch();
		onHoverOut(null);
	}

	@Override
	protected void onHover(TouchEvent event) {
		_isOver = true;
		_color = event.getUser().getColor();
		_arrow.set_color(_color);
	}

	@Override
	protected void onHoverOut(TouchEvent event) {
		_isOver = false;
		_color = 0xff000000;
		_arrow.set_color(_color);
	}
}
