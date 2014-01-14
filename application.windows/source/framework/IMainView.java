package framework;

import java.util.ArrayList;

import framework.audio.IAudioView;
import framework.interaction.IInteractionRegion;
import framework.interaction.IInteractionView;
import framework.scenes.SceneType;
import framework.view.CanvasState;
import framework.view.IView;
import framework.view.View;



public interface IMainView extends IView {
	ArrayList<IView> getTargetsAtLocation(float x, float y);

	void start();

	IInteractionRegion get_region();

	void addInteractionView(IInteractionView view);

	void set_region(IInteractionRegion region);

	void showMenu();

	void hideMenu();
	
	void addPressDownEvent(IView target, float x, float y, float pressure,
			int id);

	void addPressReleaseEvent(IView target, float x, float y, float pressure,
			int id);

	void addRollOverEvent(IView target, float x, float y, float pressure, int id);

	void addCancelEvent(IView target, float x, float y, float pressure, int id);

	void addMoveEvent(IView target, float x, float y, float pressure, int id);

	void startHover(int userID, int interval, IView target);
	
	void endHover(int userID);

	void addHoverStartEvent(IView target, float x, float y, float pressure,
			int id);

	void addHoverEndEvent(IView target, float x, float y, float pressure, int id);
	
	CanvasState get_currentState();
	
	void set_currentState(CanvasState state);
}
