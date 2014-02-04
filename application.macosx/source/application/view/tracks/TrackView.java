package application.view.tracks;

import java.util.ArrayList;
import java.util.Observable;

import application.audio.MinimAudioPlayer;
import application.view.MainView;
import application.view.PView;
import application.view.menu.Menu;
import framework.audio.IAudioView;
import framework.data.MusicEntry;
import framework.events.TouchEvent;
import framework.events.TracksHideEvent;
import framework.view.View;

public class TrackView extends PView implements IAudioView {

	private ArrayList<TrackEntryView> _trackViews;
	private Boolean _isExpanded = false;
	private Boolean _isShowing = false;

	public TrackView() {

	}

	@Override
	public void update(Observable arg0, Object arg1) {

		MinimAudioPlayer player = (MinimAudioPlayer) arg0;
		MusicEntry currentEntry = (MusicEntry) arg1;
		Boolean isPlaying = player.isPlaying();

		if (_trackViews == null) {
			createTracks(player);
		}

		for (TrackEntryView view : _trackViews) {
			if (currentEntry != null && view.get_entry() == currentEntry) {
				view.setPlaying(isPlaying);
			} else {
				view.setPlaying(false);
			}
		}

	}

	private void createTracks(MinimAudioPlayer player) {
		// init views
		_trackViews = new ArrayList<TrackEntryView>();
		float x = 0;
		ArrayList<MusicEntry> entries = player.get_entries();
		for (MusicEntry entry : entries) {
			TrackEntryView view = new TrackEntryView(entry);

			view.set_x(x);
			x += view.get_width();
			_trackViews.add(view);
			addChild(view);
		}

		_width = x - MainView.DividorWidth;
		_height = MainView.BUTTON_HEIGHT;

	}

	@Override
	public void show() {
		_isShowing = true;
		
		for(TrackEntryView view : _trackViews)
			view.setClosed();
	}

	@Override
	public void hide() {
		_isShowing = false;
	}

	@Override
	public void collapse() {
		_isExpanded = false;
	}

	@Override
	public void expand() {
		_isExpanded = true;
	}

	@Override
	public Boolean isPressTarget() {
		return _isExpanded;
	}

	@Override
	public Boolean isTouchEnabled() {
		// TODO Auto-generated method stub
		return _isShowing;
	}

	@Override
	protected void onHoverOut(TouchEvent event) {
		new TracksHideEvent().dispatch();
	}

}
