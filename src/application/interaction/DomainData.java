package application.interaction;

import java.util.HashMap;

import processing.core.PApplet;
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
		_currTarget = _pos = new PVector(0.5f, 0.5f, 0);
	}

	private void addHand(PVector pos, int handId) {

		if (_handMap.values().size() > 1)
			return;

		if (primary == null) {
			// init first hand (defaults to right)
			primary = new HandData(handId, HandType.Right);
			_handMap.put(handId, primary);
		} else {

			HandType newHandType = pos.x < _position.x ? HandType.Right
					: HandType.Left;

			// test if not the same
			if (newHandType != getPrimaryHand().getType()) {
				_handMap.put(handId, new HandData(handId, newHandType));
			}
		}
	}

	@Override
	public PVector getPosition() {
		return new PVector(_pos.x, _pos.y, _mappedPos.z);
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
	public void addPosition(PVector pos, float dampening, int handId) {

		if (_handMap.containsKey(handId))
			_handMap.get(handId).updateCount();
		else
			addHand(pos, handId);

		primary = getPrimaryHand();

		if (primary.get_id() == handId)
			super.addPosition(pos, dampening, handId);

		updateMagnitude(getMappedPosition());

		_isUpdated = true;
	}

	private String printV(PVector v) {
		return "[" + v.x + "," + v.y + "]";
	}

}
