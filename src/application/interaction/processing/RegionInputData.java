package application.interaction.processing;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import application.view.MainView;
import framework.pressing.PressState;

public class RegionInputData {

	protected int _id;

	float maxDist = 0;

	private final int SAMPLES = 7;

	protected PVector _currTarget;
	private int _snapCount = 0;
	private Boolean _isSnap = true;

	// private PVector _intent;
	protected PVector _pos;

	protected PVector _magnitude;
	protected PVector _mappedPos;
	protected PressState _pressState = PressState.None;

	protected float _dampening;
	protected int _sampleCount = 0;

	protected ArrayList<PVector> _positions;
	protected PVector _position;

	protected Boolean _isUpdated = false;

	public static int ZRANGE = 300;
	public static int XRANGE = 200;
	public static int YRANGE = 200;

	protected float minZ = Float.MAX_VALUE;
	protected float maxZ = Float.MIN_VALUE;
	protected float minX = Float.MAX_VALUE;
	protected float maxX = Float.MIN_VALUE;
	protected float minY = Float.MAX_VALUE;
	protected float maxY = Float.MIN_VALUE;

	public RegionInputData() {
		_id = 0;
	}

	protected void init(PVector pos) {
		_position = pos;
		_positions = new ArrayList<PVector>();

		_pos = _currTarget = new PVector(0.5f, 0.5f);

		minX = 0;
		maxX = MainView.SCREEN_WIDTH;

		minY = 0;
		maxY = MainView.SCREEN_HEIGHT;

		minZ = 0;
		maxZ = 1;

		_sampleCount++;
	}

	public int get_id() {
		return _id;
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

		// updateMagnitude(pos);
		updatePosition();

		_isUpdated = true;
	}

	private void updatePosition() {
		if (get_isDrawing()) {

		} else {

		}
	}

	protected void updateMagnitude(PVector pos) {
		_mappedPos = pos; // getMappedPosition();
		_magnitude = getMagnitude();

		_pos.sub(_magnitude);
		normalizeVector(_pos);

		_pos.z = pos.z;
	}

	protected void normalizeVector(PVector v) {
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

	protected void update(PVector pos) {
		addPosition(pos);
	}

	protected void addPosition(PVector pos) {
		_position = PVector.lerp(_position, pos, _dampening);
		_positions.add(_position);
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

	public void setPressState(PressState state) {
		Boolean changed = _pressState != state;
		_pressState = state;

		if (changed) {
			if (_pressState == PressState.Drawing
					|| _pressState == PressState.PreDrawing)
				startDraw();
			else
				endDraw();
		}
	}

	protected void updateRanges(PVector pos) {

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

	public PVector getPosition() {
		return new PVector(_pos.x, _pos.y, _mappedPos.z);
	}

	protected PVector getMagnitude() {

		PVector magnitude = new PVector();
		PVector prevIntent = new PVector(_currTarget.x, _currTarget.y);
		PVector positionMag = new PVector();
		Boolean isDrawing = get_isDrawing();

		if (isDrawing) {
			_currTarget = PVector.lerp(_currTarget, _mappedPos, 0.1f);
			_currTarget.lerp(_mappedPos, 0.1f);
		} else {
			if (_isSnap) {
				// project real position from virtual position
				positionMag = PVector.sub(_mappedPos, _pos);

				_currTarget = PVector.add(positionMag, _currTarget);
			} else
				_currTarget = _mappedPos;
		}

		magnitude = PVector.sub(prevIntent, _currTarget);

		float distance = PVector.dist(new PVector(prevIntent.x, prevIntent.y), new PVector(_currTarget.x, _currTarget.y));
		float multFactor = getMagFactor(isDrawing, distance);

		magnitude.mult(multFactor);

		// println(printV(positionMag) + " : " + printV(magnitude) + "  * " +
		// multFactor + " : " + distance);
		return magnitude;
	}

	private String printV(PVector v) {
		return "[" + (int) v.x + "," + (int) v.y + "]";

	}

	private Boolean get_isDrawing() {
		if (_pressState == PressState.PreDrawing
				|| _pressState == PressState.Drawing)
			return true;
		else
			return false;
	}

	private float getMagFactor(Boolean isDrawing, float distance) {

		if (distance > maxDist)
			maxDist = distance;

		if (isDrawing) {
			float factor = PApplet.map(distance, 0f, 0.1f, 0.5f, 2.5f);
			return factor;
		} else {
			if (_isSnap) {
				return getSnapValue();
			} else
				return 0.5f;
		}
	}

	private float getSnapValue() {
		_snapCount = _snapCount < 100 ? _snapCount + 1 : 100;
		float ratio = _snapCount / 100.0f;

		return PApplet.map(ratio * ratio, 0f, 1f, 0.01f, 0.5f);
	}

	protected void startDraw() {
		_currTarget = _position; // getMappedPosition();
	}

	private void endDraw() {
		if (_mappedPos != null) {
			_snapCount = 0;
			_currTarget = _mappedPos;
		}
	}

	protected void println(String msg) {
		System.out.println(msg);
	}

}
