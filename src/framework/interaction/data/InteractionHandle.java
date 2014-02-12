package framework.interaction.data;

import java.util.ArrayList;

import framework.view.IView;

public class InteractionHandle {
	private IView _target;
	private int _userId;
	private ArrayList<InteractionData> _data;
	private Boolean _isPressing = false;
	private Boolean _isPreHovering = false;
	private Boolean _isHovering = false;
	private Boolean _updated = false;
	private int _startMillis;

	public InteractionHandle(int id, IView target) {
		_target = target;
		_userId = id;
	}

	public void add(InteractionData data) {
		if (_data == null)
			_data = new ArrayList<InteractionData>();

		if(_data.size() > 2)
			_data.remove(2);
		
		_data.add(data);

		_updated = true;
	}

	public IView get_target() {
		return _target;
	}

	public int get_userId() {
		return _userId;
	}

	public void cancel() {
	}

	public Boolean isUpdated() {
		return _updated;
	}

	public float get_currentX() {
		return _data.get(_data.size() - 1).getX();
	}

	public float get_currentY() {
		return _data.get(_data.size() - 1).getY();
	}

	public InteractionData get_lastInteraction() {
		if (_data.size() >= 2)
			return _data.get(_data.size() - 2);

		else
			return null;
	}

	public InteractionData get_currentInteraction() {
		return _data.get(_data.size() - 1);
	}

	public Boolean isPressing() {
		return _isPressing;
	}

	public void reset() {
		// only update press state after initial processing
		_isPressing = get_currentInteraction().isPressing;
		_updated = false;
	}

	public float getCurrentPressure() {
		return get_currentInteraction().getZ();
	}

	public boolean isHovering() {
		// TODO Auto-generated method stub
		return _isHovering;
	}

	public int get_startMillis() {
		return _startMillis;
	}

	public void set_startMillis(int millis) {
		_startMillis = millis;
	}

	public float get_dX() {
		return get_currentX() - get_lastInteraction().getX();
	}

	public float get_dY() {
		return get_currentY() - get_lastInteraction().getY();
	}

	public void endPreHovering() {
		_isPreHovering = false;
	}

	public void startHover() {
		_isHovering = true;
		_isPreHovering = true;
	}

	public boolean isPreHovering() {
		return _isPreHovering;
	}
	
	public Boolean isDrawing(){
		return _target.isDrawTarget();
	}
}
