package framework.interaction.data;

import framework.interaction.Vector;

public class InteractionData {
	public Vector vector;
	public Boolean isPressing = false;
	public Boolean isDrawing = false;

	public InteractionData(Vector vector){
		this.vector = vector;
	}
	
	public InteractionData(Vector vector, Boolean isPressing, Boolean isDrawing) {
		this.vector = vector;
		this.isPressing = isPressing;
		this.isDrawing = isDrawing;
	}
	

	public float getX() {
		return vector.x;
	}

	public float getY() {
		return vector.y;
	}

	public float getZ() {
		return vector.z;
	}
}
