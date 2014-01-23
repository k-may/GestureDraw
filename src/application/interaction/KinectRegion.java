package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import processing.core.PVector;
import application.interaction.processing.RegionInputData;
import framework.events.HandDetectedEvent;
import framework.interaction.InteractionStreamData;
import framework.interaction.InteractionTargetInfo;
import framework.interaction.PressData;
import framework.interaction.Region;
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
			RegionInputData.XRANGE = xRange;

		if (yRange != -1)
			RegionInputData.YRANGE = yRange;

		if (zRange != -1)
			RegionInputData.ZRANGE = zRange;

		_adapter = new Adapter();
	}

	@Override
	public void runInteractions() {
		_stream = new ArrayList<InteractionStreamData>();

		if (_domainData == null)
			return;

		_adapter.beginInteractionFrame();

		for (DomainData domain : _domainData.values()) {
			Boolean ready = domain.isReady();
			Boolean updated = domain.isUpdated();
			if (ready && updated)
				_stream.add(processInput(domain));
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
	public void removeDomain(int id) {
		println("remove hand : " + id);
		if (_domainData.containsKey(id)) {
			DomainData data = _domainData.get(id);
			// remove domains / hand ids from map
			ArrayList<Integer> entries = new ArrayList<Integer>();
			for (Entry<Integer, Integer> set : _handIdDomainIdMap.entrySet()) {
				if (set.getValue() == id)
					entries.add(set.getKey());
			}
			for (int handId : entries) {
				_handIdDomainIdMap.remove(handId);
			}

			_domainData.remove(id);
		} else {
			System.out.println("wierd : remove hand that doesn't exist!");
		}
	}

	private void println(String msg) {
		System.out.println(msg);
	}

	/*
	 * Mapping values only seems to work for the SONRegion at the moment, (may
	 * be skipping frames!?)
	 */
	protected InteractionStreamData processInput(DomainData handData) {

		PVector position = handData.getPosition();

		if (_regionType == RegionType.SimpleOpenNI)
			MapValuesToCurvedPlane(position);

		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(position.x, position.y, handData.get_id(), _type);
		Boolean isHoverTarget = info.get_isHoverTarget();

		PressData pressData = _pressHandler.getPressData(handData, info);

		InteractionStreamData data = new InteractionStreamData(pressData.get_x(), pressData.get_y(), position.z, handData.get_id(), _type, isHoverTarget, pressData.get_isPressTarget(), pressData.get_isPressing(), pressData.getPressPressure(), handData.handType);

		return data;
	}

	/*
	 * in-point, takes data directly from framework
	 */
	protected DomainData updateHand(int handId, PVector pos) {

		if (_domainData == null)
			initialize();

		DomainData data = null;
		int domain = getDomainForHand(pos.x);
		if (domain == -1)
			return null;

		if (_handIdDomainIdMap.containsKey(handId)) {
			// check if hand has left domain
			if (_handIdDomainIdMap.get(handId) == domain)
				data = _domainData.get(domain);
		} else {
			// no domain found for hand id
			_handIdDomainIdMap.put(handId, domain);

			if (_domainData.containsKey(domain)) {
				// new hand in domain (probably want to check if there's
				// only two)
				data = _domainData.get(domain);
			} else {
				// new domain
				data = new DomainData(domain);
				_domainData.put(domain, data);
			}
		}

		// TODO change dampening strategy (color wheel will have static
		// location)
		/*if (data != null) {
			PressState state = getPressStateForHand(domain);
			data.setPressState(state);
			data.addPosition(pos, PressStateDampening(state), handId);
		}*/
		
		data.addPosition(pos, handId);
		
		return data;

	}

	private void initialize() {
		// initialize map
		_domainData = new HashMap<Integer, DomainData>();
		new HandDetectedEvent().dispatch();
	}

	private int getDomainForHand(float x) {
		return getDomainForNormalizedPos(x / CAM_WIDTH);
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

}
