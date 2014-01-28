package application.view.menu;

import static processing.core.PApplet.println;
import framework.data.MusicEntry;
import framework.events.TracksShowEvent;
import framework.events.TouchEvent;
import application.content.ContentManager;
import application.view.MainView;
import application.view.ShadowButton;
import application.view.image.Image;
import application.view.labels.LabelView;

public class NowPlayingButton extends ShadowButton {

	private Image _text;
	private Image _icon;

	private int _textPaddingLeft = 8;
	private int _iconPaddingLeft = 280;

	private LabelView _trackLabel;

	public NowPlayingButton() {
		super();
		_width = 362;
		_height = MainView.BUTTON_HEIGHT;

		createChilds();
	}

	private void createChilds() {
		_text = new Image("nowPlayingText");
		addChild(_text);
		_text.set_color(0xff000000);

		_icon = new Image("musicIcon");
		_icon.set_x(_iconPaddingLeft);
		addChild(_icon);
		_icon.set_color(MainView.ICON_COLOR);
	}

	@Override
	public Boolean isHoverTarget() {
		return true;
	}

	@Override
	public Boolean isPressTarget() {
		return false;
	}

	@Override
	protected void onHover(TouchEvent event) {
		super.onHover(event);
		_icon.set_color(_color);
		if (_trackLabel != null)
			_trackLabel.set_color(_color);
	}

	@Override
	protected void onHoverOut(TouchEvent event) {
		super.onHoverOut(event);
		_icon.set_color(MainView.ICON_COLOR);
		if (_trackLabel != null)
			_trackLabel.set_color(MainView.ICON_COLOR);
	}

	@Override
	protected void onHoverEnd(TouchEvent event) {
		new TracksShowEvent().dispatch();
	}

	public void setTrackEntry(MusicEntry entry, Boolean isPlaying) {
		if (entry != null && isPlaying) {
			String text = entry.artist + ",\"" + entry.trackName + "\"";

			if (_trackLabel == null)
				_trackLabel = new LabelView(text, MainView.ICON_COLOR, ContentManager.GetFont("small"));
			else
				_trackLabel.set_text(text);

			_trackLabel.set_x(_textPaddingLeft);
			_trackLabel.set_y(50);
			addChild(_trackLabel);
		} else {
			if (_trackLabel != null)
				removeChild(_trackLabel);
		}
	}

}
