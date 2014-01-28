package framework.data;

public class GalleryEntry<T> {

	private ImageEntry _entry;
	private T _image;
	
	public GalleryEntry(ImageEntry entry, T image){
		_entry = entry;
		_image = image;
	}

	public String get_title() {
		return _entry.title;
	}

	public String[] get_artists() {
		return _entry.artists;
	}

	public T get_image() {
		return _image;
	}
}
