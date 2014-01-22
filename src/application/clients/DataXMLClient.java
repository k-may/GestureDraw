package application.clients;

import static processing.core.PApplet.println;

import java.util.ArrayList;

import processing.data.XML;
import framework.ErrorType;
import framework.PathUtil;
import framework.clients.IDataClient;
import framework.data.ImageEntry;
import framework.data.MusicEntry;
import framework.events.ErrorEvent;
import gesturedraw.GestureDraw;

public class DataXMLClient implements IDataClient {

	private static DataXMLClient instance;
	private static XML dataXML;
	private String _filePath;

	public static DataXMLClient getInstance() {
		if (instance == null)
			instance = new DataXMLClient(PathUtil.GetDataPath());

		return instance;
	}

	private DataXMLClient(String filePath) {
		_filePath = filePath + "config.xml";
		loadDataXML();
	}

	private void loadDataXML() {
		
		dataXML = GestureDraw.instance.loadXML(_filePath);

		if (dataXML == null) {
			new ErrorEvent(ErrorType.XMLPath, "path '" + _filePath
					+ "' could not be found").dispatch();
			println("cant load");
		} else
			setTracksPath();
	}

	private void setTracksPath() {
		XML child = dataXML.getChild("trackpath");

		if (child != null) {
			String path = child.getContent().trim();
			println("-->>>>tracks path : " + path);
			PathUtil.TRACKS_PATH = path;
		}
	}

	public void writeImageEntry(ImageEntry entry) {
		String data = entry.toString();
		XML newChild = dataXML.addChild("image");
		newChild.setContent(PathUtil.GetImagePath() + entry.filePath);
		newChild.setString("title", entry.title);
		newChild.setString("artists", join(entry.artists, ","));
		save();
	}

	public void writeMusicEntry(MusicEntry entry) {
		// TODO Auto-generated method stub
		String data = entry.toString();
		XML newChild = dataXML.addChild("music");
		newChild.setContent(data);
		save();
	}

	private void save() {
		try {
			GestureDraw.instance.saveXML(dataXML, _filePath);
		} catch (RuntimeException e) {
			new ErrorEvent(ErrorType.Save, "could not save file at path, '"
					+ _filePath
					+ "'. Try checking the permissions on the folder").dispatch();
		}
	}

	public ArrayList<MusicEntry> readMusicEntries() {

		if (PathUtil.TRACKS_PATH == null) {
			setTracksPath();
		}

		ArrayList<MusicEntry> entries = new ArrayList<MusicEntry>();

		for (XML child : dataXML.getChildren("music")) {
			String filePath = PathUtil.TRACKS_PATH + child.getContent().trim();
			String artist = child.getString("artist").trim();
			String track = child.getString("name").trim();
			MusicEntry entry = new MusicEntry(filePath, artist, track);
			entries.add(entry);
		}

		return entries;
	}

	public ArrayList<ImageEntry> readImageEntries() {
		ArrayList<ImageEntry> entries = new ArrayList<ImageEntry>();

		for (XML child : dataXML.getChildren("image")) {
			String filePath = child.getContent().trim();
			String[] artists = child.getString("artists").split("\\,");
			String title = child.getString("title");
			String date = child.getString("date");
			ImageEntry entry = new ImageEntry(filePath, title, artists, date);
			entries.add(entry);
		}

		return entries;
	}

	public static String join(String r[], String d) {
		if (r.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		int i;
		for (i = 0; i < r.length - 1; i++)
			sb.append(r[i] + d);
		return sb.toString() + r[i];
	}

	@Override
	public String getInputType() {
		return getContent("input");
	}

	@Override
	public int getMaxNumHands() {
		return getIntContent("input_max_handnum");
	}

	@Override
	public int getXInputRange() {
		return getIntContent("input_hor_range");
	}

	@Override
	public int getYInputRange() {
		return getIntContent("input_ver_range");
	}

	@Override
	public int getZInputRange() {
		return getIntContent("input_for_range");
	}

	@Override
	public Boolean getClearable() {
		return getBooleanContent("ui_clearable");
	}

	public String getStartGestureType() {
		return getContent("soni_start_gesture");
	}

	private int getIntContent(String name) {
		return Integer.parseInt(getContent(name));
	}

	private float getFloatContent(String name) {
		return Float.parseFloat(getContent(name));
	}

	private Boolean getBooleanContent(String name) {
		Boolean value = true;
		value = Boolean.parseBoolean(getContent(name));
		return value;
	}

	private String getContent(String name) {
		String value = "";
		try {
			value = dataXML.getChild(name).getContent();// Integer.parseInt(dataXML.getChild("input_for_range").getContent());
		} catch (NullPointerException e) {
			new ErrorEvent(ErrorType.XMLParsing, "couldn't find value for '"
					+ name + "' in config.xml").dispatch();
		}
		return value;
	}

	@Override
	public float getHorUserRegion1() {
		return getFloatContent("user_region_ratio_1");
	}

	@Override
	public float getHorUserRegion2() {
		return getFloatContent("user_region_ratio_2");
	}

}
