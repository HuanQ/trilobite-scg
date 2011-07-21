package geometry;

import geometry.Vec2;

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
	
	public Vec2 getDirection() {
		return new Vec2((float) Math.cos(rotation), (float) Math.sin(rotation)); 
	}
}
