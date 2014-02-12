package application.interaction.gestTrackOSC.domain;

import java.util.HashMap;

import application.interaction.KinectInputData;
import application.interaction.processing.RangeHandler;

import processing.core.PVector;
import framework.data.HandData;
import framework.depth.DepthState;
import framework.interaction.Types.HandType;

public class DomainInputData extends KinectInputData {


	private static int ID_COUNT = 0;
	private int GetUniqueID() {
		return ID_COUNT++;
	}

	private HashMap<Integer, HandData> _handMap;
	public HandType handType = HandType.Right;
	private HandData primary;
	public int domain;

	/*
	 * Class is extra layer on top of regular input data to map input hand ids
	 * and determine a primary hand. Only tested so far with GestTracker
	 */
	public DomainInputData() {
		super(new DomainCursorHandler(), new RangeHandler());
		_id = GetUniqueID();
		_handMap = new HashMap<Integer, HandData>();
	}

	private void addHand(PVector pos, int handId) {

		if (_handMap.values().size() >= 2)
			return;

		if (primary == null) {
			// init first hand (defaults to right)
			primary = new HandData(handId, HandType.Right);
			_handMap.put(handId, primary);
		} else {

			HandType newHandType = pos.x < _rawPosition.x ? HandType.Right
					: HandType.Left;

			// test if not the same
			if (newHandType != getPrimaryHand().getType()) {
				_handMap.put(handId, new HandData(handId, newHandType));
			}
		}
	}

	public HandData getPrimaryHand() {
		int count = -1;
		HandData data = null;
		for (HandData handData : _handMap.values()) {
			if (handData.getUseCount() > count) {
				data = handData;
				count = handData.getUseCount();
			}
		}
		return data;
	}

	@Override
	public void addRawPosition(PVector pos, int handId) {
		
		//update count to determine primary hand id for domain
		if (_handMap.containsKey(handId))
			_handMap.get(handId).updateCount();
		else
			addHand(pos, handId);

		primary = getPrimaryHand();

		if (primary.get_id() == handId)
			super.addRawPosition(pos, handId);

		updated = true;
	}

	
	@Override
	protected Boolean updateDepthState(DepthState state) {
		Boolean changed = _depthState != state;
		_depthState = state;

		if (changed) {
			if (!_depthState.isDrawing()) {
				resetRange(_rawPosition, _cursorPos);
			}
		}

		return changed;
	}
	


}
