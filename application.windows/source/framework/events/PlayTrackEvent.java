package framework.events;

import framework.data.MusicEntry;

public class PlayTrackEvent extends Event {

	private MusicEntry _entry;
	
	public PlayTrackEvent(MusicEntry entry) {
		super(EventType.PlayTrack);
		
		set_entry(entry);
	}
	public MusicEntry get_entry() {
		return _entry;
	}
	public void set_entry(MusicEntry _entry) {
		this._entry = _entry;
	}

}
