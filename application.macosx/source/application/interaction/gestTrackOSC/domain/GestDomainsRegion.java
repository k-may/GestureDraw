package application.interaction.gestTrackOSC.domain;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import oscP5.OscP5;
import processing.core.PVector;
import application.interaction.RegionType;
import application.interaction.gestTrackOSC.GestTrackOSCRegion;
import application.interaction.gestTrackOSC.hand.HandInputData;
import application.interaction.processing.RegionInputData;
import application.view.MainView;
import framework.depth.DepthState;
import framework.depth.DepthStateData;
import framework.interaction.Vector;
import framework.interaction.Types.HandType;
import framework.interaction.data.InteractionData;
import framework.interaction.data.InteractionStreamData;
import framework.interaction.data.InteractionTargetInfo;

/*
 * This region represents some experimentation I'd like to pursue, having to do with
 * not only managing users (really a short-coming of the GestureTek Hand Tracker
 * no supporting users) but with extending the interface more through different depth states.
 * We found it just wasn't intuitive enough for the user. The strategy for digesting
 * data was refractored, checkout the DomainCursorHandler for details. 
 */

public class GestDomainsRegion extends GestTrackOSCRegion<DomainInputData> {

	private DomainManager _domainManager;

	public GestDomainsRegion(OscP5 source) {
		super(source, RegionType.GestDomain);
	}

	public void setDomains(float first, float second) {
		MainView.DOMAIN_1 = first;
		MainView.DOMAIN_2 = second;

		if (_domainManager == null)
			_domainManager = DomainManager.getInstance(); // new										// second);
	}
	
	@Override
	public int get_inputCount() {
		return _domainManager.getDataCount();
	}
	
	@Override
	protected ArrayList<DomainInputData> digestInputData(Map<Integer, PVector> inputQueue, ArrayList<Integer> removeQueue) {

		// digest input queue
		for (Entry<Integer, PVector> entry : inputQueue.entrySet()) {
			digestPosition(entry.getKey(), entry.getValue());
		}
		ArrayList<DomainInputData> domainData = _domainManager.get_domainData();

		// remove
		for (Integer id : removeQueue) {
			for (RegionInputData user : domainData) {
				if (user.get_id() == id)
					user.removed = true;
			}
			_domainManager.removeDomain(id);
		}

		return domainData;
	}
	

	private DomainInputData digestPosition(int handId, PVector pos) {
		DomainInputData data = _domainManager.getDomainData(handId, pos.x);
		if (data != null) {
			data.addRawPosition(pos, handId);
		}
		return data;

	}

	@Override
	protected InteractionStreamData convertInputData(DomainInputData domainData) {
		HandType handType = domainData.handType;
		int id = domainData.get_id();
		// returns current position
		PVector position = domainData.getPosition();
		// HandType handType = domainData.handType;

		// digest UI information relevant to current position
		InteractionTargetInfo info = _adapter.getInteractionInfoAtLocation(position.x, position.y);
		Boolean isHoverTarget = info.get_isHoverTarget();
		Boolean isPressTarget = info.get_isPressTarget();

		// digest depth state
		DepthStateData depthStateData;
		Boolean isPressing = false;

		if (domainData.removed) {
			depthStateData = DepthStateData.Removed;
		} else {
			depthStateData = _adapter.getInteractionInfoAtDepth(position.z);// getUserForDomain(id).get_depthStateData();
			// handle press intention
			if (isPressTarget)
				isPressing = _pressHandler.getPressData(id, position.z, info.get_targetID());
		}

		// apply attraction forces
		position = domainData.digest(info, depthStateData.get_state());

		// handle draw intention
		Boolean isDrawing = info.get_canvas() != null
				&& depthStateData.get_state() == DepthState.Drawing;

		InteractionData data = new InteractionData(new Vector(position.x, position.y, position.z), isPressing, isDrawing);

		return new InteractionStreamData(data, id, isHoverTarget, isPressTarget, handType, info.get_targets(), depthStateData);

	}
}
