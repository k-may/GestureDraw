package framework.view;

import java.util.ArrayList;

import framework.data.GalleryEntry;



public interface IGallery<T> {
	void setImages(ArrayList<GalleryEntry<T>> entries);
	void addImage(GalleryEntry<T> entry);
	void navigate(String direction);
}
