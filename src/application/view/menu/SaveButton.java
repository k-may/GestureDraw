package application.view.menu;

import framework.events.SaveCanvasEvent;
import framework.events.TouchEvent;

public class SaveButton extends MenuButton {

	public SaveButton() {
		super("saveIcon","saveIconLarge", "saveText", 2000);
		_iconPaddingLeft = 151;
		_iconPaddingTop = -6;
		_icon.set_x(_iconPaddingLeft);
		_icon.set_y(_iconPaddingTop);
	}

	@Override
	protected void onHoverEnd(TouchEvent event) {
		new SaveCanvasEvent().dispatch();
		setClosed();
	}
	
	@Override
	protected void onPress(TouchEvent event) {
		if (_isOpen) {
			new SaveCanvasEvent().dispatch();
			setClosed();
		}
	}

}
