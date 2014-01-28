package framework.view;

import java.util.ArrayList;
import java.util.Observer;

import framework.audio.IAudioView;
import framework.data.GalleryEntry;

public interface ICanvasScene<T> {
	//ICanvas<T> get_canvas();
	IAudioView get_audioView();
	ArrayList<Observer> get_audioObservers();
	void showTracks();
	void hideTracks();
	GalleryEntry<T> save(String filePath, String date);
	void clearCanvas();
	void set_isSaving(Boolean value);
	Boolean get_isSaving();
}
