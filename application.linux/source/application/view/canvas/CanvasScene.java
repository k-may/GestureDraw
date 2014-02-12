package application.view.canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observer;

import processing.core.PApplet;
import processing.core.PImage;
import application.interaction.RegionType;
import application.view.MainView;
import application.view.menu.Menu;
import application.view.scene.Scene;
import framework.audio.IAudioView;
import framework.data.GalleryEntry;
import framework.data.ImageEntry;
import framework.scenes.SceneManager;
import framework.scenes.SceneType;
import framework.stroke.ICanvas;
import framework.view.ICanvasScene;

public class CanvasScene extends Scene implements ICanvasScene<PImage> {

	private Menu _menu;
	private Canvas _canvas;
	private SaveScreen _saveScreen;
	private Boolean _isSaving = false;

	public CanvasScene() {
		super(SceneType.Canvas);

		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;

		createChilds();
	}

	private void createChilds() {

		if (MainView.REGION_TYPE == RegionType.GestDomain)
			_canvas = new CanvasGL();
		else
			_canvas = new Canvas();

		_canvas.set_width(_width);
		_canvas.set_height(_height);
		addChild(_canvas);

		_menu = new Menu();
		addChild(_menu);

		_saveScreen = new SaveScreen();

	}

	@Override
	public void draw(PApplet p) {

		p.background(0x000);
		super.draw(p);
	}

	public ICanvas<PImage> getCanvas() {
		return _canvas;
	}

	@Override
	public IAudioView get_audioView() {
		// TODO Auto-generated method stub
		return _menu.get_trackView();
	}

	@Override
	public void showTracks() {
		_menu.get_trackView().show();
		_menu.showTracks();
	}

	@Override
	public void hideTracks() {
		_menu.get_trackView().hide();
		_menu.hideTracks();
	}

	@Override
	public GalleryEntry<PImage> save(String filePath, String date) {

		ImageEntry entry = new ImageEntry(filePath, "", new String[] { "me" }, date);

		GalleryEntry<PImage> galleryEntry = new PGalleryEntry(entry, _canvas.getImage());// KinectApp.instance.loadImage(filePath));
		_canvas.save(filePath);

		return galleryEntry;
	}

	@Override
	public ArrayList<Observer> get_audioObservers() {
		return new ArrayList<Observer>(Arrays.asList(_menu));
	}

	@Override
	public void clearCanvas() {
		_canvas.clear();
	}

	@Override
	public void set_isSaving(Boolean value) {
		_isSaving = value;
		if (_isSaving) {
			_canvas.set_alpha(50);
			_saveScreen = new SaveScreen();
			removeChild(_menu);
			addChild(_saveScreen);
		} else {
			_canvas.set_alpha(255);
			addChild(_menu);
			removeChild(_saveScreen);
			_saveScreen = null;
		}

		SceneManager.getInstance().invalidate();
	}

	@Override
	public Boolean get_isSaving() {
		return _isSaving;
	}

	@Override
	public ICanvas get_canvas() {
		return _canvas;
	}

}
