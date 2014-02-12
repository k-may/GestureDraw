package application.interaction.gestTrackOSC.hand;

import application.interaction.KinectInputData;

public class HandInputData extends KinectInputData {

	public HandInputData(int id) {
		super(new HandCursorHandler());
		_id = id;
	}

}
