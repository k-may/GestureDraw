package framework;

import framework.events.Event;
import framework.events.EventType;
import framework.view.CanvasState;

public class CanvasStateUpdateEvent extends Event {

	private CanvasState _state;

	public CanvasStateUpdateEvent(CanvasState state) {
		super(EventType.CanvasStateUpdate);
		_state = state;
	}
	
	public CanvasState getState(){
		return _state;
	}

}
