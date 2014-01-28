package application.interaction;

import framework.ErrorType;
import framework.events.ErrorEvent;

public class RegionTypeHelper {

	public static RegionType GetTypeForString(String str) {
		RegionType type = RegionType.Processing;

		if (str.equals("gesttracker"))
			type = RegionType.GestTrackOSC;
		else if (str.equals("soni"))
			type = RegionType.SimpleOpenNI;
		else if (!str.equals("mouse"))
			new ErrorEvent(ErrorType.INIT, "no input type for '" + str
					+ "', try 'mouse', 'gesttracker', or 'soni'").dispatch();

		return type;
	}
}
