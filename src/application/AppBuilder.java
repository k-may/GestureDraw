package application;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import oscP5.OscP5;
import processing.core.PApplet;
import processing.core.PImage;
import SimpleOpenNI.SimpleOpenNI;
import application.audio.MinimAudioPlayer;
import application.canvas.PCanvasController;
import application.clients.DataXMLClient;
import application.clients.XMLClient;
import application.content.ContentManager;
import application.interaction.RegionType;
import application.interaction.RegionTypeHelper;
import application.interaction.gestTrackOSC.GestTrackOSCRegion;
import application.interaction.processing.PRegion;
import application.interaction.soni.SONRegion;
import application.view.MainView;
import application.view.canvas.CanvasScene;
import application.view.home.HomeScene;
import application.view.menu.Menu;
import de.looksgood.ani.Ani;
import framework.Controller;
import framework.ErrorType;
import framework.IMainView;
import framework.PathUtil;
import framework.audio.IAudioPlayer;
import framework.data.AssetEntry;
import framework.data.FontEntry;
import framework.data.GalleryEntry;
import framework.data.MusicEntry;
import framework.events.ErrorEvent;
import framework.events.LogEvent;
import framework.interaction.IInteractionRegion;
import framework.scenes.SceneManager;
import gesturedraw.GestureDraw;

public class AppBuilder {

	public static String logPath;
	private IInteractionRegion _region;
	GestureDraw _parent;
	IMainView _root;
	IAudioPlayer _player;
	Controller _controller;
	CanvasScene _canvasScene;
	HomeScene _homeScene;
	PCanvasController _canvasController;

	public AppBuilder(PApplet parent) {
		_parent = (GestureDraw) parent;

		init();
		load();
		initScenes();
		start();
	}

	private void load() {
		DataXMLClient dataClient;
		dataClient = DataXMLClient.getInstance();
		_controller.registerDataClient(dataClient);

		XMLClient assetClient = new XMLClient();

		try {
			ContentManager contentManager = ContentManager.getInstance();

			// load assets
			ArrayList<AssetEntry> readAssetEntries = assetClient.readAssetEntries();
			ArrayList<FontEntry> fontEntries = assetClient.readFontEntries();

			// load gallery images
			contentManager.loadAssets(_parent, readAssetEntries, fontEntries);
			contentManager.loadGalleryEntries(_parent, dataClient.readImageEntries());

			ArrayList<MusicEntry> trackEntries = dataClient.readMusicEntries();
			_player.setEntries(trackEntries);

			if (trackEntries.size() == 0)
				Menu.TRACKS = false;

		} catch (NullPointerException e) {
			new ErrorEvent(ErrorType.AssetError, e.getMessage()).dispatch();
			// _parent.exit();
		}

	}

	private void start() {
		_controller.start();
	}

	private void init() {

		initControllers();
		initMainView();
		initAnimationEngine();
		initInteraction();
		initPlayer();
	}

	private void initControllers() {
		_controller = Controller.getInstance();

		String path = getClassPath();

		PathUtil.SetDataPath(path);
		_canvasController = new PCanvasController();
		_controller.registerController(_canvasController);

	}

	private String getClassPath() {
		String path = AppBuilder.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			path = URLDecoder.decode(path, "UTF-8");
			return path;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			new ErrorEvent(ErrorType.Decode, "Couldn't decode path, '" + path
					+ "' : " + e1.getMessage()).dispatch();

			e1.printStackTrace();
			return null;
		}
	}

	private void initMainView() {
		_root = new MainView(_parent);
		_controller.registerParent(_root);
		_parent.setRoot(_root);
	}

	private void initPlayer() {
		if (Menu.TRACKS) {
			_player = new MinimAudioPlayer();
			_controller.registerTrackPlayer(_player);
		}
	}

	private void initAnimationEngine() {
		Ani.init(_parent);
		Ani.overwrite();
	}

	private void initScenes() {

		Menu.CLEARABLE = DataXMLClient.getInstance().getClearable();

		_canvasScene = new CanvasScene();
		_homeScene = new HomeScene();

		_canvasController.registerCanvasScene(_canvasScene);
		_canvasController.registerHomeScene(_homeScene);

		_controller.registerHomeScene(_homeScene);

		SceneManager.getInstance().addObserver((MainView) _root);

		ArrayList<GalleryEntry<PImage>> images = ContentManager.GetGalleyImages();
		_homeScene.setImages(images);

		if (Menu.TRACKS)
			_player.set_view(_canvasScene.get_audioView());

		ArrayList<Observer> audioObservers = _canvasScene.get_audioObservers();
		for (Observer ob : audioObservers) {
			((Observable) _player).addObserver(ob);
		}
	}

	private void initInteraction() {

		DataXMLClient dataClient = DataXMLClient.getInstance();
		_controller.registerDataClient(dataClient);

		MainView.REGION_TYPE = RegionTypeHelper.GetTypeForString(dataClient.getInputType());

		switch (MainView.REGION_TYPE) {
			case GestTrackOSC:
				initGestTrackOSCRegion();
				break;
			case SimpleOpenNI:
				initSimpleOpenNIRegion();
				break;
			default:
				_region = new PRegion(_parent);
		}

		new LogEvent("Region created : " + _region.getType().toString()).dispatch();

		_region.get_adapter().set_canvas(_root);
		_root.set_region(_region);

	}

	private void initSimpleOpenNIRegion() {
		DataXMLClient dataClient = DataXMLClient.getInstance();
		int xRange = dataClient.getXInputRange();
		int yRange = dataClient.getYInputRange();
		int zRange = dataClient.getZInputRange();
		String gestureType = dataClient.getStartGestureType();
		int maxNumHands = dataClient.getMaxNumHands();
		try {
			SimpleOpenNI context = new SimpleOpenNI(GestureDraw.instance);
			if (context.init()) {
				_region = new SONRegion(context, maxNumHands, xRange, yRange, zRange, gestureType);
			} else {
				new ErrorEvent(ErrorType.KinectError, "Unable to initate SimpleOpenNI, make sure all KinectAPI drivers are properly installed").dispatch();
				_region = new PRegion(_parent);
			}
		} catch (UnsatisfiedLinkError e) {
			new ErrorEvent(ErrorType.SimpleOpenNI, "Unable to load framework : "
					+ e.getMessage()).dispatch();
			_region = new PRegion(_parent);

		}
	}

	private void initGestTrackOSCRegion() {
		DataXMLClient dataClient = DataXMLClient.getInstance();
		int xRange = dataClient.getXInputRange();
		int yRange = dataClient.getYInputRange();
		int zRange = dataClient.getZInputRange();
		float firstRegion = dataClient.getHorUserRegion1();
		float secondRegion = dataClient.getHorUserRegion2();

		OscP5 osc = new OscP5(GestureDraw.instance, 12345);
		_region = new GestTrackOSCRegion(osc, xRange, yRange, zRange);
		_region.setDomains(firstRegion, secondRegion);
	}

}
