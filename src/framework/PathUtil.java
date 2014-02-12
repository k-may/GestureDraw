package framework;

import static processing.core.PApplet.println;
import framework.events.ErrorEvent;

public class PathUtil {

	private static String DATA_PATH;
	private static String IMAGE_PATH;
	private static String LOG_PATH;
	public static String TRACKS_PATH;

	public static void SetDataPath(String path) {
		int rootIndex = path.indexOf("lib/");

		if (rootIndex == -1)
			rootIndex = path.indexOf("bin/");

		try {
			path = path.substring(0, rootIndex);
			path += "bin/";

			println("::: BIN PATH :" + path + " :::");

			DATA_PATH = path;
			IMAGE_PATH = path + "images/";
			LOG_PATH = path + "logs/";
			TRACKS_PATH = path + "tracks/";

		} catch (Exception e) {
			new ErrorEvent(ErrorType.PathError, "can't find 'GestureDraw/bin' on application path, you may need to rename or move the application").dispatch();
		}
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
