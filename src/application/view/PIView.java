package application.view;

import processing.core.PApplet;
import processing.core.PVector;

public interface PIView {

	public abstract PVector get_absPos();

	public abstract void draw(PApplet p);

}