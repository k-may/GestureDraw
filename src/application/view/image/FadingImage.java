package application.view.image;

import processing.core.PApplet;

public class FadingImage extends Image {

	private static float[] sin_values;
	private int _startVal = 100;
	private int _endVal = 255;
	private float _noiseSeed = 0.0f;
	private int count = 0;
	

	public FadingImage(String name, int startValue, int endValue){
		this(name);
		_startVal = startValue;
		_endVal = endValue;
	}
	
	public FadingImage(String name) {
		super(name);

		if (sin_values == null)
			initSinValues();
	}

	private void initSinValues() {
		int numValues = 100;
		sin_values = new float[numValues];
		float theta = 0.0f;
		float inc = (float) ((2 * Math.PI) / numValues);

		for (int i = 0; i < numValues; i++) {
			float value = (float) ((Math.sin(theta) + 1) / 2f);
			sin_values[i] = value;
			theta += inc;
		}
	}

	@Override
	public void draw(PApplet p) {
		count++;
		int diff = _endVal - _startVal;
		
		//int alpha = (int) (sin_values[count % sin_values.length] * diff) + _startVal;
		//add some noise
		_noiseSeed += 0.05;
		int alpha = (int) (p.noise(_noiseSeed) * diff + _startVal);
		//alpha += noise;
		
		//alpha = alpha > _endVal ? _endVal : alpha;
		
		set_alpha(alpha);

		super.draw(p);
	}

}
