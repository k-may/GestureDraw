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

public class DataXMLClient extends XMLClient implements IDataClient {

	private static DataXMLClient instance;
	private static XML dataXML;
	private String _filePath;

	public static DataXMLClient getInstance() throws Exception{
		
		if (instance == null)
			instance = new DataXMLClient(PathUtil.GetDataPath());

		return instance;
	}

	private DataXMLClient(String filePath) throws Exception {
		_filePath = filePath + "config.xml";
		loadDataXML();
	}

	public void loadDataXML() throws Exception{
		try {
			dataXML = loadXML(GestureDraw.instance, _filePath);// GestureDraw.instance.loadXML(_filePath);
		} catch (Exception e) {
			new ErrorEvent(ErrorType.XMLPath, "path '" + _filePath
					+ "' could not be found").dispatch();
			println("cant load");
			throw e;
		}
		/*
		 * if (dataXML == null) { new ErrorEvent(ErrorType.XMLPath, "path '" +
		 * _filePath + "' could not be found").dispatch(); println("cant load");
		 * } else setTracksPath();
		 */
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

	public int getMaxStroke() {
		return getIntContent("ui_max_stroke");
	}

	public int getMinStroke() {
		return getIntContent("ui_min_stroke");
	}

	public int getColorWheelRadius() {
		return getIntContent("ui_color_wheel_radius");
	}

	public int getButtonSize() {
		return getIntContent("ui_button_size");
	}

	public float getCenterMass() {
		return getFloatContent("mass_center");
	}

	public float getTargetMass() {
		return getFloatContent("mass_button");
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

	@Override
	public float getHorUserRegion1() {
		return getFloatContent("user_region_ratio_1");
	}

	@Override
	public float getHorUserRegion2() {
		return getFloatContent("user_region_ratio_2");
	}
	
	@Override
	protected String getContent(String name) {
		try {
			return getContent(name, dataXML);
		}catch(Exception e){
			new ErrorEvent(ErrorType.XMLParsing, "couldn't find value for '"
					+ name + "' in config.xml").dispatch();
			return "";
		}
	}

}
