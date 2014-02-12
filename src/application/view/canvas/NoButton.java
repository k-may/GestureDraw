package application.view.canvas;

import processing.core.PApplet;
import application.view.menu.MenuButton;
import framework.events.NoPressedEvent;
import framework.events.TouchEvent;

public class NoButton extends MenuButton {

	public NoButton() {
		super("noIcon", "noIcon", "noText");
		
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
	protected void onHoverEnd(TouchEvent event) {
		// TODO Auto-generated method stub
		super.onHoverEnd(event);
		new NoPressedEvent().dispatch();
		setClosed();
	}
	@Override
	protected void onPress(TouchEvent event) {
		// TODO Auto-generated method stub
		super.onPress(event);

		if (_isOpen) {
			new NoPressedEvent().dispatch();
			setClosed();
		}
	}
	

}
