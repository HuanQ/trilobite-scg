package components;

import geometry.Angle;

import geometry.Vec2;

public class Placement {
	static public final int										gameSide = 0;
	static public final int										fullScreen = 1;
	static public final int										leftSide = 2;
	static public final int										rightSide = 3;
	static public final int										leftSideFull = 4;
	static public final int										rightSideFull = 5;
	
	private Vec2												position;
	private Angle												angle;
	private int													screenSide;
	
	//TODO: Afegir rotations a tots els objectes (draw rectangle i poster gunpoint i poc mes)
	
	public Placement( int side ) {
		position = new Vec2();
		angle = new Angle();
		screenSide = side;
	}
	
	public Placement( final Vec2 p, int side ) {
		position = p;
		angle = new Angle();
		screenSide = side;
	}
	
	public int getScreenSide() {
		return screenSide;
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
