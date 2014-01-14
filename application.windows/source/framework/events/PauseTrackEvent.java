package framework.events;

import framework.data.MusicEntry;

public class PauseTrackEvent extends Event {

	private MusicEntry _entry;

	public PauseTrackEvent(MusicEntry entry) {
		super(EventType.PauseTrack);
		_entry = entry;
	}

	public MusicEntry get_entry() {
		return _entry;
	}
}
