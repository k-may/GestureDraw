package framework;

import processing.core.PVector;

public class Rectangle {
	private int _x;
	private int _y;
	private int _width;
	private int _height;

	public Rectangle() {

	}
	
	public Rectangle(int x, int y, int width, int height){
		_x = x;
		_y = y;
		_width = width;
		_height = height;
	}

	public Rectangle offset(PVector point){
		return new Rectangle(_x + (int)point.x,_y + (int)point.y, _width , _height );
	}
	
	public int get_x() {
		return _x;
	}

	public void set_x(int _x) {
		this._x = _x;
	}

	public int get_y() {
		return _y;
	}

	public void set_y(int _y) {
		this._y = _y;
	}

	public int get_width() {
		return _width;
	}

	public void set_width(int _width) {
		this._width = _width;
	}

	public int get_height() {
		return _height;
	}

	public void set_height(int _height) {
		this._height = _height;
	}

	public boolean contains(float x, float y) {
		// TODO Auto-generated method stub
		return (x >= _x && x < _x + _width && y >= _y && y < _y + _height);
	}
}
