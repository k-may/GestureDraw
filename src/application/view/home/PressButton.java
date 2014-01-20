package application.view.home;

import processing.core.PApplet;
import processing.core.PVector;
import application.view.Image;
import application.view.MainView;
import application.view.menu.Menu;
import framework.Rectangle;
import framework.events.LabelButtonPressed;
import framework.events.TouchEvent;
import framework.view.View;

public class PressButton extends View {

	private Image _icon;
	private Image _text;
	private Image _bg;
	private Boolean _isOpen = false;

	public PressButton() {
		_isHoverEnabled = true;
		_width = Menu.BUTTON_WIDTH;
		_height = Menu.BUTTON_HEIGHT;

		_icon = new Image("pressIcon");
		_icon.set_x((_width - _icon.get_width()) / 2);
		_icon.set_y((_height - _icon.get_height()) / 2);
		addChild(_icon);

		_bg = new Image("shadow");
		_bg.set_width(_width);
		_bg.set_height(_height);
		//addChild(_bg);

		_text = new Image("pressText");
		_text.set_color(0xff000000);
		_text.set_y(MainView.BUTTON_TEXT_TOP);
		//addChild(_text);
	}

	@Override
	public Boolean isTouchEnabled() {
		// TODO Auto-generated method stub
		return super.isTouchEnabled();
	}
	
	@Override
	public void draw(PApplet p) {
		super.draw(p);

		PVector absPos = get_absPos();

		p.noFill();
		p.stroke(150);
		p.rect(absPos.x, absPos.y, _width, _height);

	}

	@Override
	public void handleInteraction(TouchEvent event) {
		super.handleInteraction(event);

		if (_isOpen)
			event.cancelPropogation();
	}

	@Override
	protected void onHover(TouchEvent event) {
		_icon.set_color(event.getColor());
	}

	@Override
	protected void onHoverOut(TouchEvent event) {
		_icon.set_color(MainView.ICON_COLOR);
		_isPressTarget = false;
		addChild(_icon);
		removeChild(_text);
		removeChild(_bg);
		_isOpen = false;
	}

	@Override
	protected void onHoverEnd(TouchEvent event) {
		super.onHoverEnd(event);
		_isPressTarget = true;
		addChild(_bg);
		addChild(_text);
		_isOpen = true;
	}

	@Override
	protected void onPress(TouchEvent event) {
		new LabelButtonPressed("START").dispatch();
	}
}
