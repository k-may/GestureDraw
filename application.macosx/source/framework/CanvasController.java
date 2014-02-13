package framework;

import framework.data.GalleryEntry;
import framework.events.Event;
import framework.events.EventType;
import framework.events.RefreshEvent;
import framework.events.SaveCanvasEvent;
import framework.events.UserRemovedEvent;
import framework.scenes.IHomeScene;
import framework.scenes.SceneManager;
import framework.stroke.ICanvas;
import framework.view.ICanvasScene;
import framework.view.IGallery;

public abstract class CanvasController<T> implements IController {

	protected ICanvasScene<T> _canvasScene;
	protected ICanvas _canvas;
	protected IHomeScene<T> _homeScene;

	public abstract void registerHomeScene(IHomeScene<T> homeScene);

	public abstract void registerCanvasScene(ICanvasScene<T> scene);

	public void processEvent(Event event) {
		EventType type = event.get_type();
		switch (type) {
			case SaveCanvas:
				handleSaveCanvas((SaveCanvasEvent) event);
				break;
			case OpenTracks:
				handleOpenTracks();
				break;
			case CloseTracks:
				handleCloseTracks();
				break;
			case NoPressed:
				handleNoPressed();
			case YesPressed:
				handleContinue();
				break;
			case ClearCanvas:
				handleClear();
				break;
			case UserRemoved:
				removeStroke((UserRemovedEvent) event);
		}
	}


	private void removeStroke(UserRemovedEvent event) {
		_canvas.onStrokeEnd(event.get_user().get_id());
	}

	private void handleClear() {
		_canvasScene.clearCanvas();
	}

	private void handleCloseTracks() {
		_canvasScene.hideTracks();
	}

	private void handleOpenTracks() {
		_canvasScene.showTracks();
	}

	private void handleSaveCanvas(SaveCanvasEvent event) {

		String filePath = PathUtil.GetImagePath() + event.getFilePath();
		String date = event.getDate();

		GalleryEntry<T> galleryEntry = _canvasScene.save(filePath, date);

		IGallery<T> homeGallery = (IGallery<T>) _homeScene.getGallery();
		homeGallery.addImage(galleryEntry);
		
		_canvasScene.set_isSaving(true);
	}
	
	private void handleNoPressed(){
		_canvasScene.clearCanvas();
		
		new RefreshEvent().dispatch();
	}
	
	private void handleContinue(){
		_canvasScene.set_isSaving(false);
	}

}
