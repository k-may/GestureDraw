package application.interaction.gestTrackOSC.hand;

import static application.interaction.InteractionHelper.getTargetAttr;
import static application.interaction.InteractionHelper.normalizeVector;
import processing.core.PVector;
import framework.depth.DepthState;
import framework.interaction.data.InteractionTargetInfo;
import application.interaction.ICursorHandler;
import application.view.MainView;

public class HandCursorHandler implements ICursorHandler{

	@Override
	public void handle(PVector cursorPos, PVector mappedPos, PVector rawPos,
			DepthState depthState, InteractionTargetInfo info, Boolean changed) {
		PVector currPos = new PVector(mappedPos.x, mappedPos.y);

		if (info.get_isOverTarget()) {
			// ease to target
			PVector target = new PVector(info.get_pressAttractionX(), info.get_pressAttractionY());
			PVector dir = getTargetAttr(new PVector(cursorPos.x, cursorPos.y), target, MainView.TARGET_MASS);
			cursorPos.add(dir);
		} else if (depthState == DepthState.Start) {
			// ease back to center of screen
			PVector target = new PVector(0.5f, 0.5f);
			PVector dir = PVector.sub(new PVector(cursorPos.x, cursorPos.y), target);
			dir.normalize();
			dir.mult(MainView.CENTER_SCREEN_MASS);// 0.0005f);
			cursorPos.add(dir);
		}

		// apply smoothing
		cursorPos.lerp(currPos, MainView.CURSOR_LERP);

		// check boundaries
		normalizeVector(cursorPos);

		cursorPos.z = mappedPos.z;
	}

}
