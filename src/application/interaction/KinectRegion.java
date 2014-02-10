package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PVector;
import application.view.MainView;
import framework.depth.DepthState;
import framework.depth.DepthStateData;
import framework.interaction.Region;
import framework.interaction.Vector;
import framework.interaction.Types.HandType;
import framework.interaction.data.InteractionData;
import framework.interaction.data.InteractionStreamData;
import framework.interaction.data.InteractionTargetInfo;

public abstract class KinectRegion<T> extends Region<T> {

	protected static final float MIN_DAMPENING = 0.1f;
	protected static final float MAX_DAMPENING = 0.05f;

	private Map<Integer, PVector> _inputQueue;
	private ArrayList<Integer> _removeQueue;

	protected float firstDomain;
	protected float secondDomain;

	protected int _maxHands;

	protected RegionType _regionType;

	protected HashMap<Integer, DepthStateData> _pressStateMap;

	private DomainManager _domainManager;

	public KinectRegion(T source, int maxHands, int xRange, int yRange,
			int zRange, RegionType type) {
		super(source);

		_inputQueue = new HashMap<Integer, PVector>();// new HashMap<Integer,
														// ArrayList<PVector>>();
		_removeQueue = new ArrayList<Integer>();

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

		// digest interactions
		ArrayList<UserInputData> domainData = digestInput();
		_stream = new ArrayList<InteractionStreamData>();
		for (UserInputData domain : domainData) {
			_stream.add(digestInputData(domain));
		}

		// update user states
		_adapter.beginInteractionFrame();
		_adapter.handleStreamData(_stream);
		_adapter.endInteractionFrame();
	}

	private ArrayList<UserInputData> digestInput() {

		// digest input queue
		for (Entry<Integer, PVector> entry : _inputQueue.entrySet()) {
			digestPosition(entry.getKey(), entry.getValue());
		}
		_inputQueue.clear();

		ArrayList<UserInputData> domainData = _domainManager.get_domainData();

		// remove
		for (Integer id : _removeQueue) {
			for (UserInputData user : domainData) {
				if (user.get_id() == id)
					user.removed = true;
			}
			_domainManager.removeDomain(id);
		}

		return domainData;
	}

	/*
	 * Mapping values only seems to work for the SONRegion at the moment, (may
	 * be skipping frames!?)
	 */
	protected InteractionStreamData digestInputData(UserInputData domainData) {

		int id = domainData.get_id();
		// returns current position
		PVector position = domainData.getPosition();
		HandType handType = domainData.handType;

		// digest UI information relevant to current position
		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(position.x, position.y, _type);
		Boolean isHoverTarget = info.get_isHoverTarget();
		Boolean isPressTarget = info.get_isPressTarget();

		// digest depth state
		DepthStateData depthStateData;
		Boolean isPressing = false;

		if (domainData.removed) {
			depthStateData = DepthStateData.Removed;
		} else {
			depthStateData = _adapter.getInteractionInfoAtDepth(position.z);// getUserForDomain(id).get_depthStateData();
			// handle press intention
			if (isPressTarget)
				isPressing = _pressHandler.getPressData(id, position.z, info.get_targetID());
		}

		// apply attraction forces
		position = domainData.digest(info, depthStateData.get_state());

		// handle draw intention
		Boolean isDrawing = info.get_canvas() != null
				&& depthStateData.get_state() == DepthState.Drawing;

		InteractionData data = new InteractionData(new Vector(position.x, position.y, position.z), isPressing, isDrawing);

		return new InteractionStreamData(data, id, _type, isHoverTarget, isPressTarget, handType, info.get_targets(), depthStateData);

	}

	/*
	 * in-point, takes data directly from framework add to queue to avoid
	 * threading issues
	 */
	protected void updateHand(int handId, PVector pos) {
		_inputQueue.put(handId, pos);
	}

	private UserInputData digestPosition(int handId, PVector pos) {
		UserInputData data = _domainManager.getDomainData(handId, pos.x);
		if (data != null) {
			data.addRawPosition(pos, handId);
		}
		return data;

	}

	@Override
	public void removeUser(int id) {
		if (!_removeQueue.contains(id))
			_removeQueue.add(id);
	}

	@Override
	public ArrayList<InteractionStreamData> getStream() {
		return _stream;
	}

	public void setDomains(float first, float second) {
		MainView.DOMAIN_1 = first;
		MainView.DOMAIN_2 = second;

		if (_domainManager == null)
			_domainManager = DomainManager.getInstance(); // new
															// DomainManager(first,
															// second);
	}

	private void println(Object msg) {
		System.out.println(msg);
	}

	@Override
	public int get_inputCount() {
		return _domainManager.getDataCount();
	}

	public static PVector MapValuesToCurvedPlane(PVector position) {
		float midX = 0.5f; // rangeX /2;
		float mX = (midX - position.x) / midX;
		float thetaX = (float) (mX * Math.PI / 2);

		PVector newPosition = new PVector();
		newPosition.z = position.z;
		// println(mX + " : " + thetaX);
		newPosition.x = (float) (Math.sin(thetaX) * -midX + midX);

		// float rangeY = height;
		float midY = 0.5f;// rangeY / 2;
		float mY = (midY - position.y) / midY;
		float thetaY = (float) (mY * Math.PI / 2);
		newPosition.y = (float) (Math.sin(thetaY) * -midY + midY);

		return newPosition;
	}
}
