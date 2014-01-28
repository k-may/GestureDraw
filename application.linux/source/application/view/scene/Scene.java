package application.view.scene;

import framework.scenes.SceneManager;
import framework.scenes.SceneType;
import framework.view.View;

public class Scene extends View {
	public Scene(SceneType type) {
		SceneManager.registerScene(this, type);
		_isTouchEnabled = false;
	}

}
