package application;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import oscP5.OscP5;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;
import application.audio.MinimAudioPlayer;
import application.canvas.PCanvasController;
import application.clients.AssetsXMLClient;
import application.clients.DataXMLClient;
import application.content.ContentManager;
import application.interaction.RegionType;
import application.interaction.RegionTypeHelper;
import application.interaction.gestTrackOSC.domain.GestDomainsRegion;
import application.interaction.gestTrackOSC.hand.GestHandTrackRegion;
import application.interaction.processing.PRegion;
import application.interaction.soni.SONRegion;
import application.view.MainView;
import application.view.canvas.CanvasScene;
import application.view.home.HomeScene;
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

	DataXMLClient _dataClient;
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
		initConfigurationProperties();
		initScenes();
		start();
	}

	private void load() {

		_controller.registerDataClient(_dataClient);

		AssetsXMLClient assetClient = new AssetsXMLClient();

		try {
			ContentManager contentManager = ContentManager.getInstance();

			// load assets
			ArrayList<AssetEntry> readAssetEntries = assetClient.readAssetEntries();
			ArrayList<FontEntry> fontEntries = assetClient.readFontEntries();

			// load gallery images
			contentManager.loadAssets(_parent, readAssetEntries, fontEntries);
			contentManager.loadGalleryEntries(_parent, _dataClient.readImageEntries());

			ArrayList<MusicEntry> trackEntries = _dataClient.readMusicEntries();
			_player.setEntries(trackEntries);

			if (trackEntries.size() == 0)
				MainView.TRACKS = false;

		} catch (NullPointerException e) {
			new ErrorEvent(ErrorType.AssetError, e.getMessage()).dispatch();
			// _parent.exit();
		}

	}

	private void start() {
		_controller.start();
	}

	private void init() {
		String path = getClassPath();
		if(path == null){
			System.out.println("\n!!! PATH ERROR !!!");
			System.out.println("!!! PATH : " + path + " !!!");
			return;
		}
			
		PathUtil.SetDataPath(path);

		try {
			_dataClient = DataXMLClient.getInstance();
		} catch (Exception e) {
			new ErrorEvent(ErrorType.XMLPath, "Can't load config.xml : "
					+ e.getLocalizedMessage()).dispatch();
			System.out.println("!!! LOAD ERROR :" + e.getLocalizedMessage() + " !!!");
			return;
		}
		
		initControllers();
		initMainView();
		initAnimationEngine();
		initRanges();
		initInteraction();
		initPlayer();
	}

	private void initControllers() {
		_controller = Controller.getInstance();
		_canvasController = new PCanvasController();
		_controller.registerController(_canvasController);

	}

	private String getClassPath() {
		String path = AppBuilder.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println("::: BASE PATH :::");
		System.out.println("::: PATH : " + path + " :::");
		
		try {
			path = URLDecoder.decode(path, "UTF-8");
			return path;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			new ErrorEvent(ErrorType.Decode, "Couldn't decode path, '" + path
					+ "' : " + e1.getMessage()).dispatch();

			System.out.println("!!! ERROR : " +e1.getMessage() + " !!!");
			e1.printStackTrace();
			return null;
		}
	}

	private void initMainView() {
		_root = new MainView(_parent);
		_controller.registerParent(_root);
		_parent.setRoot((MainView) _root);
	}

	private void initPlayer() {
		if (MainView.TRACKS) {
			_player = new MinimAudioPlayer();
			_controller.registerTrackPlayer(_player);
		}
	}

	private void initAnimationEngine() {
		Ani.init(_parent);
		Ani.overwrite();
	}

	private void initConfigurationProperties() {
		MainView.BUTTON_HEIGHT = MainView.BUTTON_WIDTH = _dataClient.getButtonSize();
		MainView.COLORWHEEL_RADIUS = _dataClient.getColorWheelRadius();
		MainView.ICON_MAX_RADIUS = _dataClient.getMaxStroke();
		MainView.ICON_MIN_RADIUS = _dataClient.getMinStroke();
		MainView.CLEARABLE = _dataClient.getClearable();
		MainView.CENTER_SCREEN_MASS = _dataClient.getCenterMass();
		MainView.TARGET_MASS = _dataClient.getTargetMass();
	}

	private void initScenes() {

		_canvasScene = new CanvasScene();
		_homeScene = new HomeScene();

		_canvasController.registerCanvasScene(_canvasScene);
		_canvasController.registerHomeScene(_homeScene);

		_controller.registerHomeScene(_homeScene);

		SceneManager.getInstance().addObserver((MainView) _root);

		ArrayList<GalleryEntry<PImage>> images = ContentManager.GetGalleyImages();
		_homeScene.setImages(images);

		if (MainView.TRACKS)
			_player.set_view(_canvasScene.get_audioView());

		ArrayList<Observer> audioObservers = _canvasScene.get_audioObservers();
		for (Observer ob : audioObservers) {
			((Observable) _player).addObserver(ob);
		}
	}

	private void initInteraction() {

		// DataXMLClient dataClient = DataXMLClient.getInstance();
		_controller.registerDataClient(_dataClient);

		MainView.REGION_TYPE = RegionTypeHelper.GetTypeForString(_dataClient.getInputType());

		switch (MainView.REGION_TYPE) {
			case GestTrack:
			case GestDomain:
				initGestTrackOSCRegion();
				break;
			case SimpleOpenNI:
				initSimpleOpenNIRegion();
				break;
			default:
				_region = new PRegion(_parent);
		}

		new LogEvent("Region created : " + MainView.REGION_TYPE.toString()).dispatch();

		_region.get_adapter().set_canvas(_root);
		_root.set_region(_region);

	}

	private void initSimpleOpenNIRegion() {
		// DataXMLClient dataClient = DataXMLClient.getInstance();
		String gestureType = _dataClient.getStartGestureType();

		try {
			SimpleOpenNI context = new SimpleOpenNI(GestureDraw.instance);
			if (context.init()) {
				_region = new SONRegion(context, gestureType);
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
		// DataXMLClient dataClient = DataXMLClient.getInstance();
		float firstRegion = _dataClient.getHorUserRegion1();
		float secondRegion = _dataClient.getHorUserRegion2();

		OscP5 osc = new OscP5(GestureDraw.instance, 12345);

		if (MainView.REGION_TYPE == RegionType.GestDomain) {
			_region = new GestDomainsRegion(osc);
			((GestDomainsRegion) _region).setDomains(firstRegion, secondRegion);
		} else if (MainView.REGION_TYPE == RegionType.GestTrack) {
			_region = new GestHandTrackRegion(osc);
		}
	}

	private void initRanges() {
		// DataXMLClient dataClient = DataXMLClient.getInstance();

		int xRange = _dataClient.getXInputRange();
		int yRange = _dataClient.getYInputRange();
		int zRange = _dataClient.getZInputRange();

		if (xRange != -1)
			MainView.XRANGE = (int) xRange;

		if (yRange != -1)
			MainView.YRANGE = (int) yRange;

		if (zRange != -1)
			MainView.ZRANGE = (int) zRange;
	}

}
