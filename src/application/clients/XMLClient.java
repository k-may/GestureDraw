package application.clients;

import static processing.core.PApplet.println;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import framework.ErrorType;
import framework.clients.IDataClient;
import framework.data.AssetEntry;
import framework.data.FontEntry;
import framework.data.ImageEntry;
import framework.data.MusicEntry;
import framework.data.ShaderEntry;
import framework.events.ErrorEvent;
import gesturedraw.GestureDraw;

import processing.data.XML;

public class XMLClient{

	private XML assetXML;

	public XMLClient() {
		loadAssetsXML();	
	}
	
	private void loadAssetsXML(){
		assetXML = GestureDraw.instance.loadXML("assets.xml");
		
		if (assetXML == null) {
			new ErrorEvent(ErrorType.XMLPath, "assets.xml could not be found").dispatch();
			println("cant load");
		}
	}

	private void log(String msg) {
		println("XMLCLient : " + msg);
	}

	public ArrayList<ShaderEntry> readShaderEntries(){
		ArrayList<ShaderEntry> entries = new ArrayList<ShaderEntry>();
		
		for(XML child : assetXML.getChildren("shader")){
			String filePath = child.getContent().trim();
			String name = child.getString("name");
		}
		
		return entries;
	}
	
	
	public ArrayList<AssetEntry> readAssetEntries() {
		ArrayList<AssetEntry> entries = new ArrayList<AssetEntry>();
		for (XML child : assetXML.getChildren("asset")) {
			String filePath = child.getContent().trim();
			String name = child.getString("name");
			println("name : " + name);
			String type = child.getString("type");
			AssetEntry entry = new AssetEntry(name, type, filePath);
			entries.add(entry);
		}
		return entries;
	}

	public ArrayList<FontEntry> readFontEntries() {
		ArrayList<FontEntry> entries = new ArrayList<FontEntry>();
		for (XML child : assetXML.getChildren("font")) {
			String filePath = child.getContent().trim();
			String name = child.getString("name");
			int size = child.getInt("size");
			FontEntry entry = new FontEntry(name, filePath, size);
			entries.add(entry);
		}
		return entries;
	}

}
