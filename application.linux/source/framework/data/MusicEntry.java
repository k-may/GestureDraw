package framework.data;

public class MusicEntry{
	public String filePath;
	public String trackName;
	public String artist;
	
	public MusicEntry(String filePath, String artist, String track) {
		this.filePath = filePath;
		this.artist = artist;
		this.trackName = track;
	}
}
