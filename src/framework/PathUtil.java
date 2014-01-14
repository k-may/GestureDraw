package framework;
import static processing.core.PApplet.println;

public class PathUtil {

	private static String DATA_PATH;
	private static String IMAGE_PATH;
	private static String LOG_PATH;
	public static String TRACKS_PATH;

	public static void SetDataPath(String path) {
		int rootIndex = path.indexOf("GestureDraw/");
		path = path.substring(0, rootIndex);
		path += "GestureDraw/bin/";
		
		println("==== BIN PATH :" + path + "====");
		
		DATA_PATH = path;
		IMAGE_PATH = path + "images/";
		LOG_PATH = path + "logs/";
	}

	public static String GetDataPath() {
		return DATA_PATH;
	}

	public static String GetImagePath() {
		return IMAGE_PATH;
	}

	public static String GetLogPath() {
		return LOG_PATH;
	}
}
