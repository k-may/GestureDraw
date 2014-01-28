package framework.scenes;

import java.util.ArrayList;

import framework.data.GalleryEntry;
import framework.view.IGallery;

public interface IHomeScene<T> {
	void setReady(Boolean value);
	IGallery<T> getGallery();
	void setImages(ArrayList<GalleryEntry<T>> images);

}
