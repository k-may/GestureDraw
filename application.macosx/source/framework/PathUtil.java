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

		if (rootIndex == -1) {
			// local
			rootIndex = path.indexOf("bin/");
			path = path.substring(0, rootIndex);
			path += "bin/";
		} else {
			path = path.substring(0, rootIndex);
		}
		println("::: BIN PATH :" + path + " :::");

		DATA_PATH = path;
		IMAGE_PATH = path + "images/";
		LOG_PATH = path + "logs/";
		TRACKS_PATH = path + "tracks/";
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
