package framework.clients;

import java.util.ArrayList;

import framework.data.ImageEntry;
import framework.data.MusicEntry;

public interface IDataClient {

	String getInputType();

	int getXInputRange();

	int getYInputRange();

	int getZInputRange();

	float getHorUserRegion1();

	float getHorUserRegion2();

	int getMaxNumHands();

	/**
	 * @getClearable Whether "Clear" button is available in UI
	 */
	Boolean getClearable();

	ArrayList<ImageEntry> readImageEntries();

	void writeImageEntry(ImageEntry entry);

	ArrayList<MusicEntry> readMusicEntries();

	void writeMusicEntry(MusicEntry entry);
}
