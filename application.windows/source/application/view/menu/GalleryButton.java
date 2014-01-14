package application.view.menu;

import framework.events.GallerySelectedEvent;
import framework.events.TouchEvent;

public class GalleryButton extends MenuButton {


	public GalleryButton() {
		super("galleryIcon","galleryIconLarge", "galleryText");
		_iconPaddingLeft = 10;
		_iconPaddingTop = 121;
		_icon.set_x(_iconPaddingLeft);
		_icon.set_y(_iconPaddingTop);
	}

	@Override
	protected void onPress(TouchEvent event) {
		if (_isOpen) {
			new GallerySelectedEvent().dispatch();
			setClosed();
		}
	}

}
