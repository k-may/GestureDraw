package framework.pressing;

import framework.view.CanvasState;

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

	public PressStateData getStateData(float pressure, CanvasState state,
			Boolean overTarget) {
		switch (state) {
			case Canvas:
				return getCanvasStateData(pressure, overTarget);
			default:
				return getGalleryStateData(pressure, overTarget);
		}
	}

	private PressStateData getCanvasStateData(float pressure, Boolean overTarget) {
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

	private PressStateData getGalleryStateData(float pressure,
			Boolean overTarget) {
		/*
		 * PressStateData data = null;
		 * 
		 * if (pressure >= _startS && pressure < _startE || overTarget) { data =
		 * new PressStateData(PressState.Start, map(pressure, _startS,
		 * _startE)); } else{ data = new PressStateData(PressState.PreDrawing,
		 * map(pressure, _startE, 1.0f)); }
		 */

		return new PressStateData(PressState.Start, pressure);
	}

	private float map(float value, float start, float end) {
		return (value - start) / (end - start);
	}
}
