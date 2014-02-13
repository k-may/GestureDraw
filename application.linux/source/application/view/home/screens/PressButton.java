package application.view.home.screens;

import processing.core.PApplet;
import application.view.menu.MenuButton;
import framework.events.LabelButtonPressed;
import framework.events.TouchEvent;

public class PressButton extends MenuButton {

	public PressButton() {
		super("pressIcon", "pressIcon", "pressText", 2000);
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
		new LabelButtonPressed("START").dispatch();
	}

}
