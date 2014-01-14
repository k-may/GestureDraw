package application.view.tracks;

import application.content.ContentManager;
import application.view.Image;
import application.view.MainView;
import application.view.ShadowButton;
import application.view.labels.LabelView;
import application.view.menu.Menu;
import de.looksgood.ani.Ani;
import framework.data.MusicEntry;
import framework.events.PauseTrackEvent;
import framework.events.PlayTrackEvent;
import framework.events.TouchEvent;
import framework.view.View;
import processing.core.PApplet;

import static processing.core.PApplet.println;

public class TrackEntryView extends ShadowButton {
	private MusicEntry _entry;

	private Boolean _isActive = false;

	private LabelView _trackLabel;
	private LabelView _artistLabel;

	private Image _playIcon;
	private Image _pauseIcon;
	private Image _playText;
	private Image _pauseText;

	private Boolean _isPlaying = false;

	private int _textPaddingTop = 116;
	private int _trackPaddingTop = 146;
	private int _artistPaddingTop = 165;

	public int alpha = 0x00;

	public TrackEntryView(MusicEntry entry) {
		super();
		_entry = entry;
		

		createChilds();
	}

	private void createChilds() {

		_width = Menu.BUTTON_WIDTH;
		_height = Menu.BUTTON_HEIGHT;

		set_height(_height);

		_bg.set_width(_width);
		_bg.set_height(_height);

		_playIcon = new Image("playIcon");
		_playIcon.set_color(MainView.ICON_COLOR);
		_playIcon.set_x((_width - _playIcon.get_width()) / 2);
		_playIcon.set_y((_height - _playIcon.get_height()) / 2);

		_pauseIcon = new Image("pauseIcon");
		_pauseIcon.set_color(MainView.ICON_COLOR);
		_pauseIcon.set_x((_width - _pauseIcon.get_width()) / 2);
		_pauseIcon.set_y((_height - _pauseIcon.get_height()) / 2);

		_playText = new Image("playText");
		_playText.set_color(0xff000000);
		_playText.set_y(_textPaddingTop);
		_pauseText = new Image("pauseText");
		_pauseText.set_color(0xff000000);
		_pauseText.set_y(_textPaddingTop);

		_width +=  Menu.DividorWidth;
		_invalidated = true;
	}

	public MusicEntry get_entry() {
		// TODO Auto-generated method stub
		return _entry;
	}

	public void setActive(boolean b) {
		_isActive = b;
	}

	public void setPlaying(Boolean isPlaying) {
		_isPlaying = isPlaying;

		if (!_isPlaying) {
			addChild(_playText);
			addChild(_playIcon);

			removeChild(_pauseIcon);
			removeChild(_pauseText);
		} else {
			removeChild(_playIcon);
			removeChild(_playText);

			addChild(_pauseText);
			addChild(_pauseIcon);
		}

		if (_trackLabel != null)
			addChild(_trackLabel);

		if (_artistLabel != null)
			addChild(_artistLabel);
	}

	@Override
	public void draw(PApplet p) {
		if (_invalidated) {
			_trackLabel = new LabelView(_entry.trackName, MainView.TEXT_COLOR, ContentManager.GetFont("smallItalic"));
			addChild(_trackLabel);
			_trackLabel.set_y(_trackPaddingTop);
			_trackLabel.set_x(_width - _trackLabel.get_width() - 5);

			_artistLabel = new LabelView(_entry.artist, MainView.TEXT_COLOR, ContentManager.GetFont("small"));
			addChild(_artistLabel);
			_artistLabel.set_x(_width - _artistLabel.get_width() - 5);
			_artistLabel.set_y(_artistPaddingTop);
			_invalidated = false;
		}

		super.draw(p);
	}

	@Override
	public Boolean isPressTarget() {
		return true;
	}

	@Override
	protected void onHover(TouchEvent event) {
		super.onHover(event);
		//println("hover track view");
		_playIcon.set_color(_color);
		_pauseIcon.set_color(_color);
		_artistLabel.set_color(_color);
		_trackLabel.set_color(_color);
	}

	@Override
	protected void onHoverOut(TouchEvent event) {
		// TODO Auto-generated method stub
		super.onHoverOut(event);
		println("hover out!");
		_playIcon.set_color(_color);
		_pauseIcon.set_color(_color);
		_artistLabel.set_color(MainView.TEXT_COLOR);
		_trackLabel.set_color(MainView.TEXT_COLOR);
	}

	@Override
	protected void onPress(TouchEvent event) {
		if (!_isPlaying)
			new PlayTrackEvent(_entry).dispatch();
		else
			new PauseTrackEvent(_entry).dispatch();
	}

}
