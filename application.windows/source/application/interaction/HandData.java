package application.interaction;

import java.util.ArrayList;

import processing.core.PVector;
import static processing.core.PApplet.println;

public class HandData {

	private ArrayList<PVector> _positions;

	private int _id;

	public int get_id() {
		return _id;
	}

	private PVector position;
	private float minZ = Float.MAX_VALUE;
	private float maxZ = Float.MIN_VALUE;
	private float minX = Float.MAX_VALUE;
	private float maxX = Float.MIN_VALUE;
	private float minY = Float.MAX_VALUE;
	private float maxY = Float.MIN_VALUE;

	private final float DAMPENING = 0.1f;
	public static int ZRANGE = 300;
	public static int XRANGE = 200;
	public static int YRANGE = 200;
	private final int SAMPLES = 7;

	public Boolean updated = false;

	public HandData(int id) {
		_id = id;
	}

	public void addPosition(PVector pos) {
		updated = true;

		if (_positions == null) {
			init(pos);
		}else
			update(pos);
	}

	private void update(PVector pos) {

		position = PVector.lerp(position, pos, DAMPENING);

		_positions.add(position);

		if (minX > pos.x) {
			minX = ease(minX, pos.x, DAMPENING);// minX + (pos.x - minX)*0.1f;
			maxX = minX + XRANGE;
		}

		if (maxX < pos.x) {
			maxX = ease(maxX, pos.x, DAMPENING);// maxX + (pos.x - maxX)*0.1f;
			minX = maxX - XRANGE;
		}

		if (minY > pos.y) {
			minY = ease(minY, pos.y, DAMPENING);//minY pos.y;
			maxY = minY + YRANGE;
		}

		if (maxY < pos.y) {
			maxY = ease(maxY, pos.y, DAMPENING);
			minY = maxY - YRANGE;
		}
		if (minZ > pos.z) {
			minZ = ease(minZ, pos.z, DAMPENING);
			maxZ = minZ + ZRANGE;
		}

		if (maxZ < pos.z) {
			maxZ = ease(maxZ, pos.z, DAMPENING);
			minZ = maxZ - ZRANGE;
		}

		// println("add pos : " + pos + " :" + minX + " / " + minY + " / " +
		// minZ);
		
	}
	
	private float ease(float start, float dest, float easing){
		return start + (dest - start)*easing;
	}

	private void init(PVector pos) {
		position = pos;
		_positions = new ArrayList<PVector>();
		
		minX = pos.x - XRANGE/2;
		maxX = minX + XRANGE;
		
		minY = pos.y - YRANGE/2;
		maxY = minY + YRANGE;
		
		minZ = pos.z - ZRANGE/2;
		maxZ = minZ + XRANGE;
	}

	private float getMapped(float val, float min, float range) {
		// println(position.z + " : " + minZ + " : " + maxZ);
		return Math.max(0.0f, Math.min(1.0f, Math.abs(val - min) / range));
	}

	public PVector getPosition() {
		float x = getMapped(position.x, minX, XRANGE);
		float y = getMapped(position.y, minY, YRANGE);
		float z = 1 - getMapped(position.z, minZ, ZRANGE);

		return new PVector(x, y, z);
	}

	private PVector getLastPosition() {
		return _positions.get(_positions.size() - 3);
	}

	public PVector getTendency() {
		int i = _positions.size() - 1;
		int count = 0;
		float xDiff = 0, yDiff = 0, zDiff = 0;

		while (i > 0 && i > _positions.size() - SAMPLES + 1) {
			PVector start = _positions.get(i - 1);
			PVector finish = _positions.get(i);
			xDiff += start.x - finish.x;
			yDiff += start.y - finish.y;
			zDiff += start.z - finish.z;
			i--;
			count++;
		}

		xDiff /= count;
		yDiff /= count;
		zDiff /= count;

		return new PVector(xDiff, yDiff, zDiff);
	}
}
