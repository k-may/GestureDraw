package application.clients;

import framework.ErrorType;
import framework.events.ErrorEvent;
import processing.core.PApplet;
import processing.data.XML;

public abstract class XMLClient {

	protected XML loadXML(PApplet parent, String filePath) throws Exception {
		XML xml = parent.loadXML(filePath);
		if (xml == null) {
			throw new Exception("couldn't load xml : " + filePath);
		}
		return xml;
	}

	protected void writeXML(PApplet parent, String filePath, XML data)
			throws Exception {
		try {
			parent.saveXML(data, filePath);
		} catch (RuntimeException e) {
			throw new Exception("couldn't save xml: " + filePath);
		}
	}

	protected int getIntContent(String name) {
		try {
			return Integer.parseInt(getContent(name));
		} catch (NumberFormatException e) {
			new ErrorEvent(ErrorType.XMLNumberFormat, "XMLEntry " + name
					+ ", can't be formatted").dispatch();
			return 0;
		}
	}

	protected float getFloatContent(String name) {
		return Float.parseFloat(getContent(name));
	}

	protected Boolean getBooleanContent(String name) {
		Boolean value = true;
		value = Boolean.parseBoolean(getContent(name));
		return value;
	}

	protected abstract String getContent(String name);

	protected String getContent(String name, XML xml) throws Exception {
		String value = "";
		try {
			value = xml.getChild(name).getContent();// Integer.parseInt(dataXML.getChild("input_for_range").getContent());
		} catch (NullPointerException e) {
			throw new Exception("couldn't get data : " + name);
		}
		return value;
	}

}
