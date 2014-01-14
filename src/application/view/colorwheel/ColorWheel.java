package application.view.colorwheel;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import framework.view.View;
import gesturedraw.GestureDraw;

public class ColorWheel{

	public static int WHEEL_RADIUS = 170;

	private static PImage _wheel;

	public ColorWheel() {
		if (_wheel == null)
			_wheel = createWheel();
	}

	public PImage getImage(){
		return _wheel;
	}
	
	public int getColor(int x, int y){
		return _wheel.get(x, y);
	}

	private PImage createWheel() {
		PGraphics img = GestureDraw.instance.createGraphics(WHEEL_RADIUS*2,WHEEL_RADIUS*2);

		int bLength = 20;
		int radius = WHEEL_RADIUS - bLength;

		img.beginDraw();
		img.colorMode(PApplet.HSB, 100, 100, 100);
		img.noStroke();

		for (int r = radius + bLength; r >= 0; r--) {
			float sat = r < radius ? ((float) r / radius) : 1;
			sat = (float) (Math.pow(sat, 1.6) * 100);
			float br = r > radius ? (1 - (float) (r - radius) / bLength) : 1;

			float alpha = (float) r / (radius + bLength);
			alpha = (float) Math.pow(alpha, 3);
			alpha = 1 - alpha;

			// br =1- log(br);
			br = (float) (br + 0.5);
			//println(br);
			br = br * 100;

			int circumference = (int) (2 * Math.PI * r);
			for (int c = 0; c < circumference; c++) {

				float theta = (float) c / circumference;
				float rads = (float) (theta * 2 * Math.PI);
				float hue = theta * 100;

				img.fill(hue, sat, br, alpha * 255);

				float x = (float) (Math.sin(rads) * r + WHEEL_RADIUS);
				float y = (float) (Math.cos(rads) * r + WHEEL_RADIUS);

				img.ellipse(x, y, 2, 2);
			}
		}

		img.fill(17, 16, 17);
		img.ellipse(WHEEL_RADIUS, WHEEL_RADIUS, 50, 50);

		img.endDraw();
		
		return img.get();
	}
}
