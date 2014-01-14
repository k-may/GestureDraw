package application.view;

import application.content.ContentManager;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class BlurImage extends Image {

	private final static String BLUR = "blur";
	private Boolean _initiated = false;
	
	public BlurImage(String name) {
		super(name);
		
	}

	@Override
	public void draw(PApplet p) {
	
		if(!_initiated){
			
			_name = CreateBlurImage(_name, p);
			_initiated = true;
		}
	}
	
	public static String CreateBlurImage(String name, PApplet p){
		PImage originalImage = ContentManager.GetIcon(name);
		
		int w = originalImage.width;
		int h = originalImage.height;
		
		PGraphics buffer = p.createGraphics(originalImage.width, originalImage.height,PApplet.JAVA2D);
		buffer.beginDraw();
		buffer.background(0,0);
		buffer.image(originalImage, 0, 0);
		buffer.filter(PApplet.BLUR, 3.0f);
		buffer.filter(PApplet.INVERT);
		buffer.filter(PApplet.THRESHOLD);
		//buffer.filter(PApplet.BLUR, 5.0f);
		buffer.endDraw();

		String newName = name + "_" + BLUR;
		ContentManager.SetIcon(newName, buffer.get());
		
		return newName;
	}
	
	

}
