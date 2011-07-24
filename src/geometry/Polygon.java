package geometry;


import graphics.Sprite;

import geometry.Vec2;
import geometry.Vec3;

public abstract class Polygon {
	protected final Vec3												offset;
	protected Vec3														color;
	protected Sprite													texture;
	// Internal data
	protected float														sqradius;
	
	protected Polygon(final Vec3 off) {
		offset = off;
		color = null;
		texture = null;
	}
	
	protected Polygon(final Polygon p) {
		offset = new Vec3( p.getOffset() );
		if(p.getColor() != null) {
			color = new Vec3( p.getColor() );
		}
		sqradius = p.getSqRadius();
		texture = null;
	}
	
	// TODO: Estalviar-se aixo per a poder afegir formes mes facilment
	public abstract int whoAmI();
	
	public abstract void multSize( float m );
	
	public abstract void draw( final Vec2 pos, final Vec3 defColor );
	
	public abstract boolean Collides( final Vec2 myPos, final Polygon p, Vec2 hisPos);
	
	public final float getSqRadius() {
		return sqradius;
	}
	
	public final Vec3 getOffset() {
		return offset;
	}
	
	public final void setColor( final Vec3 col ) {
		color = col;
	}

	public final void setTexture( final String tex ) {
		texture = new Sprite( tex );
	}
	
	public final Vec3 getColor() {
		return color;
	}
}
