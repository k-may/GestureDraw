package gesturedraw;

import SimpleOpenNI.SimpleOpenNI;
import application.AppBuilder;
import application.interaction.RegionType;
import application.interaction.soni.SONRegion;
import application.view.MainView;
import framework.IMainView;
import processing.core.PApplet;
import processing.core.PVector;

public class GestureDraw extends PApplet {

	private MainView _root;
	public static PApplet instance;
	private Boolean isFullScreen = true;

	public void setup() {

		background(0);
		noLoop();
		if (!isFullScreen) {
			size(1024, 768, PApplet.P3D);
		} else {
			size(displayWidth, displayHeight, PApplet.P3D);// PApplet.OPENGL);//PApplet.JAVA2D);//
		}
		noCursor();
		instance = this;
		AppBuilder appBuilder = new AppBuilder(this);
	}

	public void draw() {
		_root.draw(this);
	}

	@Override
	public boolean sketchFullScreen() {
		return isFullScreen;
	}

	public void setRoot(MainView root) {
		_root = root;
	}

	// -----------------------------------------------------------------
	// hand events

	public void onNewHand(SimpleOpenNI curContext, int handId, PVector pos) {
		SONRegion region = (SONRegion) _root.get_region();
		region.onNewHand(handId, pos);
	}

	public void onTrackedHand(SimpleOpenNI curContext, int handId, PVector pos) {
		SONRegion region = (SONRegion) _root.get_region();
		region.onTrackedHand(handId, pos);
	}

	public void onLostHand(SimpleOpenNI curContext, int handId) {
		SONRegion region = (SONRegion) _root.get_region();
		region.onLostHand(handId);
	}

	// -----------------------------------------------------------------
	// gesture events
	public void onCompletedGesture(SimpleOpenNI curContext, int gestureType,
			PVector pos) {
		SONRegion region = (SONRegion) _root.get_region();
		region.onCompletedGesture(gestureType, pos);
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { gesturedraw.GestureDraw.class.getName() });
	}

	public void mousePressed() {
		// if (AppBuilder.REGION_TYPE != RegionType.Processing)
		// exit();
	}
}
