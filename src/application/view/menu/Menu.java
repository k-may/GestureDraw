package application.view.menu;

import java.util.Observable;
import java.util.Observer;

import application.view.MainView;
import application.view.PView;
import application.view.tracks.TrackView;

public class Menu extends PView implements Observer {

	private ClearButton _clearButton;
	private SaveButton _saveButton;
	private TracksButtonCont _tracksButton;

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
		if (MainView.TRACKS) {
			_tracksButton = new TracksButtonCont();
			addChild(_tracksButton);
		}
		
		if (MainView.CLEARABLE) {
			_clearButton = new ClearButton();
			addChild(_clearButton);
			_clearButton.set_x(_width - _clearButton.get_width());
			_clearButton.set_y(_height - _clearButton.get_height());
		}

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
