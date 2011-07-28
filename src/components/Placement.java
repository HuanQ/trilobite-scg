package components;

import geometry.Angle;

import geometry.Vec2;

public class Placement {
	private Vec2												position;
	private Angle												angle;
	private boolean												gameElement;
	
	//TODO: Afegir rotations a tots els objectes (draw rectangle i poster gunpoint i poc mes)
	
	public Placement( boolean gElem ) {
		position = new Vec2();
		angle = new Angle();
		gameElement = gElem;
	}
	
	public Placement( final Vec2 p, boolean gElem ) {
		position = p;
		angle = new Angle();
		gameElement = gElem;
	}
	
	public boolean isGameElement() {
		return gameElement;
	}
	
	public void interpPosition( final Vec2 start, final Vec2 end, float step ) {
		position.interpolate(start, end, step);
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
