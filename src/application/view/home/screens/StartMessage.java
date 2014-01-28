package application.view.home.screens;

import processing.core.PApplet;
import application.view.MainView;
import application.view.image.GlowingImage;
import framework.events.TouchEvent;
import framework.view.View;

public class StartMessage extends View {

	public GlowingImage _title;
	private int _titlePaddingTop = 53;
	private PressButton _pressButton;
	private int _buttonPaddingTop = 254;

	public StartMessage() {
		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;
		_x = (MainView.SCREEN_WIDTH - _width) / 2;
		_y = (MainView.SCREEN_HEIGHT - _height) / 2;

		createChilds();
	}

	private void createChilds() {
		_title = new GlowingImage("startTitle", "startTitleBlur");
		addChild(_title);

		_pressButton = new PressButton();
		addChild(_pressButton);

		_invalidated = true;
	}

	@Override
	public void draw(PApplet p) {
		if (_invalidated) {
			_invalidated = true;
			setup();
		}


		
		super.draw(p);
	}

	private void setup() {

		_title.set_x((_width - _title.get_width()) / 2);
		_pressButton.set_x((_width - MainView.BUTTON_WIDTH) / 2);
		
		_title.set_y(_height/2 - _title.get_height());
		_pressButton.set_y(_height/2);
	}

	@Override
	public void handleInteraction(TouchEvent event) {
		// TODO Auto-generated method stub
		super.handleInteraction(event);
	}

	@Override
	public Boolean isTouchEnabled() {
		// TODO Auto-generated method stub
		return super.isTouchEnabled();
	}
}