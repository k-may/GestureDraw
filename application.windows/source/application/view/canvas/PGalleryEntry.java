package application.view.canvas;

import processing.core.PImage;
import framework.data.GalleryEntry;
import framework.data.ImageEntry;

public class PGalleryEntry extends GalleryEntry<PImage> {

	public PGalleryEntry(ImageEntry entry, PImage image) {
		super(entry, image);
	}

}
