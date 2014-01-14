package application.view.menu;

import application.view.Image;
import application.view.MainView;
import application.view.tracks.TrackView;
import framework.events.TracksShowEvent;
import framework.events.TouchEvent;
import framework.view.View;

public class TracksButton extends View {

	private Boolean _tracksOpen = false;
	private Image _icon;
	private TrackView _tracksView;
	private int _iconPaddingTop = -6;
	private int _iconPaddingLeft = 0;

	public TracksButton() {
		_isPressTarget = false;
		_isHoverEnabled = true;
		_width = Menu.BUTTON_WIDTH;
		_height = Menu.BUTTON_HEIGHT;
		_icon = new Image("musicIcon");
		addChild(_icon);
		_icon.set_y(_iconPaddingTop);
		_icon.set_x(_iconPaddingLeft);
		_icon.set_color(MainView.ICON_COLOR);
		
		_tracksView = new TrackView();
	}

	@Override
	public void handleInteraction(TouchEvent event) {
		// TODO Auto-generated method stub
		super.handleInteraction(event);

		//if (_tracksOpen)
			//event.cancelPropogation();
	}
	
	@Override
	protected void onHover(TouchEvent event) {
		_icon.set_color(event.getColor());
	}

	@Override
	protected void onHoverOut(TouchEvent event) {
		_icon.set_color(MainView.ICON_COLOR);
		hideTracks();
	}

	@Override
	protected void onHoverEnd(TouchEvent event) {
		new TracksShowEvent().dispatch();
		showTracks();
	}

	public TrackView getTrackView() {
		return _tracksView;
	}

	public void hideTracks() {
		removeChild(_tracksView);
		addChild(_icon);
		_width = Menu.BUTTON_WIDTH;
		_isHoverEnabled = true;
		_tracksOpen = false;
	}

	public void showTracks() {
		_isHoverEnabled = true;
		removeChild(_icon);
		addChild(_tracksView);
		_tracksOpen = true;
		_width = _tracksView.get_width();
	}
}
