package framework.interaction;

import java.util.ArrayList;

import application.interaction.RegionType;

import processing.core.PVector;
import framework.interaction.Types.InteractionType;
import framework.interaction.data.InteractionStreamData;
import framework.interaction.press.PressHandler;

public abstract class Region<T> implements IInteractionRegion {

	protected PressHandler _pressHandler;
	protected IAdapter _adapter;
	protected T _source;
	protected ArrayList<InteractionStreamData> _stream;

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

}
