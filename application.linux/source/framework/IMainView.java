package framework;

import java.util.ArrayList;

import framework.interaction.IInteractionRegion;
import framework.interaction.IInteractionView;
import framework.view.IUserMenuView;
import framework.view.IView;

public interface IMainView extends IView {
	ArrayList<IView> getTargetsAtLocation(float x, float y);

	void start();

	IInteractionRegion get_region();
	IUserMenuView get_userMenuView();

	void addInteractionView(IInteractionView view);

	void set_region(IInteractionRegion region);

	void showMenu();

	void hideMenu();
	
	void addPressDownEvent(IView target, float x, float y,
			int id);

	void addPressReleaseEvent(IView target, float x, float y,
			int id);

	void addRollOverEvent(IView target, float x, float y,int id);

	void addCancelEvent(IView target, float x, float y, int id);

	void addMoveEvent(IView target, float x, float y, int id);

	void startHover(int userID, int interval, IView target);
	
	void endHover(int userID);

	void addHoverStartEvent(IView target, float x, float y,
			int id);

	void addHoverEndEvent(IView target, float x, float y, int id);

}
