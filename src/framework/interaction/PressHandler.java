package framework.interaction;

import java.util.HashMap;

import processing.core.PVector;
import application.interaction.DomainData;

public class PressHandler {

	protected HashMap<Integer, Boolean> _pressStates;
	protected HashMap<Integer, PressHandle> _pressHandles;

	public PressHandler() {
		_pressHandles = new HashMap<Integer, PressHandle>();
		_pressStates = new HashMap<Integer, Boolean>();
	}

	public PressData getPressData(DomainData handData, InteractionTargetInfo info) {

		Boolean isPressTarget = false;
		Boolean isPressTendency = getPressState(handData);
		
		float pressure = 0.0f;
		float x = handData.getMappedPosition().x;
		float y = handData.getMappedPosition().y;

		PressHandle pressHandle = getPressHandle(handData, info, isPressTendency);
		if (pressHandle != null) {
			pressHandle.update(handData.getTendency(), handData.getMappedPosition());
			isPressTarget = true;
			pressure = pressHandle.getPressure();

			if (pressHandle.getPressTendency())
				isPressTendency = false;

			pressHandle.setPressTendency(isPressTendency);
		}

		return new PressData(x, y, pressure, isPressTarget, isPressTendency);
	}

	private Boolean getPressState(DomainData handData) {
		Boolean pressState = handData.getMappedPosition().z > PressHandle.PRESS_EXTENTS;
		Boolean currentState = false;

		if (_pressStates.containsKey(handData.get_id()))
			currentState = _pressStates.get(handData.get_id());

		if (pressState) {
			if (!currentState) {
				_pressStates.put(handData.get_id(), true);
				return true;
			} else if (currentState) {
				return false;
			}
		}

		_pressStates.put(handData.get_id(), false);
		return false;

	}

	private PressHandle getPressHandle(DomainData handData,
			InteractionTargetInfo info, Boolean isPressTendency) {
		PressHandle handle = null;
		if (_pressHandles.containsKey(handData.get_id())) {
			if (_pressHandles.get(handData.get_id()).get_targetID() == info.get_targetID())
				handle = _pressHandles.get(handData.get_id());
			else {
				//println("===>>" + "remove handle");
				_pressHandles.remove(handData.get_id());
			}

		}

		if (handle == null && info.get_isPressTarget() && !isPressTendency) {
			//println("===>>" + "create new handle");
			PVector targetPos = new PVector(info.get_pressAttractionX(), info.get_pressAttractionY(), 1.0f);
			handle = new PressHandle(info.get_targetID(), targetPos, handData.getMappedPosition());
			_pressHandles.put(handData.get_id(), handle);
		}

		return handle;
	}

}
