package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import processing.core.PVector;
import application.interaction.processing.RegionInputData;
import application.view.MainView;
import framework.depth.DepthStateData;
import framework.interaction.Region;
import framework.interaction.data.InteractionStreamData;

public abstract class KinectRegion<B extends RegionInputData, T> extends
		Region<T> {

	protected static final float MIN_DAMPENING = 0.1f;
	protected static final float MAX_DAMPENING = 0.05f;

	private Boolean _isDigesting = false;

	private Map<Integer, PVector> _inputQueue;
	private ArrayList<Integer> _removeQueue;

	protected RegionType _regionType;
	protected HashMap<Integer, DepthStateData> _pressStateMap;

	public KinectRegion(T source, RegionType type) {
		super(source);

		_inputQueue = new HashMap<Integer, PVector>();// new HashMap<Integer,
														// ArrayList<PVector>>();
		_removeQueue = new ArrayList<Integer>();

		MainView.SRC_WIDTH = 640;
		MainView.SRC_HEIGHT = 480;
		MainView.TARGET_MASS = 0.01f;

		MainView.INVALID_DISTANCE = 100.0f / MainView.SRC_WIDTH;

		_regionType = type;
		_pressStateMap = new HashMap<Integer, DepthStateData>();

		_adapter = new Adapter();
	}

	@Override
	public void runInteractions() {

		_isDigesting = true;
		// digest interactions, input queue
		ArrayList<B> inputData = digestInputData(_inputQueue, _removeQueue);
		_inputQueue.clear();
		_removeQueue.clear();
		_stream = new ArrayList<InteractionStreamData>();
		// convert inputs to outputs
		for (B data : inputData) {
			InteractionStreamData iSD = convertInputData(data);
			if (iSD != null)
				_stream.add(iSD);
		}

		// update user states
		_adapter.beginInteractionFrame();
		_adapter.handleStreamData(_stream);
		_adapter.endInteractionFrame();

		_isDigesting = false;
	}

	protected abstract ArrayList<B> digestInputData(
			Map<Integer, PVector> inputQueue, ArrayList<Integer> removeQueue);

	/*
	 * Regions may want to utilize different types/algorithms for digesting
	 * input
	 */
	protected abstract InteractionStreamData convertInputData(B domainData);

	/*
	 * in-point, takes data directly from framework add to queue to avoid
	 * threading issues
	 */
	protected void updateHand(int handId, PVector pos) {
		if (!_isDigesting)
			_inputQueue.put(handId, pos);
	}

	@Override
	public void removeUser(int id) {
		//System.out.println("remove user : " + id);
		if (!_removeQueue.contains(id))
			_removeQueue.add(id);
	}

	@Override
	public ArrayList<InteractionStreamData> getStream() {
		return _stream;
	}

	private void println(Object msg) {
		//System.out.println(msg);
	}

}
