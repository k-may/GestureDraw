package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PVector;
import application.interaction.processing.RegionInputData;
import application.view.MainView;
import framework.depth.DepthState;
import framework.depth.DepthStateData;
import framework.events.HandDetectedEvent;
import framework.interaction.Region;
import framework.interaction.Vector;
import framework.interaction.Types.HandType;
import framework.interaction.data.InteractionData;
import framework.interaction.data.InteractionStreamData;
import framework.interaction.data.InteractionTargetInfo;

public abstract class KinectRegion<T> extends Region<T> {

	protected static final float MIN_DAMPENING = 0.1f;
	protected static final float MAX_DAMPENING = 0.05f;

	protected float firstDomain;
	protected float secondDomain;

	protected int _maxHands;

	protected RegionType _regionType;

	protected HashMap<Integer, DepthStateData> _pressStateMap;

	private DomainManager _domainManager;

	public KinectRegion(T source, int maxHands, int xRange, int yRange,
			int zRange, RegionType type) {
		super(source);

		MainView.SRC_WIDTH = 640;
		MainView.SRC_HEIGHT = 480;
		MainView.TARGET_MASS = 0.01f;

		_regionType = type;
		_maxHands = maxHands;

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

		ArrayList<UserInputData> domainData = _domainManager.get_domainData();

		if (domainData.size() == 0)
			return;

		_adapter.beginInteractionFrame();

		for (UserInputData domain : domainData) {
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
	protected InteractionStreamData digestInput(UserInputData domainData) {

		int id = domainData.get_id();
		//returns current position
		PVector position = domainData.getPosition();
		HandType handType = domainData.handType;

		//digest UI information relevant to current position
		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(position.x, position.y, _type);
		Boolean isHoverTarget = info.get_isHoverTarget();
		Boolean isPressTarget = info.get_isPressTarget();

		//digest depth state
		DepthStateData depthStateData = _adapter.getInteractionInfoAtDepth(position.z);//getUserForDomain(id).get_depthStateData();
		DepthState pressState = depthStateData == null ? DepthState.None
				: depthStateData.get_state();

		//handle press intention
		Boolean isPressing = false;
		if (isPressTarget)
			isPressing = _pressHandler.getPressData(id, position.z, info.get_targetID());

		//apply attraction forces
		position = domainData.digest(info, pressState);
	
		//handle draw intention
		Boolean isDrawing = info.get_canvas() != null
				&& pressState == DepthState.Drawing;
		
		InteractionData data = new InteractionData(new Vector(position.x, position.y, position.z), isPressing, isDrawing);

		return new InteractionStreamData(data, id, _type, isHoverTarget, isPressTarget, handType, info.get_targets());

	}

	/*
	 * in-point, takes data directly from framework
	 */
	protected UserInputData updateHand(int handId, PVector pos) {
		UserInputData data = _domainManager.getDomainData(handId, pos);

		if (data != null) {
			data.addRawPosition(pos, handId);
		}
		return data;

	}

	@Override
	public void removeDomain(int id) {
		_domainManager.removeDomain(id);
	}

	@Override
	public ArrayList<InteractionStreamData> getStream() {
		return _stream;
	}

	public void setDomains(float first, float second) {
		if (_domainManager == null)
			_domainManager = new DomainManager(first, second);
	}

	private void println(Object msg) {
		System.out.println(msg);
	}

}
