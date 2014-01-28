package framework.events;

import processing.core.PApplet;

public class SaveCanvasEvent extends Event {

	private String _filePath;
	private String _date;
	
	public SaveCanvasEvent() {
		super(EventType.SaveCanvas);
		_date = PApplet.month() + "_" + PApplet.day() + "_"
				+ PApplet.minute() + "_" + PApplet.second();
		_filePath = PApplet.minute() + "_" + PApplet.second()
				+ ".jpg";
	}
	
	public String getDate(){
		return _date;
	}
	
	public String getFilePath(){
		return _filePath;
	}
}
