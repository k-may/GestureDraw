package application.interaction.gestTrackOSC.hand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.omg.stub.java.rmi._Remote_Stub;

import oscP5.OscP5;
import processing.core.PVector;
import application.interaction.RegionType;
import application.interaction.gestTrackOSC.GestTrackOSCRegion;
import application.interaction.gestTrackOSC.domain.DomainInputData;
import application.interaction.processing.RegionInputData;
import application.view.MainView;
import framework.depth.DepthState;
import framework.depth.DepthStateData;
import framework.interaction.Vector;
import framework.interaction.Types.HandType;
import framework.interaction.data.InteractionData;
import framework.interaction.data.InteractionStreamData;
import framework.interaction.data.InteractionTargetInfo;

public class GestHandTrackRegion extends GestTrackOSCRegion<HandInputData> {

	private Map<Integer, HandInputData> _handData;

	public GestHandTrackRegion(OscP5 source) {
		super(source, RegionType.GestTrack);

		_handData = new HashMap<Integer, HandInputData>();

		MainView.START_TO = 0f;
		MainView.COLOR_TO = 0.15f;
		MainView.PREDRAW_TO = 0.4f;

	}

	@Override
	public int get_inputCount() {
		// TODO Auto-generated method stub
		return _handData.size();
	}

	@Override
	protected ArrayList<HandInputData> digestInputData(
			Map<Integer, PVector> inputQueue, ArrayList<Integer> removeQueue) {
		// digest input queue
		HandInputData data;

		for (Entry<Integer, HandInputData> entry : _handData.entrySet()) {
			data = entry.getValue();
			int id = data.get_id();

			if (inputQueue.containsKey(id)) {
				data.addRawPosition(inputQueue.get(id), id);
				inputQueue.remove(id);
			} else
				data.addRawPosition(null, id);
		}
		// create new
		for (Entry<Integer, PVector> entry : inputQueue.entrySet()) {
			data = new HandInputData(entry.getKey());
			data.addRawPosition(entry.getValue(), entry.getKey());
			_handData.put(entry.getKey(), data);
		}

		// digest remove queue
		for (Integer id : removeQueue) {
			for (HandInputData user : _handData.values()) {
				if (user.get_id() == id) {
					user.removed = true;
				}
			}
			_handData.remove(id);
		}
		// System.out.println("hands : " + _handData.size());
		return new ArrayList<HandInputData>(_handData.values());
	}

	/*
	 * Since we're not monitoring domains the Hands will be ambiguous
	 */
	@Override
	protected InteractionStreamData convertInputData(HandInputData handData) {

		int id = handData.get_id();
		// returns current position
		PVector position = handData.getPosition();

		// digest UI information relevant to current position
		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(position.x, position.y);
		Boolean isHoverTarget = info.get_isHoverTarget();
		Boolean isPressTarget = info.get_isPressTarget();

		// digest depth state
		DepthStateData depthStateData;
		Boolean isPressing = false;

		if (handData.removed) {
			depthStateData = DepthStateData.Removed;
		} else {
			depthStateData = _adapter.getInteractionInfoAtDepth(position.z);// getUserForDomain(id).get_depthStateData();
			// handle press intention
			if (isPressTarget)
				isPressing = _pressHandler.getPressData(id, position.z, info.get_targetID());
		}

		// apply attraction forces
		position = handData.digest(info, depthStateData.get_state());

		// handle draw intention
		Boolean isDrawing = !isPressTarget
				&& (info.get_canvas() != null && depthStateData.get_state() == DepthState.Drawing);

		InteractionData data = new InteractionData(new Vector(position.x, position.y, position.z), isPressing, isDrawing);

		// release / add to remove queue
		if (handData.invalidDataCount >= RegionInputData.MAX_INVALID)
			removeUser(handData.get_id());

		return new InteractionStreamData(data, id, isHoverTarget, isPressTarget, HandType.Right, info.get_targets(), depthStateData);

	}
}
