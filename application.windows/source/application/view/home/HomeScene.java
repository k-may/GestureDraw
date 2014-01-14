package application.view.home;

import java.util.ArrayList;

import de.looksgood.ani.Ani;

import processing.core.PApplet;
import processing.core.PImage;
import application.content.ContentManager;
import application.view.GlowingImage;
import application.view.Image;
import application.view.MainView;
import application.view.gallery.NavButton;
import application.view.scene.Scene;
import framework.data.GalleryEntry;
import framework.events.LabelButtonPressed;
import framework.events.TouchEvent;
import framework.scenes.IHomeScene;
import framework.scenes.SceneType;
import framework.view.IGallery;

import static processing.core.PApplet.println;

public class HomeScene extends Scene implements IHomeScene<PImage> {

	private Boolean _ready = false;
	private HomeBg _bg;
	private HomeGallery _gallery;

	private GlowingImage _welcome;
	private GlowingImage _start;

	private Image _poweredByText;
	private NavButton _rightButton;


	public static int BG_COLOR = 0xff111011;

	public HomeScene() {
		super(SceneType.Home);

		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;

		createChilds();
	}

	@Override
	public void draw(PApplet p) {

		p.background(0xff000000);

		if (_invalidated) {
			float wX = (_width - _welcome.get_width()) / 2;
			float wY = 50; //(_height - _welcome.get_height()) / 2;

			_welcome.set_x(wX);
			_welcome.set_y(wY);

			_start.set_x((_width - _start.get_width()) / 2);
			_start.set_y(50);//(_height - _start.get_height()) / 2);

			float pX = (_width - _poweredByText.get_width()) / 2;
			float pY = (_height - _poweredByText.get_height());

			_poweredByText.set_x(pX);
			_poweredByText.set_y(pY);

			_invalidated = false;
		}

		super.draw(p);
	}

	private void createChilds() {

		_bg = new HomeBg();
		addChild(_bg);

		createGallery();
		_welcome = new GlowingImage("welcomeText", "welcomeBlur");

		_start = new GlowingImage("startText", "startBlur");
		
		_poweredByText = new Image("poweredByText");
		_poweredByText.set_alpha(200);
		
		addChild(_poweredByText);

		_invalidated = true;	
		
		_rightButton = new NavButton("right");
		//addChild(_rightButton);
		_rightButton.set_x(_width - _rightButton.get_width());
		_rightButton.set_y(567);
	}

	private void createGallery() {

		_gallery = new HomeGallery();
		addChild(_gallery);
		float galleryScale = 0.5f;
		
		float galleryWidth = _width * galleryScale;
		float galleryHeight = _height * galleryScale;
		float galleryX = (_width - galleryWidth)/2;
		float galleryY = (_height - galleryHeight)/2;

		_gallery.set_x(galleryX);
		_gallery.set_y(galleryY);
		_gallery.set_width(galleryWidth);
		_gallery.set_height(galleryHeight);
		
	}

	@Override
	public void setReady(Boolean value) {
		if (value) {
			addChild(_start);
			removeChild(_welcome);
			_isTouchEnabled = true;
		} else {
			removeChild(_start);
			addChild(_welcome);
			_isTouchEnabled = false;
		}
		_ready = value;
	}

	@Override
	public IGallery getGallery() {
		return _gallery;
	}

	@Override
	public void setImages(ArrayList<GalleryEntry<PImage>> images) {
		_gallery.setImages(images);
	}
	
	@Override
	protected void onPress(TouchEvent event) {
		new LabelButtonPressed("START").dispatch();
	}
}
