package framework.interaction;

import java.util.ArrayList;

import framework.BaseMainView;
import framework.IMainView;
import framework.interaction.Types.InteractionEventType;
import framework.interaction.data.InteractionData;
import framework.interaction.data.InteractionHandle;
import framework.interaction.data.InteractionStreamData;
import framework.view.IView;

public class InteractionDispatcher {

	public IMainView _mainView;
	public ArrayList<InteractionHandle> _handles;
	private ArrayList<InteractionHandle> _completeHandles;


	public InteractionDispatcher(IMainView mainView) {
		_mainView = mainView;
		_handles = new ArrayList<InteractionHandle>();
	}

	public void setStream(ArrayList<InteractionStreamData> data) {
		if (data == null)
			return;

		for (InteractionStreamData d : data)
			seeData(d);
	}

	private void seeData(InteractionStreamData data) {
		float x = data.get_x();
		float y = data.get_y();
		ArrayList<IView> targets = data.getTargets(); // _mainView.getTargetsAtLocation(x,
														// y);

		// if press button is found, no other targets will be added to stack
		// need to check if handle exists which overrides press button (ie.
		// canvas)
		
		//TODO create drawing dispatcher
		
		if (data.isDrawing()) {
			for (InteractionHandle handle : _handles) {
				if (handle.get_userId() == data.get_userId() && handle.isDrawing()) {
					// println("pressing : " + data.isPressing());
					handle.add(data.get_data());
					break;
				}
			}
		} else {
			for (InteractionHandle handle : _handles) {

				if (handle.get_userId() == data.get_userId()) {
					for (int i = 0; i < targets.size(); i++) {
						if (handle.get_target() == targets.get(i)) {
							handle.add(data.get_data());
							targets.remove(i);
							break;
						}
					}
				}
			}
			// }
			if (!data.isPressing()){// || !data.isDrawing()) {
				for (IView target : targets) {
					//System.out.println("add handle " + target.isPressTarget());
					InteractionHandle handle = new InteractionHandle(data.get_userId(), target);
					handle.add(data.get_data());
					_handles.add(handle);
				}
			}
		}
	}

	public void process(int millis) {

		_completeHandles = new ArrayList<InteractionHandle>();

		for (InteractionHandle handle : _handles) {
			if (!handle.isUpdated()) {
				disposeHandle(handle);
			} else {
				if (handle.get_lastInteraction() != null) {
					processHandle(handle, millis);
				} else {
					initHandle(handle, millis);
				}
			}
		}

		disposeHandles();
		resetHandles();
	}
	
	public void removeUser(int id){
		for(InteractionHandle handle : _handles){
			if(handle.get_userId() == id)
				disposeHandle(handle);
		}
	}

	private void processHandle(InteractionHandle handle, int millis) {
		InteractionData currentInteraction = handle.get_currentInteraction();
		IView target = handle.get_target();
		float x = handle.get_currentX();
		float y = handle.get_currentY();
		int id = handle.get_userId();

		if (handle.get_dX() != 0.0f || handle.get_dY() != 0.0f)
			dispatchEvent(target, InteractionEventType.Move, x, y, id);

		// if (handle.get_target().isDrawTarget())
		// System.out.println(target + ", pT: " + target.isPressTarget() + " /  cR press: " + currentInteraction.isPressing + " / " + handle.isPressing());

		if (currentInteraction.isPressing && !handle.isPressing()){
		//System.out.println("-->press\n\n");
			dispatchEvent(target, InteractionEventType.PressDown, x, y, id);
		}else if (currentInteraction.isDrawing && !handle.isDrawing())
			dispatchEvent(target, InteractionEventType.PressDown, x, y, id);

		if (!currentInteraction.isPressing && handle.isPressing())
			dispatchEvent(target, InteractionEventType.PressUp, x, y, id);

		if (handle.isHovering()) {
			if (handle.isPreHovering()) {
				int elapsed = millis - handle.get_startMillis();
				if (elapsed > handle.hoverInterval){
					dispatchEvent(target, InteractionEventType.HoverEnd, x, y, id);
					handle.endPreHovering();
				}
			}
		}

	}

	private void initHandle(InteractionHandle handle, int millis) {
		handle.set_startMillis(millis);
		int id = handle.get_userId();
		IView target = handle.get_target();
		float x = handle.get_currentX();
		float y = handle.get_currentY();

		if (target.isHoverTarget()) {
			dispatchEvent(handle.get_target(), InteractionEventType.HoverStart, x, y, id);
			handle.startHover();
		}

		dispatchEvent(target, InteractionEventType.RollOver, x, y, id);
	}

	private void resetHandles() {
		// update handles post dispatch (update press state, etc)
		for (InteractionHandle handle : _handles)
			handle.reset();
	}

	private void disposeHandle(InteractionHandle handle) {
		_completeHandles.add(handle);
		dispatchEvent(handle.get_target(), InteractionEventType.Cancel, -1f, -1f, handle.get_userId());
	}

	private void disposeHandles() {
		// clear completed
		for (InteractionHandle handle : _completeHandles) {
			_handles.remove(handle);
		}
	}

	private void dispatchEvent(IView target, InteractionEventType type,
			float x, float y, int id) {
		// println("dispatch : " + type);
		switch (type) {
			case None:
				break;
			case PressDown:
				_mainView.addPressDownEvent(target, x, y, id);
				break;
			case PressUp:
				_mainView.addPressReleaseEvent(target, x, y, id);
				break;
			case Cancel:
				_mainView.addCancelEvent(target, x, y, id);
				break;
			case RollOver:
				_mainView.addRollOverEvent(target, x, y, id);
				break;
			case Move:
				_mainView.addMoveEvent(target, x, y, id);
				break;
			case HoverStart:
				_mainView.addHoverStartEvent(target, x, y, id);
				break;
			case HoverEnd:
				_mainView.addHoverEndEvent(target, x, y, id);
				break;
		}
	}
}
