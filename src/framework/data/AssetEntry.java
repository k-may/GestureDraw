package framework.data;

public class AssetEntry {
	private String _name;
	private String _type;
	private String _filePath;
	private int _size;

	public int get_size() {
		return _size;
	}

	public void set_size(int _size) {
		this._size = _size;
	}

	public AssetEntry(String name, String type, String filePath) {
		_name = name;
		_type = type;
		_filePath = filePath;
	}

	public String get_filePath() {
		return _filePath;
	}

	public String get_name() {
		return _name;
	}

	public String get_type() {
		return _type;
	}

}
