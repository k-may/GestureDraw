package framework.events;

import framework.interaction.IInteractionRegion;

public class InteractionRegionReadyEvent extends Event {

	private IInteractionRegion _region;
	public InteractionRegionReadyEvent(IInteractionRegion region) {
		super(EventType.InteractionReady);
		_region = region;
		// TODO Auto-generated constructor stub
	}
	public IInteractionRegion get_region() {
		return _region;
	}
	public void set_region(IInteractionRegion _region) {
		this._region = _region;
	}

}
