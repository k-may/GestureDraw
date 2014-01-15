package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import processing.core.PVector;
import framework.events.HandDetectedEvent;
import framework.events.LogEvent;
import framework.interaction.Types.HandType;
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

	protected HashMap<Integer, Integer> _handIdDomainIdMap;
	protected HashMap<Integer, PressStateData> _pressStateMap;

	public KinectRegion(T source, int maxHands, int xRange, int yRange,
			int zRange, RegionType type) {
		super(source);

		_regionType = type;
		_maxHands = maxHands;
		_handIdDomainIdMap = new HashMap<Integer, Integer>();
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

		InteractionStreamData data = new InteractionStreamData(pressData.get_x(), pressData.get_y(), position.z, handData.get_id(), _type, isHoverTarget, pressData.get_isPressTarget(), pressData.get_isPressing(), pressData.getPressPressure(), handData.handType);

		_stream.add(data);
	}

	protected HandData updateHand(int handId, PVector pos) {

		if (_handData == null)
			initialize();

		HandData data = null;
		int id;

		if (_handIdDomainIdMap.containsKey(handId)) {
			id = _handIdDomainIdMap.get(handId);
			data = _handData.get(id);
		} else {
			// no domain found for hand id
			id = getDomainForHand(pos.x);

			if (id != -1) {
				// no domain est. for hand id
				_handIdDomainIdMap.put(handId, id);
				if (_handData.containsKey(id)) {
					data = _handData.get(id);
					// if new hand in est. domain, user probably switched hands
					updateHandType(data, pos.x);
				} else {
					data = new HandData(id);
					_handData.put(id, data);
				}
			} else {
				System.out.println("can't create domain for pos : " + pos.x);
				return null;
			}
		}

		data.addPosition(pos, getDampeningForHand(id));

		return data;

	}

	private void initialize() {
		// initialize map
		_handData = new HashMap<Integer, HandData>();
		new HandDetectedEvent().dispatch();
	}

	private void updateHandType(HandData hand, float x) {
		HandType newHandType = x < hand.getPosition().x ? HandType.Right
				: HandType.Left;
		hand.handChanged = newHandType != hand.handType;
		hand.handType = newHandType;
	}

	private int getDomainForHand(float x) {
		return getDomainForNormalizedPos(x/CAM_WIDTH);
	}

	private int getDomainForNormalizedPos(float x) {
		if (x <= firstDomain)
			return 0;
		else if (x > firstDomain & x <= secondDomain)
			return 1;
		else if (x > secondDomain && x <= 1)
			return 2;
		else
			return -1;
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

	@Override
	public void removeHand(int id) {
		if(_handData.containsKey(id)){
			HandData data = _handData.get(id);
			//remove domains / hand ids from map 
			ArrayList<Integer> entries = new ArrayList<Integer>();
			for(Entry<Integer, Integer> set: _handIdDomainIdMap.entrySet()){
				if(set.getValue() == id)
					entries.add(set.getKey());
			}
			for(int handId : entries){
				_handIdDomainIdMap.remove(handId);
			}
			
			_handData.remove(id);
		}else{
			System.out.println("wierd : remove hand that doesn't exist!");
		}
	}
}
