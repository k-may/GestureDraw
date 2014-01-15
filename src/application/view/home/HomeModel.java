package application.view.home;

import java.util.ArrayList;
import java.util.Observable;
import framework.data.GalleryEntry;

import processing.core.PImage;

public class HomeModel extends Observable {

	private HomeScene _home;
	private HomeGallery _gallery;

	private final int ROTATION_COUNT = 3;
	private int _rotationCount;

	private int _lastUpdate = 0;
	private final int TTime = 5000;
	private int _index = 0;
	private ArrayList<PImage> _images;
	private int _imageCount = 0;

	public HomeModel(HomeScene home) {
		_home = home;
		_images = new ArrayList<PImage>();

	}

	public void update(int millis) {

		if (_imageCount == 0)
			return;

		if (millis - _lastUpdate > TTime) {
			_lastUpdate = millis;
			navigate("right");
		}

	}

	public void registerHomeGallery(HomeGallery gallery) {
		_gallery = gallery;
	}

	private void navigate(String string) {
		if (_images.size() > 1) {
			_index++;
			_index = _index % _images.size();
			if (_index < 0)
				_index += _images.size();

		}

		_gallery.setImage(_images.get(_index));
	}

	public void setImages(ArrayList<GalleryEntry<PImage>> entries) {

		if (entries == null)
			return;

		for (GalleryEntry<PImage> entry : entries) {
			addImage(entry);
		}
		changed();
		navigate("");
	}

	public void addImage(GalleryEntry<PImage> entry) {
		_images.add(entry.get_image());
		_imageCount = _images.size();
	}
	
	private void changed() {
		this.setChanged();
		this.notifyObservers();
	}

}
