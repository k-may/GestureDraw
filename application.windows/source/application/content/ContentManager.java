package application.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import application.clients.XMLClient;
import application.view.canvas.PGalleryEntry;
import application.view.colorwheel.ColorWheel;

import framework.Controller;
import framework.ErrorType;
import framework.data.AssetEntry;
import framework.data.FontEntry;
import framework.data.GalleryEntry;
import framework.data.ImageEntry;
import framework.data.ShaderEntry;
import framework.events.ErrorEvent;
import gesturedraw.GestureDraw;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.opengl.PShader;
import static processing.core.PApplet.println;

public class ContentManager {

	private static Map<String, PShader> Shaders;
	private static Map<String, PIcon> Icons;
	private static Map<String, FontInfo> Fonts;
	private static ArrayList<GalleryEntry<PImage>> GalleryEntries;
	private static PApplet _parent;
	private static ContentManager _instance;

	public static ContentManager getInstance() {
		if (_instance == null)
			_instance = new ContentManager(GestureDraw.instance);

		return _instance;
	}

	private ContentManager(PApplet parent) {
		_parent = parent;
		
		ColorWheel wheel = new ColorWheel();
	}

	public void loadAssets(GestureDraw parent,
			ArrayList<AssetEntry> readAssetEntries,
			ArrayList<FontEntry> fontEntries) {

		ArrayList<AssetEntry> shaderEntries = new ArrayList<AssetEntry>();
		ArrayList<AssetEntry> iconEntries = new ArrayList<AssetEntry>();

		String type;
		for (AssetEntry entry : readAssetEntries) {
			type = entry.get_type();
			if (type.equals("shader"))
				shaderEntries.add(entry);
			else if (type.equals("image")) {
				iconEntries.add(entry);
			}
		}

		try {
			//loadShaders(parent, shaderEntries);
			loadIcons(parent, iconEntries);
			loadFonts(parent, fontEntries);
		} catch (Exception e) {
			dispatchAssetError(e.getMessage());
		}
	}

	private void loadShaders(GestureDraw parent, ArrayList<AssetEntry> entries) {
		PShader shader;
		String path;
		for (AssetEntry entry : entries) {
			path = entry.get_filePath();
			shader = parent.loadShader(path);
			if (shader == null) {
				dispatchAssetError(path);
				break;
			}

			if (Shaders == null)
				Shaders = new HashMap<String, PShader>();

			Shaders.put(entry.get_name(), shader);
		}
	}

	// fonts
	public void loadFonts(PApplet instance, ArrayList<FontEntry> readFontEntries) {

		PFont font;
		String path;
		for (FontEntry entry : readFontEntries) {
			path = entry.get_filePath();
			try {
				font = instance.loadFont(path);
			} catch (NullPointerException e) {
				dispatchAssetError(path);
				break;
			} catch (RuntimeException e) {
				// if (font == null) {
				dispatchAssetError(path);
				break;
			}

			FontInfo fontInfo = new FontInfo(font, entry);

			if (Fonts == null)
				Fonts = new HashMap<String, FontInfo>();

			Fonts.put(entry.get_name(), fontInfo);
		}
	}

	// icons
	private void loadIcons(PApplet parent, ArrayList<AssetEntry> entries)
			throws Exception {

		String path;
		PImage icon;
		for (AssetEntry entry : entries) {
			path = entry.get_filePath();
			icon = parent.loadImage(path);

			if (icon == null) {
				throw new Exception(path);
			}

			PIcon asset = new PIcon(entry, icon);
			if (Icons == null)
				Icons = new HashMap<String, PIcon>();

			Icons.put(entry.get_name(), asset);
		}

	}

	// galleries
	public void loadGalleryEntries(PApplet instance,
			ArrayList<ImageEntry> readImageEntries) {

		PImage image;
		String path;
		for (ImageEntry entry : readImageEntries) {
			path = entry.filePath;
			image = instance.loadImage(path);
			if (image == null) {
				dispatchAssetError(path);
				break;
			}
			addGalleryImage(entry, instance.loadImage(entry.filePath));
		}
	}

	private void addGalleryImage(ImageEntry entry, PImage image) {
		printload(entry.title);

		if (image == null)
			return;

		if (GalleryEntries == null)
			GalleryEntries = new ArrayList<GalleryEntry<PImage>>();

		GalleryEntries.add(new PGalleryEntry(entry, image));
	}

	public static ArrayList<GalleryEntry<PImage>> GetGalleyImages() {
		return GalleryEntries;
	}

	private void printload(String name) {
		println("--> loaded : " + name);
	}

	public static PShader GetShader(String name) {
		if (Shaders.containsKey(name))
			return Shaders.get(name);

		return null;
	}

	public static FontInfo GetFont(String name) {
		if (Fonts.containsKey(name))
			return Fonts.get(name);

		return null;
	}

	public static PImage GetIcon(String name) {
		if (Icons.containsKey(name))
			return Icons.get(name).get_image();

		return null;
	}

	public static void SetIcon(String name, PImage image) {
		Icons.put(name, new PIcon(new AssetEntry(name, "image", ""), image));
	}

	private void dispatchAssetError(String path) {
		new ErrorEvent(ErrorType.AssetError, "path '" + path
				+ "' could not be found").dispatch();
	}

}
