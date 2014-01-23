package application.interaction.processing;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import application.view.MainView;
import framework.interaction.InteractionTargetInfo;
import framework.pressing.PressState;
import framework.pressing.PressStateData;

public class RegionInputData {

	protected int _id;

	// float maxDist = 0;

	private final int SAMPLES = 7;

	// protected PVector _currTarget;
	// private int _snapCount = 0;
	// private Boolean _isSnap = true;

	// private PVector _intent;
	// protected PVector _pos;

	// protected PVector _magnitude;
	// protected PVector _mappedPos;
	// protected PressState _pressState = PressState.None;

	protected float _dampening = 0.01f;
	protected int _sampleCount = 0;

	protected ArrayList<PVector> _positions;
	protected PVector _position;
	protected PVector _cursorPos;
	private PVector _prevPos;

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

	protected void init(PVector position) {
		_position = position;
		_positions = new ArrayList<PVector>();

		_cursorPos = _position;

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
	protected void addPosition(PVector pos, int handId) {
		if (_positions == null)
			init(pos);
		else
			addPosition(pos);

		_isUpdated = true;
	}

	protected void addPosition(PVector pos) {
		_position = pos;
		_positions.add(_position);
		updateRanges(_position);
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
		return _cursorPos;
	}

	public PVector digest(InteractionTargetInfo info,
			PressStateData pressStateData) {
		PVector pos = new PVector(_position.x, _position.y);
		Boolean isDrawing = false;

		if (pressStateData != null) {
			PressState state = pressStateData.get_state();
			isDrawing = state == PressState.Drawing
					|| state == PressState.PreDrawing;
		}
		
		if (isDrawing) {
			_cursorPos.add(getDrawingAttr());
		} else {
			if (info.get_isOverTarget()) {
				PVector target = new PVector(info.get_pressAttractionX(), info.get_pressAttractionY());
				_cursorPos.add(getTargetAttr(_cursorPos, target, 0.05f));
			}
			_cursorPos.lerp(_position, 0.2f);
		}

		_prevPos = new PVector(_position.x, _position.y);

		return _cursorPos;
	}

	private PVector getTargetAttr(PVector start, PVector dest, float mass) {
		PVector dir = PVector.sub(dest, start);
		float mag = dir.mag();
		if (mag > mass) {
			float scalar = mass / mag;
			System.out.println(scalar);
			dir.mult(scalar);
		}
		return dir;
	}

	private PVector getDrawingAttr() {
		PVector dir = PVector.sub(new PVector(_position.x, _position.y), _prevPos);
		float mag = dir.mag();
		float scalar = PApplet.map(mag, 0f, MainView.SCREEN_WIDTH, 0.5f, 2f);
		dir.mult(scalar);
		return dir;
	}

	// utility func

	protected String printV(PVector v) {
		return "[" + (int) v.x + "," + (int) v.y + "]";
	}

	protected void println(String msg) {
		System.out.println(msg);
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

}
