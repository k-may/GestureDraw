package framework.scenes;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import framework.view.IView;

import application.view.scene.Scene;



public class SceneManager extends Observable {

	private static SceneManager instance;
	
	private static SceneType _type;
	
	private static Map<SceneType, IView> _sceneMap;

	public static void registerScene(IView scene, SceneType type) {
		if (_sceneMap == null)
			_sceneMap = new HashMap<SceneType, IView>();

		_sceneMap.put(type, scene);
	}

	public static IView getScene() {
		return _sceneMap.get(_type);
	}
	
	public static SceneType  GetSceneType(){
		return _type;
	}
	
	public static void setScene(SceneType type){
		if(_type != type){
			_type = type;
			getInstance().invalidate();
		}
	}
	
	public static SceneManager getInstance(){
		if(instance == null)
			instance = new SceneManager();
		
		return instance;
	}
	
	private void invalidate(){
		setChanged();
		notifyObservers(_type);
		
	}
}
