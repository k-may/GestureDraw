package application.view.canvas;

import static processing.core.PApplet.println;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import application.view.MainView;
import application.view.avatar.AvatarCursor;
import framework.data.UserData;
import framework.events.TouchEvent;
import framework.stroke.ICanvas;
import framework.stroke.StrokeFragment;
import framework.stroke.StrokeHandler;
import framework.stroke.StrokeType;
import framework.view.View;

public class Canvas extends View implements ICanvas<PImage> {

	private PGraphics _strokeBuffer;
	private StrokeHandler _handler;
	private PGraphics _buffer;
	private HashMap<Integer, PGraphics> _buffers;
	private ArrayList<PGraphics> _completedBuffers;

	public Canvas() {
		_handler = new StrokeHandler();
		_buffers = new HashMap<Integer, PGraphics>();
		_completedBuffers = new ArrayList<PGraphics>();
	}

	@Override
	public void draw(PApplet p) {

		renderStrokeBuffers(p);
		drawCanvasBuffer(p);
		drawStrokeBuffers(p);

		super.draw(p);
	}

	private void drawStrokeBuffers(PApplet p) {
		for (PGraphics buffer : _buffers.values()) {
			p.image(buffer, _x, _y);
		}
	}

	private void drawCanvasBuffer(PApplet p) {
		if (_buffer == null) {
			_buffer = p.createGraphics((int) _width, (int) _height);//, PApplet.OPENGL);
			_buffer.noFill();
			_buffer.beginDraw();
			_buffer.background(MainView.BG_COLOR);
			_buffer.endDraw();
		}else if (_completedBuffers.size() > 0) {
			_buffer.beginDraw();
			for (PGraphics buffer : _completedBuffers) {
				buffer.loadPixels();
				//buffer.
				_buffer.image(buffer, 0, 0);
				buffer.dispose();
				p.removeCache(buffer);
			}
			_completedBuffers.clear();
			_buffer.endDraw();
		}

		p.image(_buffer, _x, _y);
	}

	private void renderStrokeBuffers(PApplet p) {

		ArrayList<StrokeFragment> strokes = _handler.getStrokes();
		for (StrokeFragment stroke : strokes) {
			PGraphics buffer = getBuffer(stroke.get_id(), p);
			drawStrokeFragment(stroke, buffer, p);

			if (stroke.get_type() == StrokeType.End) {
				_completedBuffers.add(buffer);
				_buffers.remove(stroke.get_id());
			}
		}
	}

	protected void drawStrokeFragment(StrokeFragment stroke, PGraphics buffer,
			PApplet p) {
		buffer.noFill();
		buffer.beginDraw();
		// buffer.blendMode(PApplet.LIGHTEST);
		PGraphics strokeGraphic = createStroke(stroke.get_startPt(), stroke.get_ctrlPt(), stroke.get_endPt(), stroke.get_pressure(), stroke.get_color(), p);
		// buffer.image(strokeGraphic, 0, 0);
		// buffer.blend(strokeGraphic, 0, 0, (int) _width, (int) _height, 0, 0,
		// (int) _width, (int) _height, PApplet.DIFFERENCE);
		buffer.copy(strokeGraphic, 0, 0, (int) _width, (int) _height, 0, 0, (int) _width, (int) _height);
		buffer.endDraw();
		strokeGraphic.dispose();
		p.removeCache(strokeGraphic);
	}

	private PGraphics createStroke(PVector pt1, PVector ctrl, PVector pt2,
			float pressure, int color, PApplet p) {
		// PGraphics buffer = createBuffer(p);
		if (_strokeBuffer == null)
			_strokeBuffer = createBuffer(p);

		_strokeBuffer.beginDraw();
		_strokeBuffer.background(0, 0);

		_strokeBuffer.beginShape();
		_strokeBuffer.noFill();
		_strokeBuffer.stroke(color);
		_strokeBuffer.strokeWeight(AvatarCursor.GetRadiusForPressure(pressure));
		_strokeBuffer.vertex(pt1.x, pt1.y);
		_strokeBuffer.quadraticVertex(ctrl.x, ctrl.y, pt2.x, pt2.y);
		_strokeBuffer.endShape();
		_strokeBuffer.endDraw();
		return _strokeBuffer;
	}

	private PGraphics getBuffer(int id, PApplet p) {
		if (_buffers.containsKey(id)) {
			return _buffers.get(id);
		}
		PGraphics buffer = createBuffer(p);
		// buffer.blendMode(PApplet.LIGHTEST);
		_buffers.put(id, buffer);
		return buffer;

	}

	protected PGraphics createBuffer(PApplet p) {
		PGraphics buffer = p.createGraphics((int) _width, (int) _height);
		buffer.noFill();
		buffer.beginDraw();
		buffer.background(0x000000, 0);
		buffer.smooth();
		return buffer;
	}

	@Override
	public void handleInteraction(TouchEvent event) {
		UserData data = event.getUser();
		float strokePressure = data.get_strokePressure();

		switch (event.get_interactionType()) {
			case PressDown:
				//println("press down");
				_handler.start(event.get_localX(), event.get_localY(), strokePressure, event.getUser());
				break;
			case PressUp:
				//println("press up");
				_handler.end(event.get_localX(), event.get_localY(), strokePressure, event.getUser());
				break;
			case Move:
				//println("move : " + event.get_localX() + " / " + event.get_localY());
				_handler.move(event.get_localX(), event.get_localY(), strokePressure, event.getUser());
				break;
		}
	}

	public void onStrokeStart(int id) {
		_buffers.put(id, null);
	}

	public void onStrokeEnd(int id) {
		if (_buffers.containsKey(id)) {
			_buffer.image(_buffers.get(id), 0, 0);
			_buffers.remove(id);
		}
	}

	@Override
	public void clear() {
		_buffer.background(MainView.BG_COLOR);
	}

	@Override
	public void save(String filePath) {
		_buffer.save(filePath);
	}

	@Override
	public PImage getImage() {
		return _buffer.get();
	}
	
	@Override
	public Boolean isDrawTarget() {
		return true;
	}

}