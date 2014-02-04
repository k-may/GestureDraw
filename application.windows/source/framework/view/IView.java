package framework.view;

import framework.Rectangle;
import framework.events.TouchEvent;
import processing.core.PApplet;
import processing.core.PVector;

public interface IView {
	void draw();
	void addChild(IView child);
	void removeChild(IView child);
	void set_parent(IView view);
	IView get_parent();
	Boolean isTouchEnabled();
	Boolean isPressTarget();
	Boolean isHoverTarget();
	Boolean isDrawTarget();
	void setVisible(Boolean isVisible);
	Rectangle get_rect();
	int get_numChildren();
	IView get_childAt(int index);
	float get_x();
	float get_y();
	float get_width();
	float get_height();
	Object get_absPos();
	void handleInteraction(TouchEvent event);
	int get_id();
}
