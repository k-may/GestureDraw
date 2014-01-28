package framework.interaction;

import java.util.ArrayList;

import framework.depth.DepthState;
import framework.depth.DepthStateData;
import framework.interaction.Types.HandType;
import framework.view.IView;

public class InteractionStreamData {

	private float _z;
	private float _x;
	private float _y;
	private int _userId;
	private HandType _handType;
	private InteractionType _type;
	private Boolean _isOverPressTarget = false;
	private Boolean _isPressing = false;
	private Boolean _isOverHoverTarget = false;
	private DepthState _pressState;
	private ArrayList<IView> _targets;
	
	public InteractionStreamData(float x, float y, float z, int userId,
			InteractionType type) {
		_userId = userId;
		_z = (z);
		_x = (x);
		_y = (y);
		//System.out.println("z : " + _z);
		_type = (type);
	}

	public InteractionStreamData(float x, float y, float z, int userId,
			InteractionType type, Boolean isOverHoverTarget,
			Boolean isOverPressTarget,Boolean isPressing,
			HandType handType, DepthState pressState, ArrayList<IView> targets) {
		this(x, y, z, userId, type);
		_isOverHoverTarget = isOverHoverTarget;
		_isOverPressTarget = isOverPressTarget;
		_isPressing = isPressing;
		_handType = handType;
		_pressState = pressState;
		_targets = targets;
	}


	public Boolean isPressing() {
		return _isPressing;
	}

	public float get_z() {
		return _z;
	}

	public float get_x() {
		return _x;
	}

	public float get_y() {
		return _y;
	}

	public int get_userId() {
		return _userId;
	}

	public InteractionType get_type() {
		return _type;
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
		return _pressState == DepthState.Drawing;// || _pressState == PressState.PreDrawing;
	}

	public ArrayList<IView> getTargets() {
		return _targets;
	}

}
