package application.interaction.processing;

import static application.interaction.InteractionHelper.Map;

import java.util.ArrayList;

import processing.core.PVector;
import application.interaction.ICursorHandler;
import application.interaction.IRangeHandler;
import application.view.MainView;
import framework.depth.DepthState;
import framework.interaction.data.InteractionTargetInfo;

public abstract class RegionInputData {

	private ICursorHandler _cursorHandler;
	private IRangeHandler _rangeHandler;

	protected int _id;

	public int get_id() {
		return _id;
	}

	protected DepthState _depthState = DepthState.None;

	protected int _sampleCount = 0;

	protected ArrayList<PVector> _positions;
	protected PVector _rawPosition;
	protected PVector _cursorPos;
	// private PVector _prevPos;

	public Boolean updated = true;
	public Boolean removed = false;

	protected int minZ = 0;
	protected int maxZ = 1;
	protected float minX = 1000;
	protected float maxX = 0;
	protected float minY = 1000;
	protected float maxY = 0;

	public static int MAX_INVALID = 200;
	public int invalidDataCount = 0;

	public RegionInputData(ICursorHandler cursorHandler) {
		this(cursorHandler, null);
	}

	public RegionInputData(ICursorHandler cursorHandler,
			IRangeHandler rangeHandler) {

		_cursorHandler = cursorHandler;
		_rangeHandler = rangeHandler;

		if (MainView.DRAW_MASS == 0.0)
			MainView.DRAW_MASS = (float) (Math.hypot(MainView.SRC_WIDTH, MainView.SRC_HEIGHT) * 0.25f);
	}

	protected abstract void resetRange(PVector rawPos, PVector cursorPos);

	public abstract PVector getPosition();

	protected abstract void addPosition(PVector pos);

	/*
	 * Screen new positions for spikes, irregular values, etc
	 */
	public void addRawPosition(PVector pos, int handId) {
		if (_positions == null)
			init(pos);
		else
			addPosition(pos);

		_sampleCount++;
		updated = true;
	}

	protected void init(PVector position) {
		_rawPosition = position;
		_cursorPos = new PVector(0.5f, 0.5f,1f);

		resetRange(_rawPosition, _cursorPos);
		_positions = new ArrayList<PVector>();
	}

	protected PVector getMappedPosition() {
		float x = Map(_rawPosition.x, minX, MainView.XRANGE);
		float y = Map(_rawPosition.y, minY, MainView.YRANGE);
		float z = 1f - Map(_rawPosition.z, minZ, MainView.ZRANGE);
		PVector mapped = new PVector(x, y, z);
		// mapped = Region.MapValuesToCurvedPlane(mapped);
		// System.out.println(MainView.ZRANGE);
		// MainView.ZRANGE = 175;
		/*
		 * System.out.println("========="); System.out.println(_rawPosition);
		 * System.out.println(mapped); System.out.println(minX + " / " + maxX);
		 * System.out.println(minY + " / " + maxY);
		 */
		return mapped;
	}

	public PVector getTendency() {
		int i = _positions.size() - 1;
		int count = 0;
		float xDiff = 0, yDiff = 0, zDiff = 0;

		while (i > 0 && i > _positions.size() - MainView.TENDENCY_SAMPLES + 1) {
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

	public PVector digest(InteractionTargetInfo info, DepthState pressState) {
		if (invalidDataCount < MAX_INVALID) {
			PVector mappedPos = getMappedPosition();
			Boolean stateChanged = updateDepthState(pressState);
			_cursorHandler.handle(_cursorPos, mappedPos, _rawPosition, _depthState, info, stateChanged);
			if (_rangeHandler != null)
				minZ = _rangeHandler.handle(minZ, mappedPos, pressState, pressState.isDrawing());
		} else {
			// release
			_cursorPos.z = 0;
		}

		return _cursorPos;
	}

	protected Boolean updateDepthState(DepthState state) {
		Boolean changed = _depthState != state;
		_depthState = state;

		return changed;
	}

}
