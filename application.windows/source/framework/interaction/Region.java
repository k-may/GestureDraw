package framework.interaction;

import java.util.ArrayList;
import java.util.Map;

import processing.core.PVector;

import application.interaction.HandData;

import framework.events.StreamEndEvent;

public abstract class Region<T> implements IInteractionRegion {

	protected Map<Integer, HandData> _handData;
	protected PressHandler _pressHandler;
	protected IAdapter _adapter;
	protected int[] _users;
	protected T _source;
	protected ArrayList<InteractionStreamData> _stream;
	protected InteractionType _type;

	public Region(T source) {
		_source = source;
		_pressHandler = new PressHandler();
	}

	/*
	 * runInteractions() begins the processing of the interaction streams
	 * 
	 * Translate 2D UI to 3D interactions, apply pressTargetAttractions to
	 * location, map z values to user, get id from stream
	 */
	@Override
	public abstract void runInteractions();

	@Override
	public abstract ArrayList<InteractionStreamData> getStream();

	@Override
	public IAdapter get_adapter() {
		// TODO Auto-generated method stub
		return _adapter;
	}

	@Override
	public String get_name() {
		// TODO Auto-generated method stub
		return _source.getClass().getName();
	}

	@Override
	public Object get_source() {
		// TODO Auto-generated method stub
		return _source;
	}

	public void removeHand(int id) {
		if (_handData != null) {
			if (_handData.containsKey(id))
				_handData.remove(id);

			if (_handData.size() == 0) {
				_handData = null;

				new StreamEndEvent().dispatch();
			}
		}
	}

	public static PVector MapValuesToCurvedPlane(PVector position){
		  float midX = 0.5f; //rangeX /2;
		  float mX = (midX - position.x) / midX;
		  float thetaX = (float) (mX * Math.PI/2);

		 // println(mX + " : " + thetaX);
		  position.x = (float) (Math.sin(thetaX) * -midX + midX);

		  //float rangeY = height;
		  float midY =0.5f;// rangeY / 2;
		  float mY = (midY - position.y) / midY;
		  float thetaY = (float) (mY * Math.PI/2);
		  position.y = (float) (Math.sin(thetaY)*-midY + midY);

		  return position;
	}
}
