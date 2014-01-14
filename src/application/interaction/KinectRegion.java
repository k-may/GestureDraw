package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PVector;
import framework.events.HandDetectedEvent;
import framework.events.LogEvent;
import framework.interaction.InteractionStreamData;
import framework.interaction.InteractionTargetInfo;
import framework.interaction.PressData;
import framework.interaction.Region;
import framework.pressing.PressState;
import framework.pressing.PressStateData;

public abstract class KinectRegion<T> extends Region<T> {
	protected static final int CAM_WIDTH = 640;
	protected static final int CAM_HEIGHT = 480;

	protected static final float MIN_DAMPENING = 0.1f;
	protected static final float MAX_DAMPENING = 0.05f;

	protected int _maxHands;

	protected RegionType _regionType;

	protected HashMap<Integer, PressStateData> _pressStateMap;

	public KinectRegion(T source, int maxHands, int xRange, int yRange,
			int zRange, RegionType type) {
		super(source);

		_regionType = type;
		_maxHands = maxHands;

		_pressStateMap = new HashMap<Integer, PressStateData>();
		if (xRange != -1)
			HandData.XRANGE = xRange;

		if (yRange != -1)
			HandData.YRANGE = yRange;

		if (zRange != -1)
			HandData.ZRANGE = zRange;

		_adapter = new Adapter();
	}

	/*
	 * Mapping values only seems to work for the SONRegion at the moment, (may
	 * be skipping frames!?)
	 */
	protected void processInput(HandData handData) {

		PVector position = handData.getPosition();

		if (_regionType == RegionType.SimpleOpenNI)
			MapValuesToCurvedPlane(position);

		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(position.x, position.y, handData.get_id(), _type);
		Boolean isHoverTarget = info.get_isHoverTarget();

		PressData pressData = _pressHandler.getPressData(handData, info);

		InteractionStreamData data = new InteractionStreamData(pressData.get_x(), pressData.get_y(), position.z, handData.get_id(), _type, isHoverTarget, pressData.get_isPressTarget(), pressData.get_isPressing(), pressData.getPressPressure());

		_stream.add(data);
	}

	protected HandData getHand(int id, PVector pos) {
		if (_handData == null) {
			_handData = new HashMap<Integer, HandData>();
			new HandDetectedEvent().dispatch();
		}

		HandData data = null;

		if (_handData.containsKey(id))
			data = _handData.get(id);
		else {
			new LogEvent("New hand detected; id: " + id).dispatch();
			data = new HandData(id);
			_handData.put(id, data);
		}

		data.addPosition(pos, getDampeningForHand(id));

		return data;

	}

	private float getDampeningForHand(int id) {
		PressStateData data = _adapter.getPressStateData(id);
		if (data != null) {
			if (data.get_state() == PressState.ColorSelection)
				return MAX_DAMPENING;
		}

		return MIN_DAMPENING;

	}

	@Override
	public void runInteractions() {
		_stream = new ArrayList<InteractionStreamData>();

		if (_handData == null)
			return;

		_adapter.beginInteractionFrame();

		for (HandData handData : _handData.values()) {
			if (handData.updated)
				processInput(handData);

			handData.updated = false;
		}

		// update users
		_adapter.handleStreamData(_stream);
		_adapter.endInteractionFrame();
	}

	@Override
	public ArrayList<InteractionStreamData> getStream() {
		return _stream;
	}

}
