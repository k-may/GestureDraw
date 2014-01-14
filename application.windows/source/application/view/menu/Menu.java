package application.view.menu;

import java.util.Observable;
import java.util.Observer;

import application.view.MainView;
import application.view.tracks.TrackView;
import framework.view.View;

public class Menu extends View implements Observer {

	public static final int BUTTON_WIDTH = 208;
	public static final int BUTTON_HEIGHT = 208;

	public static final int DividorWidth = 6;

	public static Boolean CLEARABLE = true;
	public static Boolean TRACKS = true;

	private ClearButton _clearButton;
	private GalleryButton _galleryButton;
	private SaveButton _saveButton;
	private TracksButton _tracksButton;

	public Menu() {
		init();
		createChilds();
	}

	private void init() {
		_name = "menu";
		_isTouchEnabled = false;
		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;

	}

	private void createChilds() {
		if (TRACKS) {
			_tracksButton = new TracksButton();
			addChild(_tracksButton);
		}
		
		if (CLEARABLE) {
			_clearButton = new ClearButton();
			addChild(_clearButton);
			_clearButton.set_x(_width - _clearButton.get_width());
			_clearButton.set_y(_height - _clearButton.get_height());
		}

		_galleryButton = new GalleryButton();
		addChild(_galleryButton);
		_galleryButton.set_y(_height - _galleryButton.get_height());

		_saveButton = new SaveButton();
		addChild(_saveButton);
		_saveButton.set_x(_width - _saveButton.get_width());

	}

	public TrackView get_trackView() {
		return _tracksButton.getTrackView();
	}

	public void showTracks() {

	}

	public void hideTracks() {
		_tracksButton.hideTracks();
	}

	@Override
	public void update(Observable o, Object arg) {
	}

}
