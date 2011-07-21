package components;

import geometry.Angle;

import geometry.Vec2;

public class Placement {
	private Vec2												position;
	private Angle												angle;
	
	//TODO: Afegir rotations a tots els objectes (draw rectangle i poster gunpoint i poc mes)
	
	public Placement() {
		position = new Vec2();
		angle = new Angle();
	}
	
	public Placement( final Vec2 p ) {
		position = p;
		angle = new Angle();
	}
	
	public void addPosition( final Vec2 v ) {
		position.x += v.x;
		position.y += v.y;
	}
	
	public Vec2 getPosition() {
		return position;
	}
	
	public float getRotation() {
		return angle.getRotation();
	}

	public void setRotation( float r ) {
		angle.setRotation(r);
	}
	
	public void addRotation( float r ) {
		angle.addRotation(r);
	}
}
