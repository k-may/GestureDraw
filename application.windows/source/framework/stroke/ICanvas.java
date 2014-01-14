package framework.stroke;

public interface ICanvas<T> {
	void save(String filePath);

	void clear();

	T getImage();
}
