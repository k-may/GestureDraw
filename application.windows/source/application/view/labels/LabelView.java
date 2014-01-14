package application.view.labels;

import framework.view.View;
import gesturedraw.GestureDraw;
import application.content.FontInfo;
import application.content.Utils;
import processing.core.PApplet;
import processing.core.PVector;


public class LabelView extends View {

	protected int _size;
	protected int _color;
	protected String _text;
	protected FontInfo _font;
	protected int _alignment = GestureDraw.instance.LEFT;

	public LabelView(String text,int color, FontInfo font) {
		_text = text;
		_color = color;
		_font = font;
		_invalidated = true;
	}
	

	@Override
	public void draw(PApplet p) {
		if(_invalidated){
			_invalidated = false;
			
			get_width();
			get_height();
		}
		
		p.textAlign(_alignment);
		p.textFont(_font.get_font());
		p.fill(_color);
		PVector pos = get_absPos();
		p.text(_text, pos.x, pos.y + _height);

		super.draw(p);
	}

	public int get_size() {
		return _size;
	}

	public void set_size(int _size) {
		this._size = _size;
		_invalidated = true;
	}

	public int get_color() {
		return _color;
	}

	public void set_color(int _color) {
		this._color = _color;
		_invalidated = true;
	}

	public String get_text() {
		return _text;
	}

	public void set_text(String _text) {
		this._text = _text;
		_invalidated = true;
	}


	public int get_alignment() {
		return _alignment;
	}

	public void set_alignment(int _alignment) {
		this._alignment = _alignment;
		_invalidated = true;
	}
	
	@Override
	public float get_width() {
		_width = Utils.MeasureStringWidth(_font.get_font(), _text);
		return _width;
	}
	
	@Override
	public float get_height() {
		_height = _font.get_size();
		return _height;
	}

}
