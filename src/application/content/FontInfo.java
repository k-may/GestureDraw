package application.content;

import framework.data.FontEntry;
import processing.core.PFont;

public class FontInfo {
	
	private PFont _font;
	private FontEntry _entry;
	
	public FontInfo(PFont font, FontEntry entry){
		this._font = font;
		this._entry = entry;
	}
	
	public PFont get_font() {
		return _font;
	}

	public int get_size() {
		return _entry.get_size();
	}

	public String get_name() {
		return _entry.get_name();
	}
}
