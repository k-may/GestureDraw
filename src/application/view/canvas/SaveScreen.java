package application.view.canvas;

import processing.core.PApplet;
import application.view.MainView;
import application.view.image.GlowingImage;
import framework.view.View;

public class SaveScreen extends View {

	private YesButton _yesButton;
	private NoButton _noButton;

	private GlowingImage _savedTitle;
	private GlowingImage _savedMessage;

	private GlowingImage _continueMessage;

	private int _duration = 2000;
	private int _lastTime;
	
	public SaveScreen() {
		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;

		createChilds();
	}

	private void createChilds() {

		_savedMessage = new GlowingImage("savedMessage");
		addChild(_savedMessage);

		_savedTitle = new GlowingImage("savedTitle");
		addChild(_savedTitle);

		_yesButton = new YesButton();
		_noButton = new NoButton();

		_continueMessage = new GlowingImage("continueMessage");

		_invalidated = true;
	}

	@Override
	public void draw(PApplet p) {
		int time = p.millis();
		
		if(_invalidated){
			setup();
			_invalidated = false;
			_lastTime = time;
		}
		
		if(time - _lastTime > _duration){
			removeChild(_savedMessage);
			removeChild(_savedTitle);
			
			addChild(_continueMessage);
			addChild(_noButton);
			addChild(_yesButton);
		}
		
		super.draw(p);
	}

	private void setup() {
		_savedMessage.set_x((_width - _savedMessage.get_width())/2);
		_savedMessage.set_y(500);
		addChild(_savedMessage);
		
		_savedTitle.set_x((_width - _savedTitle.get_width())/2);
		_savedTitle.set_y(200);
		addChild(_savedTitle);
		
		_continueMessage.set_x((_width - _continueMessage.get_width())/2);
		_continueMessage.set_y(300);
		
		_yesButton.set_x(_width/2 - MainView.BUTTON_WIDTH - 100);
		_yesButton.set_y(500);
		
		_noButton.set_x(_width/2 + 100);
		_noButton.set_y(500);
		
	}
}
