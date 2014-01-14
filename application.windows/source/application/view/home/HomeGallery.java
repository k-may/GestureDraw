package application.view.home;

import java.awt.List;
import java.util.ArrayList;

import de.looksgood.ani.Ani;
import de.looksgood.ani.easing.Easing;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import framework.data.GalleryEntry;
import framework.view.IGallery;
import framework.view.View;

public class HomeGallery extends View implements IGallery<PImage> {

	private int _lastUpdate = 0;
	private final int TTime = 5000;
	private int _index = 0;
	private ArrayList<PImage> _images;
	private int _imageCount = 0;

	private PImage _currentImage;
	private PImage _previousImage;

	public int alpha = 255;

	public HomeGallery() {
		_images = new ArrayList<PImage>();
		_isPressTarget = false;
		_isTouchEnabled = false;
	}

	@Override
	public void draw(PApplet p) {
		// TODO Auto-generated method stub
		super.draw(p);

		if (_imageCount == 0)
			return;

		if (p.millis() - _lastUpdate > TTime) {
			_lastUpdate = p.millis();
			navigate("right");
		}

		PVector absPos = get_absPos();

		if (_previousImage != null) {
			p.image(_previousImage, absPos.x, absPos.y, _width, _height);
		}

		p.tint(255, 255, 255, alpha);
		// draw current image
		p.image(_currentImage, absPos.x, absPos.y, _width, _height);
	}

	@Override
	public void setImages(ArrayList<GalleryEntry<PImage>> entries) {

		if (entries == null)
			return;

		for (GalleryEntry<PImage> entry : entries) {
			addImage(entry);
		}

		navigate();
	}

	private void navigate() {
		navigate("");
	}

	@Override
	public void addImage(GalleryEntry<PImage> entry) {
		_images.add(entry.get_image());
		_imageCount = _images.size();
	}

	@Override
	public void navigate(String direction) {
		if (_images.size() > 1) {
			_index++;
			_index = _index % _images.size();
			if (_index < 0)
				_index += _images.size();

			if (_currentImage != null)
				_previousImage = _currentImage;

			_currentImage = _images.get(_index);

			alpha = 0;

			Ani.to(this, 1, "alpha", 255, Easing.EXPO_OUT, "onEnd:onEnd");
		}else{
			_currentImage = _images.get(_index);
		}
	}

	public void onEnd() {
		_previousImage = null;
	}
}
