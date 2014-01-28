package application.view.tracks;

import framework.data.MusicEntry;
import framework.view.View;
import application.content.ContentManager;
import application.view.MainView;
import application.view.labels.LabelView;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class TrackSmallView extends View {

	private PImage _musicIcon;
	private PImage _nowPlayingText;

	private LabelView _trackLabel;

	private int _notePaddingLeft = 12;
	private int _notePaddingTop = 7;

	private int _textPaddingLeft = 82;

	private Boolean _isPlaying = false;
	
	public TrackSmallView() {
		createChilds();
	}

	private void createChilds() {
		_musicIcon = ContentManager.GetIcon("musicIcon");
		_nowPlayingText = ContentManager.GetIcon("nowPlayingText");
	}

	@Override
	public void draw(PApplet p) {
		// TODO Auto-generated method stub
		super.draw(p);

		PVector absPos = get_absPos();

		p.noTint();
		p.image(_nowPlayingText, absPos.x, absPos.y);

		p.tint(MainView.TEXT_COLOR, 140);
		p.image(_musicIcon, absPos.x + _notePaddingLeft, absPos.y
				+ _notePaddingTop);

	}

	public void setPlaying(Boolean isPlaying) {
		_isPlaying = isPlaying;
	}

	public void setTrackEntry(MusicEntry entry) {

		String text = entry.artist + ",\"" + entry.trackName + "\"";

		if (_trackLabel == null)
			_trackLabel = new LabelView(text, MainView.TEXT_COLOR, ContentManager.GetFont("small"));
		else
			_trackLabel.set_text(text);

		_trackLabel.set_x(_textPaddingLeft);
		_trackLabel.set_y(15);
		addChild(_trackLabel);
	}
}
