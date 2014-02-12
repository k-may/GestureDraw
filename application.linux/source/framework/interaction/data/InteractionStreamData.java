package framework.interaction.data;

import java.util.ArrayList;

import framework.depth.DepthState;
import framework.depth.DepthStateData;
import framework.interaction.Types.HandType;
import framework.interaction.Types.InteractionType;
import framework.view.IView;

public class InteractionStreamData {

	private int _userId;
	private HandType _handType;
	private InteractionType _type;
	private Boolean _isOverPressTarget = false;
	private Boolean _isOverHoverTarget = false;
	private ArrayList<IView> _targets;
	private InteractionData _data;
	private DepthStateData _depthState;


	public InteractionStreamData(InteractionData data, int userId) {
		_userId = userId;
		_data = data;
	}

	public InteractionStreamData(InteractionData data, int userId, Boolean isOverHoverTarget,
			Boolean isOverPressTarget,HandType handType, ArrayList<IView> targets, DepthStateData depthState) {
		this(data, userId);
		_isOverHoverTarget = isOverHoverTarget;
		_isOverPressTarget = isOverPressTarget;
		_handType = handType;
		_targets = targets;
		_depthState = depthState;
	}

	
	public InteractionData get_data() {
		return _data;
	}

	
	public DepthStateData get_depthState() {
		return _depthState;
	}

	public Boolean isPressing() {
		return _data.isPressing;
	}

	public float get_z() {
		return _data.getZ();
	}

	public float get_x() {
		return _data.getX();
	}

	public float get_y() {
		return _data.getY();
	}

	public int get_userId() {
		return _userId;
	}

	public Boolean isOverPressTarget() {
		return _isOverPressTarget;
	}

	public Boolean isOverHoverTarget() {
		return _isOverHoverTarget ;
	}

	public Boolean isOverTarget() {
		return _isOverHoverTarget || _isOverPressTarget;
	}

	public HandType getHandType() {
		return _handType;
	}

	public boolean isDrawing() {
		// TODO Auto-generated method stub
		return _data.isDrawing;//_pressState == DepthState.Drawing;// || _pressState == PressState.PreDrawing;
	}

	public ArrayList<IView> getTargets() {
		return _targets;
	}

}
