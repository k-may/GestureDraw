package application.interaction;

import processing.core.PApplet;
import processing.core.PVector;

public class InteractionHelper {

	public static float Map(float val, float min, float range) {
		float mapped = PApplet.map(val, min, min + range, 0, 1);
		mapped = Math.max(0.0f, Math.min(1.0f, mapped));
		return mapped;
	}
	
	public static void normalizeVector(PVector v) {
		if (v.x > 1f)
			v.x = 1f;
		else if (v.x < 0f)
			v.x = 0f;

		if (v.y > 1f)
			v.y = 1f;
		else if (v.y < 0)
			v.y = 0;

		if (v.z > 1f)
			v.z = 1f;
		else if (v.z < 0)
			v.z = 0;
	}
	public static float ease(float start, float dest, float easing) {
		return start + (dest - start) * easing;
	}
	
	public static PVector getTargetAttr(PVector start, PVector dest, float mass) {
		PVector dir = PVector.sub(dest, start);
		float mag = dir.mag();
		if (mag > mass) {
			float scalar = mass / mag;
			dir.mult(scalar);
		}

		return dir;
	}

	public static PVector getNormalizedTargetAttr(PVector start, PVector dest,
			float mass) {
		PVector dir = PVector.sub(start, dest);
		float dist = dir.mag();
		float scalar = dist / mass;
		dir.normalize();
		dir.mult(scalar);
		return dir;
	}
	protected String printV(PVector v) {
		return "[" + (int) v.x + "," + (int) v.y + "]";
	}

	protected void println(String msg) {
		//System.out.println(msg);
	}
	

	public static PVector MapValuesToCurvedPlane(PVector position) {
		float midX = 0.5f; // rangeX /2;
		float mX = (midX - position.x) / midX;
		float thetaX = (float) (mX * Math.PI / 2);

		PVector newPosition = new PVector();
		newPosition.z = position.z;
		// println(mX + " : " + thetaX);
		newPosition.x = (float) (Math.sin(thetaX) * -midX + midX);

		// float rangeY = height;
		float midY = 0.5f;// rangeY / 2;
		float mY = (midY - position.y) / midY;
		float thetaY = (float) (mY * Math.PI / 2);
		newPosition.y = (float) (Math.sin(thetaY) * -midY + midY);

		return newPosition;
	}
}
