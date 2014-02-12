package application.interaction.gestTrackOSC;

import oscP5.OscP5;
import processing.core.PVector;
import application.interaction.KinectRegion;
import application.interaction.RegionType;
import application.interaction.processing.RegionInputData;
import application.view.MainView;
import framework.ErrorType;
import framework.events.ErrorEvent;

public abstract class GestTrackOSCRegion<T extends RegionInputData> extends
		KinectRegion<T, OscP5> implements IGestOSCRegion {

	 private GestTrackingType _trackingType = GestTrackingType.Normalized;

	public GestTrackOSCRegion(OscP5 source, RegionType type) {
		super(source, type);

		GestOSCProxy proxy = new GestOSCProxy(source, this);
		
	}

	public void onHand(int id, float x, float y, float z) {
		try {
			if (_trackingType == GestTrackingType.Normalized)
				getHand(id, x, y, z);
			else
				updateHand(id, new PVector(x, y, z));
		} catch (Exception e) {
			new ErrorEvent(ErrorType.KinectError, "GestTracker error").dispatch();
			//System.out.println("error : " + e.getLocalizedMessage());
		}
	}

	private void getHand(int id, float x, float y, float z) {
		updateHand(id, new PVector(x * MainView.SRC_WIDTH, y
				* MainView.SRC_HEIGHT, z * 1000));
	}


}
