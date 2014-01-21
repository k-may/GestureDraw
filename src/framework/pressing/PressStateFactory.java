package framework.pressing;

import framework.cursor.CursorMode;
import framework.cursor.CursorState;
import framework.interaction.Types.HandType;
import framework.scenes.SceneType;

public class PressStateFactory {

	private float _startS;
	private float _startE;

	private float _colorS;
	private float _colorE;

	private float _preDrawS;
	private float _preDrawE;

	private float _drawS;
	private float _drawE;

	public PressStateFactory(float startTo, float colorTo, float preDrawTo) {
		_startS = 0.0f;
		_startE = startTo;

		_colorS = _startE;
		_colorE = colorTo;

		_preDrawS = colorTo;
		_preDrawE = preDrawTo;

		_drawS = preDrawTo;
		_drawE = 1.0f;
	}

	public PressStateData getStateData(float pressure, SceneType sceneType,
			Boolean overTarget) {
		switch (sceneType) {
			case Canvas:
				return getCanvasSceneData(pressure, overTarget);
			default:
				return getHomeSceneData(pressure, overTarget);
		}
	}

	private PressStateData getCanvasSceneData(float pressure, Boolean overTarget) {
		PressStateData data = null;

		if (pressure >= _startS && pressure < _startE) {// || overTarget) {
			data = new PressStateData(PressState.Start, map(pressure, _startS, _startE));
		} else if (pressure >= _colorS && pressure < _colorE) {
			data = new PressStateData(PressState.ColorSelection, map(pressure, _colorS, _colorE));
		} else if (pressure >= _preDrawS && pressure < _preDrawE) {
			data = new PressStateData(PressState.PreDrawing, map(pressure, _preDrawS, _preDrawE));
		} else {
			data = new PressStateData(PressState.Drawing, map(pressure, _drawS, _drawE));
		}

		return data;
	}

	private PressStateData getHomeSceneData(float pressure, Boolean overTarget) {

		return new PressStateData(PressState.Start, pressure);
	}

	private float map(float value, float start, float end) {
		return (value - start) / (end - start);
	}

	public CursorState getCursorState(SceneType sceneType,
			PressStateData pressState, Boolean isPressing,
			Boolean isOverTarget, HandType type, int color) {

		CursorState mode;
		float pressure = pressState.get_pressure();
		if (isOverTarget && pressState.get_state() != PressState.Drawing) {
			if (isPressing)
				mode = new CursorState(CursorMode.Pressing);
			else
				mode = new CursorState(CursorMode.PrePressing);
		} else {
			if (sceneType == SceneType.Home) {
				mode = new CursorState(CursorMode.Navigating);
			} else {
				// canvas & drawing
				if (pressState.get_state() == PressState.PreDrawing)
					mode = new CursorState(CursorMode.PreDrawing);
				else
					mode = new CursorState(CursorMode.Drawing);
			}
		}

		mode.set_color(color);
		mode.set_handType(type);
		mode.set_pressure(pressure);
		return mode;
	}
}
