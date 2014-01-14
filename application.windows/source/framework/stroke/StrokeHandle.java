package framework.stroke;

import java.util.ArrayList;

import processing.core.PVector;
import static processing.core.PApplet.println;

public class StrokeHandle {
	private ArrayList<StrokeInfo> _infos;
	private int _id;
	private int _color;
	private ArrayList<PVector> _pos;

	public StrokeHandle(int id, int color) {
		//println("new stroke handle : color : " + Integer.toHexString(color));
		_color = color;
		_id = id;
		_infos = new ArrayList<StrokeInfo>();
		_pos = new ArrayList<PVector>();
	}

	public void add(StrokeInfo info) {
		_infos.add(info);
		_pos.add(info.get_pos());
	}

	private StrokeInfo getCurrentInfo() {
		return _infos.get(_infos.size() - 1);
	}

	public StrokeType getCurrentStrokeType() {
		return getCurrentInfo().get_type();
	}

	private float getCurrentPressure() {
		return getCurrentInfo().get_pressure();
	}

	public int get_id() {
		return _id;
	}

	public int get_color() {
		return _color;
	}

	public StrokeFragment getStrokeFragment() {
		int index = _pos.size() - 1;
		PVector pt1, ctrl, pt2;
		switch (getCurrentStrokeType()) {
			case Move:
				if (index > 1) {
					pt1 = index == 2 ? _pos.get(0)
							: PVector.lerp(_pos.get(index - 2), _pos.get(index - 1), 0.5f);
					ctrl = _pos.get(index - 1);
					pt2 = PVector.lerp(_pos.get(index - 1), _pos.get(index), 0.5f);
					return new StrokeFragment(pt1, ctrl, pt2, getCurrentPressure(), _color);
				}
				break;
			case End:
				if (index > 2) {
					pt1 = PVector.lerp(_pos.get(index - 2), _pos.get(index - 1), 0.5f);
					ctrl = _pos.get(index);
					pt2 = _pos.get(index);
					return new StrokeFragment(pt1, ctrl, pt2, getCurrentPressure(), _color);
				}
				break;
		}
		return null;
	}
}
