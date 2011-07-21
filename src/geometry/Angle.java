package geometry;

import javax.vecmath.Vector2f;

public class Angle {
	private float												rotation;
	
	public Angle() {
		rotation = 0;
	}
	
	public Angle(final float r) {
		rotation = r;
	}
	
	public float getRotation() {
		return rotation;
	}

	public void setRotation(final float r) {
		float turns = (float) Math.floor(r / (2 * Math.PI));
		rotation =  r - turns * (2 * (float) Math.PI);
	}
	
	public void addRotation(final float r) {
		float turns = (float) Math.floor(r + rotation / (2 * Math.PI));
		rotation +=  r - turns * (2 * (float) Math.PI);
	}
	
	public Vector2f getDirection() {
		return new Vector2f((float) Math.cos(rotation), (float) Math.sin(rotation)); 
	}
}
