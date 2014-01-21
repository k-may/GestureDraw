package application.view.home;

import processing.core.PApplet;
import framework.view.View;
import gesturedraw.GestureDraw;
import application.AppBuilder;
import application.interaction.RegionType;
import application.view.MainView;
import application.view.image.GlowingImage;

public class WeclomeMessage extends View {

	public GlowingImage _title;
	public GlowingImage _message;

	private int _titlePaddingTop = 42;
	private int _messagePaddingTop = 280;

	public WeclomeMessage() {

		_width = MainView.MESSAGE_WIDTH;
		_height = MainView.MESSAGE_HEIGHT;
		_x = (MainView.SCREEN_WIDTH - _width) / 2;
		_y = (MainView.SCREEN_HEIGHT - _height) / 2;

		createChilds();
	}

	private void createChilds() {

		_title = new GlowingImage("welcomeTitle");
		addChild(_title);

		String welcomeMsg = MainView.REGION_TYPE == RegionType.SimpleOpenNI ? "welcomeMessageWave"
				: "welcomeMessagePress";
		_message = new GlowingImage(welcomeMsg);
		addChild(_message);

		_invalidated = true;
	}

	@Override
	public void draw(PApplet p) {
		if (_invalidated) {
			setup();
			_invalidated = false;
		}

		super.draw(p);
	}

	private void setup() {
		_title.set_x((_width - _title.get_width()) / 2);
		_title.set_y(_titlePaddingTop);

		_message.set_x((_width - _message.get_width()) / 2);
		_message.set_y(_messagePaddingTop);
	}
}
