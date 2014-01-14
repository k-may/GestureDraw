package application.view.menu;

import processing.core.PApplet;
import processing.core.PVector;
import application.view.Image;
import application.view.MainView;
import framework.events.TouchEvent;
import framework.view.View;

public class MenuButton extends View {

	protected Boolean _isOpen = false;

	protected int _iconPaddingLeft = 154;
	protected int _iconPaddingTop = 121;

	protected Image _bg;

	protected Image _icon;
	protected Image _iconLarge;
	protected Image _text;

	protected float _origWidth;
	protected float _origHeight;

	protected float _destWidth;
	protected float _destHeight;

	protected int _openColor;

	protected final float ICON_SCALE = 2.0f;

	public MenuButton(String icon, String iconLarge, String text) {
		_isHoverEnabled = true;
		_width = Menu.BUTTON_WIDTH;
		_height = Menu.BUTTON_HEIGHT;

		_icon = new Image(icon);
		_icon.set_color(MainView.ICON_COLOR);

		addChild(_icon);

		_iconLarge = new Image(iconLarge);
		_iconLarge.set_x((_width - _iconLarge.get_width()) / 2);
		_iconLarge.set_y((_height - _iconLarge.get_height()) / 2);

		_origWidth = _icon.get_width();
		_origHeight = _icon.get_height();

		_destWidth = _origWidth * ICON_SCALE;
		_destHeight = _origHeight * ICON_SCALE;

		_bg = new Image("shadow");
		_bg.set_width(_width);
		_bg.set_height(_height);

		_text = new Image(text);
		_text.set_color(0xff000000);
		_text.set_y(MainView.BUTTON_TEXT_TOP);

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
		_icon.fadeIn();
		_iconLarge.set_color(event.getColor());
	}

	@Override
	protected void onHoverOut(TouchEvent event) {
		_icon.set_color(MainView.ICON_COLOR);
		setClosed();
	}

	@Override
	protected void onHoverEnd(TouchEvent event) {
		super.onHoverEnd(event);

		_openColor = event.getColor();
		setOpen();
	}

	protected void setOpen() {
		_isPressTarget = true;
		addChild(_bg);
		addChild(_iconLarge);
		addChild(_text);
		
		_iconLarge.fadeIn();
		
		removeChild(_icon);
		_isOpen = true;
	}

	protected void setClosed() {
		_isPressTarget = false;
		removeChild(_iconLarge);
		addChild(_icon);
		removeChild(_text);
		removeChild(_bg);
		_isOpen = false;
	}

	@Override
	public void draw(PApplet p) {
		super.draw(p);

		PVector absPos = get_absPos();

		if (_isOpen) {
			p.noFill();
			p.stroke(_openColor);
			p.rect(absPos.x, absPos.y, _width, _height);
		}
	}
}
