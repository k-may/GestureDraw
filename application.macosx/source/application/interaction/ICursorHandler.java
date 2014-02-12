package application.interaction;

import processing.core.PVector;
import framework.depth.DepthState;
import framework.interaction.data.InteractionTargetInfo;

public interface ICursorHandler {

	public abstract void handle(PVector cursorPos, PVector mappedPos,
			PVector rawPos, DepthState depthState, InteractionTargetInfo info, Boolean changed);

}