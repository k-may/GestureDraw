package application.interaction.soni;

import java.util.ArrayList;
import java.util.Map;

import framework.events.StreamEndEvent;
import framework.interaction.data.InteractionStreamData;
import application.interaction.Adapter;
import application.interaction.KinectInputData;
import application.interaction.KinectRegion;
import application.interaction.RegionType;
import application.interaction.gestTrackOSC.domain.DomainInputData;
import processing.core.PVector;

import SimpleOpenNI.SimpleOpenNI;

import static processing.core.PApplet.println;

public class SONRegion extends KinectRegion<KinectInputData, SimpleOpenNI> {

	private Map<Integer, KinectInputData> _domainData;
	
	public SONRegion(SimpleOpenNI source, String gestureType) {
 		super(source, RegionType.SimpleOpenNI);
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
		if (_domainData.containsKey(id))
			_domainData.remove(id);
	}

	public void removeUser(int id) {

		if (_domainData != null) {
			if (_domainData.containsKey(id)) {
				KinectInputData data = _domainData.get(id);
				_domainData.remove(id);

			}

			if (_domainData.size() == 0) {
				_domainData = null;

				new StreamEndEvent().dispatch();
			}
		}
	}

	@Override
	public int get_inputCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ArrayList<KinectInputData> digestInputData(Map<Integer, PVector> inputQueue, ArrayList<Integer> removeQueue)  {
		return null;
	}

	@Override
	protected InteractionStreamData convertInputData(KinectInputData domainData) {
		// TODO Auto-generated method stub
		return null;
	}

}
