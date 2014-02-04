package framework.depth;

import framework.SceneState;
import framework.cursor.CursorMode;
import framework.cursor.CursorState;
import framework.interaction.Types.HandType;

public class DepthStateFactory {

	private float _startS;
	private float _startE;

	private float _colorS;
	private float _colorE;

	private float _preDrawS;
	private float _preDrawE;

	private float _drawS;
	private float _drawE;

	public DepthStateFactory(float startTo, float colorTo, float preDrawTo) {
		_startS = 0.0f;
		_startE = startTo;

		_colorS = _startE;
		_colorE = colorTo;

		_preDrawS = colorTo;
		_preDrawE = preDrawTo;

		_drawS = preDrawTo;
		_drawE = 1.0f;
	}

	public DepthStateData getStateData(float pressure, SceneState sceneState) {
		// System.out.println("pressure ==>" + pressure);

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
		if (pressure >= _startS && pressure < _startE) {// || overTarget) {
			data = new DepthStateData(DepthState.Start, map(pressure, _startS, _startE));
		} else if (pressure >= _colorS && pressure < _colorE) {
			data = new DepthStateData(DepthState.ColorSelection, map(pressure, _colorS, _colorE));
		} else if (pressure >= _preDrawS && pressure < _preDrawE) {
			data = new DepthStateData(DepthState.PreDrawing, map(pressure, _preDrawS, _preDrawE));
		} else {
			p = map(pressure, _drawS, _drawE);
			p *= p;
			data = new DepthStateData(DepthState.Drawing, p);
		}
		// System.out.println("pressure ==>" + pressure + " : "+
		// data.get_state().toString());

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
