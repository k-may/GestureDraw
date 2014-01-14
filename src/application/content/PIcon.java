package application.content;

import framework.data.AssetEntry;
import processing.core.PImage;

public class PIcon {

	private AssetEntry _entry;
	private PImage _image;

	public PIcon(AssetEntry entry, PImage object) {
		_entry = entry;
		_image = object;
	}

	public PImage get_image() {
		return _image;
	}

	public String get_name() {
		return _entry.get_name();
	}

}
