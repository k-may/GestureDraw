package application.interaction.processing;

import processing.core.PVector;
import application.interaction.IRangeHandler;
import application.view.MainView;
import framework.depth.DepthState;

public class RangeHandler implements IRangeHandler {

	public int handle(int minZ, PVector _rawPosition, DepthState _depthState,
			Boolean isDrawing) {

		if (!isDrawing) {
			if (_depthState == DepthState.Start) {

				if (minZ + MainView.ZRANGE > _rawPosition.z) {
					minZ -= 0.3f;
				}
			}
		}
		return minZ;

	}
}
