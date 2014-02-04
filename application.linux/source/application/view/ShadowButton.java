package application.view;

import application.view.image.Image;
import framework.events.TouchEvent;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class ShadowButton extends PView {

	protected Image _bg;
	protected Boolean _isOver = false;
	protected int _color = 0xffffffff;
	
	protected int _grey = 0xff333333;

	public ShadowButton() {
		
		_bg = new Image("shadow");
		addChild(_bg);
	}


	@Override
	public void draw(PApplet p) {


		PVector absPos = get_absPos();

		p.noTint();
		super.draw(p);
		
		if (_isOver) {
			p.noFill();
			p.noTint();
			p.stroke(_color);
			p.rect(absPos.x, absPos.y, _width, _height);
		}

	}

	@Override
	protected void onHover(TouchEvent event) {
		_isOver = true;
		_color = Image.SetAlpha(event.getUser().getColor(), 255);
	}
	
	@Override
	protected void onHoverOut(TouchEvent event) {
		_isOver = false;
		_color = MainView.ICON_COLOR;
	}
	
	
}
