package application.view.canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observer;

import framework.audio.IAudioView;
import framework.data.GalleryEntry;
import framework.data.ImageEntry;
import framework.scenes.SceneType;
import framework.stroke.ICanvas;
import framework.view.CanvasState;
import framework.view.ICanvasScene;
import framework.view.IGallery;
import application.view.MainView;
import application.view.gallery.GalleryView;
import application.view.menu.Menu;
import application.view.scene.Scene;
import processing.core.PApplet;
import processing.core.PImage;

public class CanvasScene extends Scene implements ICanvasScene<PImage> {

	private GalleryView _gallery;

	private Menu _menu;
	private Canvas _canvas;

	public CanvasScene() {
		super(SceneType.Canvas);

		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;

		createChilds();
	}

	private void createChilds() {

		_canvas = new Canvas();
		_canvas.set_width(_width);
		_canvas.set_height(_height);
		// addChild(_canvas);

		_gallery = new GalleryView();
		addChild(_gallery);
		// if (_menu == null) {
		_menu = new Menu();
		// addChild(_menu);
		// }

	}

	@Override
	public void draw(PApplet p) {

		p.background(0x000);
		super.draw(p);
	}

	public ICanvas<PImage> getCanvas() {
		return _canvas;
	}

	public IGallery<PImage> getGallery() {
		return _gallery;
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
	public void showGallery() {
		addChild(_gallery);
		removeChild(_canvas);
		removeChild(_menu);
	}

	@Override
	public void hideGallery() {
		addChild(_canvas);
		addChild(_menu);
		removeChild(_gallery);
	}

	@Override
	public void navigate(String direction) {
		_gallery.navigate(direction);
	}

	@Override
	public GalleryEntry<PImage> save(String filePath, String date) {

		ImageEntry entry = new ImageEntry(filePath, "", new String[] { "me" }, date);

		GalleryEntry<PImage> galleryEntry = new PGalleryEntry(entry, _canvas.getImage());// KinectApp.instance.loadImage(filePath));
		_gallery.addImage(galleryEntry);
		_canvas.save(filePath);
		
		return galleryEntry;
	}

	@Override
	public void setImages(ArrayList<GalleryEntry<PImage>> images) {
		_gallery.setImages(images);
	}

	@Override
	public ArrayList<Observer> get_audioObservers() {
		return new ArrayList<Observer>(Arrays.asList(_menu));
	}

	@Override
	public void clearCanvas() {
		_canvas.clear();
	}


}
