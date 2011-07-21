package geometry;


import graphics.Sprite;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import managers.Constant;



public abstract class Polygon {
	protected final Vector3f											offset;
	protected Vector3f													color;
	protected Sprite													texture;
	// Internal data
	protected float														sqradius;
	
	protected Polygon(final Vector3f off) {
		offset = off;
		color = null;
		texture = null;
	}
	
	protected Polygon(final Polygon p) {
		offset = new Vector3f();
		offset.x = p.getOffset().x;
		offset.y = p.getOffset().y;
		if(p.getColor() != null) {
			color = new Vector3f();
			color.x = p.getColor().x;
			color.y = p.getColor().y;
			color.z = p.getColor().z;
		}
		sqradius = p.getSqRadius();
		texture = null;
	}
	
	//TODO: Canviar aixo getclass o dynamic o algo
	public abstract int whoAmI();
	
	public abstract void multSize( final float m );
	
	public abstract void draw( final Vector2f pos, final Vector3f defColor );
	
	public abstract boolean Collides( final Vector2f myPos, final Polygon p, Vector2f hisPos);
	
	public final float getSqRadius() {
		return sqradius;
	}
	
	public final Vector3f getOffset() {
		return offset;
	}
	
	public final void setColor( final Vector3f col ) {
		color = col;
	}

	public final Sprite getTexture() {
		return texture;
	}
	
	public final void setTexture( final String tex ) {
		texture = new Sprite(Constant.textureLoader, tex);
	}
	
	public final Vector3f getColor() {
		return color;
	}
}
