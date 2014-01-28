package application.view.colorwheel;

import application.view.MainView;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import framework.view.View;
import gesturedraw.GestureDraw;

public class ColorWheel{


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
		PGraphics img = GestureDraw.instance.createGraphics(MainView.COLORWHEEL_RADIUS*2,MainView.COLORWHEEL_RADIUS*2);

		//width of transparency region around outside
		int bLength = 15;
		int radius = MainView.COLORWHEEL_RADIUS - bLength;

		img.beginDraw();
		img.colorMode(PApplet.HSB, 100, 100, 100);
		img.noStroke();

		for (int r = radius + bLength; r >= 0; r--) {
			float sat = r < radius ? ((float) r / radius) : 1;
			sat *= 100;// (float) (Math.pow(sat, 1.6) * 100);
			float br = r > radius ? (1 - (float) (r - radius) / bLength) : 1;

			//System.out.println(sat + " : " + r);
			
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

				float x = (float) (Math.sin(rads) * r + MainView.COLORWHEEL_RADIUS);
				float y = (float) (Math.cos(rads) * r + MainView.COLORWHEEL_RADIUS);

				img.ellipse(x, y, 2, 2);
			}
		}

		//erase color
		img.fill(MainView.BG_COLOR);
		img.ellipse(MainView.COLORWHEEL_RADIUS, MainView.COLORWHEEL_RADIUS, 50, 50);

		img.endDraw();
		
		return img.get();
	}
}
