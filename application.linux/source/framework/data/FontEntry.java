package framework.data;

public class FontEntry {
	private String _name;
	private String _filePath;
	private int _size;

	public FontEntry(String name, String filePath, int size) {
		_name = name;
		_filePath = filePath;
		_size = size;
	}

	public String get_filePath() {
		return _filePath;
	}

	public String get_name() {
		return _name;
	}
	
	public int get_size() {
		return _size;
	}
}
