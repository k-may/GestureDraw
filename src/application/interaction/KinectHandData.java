package application.interaction;

import java.util.ArrayList;

import processing.core.PVector;

public class KinectHandData {

	protected int _id;

	public int get_id() {
		return _id;
	}

	private PVector lastPosition;

	
	protected ArrayList<PVector> _positions;
	protected PVector position;
	
	protected Boolean _isUpdated = false;
	
	public static int ZRANGE = 300;
	public static int XRANGE = 200;
	public static int YRANGE = 200;
	private final int SAMPLES = 7;
	
	private float minZ = Float.MAX_VALUE;
	private float maxZ = Float.MIN_VALUE;
	private float minX = Float.MAX_VALUE;
	private float maxX = Float.MIN_VALUE;
	private float minY = Float.MAX_VALUE;
	private float maxY = Float.MIN_VALUE;
	
	protected float _dampening;
	protected int _sampleCount = 0;

	private static final int MAX_INVALID = 15;
	private int _invalidDataCount = 0;

	private static final int MAX_SAMPLES = 5;
	public KinectHandData(int id){
		_id = id;
	}

	/*
	 * Screen new positions for spikes, irregular values, etc
	 */
	protected void addPosition(PVector pos, float dampening, int handId) {
		_dampening = dampening;

		if (_positions == null)
			init(pos);
		else
			update(pos);
		
		_isUpdated = true;
	}
	
	public Boolean isUpdated() {
		_invalidDataCount = _isUpdated ? _invalidDataCount : _invalidDataCount + 1; 
		Boolean updated = _invalidDataCount < MAX_INVALID;
		_isUpdated = false;
		return updated;
	}

	public Boolean isReady(){
		return _sampleCount > MAX_SAMPLES;
	}
	
	protected void init(PVector pos) {
		position = pos;
		_positions = new ArrayList<PVector>();

		minX = pos.x - XRANGE / 2;
		maxX = minX + XRANGE;

		minY = pos.y - YRANGE / 2;
		maxY = minY + YRANGE;

		minZ = pos.z - ZRANGE / 2;
		maxZ = minZ + XRANGE;
		
		_sampleCount++;
	}
	protected void update(PVector pos) {
		if (isPositionBad(pos)) {
			_invalidDataCount++;
			return;
		} else {
			_invalidDataCount = 0;
			addPosition(pos);
		}
		
		_sampleCount++;
	}
	private Boolean isPositionBad(PVector pos) {
		double distance = 0;
		if (lastPosition != null)
			distance = Math.hypot(pos.x - lastPosition.x, pos.y
					- lastPosition.y);

		lastPosition = pos;
		return distance > 10;
	}

	private void addPosition(PVector pos) {
		position = PVector.lerp(position, pos, _dampening);
		_positions.add(position);
		updateRanges(pos);
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
	
	public PVector getMappedPosition() {
		float x = getMapped(position.x, minX, XRANGE);
		float y = getMapped(position.y, minY, YRANGE);
		float z = 1 - getMapped(position.z, minZ, ZRANGE);
		return new PVector(x, y, z);
	}
	
	private float getMapped(float val, float min, float range) {
		return Math.max(0.0f, Math.min(1.0f, Math.abs(val - min) / range));
	}
	
	protected void updateRanges(PVector pos){

		if (minX > pos.x) {
			minX = ease(minX, pos.x, _dampening);// minX + (pos.x - minX)*0.1f;
			maxX = minX + XRANGE;
		}

		if (maxX < pos.x) {
			maxX = ease(maxX, pos.x, _dampening);// maxX + (pos.x - maxX)*0.1f;
			minX = maxX - XRANGE;
		}

		if (minY > pos.y) {
			minY = ease(minY, pos.y, _dampening);// minY pos.y;
			maxY = minY + YRANGE;
		}

		if (maxY < pos.y) {
			maxY = ease(maxY, pos.y, _dampening);
			minY = maxY - YRANGE;
		}
		if (minZ > pos.z) {
			minZ = ease(minZ, pos.z, _dampening);
			maxZ = minZ + ZRANGE;
		}

		if (maxZ < pos.z) {
			maxZ = ease(maxZ, pos.z, _dampening);
			minZ = maxZ - ZRANGE;
		}
	}


	
	private float ease(float start, float dest, float easing) {
		return start + (dest - start) * easing;
	}
}
