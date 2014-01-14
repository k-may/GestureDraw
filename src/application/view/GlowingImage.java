package application.view;

import static processing.core.PApplet.println;
import processing.core.PApplet;
import framework.view.View;

public class GlowingImage extends View {

	private String _name;

	private Image _top;
	private Image _bottom;

	private int _color = 0xffffffff;

	/*
	 * different top and bottom images
	 */
	public GlowingImage(String name, String bottomName) {
		_name = name;
		_bottom = new FadingImage(bottomName, 50, 200);
		createChilds();
		addChild(_bottom);
		addChild(_top);
	}

	/*
	 * create bottom image from top (blurred)
	 */
	public GlowingImage(String name) {
		_name = name;

		createChilds();
	}

	private void createChilds() {
		_top = new Image(_name);
		_top.set_alpha(100);

	}

	@Override
	public void draw(PApplet p) {

		if (_bottom == null) {
			String bottomName = BlurImage.CreateBlurImage(_name, p);

			if (bottomName != null) {
				_bottom = new FadingImage(bottomName);
				addChild(_bottom);

				addChild(_top);
			}
		}

		super.draw(p);
	}

	public int get_color() {
		return _color;
	}

	public void set_color(int _color) {
		this._color = _color;

		_top.set_color(_color);
		_top.set_alpha(200);
		_bottom.set_color(_color);
	}

	@Override
	public float get_width() {
		return _top.get_width();
	}

	@Override
	public float get_height() {
		return _top.get_height();
	}

}
