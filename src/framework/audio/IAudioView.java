package framework.audio;

import java.util.Observer;

public interface IAudioView extends Observer{
	void show();

	void hide();

	void collapse();

	void expand();
}
