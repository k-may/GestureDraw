package application.view.home;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import application.view.MainView;
import application.view.image.Image;
import application.view.scene.Scene;
import framework.data.GalleryEntry;
import framework.scenes.IHomeScene;
import framework.scenes.SceneType;
import framework.view.IGallery;
import framework.view.View;

public class HomeScene extends Scene implements IHomeScene<PImage> {

	private View _currentScreen;

	private HomeBg _bg;
	private HomeGallery _gallery;

	private Image _poweredByText;

	private HomeModel _model;

	public HomeScene() {
		super(SceneType.Home);

		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;

		createChilds();
	}

	@Override
	public void draw(PApplet p) {

		_model.update(p.millis());

		p.background(0xff000000);

		if (_invalidated) {
			float pX = (_width - _poweredByText.get_width()) / 2;
			float pY = (_height - _poweredByText.get_height());

			_poweredByText.set_x(pX);
			_poweredByText.set_y(pY);

			_invalidated = false;
		}

		super.draw(p);
	}

	private void createChilds() {

		_model = new HomeModel(this);

		_bg = new HomeBg();
		addChild(_bg);

		createGallery();

		_poweredByText = new Image("poweredByText");
		_poweredByText.set_alpha(200);
		addChild(_poweredByText);

		_invalidated = true;
	}

	private void createGallery() {

		_gallery = new HomeGallery(_model);
		addChild(_gallery);
		float galleryScale = 0.9f;

		float galleryWidth = _width * galleryScale;
		float galleryHeight = _height * galleryScale;
		float galleryX = (_width - galleryWidth) / 2;
		float galleryY = (_height - galleryHeight) / 2;

		_gallery.set_x(galleryX);
		_gallery.set_y(galleryY);
		_gallery.set_width(galleryWidth);
		_gallery.set_height(galleryHeight);

	}

	@Override
	public void setReady(Boolean value) {
		//System.out.println("\n=== HomeScreen === set ready : " + value);
		
		this._isTouchEnabled = value;
		_model.setReady(value);
	}

	public void setScreen(View screen) {
		if (_currentScreen != null) {
			removeChild(_currentScreen);
			_currentScreen = null;
		}
		if (screen != null) {
			_currentScreen = screen;
			addChild(_currentScreen);

			_currentScreen.set_x((_width - _currentScreen.get_width()) / 2);
			_currentScreen.set_y((_height - _currentScreen.get_height()) / 2);
		}
	}

	@Override
	public IGallery<PImage> getGallery() {
		return _gallery;
	}

	@Override
	public void setImages(ArrayList<GalleryEntry<PImage>> images) {
		_gallery.setImages(images);
	}

}
