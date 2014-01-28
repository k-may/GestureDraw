package application.view.canvas;

import processing.core.PApplet;
import application.view.menu.MenuButton;
import framework.events.TouchEvent;
import framework.events.YesPressedEvent;

public class YesButton extends MenuButton {

	
	public YesButton(){
		super("yesIcon", "yesIcon", "yesText");

		_invalidated = true;
	}
	
	@Override
	public void draw(PApplet p) {
		// TODO Auto-generated method stub
		super.draw(p);

		if (_invalidated) {
			float xPos = (_width - _icon.get_width()) / 2;
			float yPos = (_height - _icon.get_height()) / 2;
			_icon.set_x(xPos);
			_iconLarge.set_x(xPos);
			
			_icon.set_y(yPos);
			_iconLarge.set_y(yPos);
			
			_invalidated = false;
		}
	}
	
	@Override
	protected void onPress(TouchEvent event) {
		// TODO Auto-generated method stub
		super.onPress(event);

		if (_isOpen) {
			new YesPressedEvent().dispatch();
			setClosed();
		}
	}
}
