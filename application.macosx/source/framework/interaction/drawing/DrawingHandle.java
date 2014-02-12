package framework.interaction.drawing;

import java.util.ArrayList;

import framework.interaction.data.InteractionData;
import framework.view.IView;

public class DrawingHandle {

	private IView _target;
	private int _id;
	private ArrayList<InteractionData> _data;
	private Boolean _isPressing = false;
	private Boolean _isPreHovering = false;
	private Boolean _isHovering = false;
	private Boolean _updated = false;
	private int _startMillis;
}
