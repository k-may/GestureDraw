package application.interaction.processing;

import java.util.ArrayList;

import framework.events.HandDetectedEvent;
import framework.interaction.InteractionStreamData;
import framework.interaction.InteractionTargetInfo;
import framework.interaction.InteractionType;
import framework.interaction.Region;
import framework.interaction.Types.HandType;
import framework.pressing.PressState;

import application.interaction.Adapter;
import application.interaction.DomainData;
import application.interaction.RegionType;
import application.view.MainView;

import processing.core.PApplet;
import processing.core.PVector;

public class PRegion extends Region<PApplet> {

	private RegionInputData data;

	public PRegion(PApplet source) {
		super(source);

		RegionInputData.XRANGE = MainView.SCREEN_WIDTH;
		RegionInputData.YRANGE = MainView.SCREEN_HEIGHT;
		RegionInputData.ZRANGE = 1;

		data = new RegionInputData();

		//source.noCursor();

		_type = InteractionType.Mouse;
		_adapter = new Adapter();

		new HandDetectedEvent().dispatch();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void runInteractions() {
		// TODO Auto-generated method stub
		float mX = (float) _source.mouseX / _source.width;
		float mY = (float) _source.mouseY / _source.height;
		float mZ = _source.mousePressed ? 1 : 0.25f;

		PVector pos = new PVector(mX, mY, mZ);
		
		PressState state = mZ == 1 ? PressState.Drawing : PressState.ColorSelection;
		data.setPressState(state);
		data.addPosition(pos, 1, 0);
		
		pos = data.getPosition();
		//pos = MapValuesToCurvedPlane(pos);

		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(pos.x, pos.y, 1, _type);

		InteractionStreamData data = new InteractionStreamData(pos.x, pos.y, mZ, 1, _type, info.get_isHoverTarget(), info.get_isPressTarget(), mZ == 1.0f, mZ, HandType.None);
		// data.set_isOverPressTarget(info.get_isPressTarget());

		_stream = new ArrayList<InteractionStreamData>();
		_stream.add(data);

		_adapter.handleStreamData(_stream);
	}

	@Override
	public ArrayList<InteractionStreamData> getStream() {
		// TODO Auto-generated method stub
		return _stream;
	}

	@Override
	public RegionType getType() {
		// TODO Auto-generated method stub
		return RegionType.Processing;
	}

	@Override
	public void removeDomain(int id) {
		// TODO Auto-generated method stub

	}
}
