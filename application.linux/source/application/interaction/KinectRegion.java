package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import processing.core.PVector;
import application.interaction.processing.RegionInputData;
import application.view.MainView;
import framework.depth.DepthState;
import framework.depth.DepthStateData;
import framework.events.HandDetectedEvent;
import framework.interaction.InteractionStreamData;
import framework.interaction.InteractionTargetInfo;
import framework.interaction.Region;
import framework.interaction.Types.HandType;

public abstract class KinectRegion<T> extends Region<T> {

	protected static final float MIN_DAMPENING = 0.1f;
	protected static final float MAX_DAMPENING = 0.05f;

	protected float firstDomain;
	protected float secondDomain;

	protected int _maxHands;

	protected RegionType _regionType;

	protected HashMap<Integer, Integer> _handIdDomainIdMap;
	protected HashMap<Integer, DepthStateData> _pressStateMap;

	public KinectRegion(T source, int maxHands, int xRange, int yRange,
			int zRange, RegionType type) {
		super(source);

		MainView.SRC_WIDTH = 640;
		MainView.SRC_HEIGHT = 480;
		MainView.TARGET_MASS = 0.01f;

		_regionType = type;
		_maxHands = maxHands;
		_handIdDomainIdMap = new HashMap<Integer, Integer>();
		_pressStateMap = new HashMap<Integer, DepthStateData>();
		if (xRange != -1)
			MainView.XRANGE = xRange;

		if (yRange != -1)
			MainView.YRANGE = yRange;

		if (zRange != -1)
			MainView.ZRANGE = zRange;

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
				_stream.add(digestInput(domain));
		}

		// update user states
		_adapter.handleStreamData(_stream);
		_adapter.endInteractionFrame();
	}

	/*
	 * Mapping values only seems to work for the SONRegion at the moment, (may
	 * be skipping frames!?)
	 */
	protected InteractionStreamData digestInput(DomainData domainData) {

		int id = domainData.get_id();
		PVector position = domainData.getPosition();
		HandType handType = domainData.handType;

		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(position.x, position.y, id, _type);
		Boolean isHoverTarget = info.get_isHoverTarget();
		Boolean isPressTarget = info.get_isPressTarget();

		DepthStateData depthStateData = _adapter.getUserForDomain(id).get_depthStateData();
		DepthState pressState = depthStateData == null ? DepthState.None
				: depthStateData.get_state();

		Boolean isPressing = false;
		if (isPressTarget) {
			// PressData pressData = _pressHandler.getPressData(id, position.z,
			// info.get_targetID());
			isPressing = _pressHandler.getPressData(id, position.z, info.get_targetID());// pressData.get_isPressing();
		}

		position = domainData.digest(info, pressState);
		float x = position.x;
		float y = position.y;
		float z = position.z;

		Boolean isDrawing = info.get_canvas() != null
				&& pressState == DepthState.Drawing;

		return new InteractionStreamData(x, y, z, id, _type, isHoverTarget, isPressTarget, isPressing, handType, pressState, info.get_targets());

	}

	/*
	 * in-point, takes data directly from framework
	 */
	protected DomainData updateHand(int handId, PVector pos) {

		if (_domainData == null)
			initialize();

		int domain = getDomainForHand(pos.x);
		if (domain == -1)
			return null;

		DomainData data = getDomainData(handId, domain);

		if (data != null) {
			data.addRawPosition(pos, handId);
		}
		return data;

	}

	private DomainData getDomainData(int handId, int domain) {
		// DomainData data = null;

		if (_handIdDomainIdMap.containsKey(handId)) {
			// check if hand has left domain
			int d = _handIdDomainIdMap.get(handId);

			if (d != domain)
				println("hand outside of domain : " + d + " => " + domain);

			return _domainData.get(d);

		} else {

			// if hand introduced in established domain we'll assume it's the other
			// hand
			if (_domainData.containsKey(domain)) {
				_handIdDomainIdMap.put(handId, domain);
				return _domainData.get(domain);
			} else {
				println("---->create domain : " + domain);

				DomainData data = new DomainData(domain);

				if (_domainData.size() == 0)
					new HandDetectedEvent().dispatch();

				_domainData.put(domain, data);
				return data;
			}
		}
	}

	@Override
	public void removeDomain(int id) {
		println("-->remove domain : " + id);
		try {
			if (_domainData.containsKey(id)) {
				DomainData data = _domainData.get(id);
				
				// remove domains / hand ids from map
				ArrayList<Integer> entries = new ArrayList<Integer>();
				for (Entry<Integer, Integer> set : _handIdDomainIdMap.entrySet()) {
					if (set.getValue() == id)
						entries.add(set.getKey());
				}
				for (int handId : entries) {
					println("-->remove hand : " + handId);
					_handIdDomainIdMap.remove(handId);
				}

				_domainData.remove(id);
			} else {
				println("wierd : remove hand that doesn't exist!");
			}
		} catch (Exception e) {
			println("cant remove hand: " + id);
		}
	}

	@Override
	public ArrayList<InteractionStreamData> getStream() {
		return _stream;
	}

	private void initialize() {
		// initialize map
		_domainData = new HashMap<Integer, DomainData>();
		new HandDetectedEvent().dispatch();
	}

	private int getDomainForHand(float x) {
		return getDomainForNormalizedPos(x / MainView.SRC_WIDTH);
	}

	private int getDomainForNormalizedPos(float x) {

		int domain = -1;
		if (x <= firstDomain)
			domain = 0;
		else if (x > firstDomain & x <= secondDomain)
			domain = 1;
		else if (x > secondDomain && x <= 1)
			domain = 2;
		return domain;
	}

	public void setDomains(float first, float second) {
		firstDomain = first;
		secondDomain = second;

		println("DOMAINS ==> " + firstDomain * MainView.SRC_WIDTH + " / " + secondDomain
				* MainView.SRC_HEIGHT);
	}

	private void println(Object msg) {
		//System.out.println(msg);
	}

}
