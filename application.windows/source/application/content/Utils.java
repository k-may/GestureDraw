package application.content;

import gesturedraw.GestureDraw;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Utils {

	public static PImage ReflectImageVertical(PImage source) {
		PImage image = new PImage(source.width, source.height);

		int[] data = source.pixels;
		int[] reflected = new int[data.length];
		for (int x = 0; x < source.width; x++) {
			for(int y = 0; y < source.height; y ++){
				image.set(source.width - x, y, source.get(x, y));
			}
		}

		return image;
	}
	
	public static float MeasureStringWidth(PFont font, String text){
		PApplet parent = GestureDraw.instance;
		parent.textFont(font);
		return parent.textWidth(text);
	}

}
