package application.view.menu;

import framework.events.ClearCanvasEvent;
import framework.events.TouchEvent;

public class ClearButton extends MenuButton {

	public ClearButton() {
		super("backIcon", "backIconLarge", "clearText");
		_iconPaddingLeft = 154;
		_iconPaddingTop = 121;
		_icon.set_x(_iconPaddingLeft);
		_icon.set_y(_iconPaddingTop);
	}

	@Override
	protected void onPress(TouchEvent event) {
		// TODO Auto-generated method stub
		super.onPress(event);

		if (_isOpen) {
			new ClearCanvasEvent().dispatch();
			setClosed();
		}

	}
}
