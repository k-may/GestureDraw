package application.interaction;

import processing.core.PVector;
import framework.depth.DepthState;

public interface IRangeHandler {

	int handle(int minZ, PVector _rawPosition, DepthState _depthState,
			Boolean isDrawing);
}
