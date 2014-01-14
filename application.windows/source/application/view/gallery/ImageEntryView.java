package application.view.gallery;
import application.view.Image;
import application.view.canvas.PGalleryEntry;
import processing.core.PImage;

public class ImageEntryView extends Image{

	private PGalleryEntry _entry;
	
	public ImageEntryView(PGalleryEntry entry) {
		super("");
		_entry = entry;
	}
	
	@Override
	public void set_width(float width) {
		// TODO Auto-generated method stub
		super.set_width(width);
		_scaleY = _scaleX;
	}
	
	@Override
	public void set_height(float height) {
		// TODO Auto-generated method stub
		super.set_height(height);
		_scaleX = _scaleY;
	}
	
	@Override
	protected PImage getImage() {
		return _entry.get_image();
	}

}
