package application.view.menu;

import application.view.ButtonBorder;
import application.view.MainView;
import application.view.PButton;
import application.view.image.Image;
import framework.events.TouchEvent;

public class MenuButton extends PButton {

	protected Boolean _isOpen = false;

	protected int _iconPaddingLeft = 154;
	protected int _iconPaddingTop = 121;

	protected Image _bg;

	protected Image _icon;
	protected Image _iconLarge;
	protected Image _text;

	protected ButtonBorder _border;

	protected float _origWidth;
	protected float _origHeight;

	protected float _destWidth;
	protected float _destHeight;

	protected int _openColor;

	protected final float ICON_SCALE = 2.0f;

	public MenuButton(String icon, String iconLarge) {
		this(icon, iconLarge, "", MainView.HOVER_ELAPSE);
	}

	public MenuButton(String icon, String iconLarge, String text) {
		this(icon, iconLarge);
	}

	public MenuButton(String icon, String iconLarge, String text, int interval) {
		super(interval);
		// _isHoverEnabled = true;
		_width = MainView.BUTTON_WIDTH;
		_height = MainView.BUTTON_HEIGHT;

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

		_border = new ButtonBorder();
		_border.hoverOver();

		if (text != "") {
			_text = new Image(text);
			_text.set_color(0xff000000);
			_text.set_y(MainView.BUTTON_TEXT_TOP);
		}
	}

	@Override
	public void handleInteraction(TouchEvent event) {
		super.handleInteraction(event);

		if (_isOpen)
			event.cancelPropogation();
	}

	@Override
	protected void onHover(TouchEvent event) {
		int color = event.getColor();
		_icon.set_color(color);
		_icon.fadeIn();
		_iconLarge.set_color(color);
		_border.set_color(color);
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
		if (_text != null)
			addChild(_text);

		// by default the border is only
		// visible in the open state
		addChild(_border);
		_border.hoverOver();

		_iconLarge.fadeIn();

		removeChild(_icon);
		_isOpen = true;
	}

	protected void setClosed() {
		_isPressTarget = false;
		removeChild(_iconLarge);
		addChild(_icon);
		if (_text != null)
			removeChild(_text);
		removeChild(_bg);
		removeChild(_border);
		_isOpen = false;
	}

}
