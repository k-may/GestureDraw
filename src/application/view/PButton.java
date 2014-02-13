package application.view;

import framework.BaseMainView;
import framework.interaction.IHoverTarget;


public class PButton extends PView implements IHoverTarget {

	protected int _hoverInterval = BaseMainView.HOVER_ELAPSE;
	
	public PButton(){
		
	}
	
	public PButton(int hoverInterval){
		_hoverInterval = hoverInterval;
	}
	
	@Override
	public int get_hoverInterval() {
		// TODO Auto-generated method stub
		return _hoverInterval;
	}

	@Override
	public Boolean isHoverTarget() {
		return true;
	}
}
