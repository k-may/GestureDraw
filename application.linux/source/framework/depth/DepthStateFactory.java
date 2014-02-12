package framework.depth;

import application.view.MainView;
import framework.SceneState;
import framework.cursor.CursorMode;
import framework.cursor.CursorState;
import framework.interaction.Types.HandType;

public class DepthStateFactory {

	public DepthStateFactory() {
	}

	public DepthStateData getStateData(float pressure, SceneState sceneState) {
		switch (sceneState) {
			case Canvas:
				return getCanvasSceneDepthData(pressure);
			default:
				return getHomeSceneDepthData(pressure);
		}

	}

	private DepthStateData getCanvasSceneDepthData(float pressure) {
		DepthStateData data = null;
		float p = 0.0f;
	//System.out.println("pressure : " + pressure);
		if (pressure >= 0f && pressure < MainView.START_TO) {// || overTarget) {
			data = new DepthStateData(DepthState.Start, map(pressure, 0, MainView.START_TO));
		} else if (pressure >= MainView.START_TO && pressure < MainView.COLOR_TO) {
			data = new DepthStateData(DepthState.ColorSelection, map(pressure, MainView.START_TO, MainView.COLOR_TO));
		} else if (pressure >= MainView.COLOR_TO && pressure < MainView.PREDRAW_TO) {
			data = new DepthStateData(DepthState.PreDrawing, map(pressure, MainView.COLOR_TO, MainView.PREDRAW_TO));
		} else {
			p = map(pressure, MainView.PREDRAW_TO, 1f);
			p *= p;
			data = new DepthStateData(DepthState.Drawing, p);
		}
		return data;
	}

	private DepthStateData getHomeSceneDepthData(float pressure) {
		return new DepthStateData(DepthState.Start, pressure);
	}

	private float map(float value, float start, float end) {
		return (value - start) / (end - start);
	}

	public CursorState getCursorState(SceneState sceneType,
			DepthStateData depthStateData, Boolean isPressing,
			Boolean isOverTarget, HandType type, int color) {

		CursorState mode;

		/*
		 * Drawing state should take precidence over pressing (ie. if already
		 * drawing we don't want the cursor to change)
		 */
		Boolean isPressingObject = isOverTarget
				&& depthStateData.get_state() != DepthState.Drawing;

		if (isPressingObject) {
			mode = getPressingCursorState(isPressing);
		} else {
			if (sceneType == SceneState.Canvas) {
				mode = getCanvasCursorState(depthStateData.get_state());
			} else {
				// if home or saving
				mode = getHomeCursorState();
			}
		}

		mode.set_color(color);
		mode.set_handType(type);
		mode.set_pressure(depthStateData.get_pressure());
		return mode;
	}

	private CursorState getCanvasCursorState(DepthState state) {
		// canvas & drawing
		switch (state) {
			case PreDrawing:
				return new CursorState(CursorMode.PreDrawing);
			case Drawing:
				return new CursorState(CursorMode.None);
			case ColorSelection:
				return new CursorState(CursorMode.ColorSelecting);
			default:
				return new CursorState(CursorMode.Navigating);
		}
	}

	private CursorState getHomeCursorState() {
		return new CursorState(CursorMode.Navigating);
	}

	private CursorState getPressingCursorState(Boolean isPressing) {
		if (isPressing)
			return new CursorState(CursorMode.Pressing);
		else
			return new CursorState(CursorMode.PrePressing);
	}
}
