package framework.interaction.press;

import java.util.HashMap;

import framework.BaseMainView;

public class PressHandler {

	protected HashMap<Integer, PressHandle> _pressHandles;

	public PressHandler() {
		_pressHandles = new HashMap<Integer, PressHandle>();
	}

	public Boolean getPressData(int userId, float position, int targetId) {

		Boolean isPressExtents = position > BaseMainView.PRESS_POS_EXTENTS; //get_isPressing(userId, position);
		PressHandle pressHandle = getPressHandle(userId, targetId);

		if (pressHandle != null) {
			isPressExtents = pressHandle.update(position);
		} else {
			// only add if not pressing
			if (!isPressExtents){
				addPressHandle(userId, targetId, position);
			}
		}

		return isPressExtents;
	}


	private PressHandle getPressHandle(int handId, int targetID) {

		if (_pressHandles.containsKey(handId)) {
			if (_pressHandles.get(handId).get_targetID() == targetID)
				return _pressHandles.get(handId);
			else {
				// target has changed
				_pressHandles.remove(handId);
				return null;
			}
		} else
			return null;
	}

	private void addPressHandle(int handId, int targetId, float startPos) {
		PressHandle handle = new PressHandle(targetId, startPos);
		_pressHandles.put(handId, handle);
	}

}
