package application.view.home;

import java.util.ArrayList;
import java.util.Observable;

import application.view.home.screens.HandsScreen;
import application.view.home.screens.HomeScreen;
import application.view.home.screens.StartScreen;
import application.view.home.screens.WelcomeMessage;
import framework.data.GalleryEntry;
import framework.view.View;

import processing.core.PImage;

public class HomeModel extends Observable {

	private HomeScene _home;
	private HomeGallery _gallery;
	private HandsScreen _handsScreen;

	private final int ROTATION_COUNT = 3;
	private int _rotationCount = 0;

	private int _lastUpdate = 0;
	private final int TTime = 6000;
	private int _index = 0;
	private ArrayList<PImage> _images;
	private int _imageCount = 0;

	private static ArrayList<View> _screens;
	private int _screenCount = 0;

	private View _welcomeScreen;
	private static View _startScreen;
	private Boolean _ready = false;

	public HomeModel(HomeScene home) {
		_home = home;
		_images = new ArrayList<PImage>();
		initScreens();
	}

	private void initScreens() {
		_startScreen = new StartScreen();
		RegisterStartScreen((HomeScreen) _startScreen);
		_welcomeScreen = new WelcomeMessage();
		RegisterScreen((HomeScreen) _welcomeScreen);
		_handsScreen = new HandsScreen();
		RegisterScreen(_handsScreen);
	}

	public void update(int millis) {

		if (millis - _lastUpdate > TTime) {
			_lastUpdate = millis;
			if (_imageCount == 0) {
				nextScreen();
			} else {
				update();
			}
		}
	}

	private void update() {

		nextImage();

		if (_rotationCount == 0) {
			nextScreen();
			_rotationCount = ROTATION_COUNT;
		} else
			clearScreen();

		_rotationCount--;
	}

	private void clearScreen() {
		if (!_ready)
			_home.setScreen(null);
	}

	private void nextImage() {
		if (_images.size() > 1) {
			_index++;
			_index = _index % _images.size();
			if (_index < 0)
				_index += _images.size();

		}

		_gallery.setImage(_images.get(_index));
	}

	private void nextScreen() {
		if (!_ready) {
			_screenCount++;
			_home.setScreen(_screens.get(_screenCount % _screens.size()));
		} else
			_home.setScreen(_startScreen);

	}

	public void registerHomeGallery(HomeGallery gallery) {
		_gallery = gallery;
	}

	public void setImages(ArrayList<GalleryEntry<PImage>> entries) {

		if (entries == null)
			return;

		for (GalleryEntry<PImage> entry : entries) {
			addImage(entry);
		}
		changed();
		update();
	}

	public void addImage(GalleryEntry<PImage> entry) {
		_images.add(entry.get_image());
		_imageCount = _images.size();
	}

	private void changed() {
		this.setChanged();
		this.notifyObservers();
	}

	public void setReady(Boolean value) {
		Boolean changed = value != _ready;
		_ready = value;

		if (changed)
			nextScreen();
	}

	public static void RegisterStartScreen(HomeScreen screen) {
		_startScreen = screen;
		RegisterScreen(screen);
	}

	public static void RegisterScreen(HomeScreen screen) {
		if (_screens == null)
			_screens = new ArrayList<View>();

		if (!_screens.contains(screen))
			_screens.add(screen);
	}
}
