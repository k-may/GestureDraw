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

		_adapter = new Adapter();

	}

	@Override
	public void runInteractions() {
		// TODO Auto-generated method stub
		float mX = (float) _source.mouseX;// / _source.width;
		float mY = (float) _source.mouseY;// / _source.height;
		float mZ = _source.mousePressed ? 1 : 0.25f;

		// System.out.println(mZ);
		// if (_source.mousePressed)
		// System.out.println("\n========= : pressed :" + _source.mousePressed);
//		new HandDetectedEvent().dispatch();

		PVector position = new PVector(mX, mY, mZ);
		data.addRawPosition(position, 0);
		int id = data.get_id();
		position = data.getPosition();
		position = new PVector(position.x / _source.width, position.y /_source.height, position.z);

		// digest stream info
		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(position.x, position.y);
		Boolean isHoverTarget = info.get_isHoverTarget();
		Boolean isPressTarget = info.get_isPressTarget();

		// digest depth state
		DepthStateData depthStateData;
		Boolean isPressing = false;

		if (data.removed) {
			depthStateData = DepthStateData.Removed;
		} else {
			depthStateData = _adapter.getInteractionInfoAtDepth(position.z);// getUserForDomain(id).get_depthStateData();
			// handle press intention
			if (isPressTarget)
				isPressing = _pressHandler.getPressData(id, position.z, info.get_targetID());
		}
		
		//System.out.println(isPressTarget + " / " + isPressing);
		Boolean isDrawing = !isPressing && (info.get_canvas() != null
				&& depthStateData.get_state() == DepthState.Drawing);
		
		//System.out.println(info.get_canvas() + " / " + depthStateData.get_state());

		InteractionData data = new InteractionData(new Vector(position.x, position.y, mZ), isPressing, isDrawing);

		InteractionStreamData streamData = new InteractionStreamData(data, 0, isHoverTarget, isPressTarget, HandType.Right, info.get_targets(), depthStateData);

		_stream = new ArrayList<InteractionStreamData>();
		_stream.add(streamData);

		_adapter.handleStreamData(_stream);
	}

	@Override
	public ArrayList<InteractionStreamData> getStream() {
		return _stream;
	}

	@Override
	public void removeUser(int id) {
	}

	@Override
	public int get_inputCount() {
		return 1;
	}
}
