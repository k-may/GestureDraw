package application.view.home.screens;

import processing.core.PApplet;
import application.view.MainView;
import application.view.image.GlowingImage;
import application.view.image.Image;
import framework.view.View;

public class HandsScreen extends View {

	private Image _hand1;
	private Image _hand2;
	private Image _hand3;

	private GlowingImage _handGlow1;
	private GlowingImage _handGlow2;
	private GlowingImage _handGlow3;

	private View _currentHand;

	private int _handCount = 0;
	private int _lastTime = 0;
	private int _duration = 1000;

	public HandsScreen() {
		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;
		_x = 0;// (MainView.SCREEN_WIDTH - _width) / 2;
		_y = 0;// (MainView.SCREEN_HEIGHT - _height) / 2;

		createChilds();
	}

	private void createChilds() {
		_hand1 = new Image("hand1");
		addChild(_hand1);

		_hand2 = new Image("hand2");
		
		addChild(_hand2);

		_hand3 = new Image("hand3");
		addChild(_hand3);
		
		setHandAlpha(10);

		_handGlow1 = new GlowingImage("hand1");
		_handGlow2 = new GlowingImage("hand2");
		_handGlow3 = new GlowingImage("hand3");

		_invalidated = true;
	}
	
	private void setHandAlpha(float alpha){
		_hand1.set_alpha(alpha);
		_hand2.set_alpha(alpha);
		_hand3.set_alpha(alpha);
	}

	@Override
	public void draw(PApplet p) {
		// TODO Auto-generated method stub

		if (_invalidated) {
			setup();
			_invalidated = false;
		}

		super.draw(p);

		int time = p.millis();
		if (time - _lastTime > _duration) {
			_lastTime = time;
			update();
		}
	}

	private void update() {
		int current = _handCount % 3;

		if (_currentHand != null)
			removeChild(_currentHand);

		switch (current) {
			case 0:
				_currentHand = _handGlow1;
				break;
			case 1:
				_currentHand = _handGlow2;
				break;
			case 2:
				_currentHand = _handGlow3;
				break;
		}

		addChild(_currentHand);

		_handCount++;
	}

	private void setup() {

		float handW = _hand1.get_width();
		float handH = _hand1.get_height();
		float xPos = (_width - handW) / 2;
		float yPos = (_height - handH)/2;
		setHandsPos(xPos, yPos);
	}

	private void setHandsPos(float xPos, float yPos) {
		setPos(_hand1, xPos, yPos);
		setPos(_hand2, xPos, yPos);
		setPos(_hand3, xPos, yPos);
		setPos(_handGlow1, xPos, yPos);
		setPos(_handGlow2, xPos, yPos);
		setPos(_handGlow3, xPos, yPos);
	}

	private void setPos(View view, float xPos, float yPos) {
		view.set_x(xPos);
		view.set_y(yPos);
	}

}
