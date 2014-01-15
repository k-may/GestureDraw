package application.interaction.soni;

import framework.events.StreamEndEvent;
import application.interaction.Adapter;
import application.interaction.HandData;
import application.interaction.KinectRegion;
import application.interaction.RegionType;
import processing.core.PVector;

import SimpleOpenNI.SimpleOpenNI;

import static processing.core.PApplet.println;

public class SONRegion extends KinectRegion<SimpleOpenNI> {

	public SONRegion(SimpleOpenNI source, int maxHands, int xRange, int yRange,
			int zRange, String gestureType) {
 		super(source, maxHands, xRange, yRange, zRange,RegionType.SimpleOpenNI);
		// enable depthMap generation
		Boolean enabled = true;
		
		enabled = source.enableDepth();
		enabled = !enabled || source.enableHand();

		if (gestureType.equals("wave"))
			source.startGesture(SimpleOpenNI.GESTURE_WAVE);
		else if (gestureType.equals("raise"))
			source.startGesture(SimpleOpenNI.GESTURE_HAND_RAISE);
		else
			source.startGesture(SimpleOpenNI.GESTURE_CLICK);

	}

	@Override
	public void runInteractions() {
		try{
		_source.update();
		}catch(Exception e){
			println("exception : " + e.getLocalizedMessage());
		}
		super.runInteractions();
	}

	public void onCompletedGesture(int gestureType, PVector pos) {
		_source.startTrackingHand(pos);
	}

	public void onNewHand(int id, PVector pos) {
		PVector lastPos = new PVector();
		_source.convertRealWorldToProjective(pos, lastPos);
		updateHand(id, lastPos);
	}

	public void onTrackedHand(int id, PVector pos) {
		PVector lastPos = new PVector();
		_source.convertRealWorldToProjective(pos, lastPos);
		updateHand(id, lastPos);
	}

	public void onLostHand(int id) {
		if (_handData.containsKey(id))
			_handData.remove(id);
	}

	@Override
	public RegionType getType() {
		// TODO Auto-generated method stub
		return RegionType.SimpleOpenNI;
	}

	public void removeHand(int id) {

		if (_handData != null) {
			if (_handData.containsKey(id)) {
				HandData data = _handData.get(id);
				System.out.println("remove hand : num samples = "
						+ data.get_sampleCount());
				_handData.remove(id);

			}

			if (_handData.size() == 0) {
				_handData = null;

				new StreamEndEvent().dispatch();
			}
		}
	}

}
