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

	private PImage _currentImage;
	private PImage _previousImage;

	public int alpha = 255;

	private HomeModel _model;

	public HomeGallery(HomeModel model) {
		_model = model;
		_model.registerHomeGallery(this);

		_isPressTarget = false;
		_isTouchEnabled = false;
	}

	@Override
	public void draw(PApplet p) {
		// TODO Auto-generated method stub
		super.draw(p);

		PVector absPos = get_absPos();

		if (_previousImage != null) {
			p.image(_previousImage, absPos.x, absPos.y, _width, _height);
		}

		if (_currentImage != null) {
			p.tint(255, 255, 255, alpha);
			// draw current image
			p.image(_currentImage, absPos.x, absPos.y, _width, _height);
		}
	}

	@Override
	public void setImages(ArrayList<GalleryEntry<PImage>> entries) {
		_model.setImages(entries);
	}

	@Override
	public void addImage(GalleryEntry<PImage> entry) {
		_model.addImage(entry);
	}


	public void setImage(PImage image) {
		if (_currentImage != null)
			_previousImage = _currentImage;

		_currentImage = image;
		alpha = 0;
		Ani.to(this, 1, "alpha", 255, Easing.EXPO_OUT, "onEnd:onEnd");

	}

	public void onEnd() {
		_previousImage = null;
	}
}
