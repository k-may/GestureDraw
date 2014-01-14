package framework.data;

public class ImageEntry {
	public String filePath;
	public String title;
	public String[] artists;
	public String date;

	public ImageEntry(String filePath, String title, String[] artists, String date) {
		this.filePath = filePath;
		this.title = title;
		this.artists = artists;
		this.date = date;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.filePath;
	}
}
