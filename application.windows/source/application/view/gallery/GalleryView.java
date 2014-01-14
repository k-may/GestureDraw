package application.view.gallery;

import java.util.ArrayList;

import framework.data.GalleryEntry;
import framework.view.IGallery;
import framework.view.View;

import application.view.Image;
import application.view.MainView;
import application.view.canvas.PGalleryEntry;

import processing.core.PApplet;
import processing.core.PImage;

public class GalleryView extends View implements IGallery<PImage> {

	private ArrayList<ImageEntryView> _entries;
	private ImageEntryView _currentImage;

	private float _paddingTop = 100;
	private float _paddingBottom = 100;

	private int _index = 0;

	private NavButton _leftButton;
	private NavButton _rightButton;
	private float _buttonPaddingTop = 567;

	private Image _text;
	private Image _icon;

	private BackButton _backButton;

	private float _textPaddingTop;
	private float _iconPaddingLeft = 152;
	
	public GalleryView() {

		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;
		_textPaddingTop = _height - 96;

		createChilds();
	}

	private void createChilds() {
		_leftButton = new NavButton("left");
		//addChild(_leftButton);
		_leftButton.set_y(_buttonPaddingTop);

		_rightButton = new NavButton("right");
		//addChild(_rightButton);
		_rightButton.set_x(_width - _rightButton.get_width());
		_rightButton.set_y(_buttonPaddingTop);

		_text = new Image("galleryText");
		_text.set_color(MainView.ICON_COLOR);
		addChild(_text);
		_text.set_y(_textPaddingTop);

		_icon = new Image("galleryIcon");
		_icon.set_color(MainView.ICON_COLOR);
		addChild(_icon);
		_icon.set_y(_textPaddingTop);
		_icon.set_x(_iconPaddingLeft);

		_backButton = new BackButton();
		addChild(_backButton);
		_backButton.set_x(_width - _backButton.get_width());
		_backButton.set_y(_textPaddingTop);
	}

	@Override
	public void draw(PApplet p) {
		super.draw(p);

	}

	@Override
	public void setImages(ArrayList<GalleryEntry<PImage>> entries) {
		_entries = new ArrayList<ImageEntryView>();
		
		if(entries == null)
			return;
		
		for (GalleryEntry<PImage> entry : entries) {
			addImage(entry);
		}

	}

	@Override
	public void addImage(GalleryEntry<PImage> entry) {

		ImageEntryView view = new ImageEntryView((PGalleryEntry) entry);
		view.set_height(get_maxImageHeight());
		_entries.add(view);
		_index = _entries.size() - 1;
		setCurrent(_entries.get(_index));
		
		if(_entries.size() > 1){
			addChild(_leftButton);
			addChild(_rightButton);
		}
	}

	private void setCurrent(ImageEntryView image) {
		if (_currentImage != null)
			removeChild(_currentImage);

		_currentImage = image;
		image.set_x((_width - image.get_width()) / 2);
		image.set_y(_paddingTop);
		addChild(image);
	}

	private float get_maxImageHeight() {
		return _height - _paddingTop - _paddingBottom;
	}

	@Override
	public void navigate(String direction) {
		removeChild(_entries.get(_index));
		int inc = direction == "left" ? -1 : 1;
		_index += inc;
		_index = _index % _entries.size();
		if (_index < 0)
			_index += _entries.size();

		setCurrent(_entries.get(_index));
	}

}
