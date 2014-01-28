package application.canvas;

import processing.core.PImage;
import framework.CanvasController;
import framework.IController;
import framework.scenes.IHomeScene;
import framework.view.ICanvasScene;

public class PCanvasController extends CanvasController<PImage> implements IController {

	@Override
	public void registerHomeScene(IHomeScene<PImage> homeScene) {
		_homeScene = homeScene;
	}

	@Override
	public void registerCanvasScene(ICanvasScene<PImage> scene) {
		_canvasScene = scene;
	}


}
