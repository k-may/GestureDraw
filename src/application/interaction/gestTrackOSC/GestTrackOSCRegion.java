package application.interaction.gestTrackOSC;

import java.util.ArrayList;

import framework.ErrorType;
import framework.events.ErrorEvent;
import framework.interaction.InteractionStreamData;
import gesturedraw.GestureDraw;

import application.interaction.Adapter;
import application.interaction.DomainData;
import application.interaction.KinectRegion;
import application.interaction.RegionType;

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscStatus;
import processing.core.PVector;

import static processing.core.PApplet.println;

public class GestTrackOSCRegion extends KinectRegion<OscP5> implements
		OscEventListener {

	private int _lastUpdate;
	private float _frameRate;

	private GestTrackingType _trackingType;

	public GestTrackOSCRegion(OscP5 source, int xRange,
			int yRange, int zRange) {
		super(source, 3, xRange, yRange, zRange, RegionType.GestTrackOSC);
		_trackingType = GestTrackingType.Normalized;

		source.addListener(this);

		for (int i = 0; i < 11; i++) {
			source.plug(this, "onHand" + i, _trackingType.toString() + "/hand"
					+ i);
		}
		/*
		 * source.plug(this, "onHand1", _trackingType.toString() + "/hand0");
		 * source.plug(this, "onHand2", _trackingType.toString() + "/hand1");
		 * source.plug(this, "onHand3", _trackingType.toString() + "/hand2");
		 */
	}

	public void onHand0(float x, float y, float z) {
		//println(x + " : " + y + " : " + z);
		onHand(0, x, y, z);
	}

	public void onHand1(float x, float y, float z) {
		onHand(1, x, y, z);
	}

	public void onHand2(float x, float y, float z) {
		onHand(2, x, y, z);
	}

	public void onHand3(float x, float y, float z) {
		onHand(3, x, y, z);
	}

	public void onHand4(float x, float y, float z) {
		onHand(4, x, y, z);
	}

	public void onHand5(float x, float y, float z) {
		onHand(5, x, y, z);
	}

	public void onHand6(float x, float y, float z) {
		onHand(6, x, y, z);
	}

	public void onHand7(float x, float y, float z) {
		onHand(7, x, y, z);
	}

	public void onHand8(float x, float y, float z) {
		onHand(8, x, y, z);
	}

	public void onHand9(float x, float y, float z) {
		onHand(9, x, y, z);
	}

	public void onHand10(float x, float y, float z) {
		onHand(10, x, y, z);
	}

	public void onHand11(float x, float y, float z) {
		onHand(11, x, y, z);
	}

	private void onHand(int id, float x, float y, float z) {
		try {
			if (_trackingType == GestTrackingType.Normalized)
				getHand(id, x, y, z);
			else
				updateHand(id, new PVector(x, y, z));
		} catch (Exception e) {
			new ErrorEvent(ErrorType.KinectError, "GestTracker error").dispatch();
			System.out.println("error : " + e.getLocalizedMessage());
		}
	}

	private void getHand(int id, float x, float y, float z) {
		/*
		 * int time = GestureDraw.instance.millis();
		 * 
		 * float elapsed = time - _lastUpdate; _frameRate = 1000 / elapsed;
		 * _lastUpdate = time;
		 * 
		 * println("framerate : " + _frameRate + " / " +
		 * GestureDraw.instance.frameRate);
		 */

		/*
		 * if (_handData != null) { if (!_handData.containsKey(id)) { float
		 * distance; PVector pos = new PVector(x, y, z); for (HandData handData
		 * : _handData.values()) { distance = PVector.dist(pos,
		 * handData.getPosition()); println("distance:" + distance);
		 * 
		 * } } }
		 */
		updateHand(id, new PVector(x * CAM_WIDTH, y * CAM_HEIGHT, z * 1000));
	}

	@Override
	public void oscEvent(OscMessage theOscMessage) {
		if (theOscMessage.checkAddrPattern("/normalized/hand0") == true) {
			/* check if the typetag is the right one. */
			if (theOscMessage.checkTypetag("fff")) {
				// println(theOscMessage.get(0).stringValue());
				/*
				 * parse theOscMessage and extract the values from the osc
				 * message arguments.
				 */
				float firstValueX = theOscMessage.get(0).floatValue();
				float secondValueY = theOscMessage.get(1).floatValue();
				float thirdValueZ = theOscMessage.get(2).floatValue();
				// println("normalizzed hand0 x: " + firstValueX + ", y: "+
				// secondValueY + ", z: " + thirdValueZ);
				return;
			}
		}
		if (theOscMessage.checkAddrPattern("/absolute/hand0") == true) {
			/* check if the typetag is the right one. */
			if (theOscMessage.checkTypetag("fff")) {
				// println(theOscMessage.get(0).stringValue());
				/*
				 * parse theOscMessage and extract the values from the osc
				 * message arguments.
				 */
				float firstValueX = theOscMessage.get(0).floatValue();
				float secondValueY = theOscMessage.get(1).floatValue();
				float thirdValueZ = theOscMessage.get(2).floatValue();
				// println("absolute hand0 x: " + firstValueX + ", y: "+
				// secondValueY + ", z: " + thirdValueZ);
				return;
			}
		}
	}

	@Override
	public void oscStatus(OscStatus arg0) {
		// TODO Auto-generated method stub
		println("status : " + arg0);
	}

	@Override
	public RegionType getType() {
		// TODO Auto-generated method stub
		return RegionType.GestTrackOSC;
	}

	@Override
	public void removeHand(int id) {

	}

}
