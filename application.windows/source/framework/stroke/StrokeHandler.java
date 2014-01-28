package framework.stroke;

import java.util.ArrayList;
import java.util.HashMap;

import framework.data.UserData;

import processing.core.PVector;
import static processing.core.PApplet.println;

public class StrokeHandler {
	private HashMap<Integer, StrokeHandle> _handles; // ArrayList<StrokeHandle>
														// _handles;
	private ArrayList<Integer> _completedHandles;

	public StrokeHandler() {
		_handles = new HashMap<Integer, StrokeHandle>();// = new
														// ArrayList<StrokeHandle>();
		_completedHandles = new ArrayList<Integer>();
	}

	public ArrayList<StrokeFragment> getStrokes() {
		ArrayList<StrokeFragment> strokes = new ArrayList<StrokeFragment>();

		for (StrokeHandle handle : _handles.values()) {
			StrokeFragment fragment = handle.getStrokeFragment();
			if (fragment != null)
				strokes.add(fragment);
		}

		clearCompleted();

		return strokes;
	}

	private void clearCompleted() {

		for (int id : _completedHandles) {
			_handles.remove(id);
		}
		_completedHandles.clear();

	}

	public void start(float x, float y, float pressure, UserData user) {
		if (pressure > 0.0f) {
			StrokeHandle handle = getHandle(user, true, false);
			handle.add(new StrokeInfo(new PVector(x, y), StrokeType.Start, pressure));
		}
	}

	public void end(float x, float y, float pressure, UserData user) {
		//println("end stroke : " + pressure);
		StrokeHandle handle = getHandle(user, false, true);
		if (handle != null)
			handle.add(new StrokeInfo(new PVector(x, y), StrokeType.End, pressure));

		_completedHandles.add(user.get_id());
	}

	public void move(float x, float y, float pressure, UserData user) {

		if (_completedHandles.contains(user.get_id()))
			return;

		StrokeHandle handle = getHandle(user, false, false);
		if (handle != null) {
			if (pressure > 0.0f)
				handle.add(new StrokeInfo(new PVector(x, y), StrokeType.Move, pressure));
			else
				end(x, y, pressure, user);
		} else
			start(x, y, pressure, user);
	}

	private StrokeHandle getHandle(UserData user, Boolean isStart, Boolean isEnd) {
		StrokeHandle handle = null;

		if (_handles.containsKey(user.get_id()))
			handle = _handles.get(user.get_id());

		if (isStart) {
			handle = new StrokeHandle(user.get_id(), user.getColor());
			_handles.put(user.get_id(), handle);
		}

		return handle;
		/*
		 * if(isEnd){
		 * 
		 * } // check if exists?? ArrayList<StrokeHandle> matches = new
		 * ArrayList<StrokeHandle>(); for (StrokeHandle h : _handles) { if
		 * (h.get_id() == user.get_id()) matches.add(h); }
		 * 
		 * if (isStart) { if (matches.size() > 0) {
		 * log("error : stroke not disposed"); for (StrokeHandle h : matches)
		 * _handles.remove(h); } handle = new StrokeHandle(user.get_id(),
		 * user.getColor()); _handles.add(handle); } else if (isEnd) { if
		 * (matches.size() == 1) handle = matches.get(0); else { if
		 * (matches.size() > 1) { log("error : stroke duplicate"); } else {
		 * log("error : stroke not initialized"); } } // remove all for
		 * (StrokeHandle h : matches) _handles.remove(h);
		 * 
		 * } else { // move if (matches.size() == 1) handle = matches.get(0);
		 * else { if (matches.size() > 1) log("error: too many stroke matches");
		 * else if (matches.size() == 0) log("error: stroke not initialized"); }
		 * }
		 * 
		 * return handle;
		 */

	}

	private void log(String msg) {
		// println("StrokeHandler : " + msg);
	}

}
