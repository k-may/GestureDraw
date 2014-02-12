package application.interaction.gestTrackOSC.domain;

import static application.interaction.InteractionHelper.getNormalizedTargetAttr;
import static application.interaction.InteractionHelper.getTargetAttr;
import static application.interaction.InteractionHelper.normalizeVector;
import processing.core.PVector;
import application.interaction.ICursorHandler;
import application.view.MainView;
import framework.depth.DepthState;
import framework.interaction.data.InteractionTargetInfo;

public class DomainCursorHandler implements ICursorHandler {

	private PVector _prevPos;

	/*
	 *if changed make sure that the ranges get reset to the new position
	 */
	@Override
	public void handle(PVector cursorPos, PVector mappedPos, PVector rawPos,
			DepthState depthState, InteractionTargetInfo info, Boolean changed) {

		if (changed)
			resetPrevious(depthState, rawPos, cursorPos);

		PVector currPos;

		if (depthState.isDrawing()) {
			currPos = new PVector(rawPos.x, rawPos.y);
			cursorPos.lerp(PVector.add(cursorPos, getNormalizedTargetAttr(currPos, _prevPos, MainView.DRAW_MASS)), MainView.DRAW_LERP);
		} else {

			currPos = new PVector(mappedPos.x, mappedPos.y);

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
		}

		// reset previous
		_prevPos = new PVector(currPos.x, currPos.y);

		// check boundaries
		normalizeVector(cursorPos);

		cursorPos.z = mappedPos.z;
	}

	private void resetPrevious(DepthState depthState, PVector rawPos,
			PVector cursorPos) {

		if (depthState.isDrawing()) {
			_prevPos = new PVector(rawPos.x, rawPos.y);
		} else {
			_prevPos = new PVector(cursorPos.x, cursorPos.y);
		}
	}

}
