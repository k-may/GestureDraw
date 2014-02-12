package application.view.colorwheel;

import gesturedraw.GestureDraw;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import application.view.MainView;

public class ColorWheel {

	private static PImage _wheel;
	private int _alphaRegion = 0;

	public ColorWheel() {
		if (_wheel == null)
			_wheel = createWheel();
	}

	public PImage getImage() {
		return _wheel;
	}

	public int getColor(int x, int y) {
		return _wheel.get(x, y);
	}

	private PImage createWheel() {
		PGraphics img = GestureDraw.instance.createGraphics(MainView.COLORWHEEL_RADIUS * 2, MainView.COLORWHEEL_RADIUS * 2);

		// width of transparency region around outside

		int radius = MainView.COLORWHEEL_RADIUS - _alphaRegion - 2;

		img.beginDraw();
		img.colorMode(PApplet.HSB, 100, 100, 100);
		img.noStroke();

		for (int r = radius + _alphaRegion; r >= 0; r--) {
			float sat = r < radius ? ((float) r / radius) : 1;
			sat *= 100;// (float) (Math.pow(sat, 1.6) * 100);
			float br = r > radius ? (1 - (float) (r - radius) / _alphaRegion)
					: 1;

			float alpha = 255;
			if (_alphaRegion > 0) {
				alpha = (float) r / (radius + _alphaRegion);
				alpha = (float) Math.pow(alpha, 3);
				alpha = 1 - alpha;
			}
			
			br = (float) (br + 0.5);
			br = br * 100;

			int circumference = (int) (2 * Math.PI * r);
			for (int c = 0; c < circumference; c++) {

				float theta = (float) c / circumference;
				float rads = (float) (theta * 2 * Math.PI);
				float hue = theta * 100;

				img.fill(hue, sat, br, alpha * 255);

				float x = (float) (Math.sin(rads) * r + MainView.COLORWHEEL_RADIUS) + 1;
				float y = (float) (Math.cos(rads) * r + MainView.COLORWHEEL_RADIUS) + 1;

				img.ellipse(x, y, 2, 2);
			}
		}

		// erase color
		img.fill(MainView.BG_COLOR);
		img.ellipse(MainView.COLORWHEEL_RADIUS, MainView.COLORWHEEL_RADIUS, 50, 50);

		img.endDraw();

		return img.get();
	}
}
