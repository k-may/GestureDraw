package application.view.home.screens;

import processing.core.PApplet;
import application.view.MainView;
import application.view.PView;
import application.view.image.GlowingImage;

public class WeclomeMessage extends PView {

	public GlowingImage _title;
	private int _titlePaddingTop = 42;

	public WeclomeMessage() {

		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;
		_x = 0;
		_y = 0;

		createChilds();
	}

	private void createChilds() {

		_title = new GlowingImage("welcomeTitle", "welcomeTitleBlur");
		addChild(_title);

		_invalidated = true;
	}

	@Override
	public void draw(PApplet p) {
		if (_invalidated) {
			setup();
			_invalidated = false;  
		}
		_title.set_y(150); 
		super.draw(p);
	}

	private void setup() {
		_title.set_x((_width - _title.get_width()) / 2);
		_title.set_y(_titlePaddingTop);
	}
}
