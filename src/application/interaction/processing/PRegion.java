package application.interaction.processing;

import java.util.ArrayList;

import framework.depth.DepthState;
import framework.depth.DepthStateData;
import framework.events.HandDetectedEvent;
import framework.interaction.Region;
import framework.interaction.Vector;
import framework.interaction.Types.HandType;
import framework.interaction.Types.InteractionType;
import framework.interaction.data.InteractionData;
import framework.interaction.data.InteractionStreamData;
import framework.interaction.data.InteractionTargetInfo;
import application.interaction.Adapter;
import application.interaction.PRegionInputData;
import application.interaction.RegionType;
import application.view.MainView;

import processing.core.PApplet;
import processing.core.PVector;

public class PRegion extends Region<PApplet> {

	private RegionInputData data;

	public PRegion(PApplet source) {
		super(source);

		MainView.SRC_WIDTH = MainView.SCREEN_WIDTH;
		MainView.SRC_HEIGHT = MainView.SCREEN_HEIGHT;
		MainView.TARGET_MASS = 0.005f;

		MainView.XRANGE = MainView.SCREEN_WIDTH;
		MainView.YRANGE = MainView.SCREEN_HEIGHT;
		MainView.ZRANGE = 1;

		data = new PRegionInputData();

		// source.noCursor();

		_type = InteractionType.Mouse;
		_adapter = new Adapter();

		new HandDetectedEvent().dispatch();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void runInteractions() {
		// TODO Auto-generated method stub
		float mX = (float) _source.mouseX;// / _source.width;
		float mY = (float) _source.mouseY;// / _source.height;
		float mZ = _source.mousePressed ? 1 : 0.25f;

		//System.out.println(mX);
		// if (_source.mousePressed)
		//System.out.println("\n========= : pressed :" + _source.mousePressed);

		PVector position = new PVector(mX, mY, mZ);
		data.addRawPosition(position, 0);
		int id = data.get_id();
		position = data.getPosition();

		// digest stream info
		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(position.x, position.y, _type);
		Boolean isHoverTarget = info.get_isHoverTarget();
		Boolean isPressTarget = info.get_isPressTarget();
		Boolean isDrawTarget = info.get_canvas() != null;

		DepthStateData pressStateData = _adapter.getUserForDomain(0).get_depthStateData();
		DepthState state = pressStateData == null ? DepthState.None
				: pressStateData.get_state();
		position = data.digest(info, state);

		Boolean isPressing = false;

		if (isPressTarget || isDrawTarget)
			isPressing = _pressHandler.getPressData(id, mZ, info.get_targetID());

		Boolean isDrawing = info.get_canvas() != null
				&& state == DepthState.Drawing;
		
		InteractionData data = new InteractionData(new Vector(position.x, position.y, mZ), isPressing, isDrawing);

		InteractionStreamData streamData = new InteractionStreamData(data, 0, _type, isHoverTarget, isPressTarget, HandType.None, info.get_targets(),pressStateData);

		_stream = new ArrayList<InteractionStreamData>();
		_stream.add(streamData);

		_adapter.handleStreamData(_stream);
	}

	@Override
	public ArrayList<InteractionStreamData> getStream() {
		return _stream;
	}

	@Override
	public RegionType getType() {
		return RegionType.Processing;
	}

	@Override
	public void removeUser(int id) {
	}

	@Override
	public int get_inputCount() {
		return 1;
	}
}
