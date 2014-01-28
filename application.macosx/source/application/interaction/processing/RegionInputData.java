package application.interaction.processing;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import application.view.MainView;
import framework.depth.DepthState;
import framework.interaction.InteractionTargetInfo;

public abstract class RegionInputData {

	protected int _id;

	private final int SAMPLES = 7;

	protected DepthState _pressState = DepthState.None;

	protected float _dampening = 0.1f;
	protected int _sampleCount = 0;

	protected ArrayList<PVector> _positions;
	protected PVector _rawPosition;
	protected PVector _cursorPos;
	private PVector _prevPos;

	protected Boolean _isUpdated = false;

	protected int minZ = 0;
	protected int maxZ = 1;
	protected float minX = 1000;
	protected float maxX = 0;
	protected float minY = 1000;
	protected float maxY = 0;

	public RegionInputData() {
		_id = 0;

		if (MainView.DRAW_MASS == 0.0)
			MainView.DRAW_MASS = (float) (Math.hypot(MainView.SRC_WIDTH, MainView.SRC_HEIGHT) * 0.25f);
	}

	public int get_id() {
		return _id;
	}

	/*
	 * Screen new positions for spikes, irregular values, etc
	 */
	protected void addRawPosition(PVector pos, int handId) {
		if (_positions == null)
			init(pos);
		else
			addPosition(pos);

		_isUpdated = true;
	}

	protected void init(PVector position) {
		_rawPosition = position;
		_positions = new ArrayList<PVector>();
		_cursorPos = new PVector(0.5f, 0.5f, 0f);

		_sampleCount++;
	}

	protected abstract void resetRange();

	protected abstract void addPosition(PVector pos);

	protected PVector getMappedPosition() {
		float x = getMapped(_rawPosition.x, minX, MainView.XRANGE);
		float y = getMapped(_rawPosition.y, minY, MainView.YRANGE);
		float z = 1f - getMapped(_rawPosition.z, minZ, MainView.ZRANGE);
		PVector mapped = new PVector(x, y, z);
		/*
		 * System.out.println("========="); System.out.println(_rawPosition);
		 * System.out.println(mapped); System.out.println(minX + " / " + maxX);
		 * System.out.println(minY + " / " + maxY);
		 */
		return mapped;
	}

	private float getMapped(float val, float min, float range) {
		float mapped = PApplet.map(val, min, min + range, 0, 1);
		mapped = Math.max(0.0f, Math.min(1.0f, mapped));
		return mapped;
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

	public PVector getPosition() {
		return _cursorPos;
	}

	public PVector digest(InteractionTargetInfo info, DepthState pressState) {

		PVector mappedPos = getMappedPosition();
		PVector currPos;
		updatePressState(pressState);

		if (isDrawing()) {
			currPos = new PVector(_rawPosition.x, _rawPosition.y);
			_cursorPos.lerp(PVector.add(_cursorPos, getNormalizedTargetAttr(currPos, _prevPos, MainView.DRAW_MASS)), MainView.DRAW_LERP);
		} else {

			currPos = new PVector(mappedPos.x, mappedPos.y);

			if (info.get_isOverTarget()) {
				// ease to target
				PVector target = new PVector(info.get_pressAttractionX(), info.get_pressAttractionY());
				PVector dir = getTargetAttr(new PVector(_cursorPos.x, _cursorPos.y), target, MainView.TARGET_MASS);
				_cursorPos.add(dir);
			} else if (_pressState == DepthState.Start) {
				// ease back to center of screen
				PVector target = new PVector(0.5f, 0.5f);
				PVector dir = getTargetAttr(new PVector(_cursorPos.x, _cursorPos.y), target, 0.01f);
				// _cursorPos.add(dir);

				// if(minZ < _rawPosition.z)
				//System.out.println(minZ + " / " + _rawPosition.z + " mapped : "
					//	+ mappedPos.z + " / " + _pressState);
				if (minZ + MainView.ZRANGE > _rawPosition.z) {
					minZ -= 0.5f;
				}
			}

			// apply smoothing
			_cursorPos.lerp(currPos, MainView.CURSOR_LERP);
		}

		// reset previous
		_prevPos = new PVector(currPos.x, currPos.y);

		// check boundaries
		normalizeVector(_cursorPos);

		_cursorPos.z = mappedPos.z;

		return _cursorPos;
	}

	private void updatePressState(DepthState state) {

		Boolean changed = _pressState != state;
		_pressState = state;

		if (changed) {
			if (isDrawing()) {
				_prevPos = new PVector(_rawPosition.x, _rawPosition.y);
			} else {
				// reset map to cursor
				resetRange();
				_prevPos = new PVector(_cursorPos.x, _cursorPos.y);
			}
			_pressState = state;
		}

	}

	private Boolean isDrawing() {
		return _pressState == DepthState.Drawing
				|| _pressState == DepthState.PreDrawing;
	}

	private PVector getTargetAttr(PVector start, PVector dest, float mass) {
		PVector dir = PVector.sub(dest, start);
		float mag = dir.mag();
		if (mag > mass) {
			float scalar = mass / mag;
			dir.mult(scalar);
		}

		return dir;
	}

	private PVector getNormalizedTargetAttr(PVector start, PVector dest,
			float mass) {
		PVector dir = PVector.sub(start, dest);
		float dist = dir.mag();
		float scalar = dist / mass;
		dir.normalize();
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

	protected float ease(float start, float dest, float easing) {
		return start + (dest - start) * easing;
	}

}
