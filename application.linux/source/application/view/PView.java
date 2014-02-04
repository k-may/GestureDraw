package application.view;

import processing.core.PApplet;
import processing.core.PVector;
import framework.Rectangle;
import framework.view.IView;
import framework.view.View;

public class PView extends View {

	public PView(){
		super();
	}
	
	public PView(String name){
		super(name);
	}

	public PVector get_absPos() {
		// TODO Auto-generated method stub

		Rectangle rect = get_rect();
		IView tempParent = _parent;

		while (tempParent != null) {
			rect = tempParent.get_rect().offset(new PVector(rect.get_x(), rect.get_y()));
			tempParent = tempParent.get_parent();
		}

		return new PVector(rect.get_x(), rect.get_y());
	}

	public void draw(PApplet p) {
		if(!_isVisible)
			return;
		// TODO Auto-generated method stub
		for (IView view : _childs)
			((PView)view).draw(p);
	}

}
