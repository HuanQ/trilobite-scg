package geometry;

import geometry.Vec2;

public class Angle {
	private float												rotation;

	public Angle( final Angle a ) {
		rotation = a.rotation;
	}
	
	public Angle() {
		rotation = 0;
	}
	
	public Angle( final float r ) {
		rotation = r;
	}
	
	public final float get() {
		return rotation;
	}
	
	public final void set( final Vec2 dir ) {
		set( (float) Math.atan(dir.y/dir.x) );
	}
	
	public final void set( float r ) {
		float turns = (float) Math.floor(r / (2 * Math.PI));
		rotation =  r - turns * (2 * (float) Math.PI);
	}
	
	public final void add( float r ) {
		float turns = (float) Math.floor(r + rotation / (2 * Math.PI));
		rotation +=  r - turns * (2 * (float) Math.PI);
	}
	
	public final Vec2 getDirection() {
		return new Vec2( (float) Math.cos(rotation), (float) Math.sin(rotation) ); 
	}
	
	public final String toString() {
		return String.valueOf(rotation);
	}
}
