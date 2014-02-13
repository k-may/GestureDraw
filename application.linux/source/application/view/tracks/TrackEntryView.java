package application.view.tracks;

import processing.core.PApplet;
import application.content.ContentManager;
import application.view.MainView;
import application.view.image.Image;
import application.view.labels.LabelView;
import application.view.menu.MenuButton;
import framework.data.MusicEntry;
import framework.events.PauseTrackEvent;
import framework.events.PlayTrackEvent;
import framework.events.TouchEvent;

public class TrackEntryView extends MenuButton { // ShadowButton {
	private MusicEntry _entry;

	private Boolean _isActive = false;

	private LabelView _trackLabel;
	private LabelView _artistLabel;

	private Image _playIcon;
	private Image _pauseIcon;
	// private Image _playText;
	// private Image _pauseText;

	private Boolean _isPlaying = false;

	private int _textPaddingTop = 116;
	private int _trackPaddingTop = 146;
	private int _artistPaddingTop = 165;

	public int alpha = 0x00;

	public TrackEntryView(MusicEntry entry) {
		super("playIcon", "playIcon", "playText");
		_entry = entry;

		_width += MainView.DividorWidth;

		// override the default border behavior, we want to always show the
		// border
		// for the tracks
		addChild(_border);
		_border.hoverOut();

		createChilds();
	}

	private void createChilds() {

		_playIcon = new Image("playIcon");
		_playIcon.set_color(MainView.ICON_COLOR);
		_playIcon.set_x((_width - _playIcon.get_width()) / 2);
		_playIcon.set_y((_height - _playIcon.get_height()) / 2);

		_pauseIcon = new Image("pauseIcon");
		_pauseIcon.set_color(MainView.ICON_COLOR);
		_pauseIcon.set_x((_width - _pauseIcon.get_width()) / 2);
		_pauseIcon.set_y((_height - _pauseIcon.get_height()) / 2);
		/*
		 * _playText = new Image("playText"); _playText.set_color(0xff000000);
		 * _playText.set_y(_textPaddingTop); _pauseText = new
		 * Image("pauseText"); _pauseText.set_color(0xff000000);
		 * _pauseText.set_y(_textPaddingTop);
		 */
	}

	public MusicEntry get_entry() {
		return _entry;
	}

	public void setPlaying(Boolean isPlaying) {

		_isPlaying = isPlaying;
		removeChild(_icon);
		_icon = _isPlaying ? _playIcon : _pauseIcon;
		addChild(_icon);

		_invalidated = true;

	}

	@Override
	public void draw(PApplet p) {
		if (_invalidated) {
			if (_trackLabel == null)
				createLabels(p);

			_invalidated = false;
		}

		super.draw(p);
	}

	private void createLabels(PApplet p) {
		_trackLabel = new LabelView(_entry.trackName, MainView.TEXT_COLOR, ContentManager.GetFont("smallItalic"));
		addChild(_trackLabel);
		_trackLabel.set_y(5);
		_trackLabel.set_x(5);

		_artistLabel = new LabelView(_entry.artist, MainView.TEXT_COLOR, ContentManager.GetFont("small"));
		addChild(_artistLabel);
		_artistLabel.set_x(5);
		_artistLabel.set_y(25);

	}

	@Override
	protected void onHoverEnd(TouchEvent event) {
		// System.out.println("hoverEnd : " + _artistLabel.get_text() + " : "
		// + event.get_time());
		super.onHoverEnd(event);

		_openColor = event.getColor();
		setOpen();
	}

	@Override
	protected void onHover(TouchEvent event) {
		int color = event.getColor();
		_playIcon.set_color(color);
		_pauseIcon.set_color(color);
		_icon.set_color(color);
		_border.set_color(color);
	}

	@Override
	protected void onHoverOut(TouchEvent event) {
		_icon.set_color(MainView.ICON_COLOR);
		_playIcon.set_color(MainView.ICON_COLOR);
		_pauseIcon.set_color(MainView.ICON_COLOR);
		_artistLabel.set_color(MainView.TEXT_COLOR);
		_trackLabel.set_color(MainView.TEXT_COLOR);

		setClosed();
	}

	@Override
	protected void setClosed() {
		// System.out.println("closed : " + _artistLabel.get_text());
		_isPressTarget = false;
		//removeChild(_text);
		removeChild(_bg);
		_isOpen = false;

		_border.hoverOut();

	}

	@Override
	protected void setOpen() {
		_isPressTarget = true;
		addChild(_bg);
		addChild(_artistLabel);
		addChild(_trackLabel);
		_trackLabel.set_color(225);
		_artistLabel.set_color(225);
		//addChild(_text);
		addChild(_icon);
		// by default the border is only
		// visible in the open state
		addChild(_border);
		_border.hoverOver();
		_isOpen = true;
	}

	@Override
	protected void onPress(TouchEvent event) {
		if (!_isPlaying)
			new PlayTrackEvent(_entry).dispatch();
		else
			new PauseTrackEvent(_entry).dispatch();
	}

}
