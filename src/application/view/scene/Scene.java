package application.view.scene;

import application.view.PView;
import framework.scenes.SceneManager;
import framework.scenes.SceneType;

public class Scene extends PView {
	public Scene(SceneType type) {
		SceneManager.registerScene(this, type);
		_isTouchEnabled = false;
	}

}
