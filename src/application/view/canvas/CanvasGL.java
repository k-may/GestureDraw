package application.view.canvas;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.opengl.PGL;
import application.view.avatar.AvatarCursor;
import framework.stroke.StrokeFragment;

public class CanvasGL extends Canvas {

	@Override
	protected void drawStrokeFragment(StrokeFragment stroke, PGraphics buffer,
			PApplet p) {

		buffer.noFill();
		buffer.smooth();
		buffer.beginDraw();
		PGL pgl = buffer.beginPGL();
		//pgl.blendFunc(PGL.ONE, PGL.SRC_COLOR);//SRC_ALPHA_SATURATE);
		pgl.blendFunc(PGL.ONE, PGL.CONSTANT_COLOR);
		int weight = (int) AvatarCursor.GetRadiusForPressure(stroke.get_pressure());
		drawStroke(stroke.get_color(), weight, stroke.get_startPt(), stroke.get_ctrlPt(), stroke.get_endPt(), buffer);
		buffer.endDraw();
	}

	private void drawStroke(int color, int weight, PVector start, PVector ctrl,
			PVector end, PGraphics buffer) {
		buffer.beginShape();
		buffer.stroke(color);
		buffer.strokeWeight(weight);
		buffer.vertex(start.x, start.y);
		buffer.quadraticVertex(ctrl.x, ctrl.y, end.x, end.y);
		//buffer.filter(PApplet.BLUR,3f);
		buffer.endShape();
	}
	
	@Override
	protected PGraphics createBuffer(PApplet p) {
		PGraphics buffer = p.createGraphics((int) _width, (int) _height, PApplet.P3D);
		buffer.noFill();
		buffer.smooth();
		buffer.strokeCap(PApplet.ROUND);
		buffer.beginDraw();
		buffer.background(0x000000, 0);
		buffer.endDraw();
		return buffer;
	}
}
