package application.view.home.screens;

import processing.core.PApplet;
import application.view.MainView;
import application.view.image.GlowingImage;

public class StartMessage extends HomeScreen{

	public GlowingImage _title;
	private PressButton _pressButton;

	public StartMessage() {
		super();
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

}
