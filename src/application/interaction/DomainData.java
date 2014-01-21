package application.interaction;

import java.util.HashMap;

import processing.core.PVector;
import framework.data.HandData;
import framework.interaction.Types.HandType;
import framework.pressing.PressState;

public class DomainData extends KinectHandData {

	private HashMap<Integer, HandData> _handMap;
	public HandType handType = HandType.Right;
	private HandData primary;

	public DomainData(int domain) {
		super(domain);// _id = domain;
		_handMap = new HashMap<Integer, HandData>();
	}

	@Override
	protected void addPosition(PVector pos, float dampening, int handId) {

		if (_handMap.containsKey(handId))
			_handMap.get(handId).updateCount();
		else
			addHand(pos, handId);

		primary = getPrimaryHand();

		if (primary.get_id() == handId)
			super.addPosition(pos, dampening, handId);
	}

	private void addHand(PVector pos, int handId) {

		if (_handMap.values().size() > 1)
			return;

		if (primary == null) {
			// init first hand (defaults to right)
			primary = new HandData(handId, HandType.Right);
			_handMap.put(handId, primary);
		} else {

			HandType newHandType = pos.x < position.x ? HandType.Right
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

	private PVector getMagnitude(PressState state) {
		PVector magnitude = null;
		if (state == PressState.PreDrawing || state == PressState.Drawing) {
			// PVector target = position;
			getDrawingMagnitude();
		} else {
			getNavigationMagnitude();
			// PVector target =
		}
		return magnitude;
	}

	private void getDrawingMagnitude() {
		// TODO Auto-generated method stub

	}

	private void getNavigationMagnitude() {
		// TODO Auto-generated method stub

	}

}
