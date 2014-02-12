package application.interaction.gestTrackOSC;

import static processing.core.PApplet.println;
import application.view.MainView;
import framework.ErrorType;
import framework.events.ErrorEvent;
import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscStatus;
import processing.core.PVector;

public class GestOSCProxy implements OscEventListener {

	private GestTrackingType _trackingType;
	private IGestOSCRegion _region;

	public GestOSCProxy(OscP5 source, IGestOSCRegion region) {
		_trackingType = GestTrackingType.Normalized;
		_region = region;

		source.addListener(this);

		for (int i = 0; i < 11; i++) {
			source.plug(this, "onHand" + i, _trackingType.toString() + "/hand"
					+ i);
		}
	}

	public void onHand0(float x, float y, float z) {
		_region.onHand(0, x, y, z);
	}

	public void onHand1(float x, float y, float z) {
		_region.onHand(1, x, y, z);
	}

	public void onHand2(float x, float y, float z) {
		_region.onHand(2, x, y, z);
	}

	public void onHand3(float x, float y, float z) {
		_region.onHand(3, x, y, z);
	}

	public void onHand4(float x, float y, float z) {
		_region.onHand(4, x, y, z);
	}

	public void onHand5(float x, float y, float z) {
		_region.onHand(5, x, y, z);
	}

	public void onHand6(float x, float y, float z) {
		_region.onHand(6, x, y, z);
	}

	public void onHand7(float x, float y, float z) {
		_region.onHand(7, x, y, z);
	}

	public void onHand8(float x, float y, float z) {
		_region.onHand(8, x, y, z);
	}

	public void onHand9(float x, float y, float z) {
		_region.onHand(9, x, y, z);
	}

	public void onHand10(float x, float y, float z) {
		_region.onHand(10, x, y, z);
	}

	public void onHand11(float x, float y, float z) {
		_region.onHand(11, x, y, z);
	}


	@Override
	public void oscEvent(OscMessage theOscMessage) {
		if (theOscMessage.checkAddrPattern("/normalized/hand0") == true) {
			/* check if the typetag is the right one. */
			if (theOscMessage.checkTypetag("fff")) {
				/*
				 * parse theOscMessage and extract the values from the osc
				 * message arguments.
				 */
				float firstValueX = theOscMessage.get(0).floatValue();
				float secondValueY = theOscMessage.get(1).floatValue();
				float thirdValueZ = theOscMessage.get(2).floatValue();
				return;
			}
		}
		if (theOscMessage.checkAddrPattern("/absolute/hand0") == true) {
			/* check if the typetag is the right one. */
			if (theOscMessage.checkTypetag("fff")) {
				/*
				 * parse theOscMessage and extract the values from the osc
				 * message arguments.
				 */
				float firstValueX = theOscMessage.get(0).floatValue();
				float secondValueY = theOscMessage.get(1).floatValue();
				float thirdValueZ = theOscMessage.get(2).floatValue();
				return;
			}
		}
	}

	@Override
	public void oscStatus(OscStatus arg0) {
		println(":::::::::::: OscStatus ::::::::::: " + arg0);
	}

}
