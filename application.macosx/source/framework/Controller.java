package framework;

import java.util.ArrayList;

import processing.core.PApplet;
import framework.audio.IAudioPlayer;
import framework.clients.ErrorLogClient;
import framework.clients.IDataClient;
import framework.clients.LogClient;
import framework.data.ImageEntry;
import framework.data.MusicEntry;
import framework.data.UserData;
import framework.events.BackEvent;
import framework.events.ErrorEvent;
import framework.events.Event;
import framework.events.EventType;
import framework.events.GallerySelectedEvent;
import framework.events.HandDetectedEvent;
import framework.events.LabelButtonPressed;
import framework.events.LogEvent;
import framework.events.PauseTrackEvent;
import framework.events.PlayTrackEvent;
import framework.events.SaveCanvasEvent;
import framework.events.TouchEvent;
import framework.events.UpdateColorEvent;
import framework.events.UserAddedEvent;
import framework.events.UserRemovedEvent;
import framework.interaction.IInteractionRegion;
import framework.scenes.IHomeScene;
import framework.scenes.SceneManager;
import framework.scenes.SceneType;
import framework.view.IView;

public class Controller implements IController {

	private ArrayList<TouchEvent> _touchEventQueue;
	private ArrayList<Event> _eventQueue;
	private static Controller _instance;
	private IMainView _mainView;
	private IAudioPlayer _player;
	private IDataClient xmlClient;
	private IHomeScene<?> _homeScene;
	private ErrorLogClient _errorLogClient;
	private LogClient _logClient;
	// private int _actionTime;

	private ArrayList<IController> _controllers;

	private Controller() {
		_touchEventQueue = new ArrayList<TouchEvent>();
		_eventQueue = new ArrayList<Event>();

		registerController(this);

	}

	public void addEvent(Event event) {
		_eventQueue.add(event);
	}

	public void update(int time) {
		processTouches();
		processEvents();
	}

	private void processTouches() {
		ArrayList<TouchEvent> tempList = new ArrayList<TouchEvent>(_touchEventQueue);
		_touchEventQueue.clear();

		for (TouchEvent evt : tempList) {
			if (!handleTouchEvent(evt))
				continue;
		}
	}

	private Boolean handleTouchEvent(TouchEvent event) {
		IView parent = event.get_target();
		parent.handleInteraction(event);
		return event.continuePropogation();
	}

	private void processEvents() {
		ArrayList<Event> tempList = new ArrayList<Event>(_eventQueue);
		_eventQueue.clear();

		for (Event evt : tempList) {
			for (IController controller : _controllers)
				controller.processEvent(evt);
		}
	}

	public void processEvent(Event event) {
		EventType type = event.get_type();
		// println("process event : " + event.toString());
		switch (type) {
			case SaveCanvas:
				handleSaveCanvas((SaveCanvasEvent) event);
				break;
			case PlayTrack:
				handlePlayTrack((PlayTrackEvent) event);
				break;
			case PauseTrack:
				handlePauseTrack((PauseTrackEvent) event);
				break;
			case Exit:
				handleExit();
				break;
			case LabelButtonPressed:
				handleLableButton((LabelButtonPressed) event);
				break;
			case HandDetected:
				handleHandDetected((HandDetectedEvent) event);
				break;
			case Error:
				handleError((ErrorEvent) event);
				break;
			case Log:
				handleLog((LogEvent) event);
				break;
			case UserRemoved:
				handleUserRemoved((UserRemovedEvent) event);
				break;
			case UserAdded:
				handleUserAdded((UserAddedEvent) event);
				break;
			case UpdateColor:
				handleUpdateColor((UpdateColorEvent) event);
				break;
			case StreamEnd:
				handleStreamEnd();
				break;
			case InAction:
				handleInAction();
				break;
		}
	}

	private void handleUpdateColor(UpdateColorEvent event) {
		int id = event.get_user().get_id();
		float position = event.get_user().get_localX();
		int color = event.get_color();
		_mainView.get_userMenuView().updateDomain(id, position, color);//updateRegion(userRegion, color);
	}

	private void handleInAction() {
		navigateToHome();
	}

	private void handleStreamEnd() {
		new LogEvent("no users").dispatch();
	}

	private void handleUserAdded(UserAddedEvent event) {
		int id = event.get_user().get_id();
		float position = event.get_user().get_localX();
		_mainView.get_userMenuView().updateDomain(id, position, UserData.START_COLOR);//updateRegion(userRegion, UserData.START_COLOR);
	}

	private void handleUserRemoved(UserRemovedEvent event) {
		IInteractionRegion region = _mainView.get_region();
		int id = event.get_user().get_id();
		region.removeDomain(id);
		_mainView.get_userMenuView().removeDomain(id);

	}

	private void handleLog(LogEvent event) {
		_logClient.addLog(event.get_time(), event.get_msg());
	}

	private void handleError(ErrorEvent event) {
		_errorLogClient.addError(event);
	}


	private void handleHandDetected(HandDetectedEvent event) {
		switch (SceneManager.GetSceneType()) {
			case Home:
				_homeScene.setReady(true);
				break;
		}
	}

	private void handleLableButton(LabelButtonPressed event) {
		String text = event.get_text();

		if (text == "START")
			navigateToCanvas();
		else if (text == "Home")
			navigateToHome();
		else if (text == "Save")
			new SaveCanvasEvent().dispatch();

	}

	private void navigateToHome() {
		SceneManager.setScene(SceneType.Home);
		_homeScene.setReady(false);
	}

	private void navigateToCanvas() {
		SceneManager.setScene(SceneType.Canvas);
	}

	private void handleExit() {
		_player.stop();
	}

	// --------tracks----------

	private void handlePlayTrack(PlayTrackEvent event) {
		MusicEntry entry = event.get_entry();
		_player.play(entry);
	}

	private void handlePauseTrack(PauseTrackEvent event) {
		MusicEntry entry = event.get_entry();
		_player.pause();
	}

	private void handleSaveCanvas(SaveCanvasEvent event) {
		String date = PApplet.month() + "_" + PApplet.day() + "_"
				+ PApplet.minute() + "_" + PApplet.second();
		String title = PApplet.minute() + "_" + PApplet.second();
		String filePath = PApplet.minute() + "_" + PApplet.second() + ".jpg";
		ImageEntry entry = new ImageEntry(filePath, "", new String[] { "me" }, date);

		xmlClient.writeImageEntry(entry);

	}

	public void start() {

		_errorLogClient = new ErrorLogClient(PathUtil.GetLogPath()
				+ "error.txt");
		_logClient = new LogClient(PathUtil.GetLogPath() + "log.txt");

		navigateToHome();
		_mainView.start();
	}

	public static Controller getInstance() {
		if (_instance == null)
			_instance = new Controller();

		return _instance;
	}

	public void addTouchEvent(TouchEvent touchEvent) {
		_touchEventQueue.add(touchEvent);
	}

	public void registerDataClient(IDataClient client) {
		xmlClient = client;
	}

	public void registerParent(IMainView parent) {
		_mainView = parent;
	}

	public void registerTrackPlayer(IAudioPlayer player) {
		_player = player;
	}

	public void registerHomeScene(IHomeScene scene) {
		_homeScene = scene;
	}

	public void registerController(IController controller) {
		if (_controllers == null)
			_controllers = new ArrayList<IController>();

		_controllers.add(controller);
	}

}
