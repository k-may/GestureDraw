package framework.interaction;

import java.util.ArrayList;

import framework.interaction.data.InteractionStreamData;

import application.interaction.RegionType;

/*
 * Interface for containing and coordinating interactions
 * 
 * Provides an interaction stream
 */
public interface IInteractionRegion {
	void runInteractions();
	ArrayList<InteractionStreamData> getStream();
	IAdapter get_adapter();
	String get_name();
	Object get_source();
	RegionType getType();
	void removeUser(int id);
	int get_inputCount();
}
