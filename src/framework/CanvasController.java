package framework;

import framework.data.GalleryEntry;
import framework.events.Event;
import framework.events.EventType;
import framework.events.SaveCanvasEvent;
import framework.scenes.IHomeScene;
import framework.view.ICanvasScene;
import framework.view.IGallery;

public abstract class CanvasController<T> implements IController {

	protected ICanvasScene<T> _canvasScene;
	protected IHomeScene<T> _homeScene;

	public abstract void registerHomeScene(IHomeScene<T> homeScene);

	public abstract void registerCanvasScene(ICanvasScene<T> scene);

	public void processEvent(Event event) {
		EventType type = event.get_type();
		//println("process event : " + event.toString());
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
		}
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
		
		_canvasScene.clearCanvas();
	}

}
