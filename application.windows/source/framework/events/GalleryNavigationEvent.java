package framework.events;

public class GalleryNavigationEvent extends Event {

	private String _direction;
	
	public GalleryNavigationEvent(String direction) {
		super(EventType.GalleryNavigation);
		_direction = direction;
	}

	public String get_direction() {
		return _direction;
	}

}
