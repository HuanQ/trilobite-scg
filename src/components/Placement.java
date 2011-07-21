package components;

import geometry.Angle;

import javax.vecmath.*;

public class Placement {
	private Vector2f											position;
	private Angle												angle;
	
	public Placement() {
		position = new Vector2f();
		angle = new Angle();
	}
	
	public Placement(final Vector2f p) {
		position = p;
		angle = new Angle();
	}
	
	public void addPosition(final Vector2f v) {
		position.x += v.x;
		position.y += v.y;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public float getRotation() {
		return angle.getRotation();
	}

	public void setRotation(final float r) {
		angle.setRotation(r);
	}
	
	public void addRotation(final float r) {
		angle.addRotation(r);
	}
}
