package geometry;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import geometry.Vec3;
import graphics.Sprite;

public abstract class Polygon {
	static public final int												circle = 0;
	static public final int												rectangle = 1;
	static public final int												text = 2;
	
	public final Vec2													offset;
	public float														layer;
	public Vec3															color;
	protected Sprite													texture = null;
	// Internal data
	protected float														sqradius;
	
	protected Polygon( final Vec2 off, float ly ) {
		offset = off;
		layer = ly;
		color = Vec3.white;
		sqradius = 0;
	}
	
	protected Polygon( final Polygon p ) {
		offset = new Vec2( p.offset );
		color = new Vec3( p.color );
		layer = p.layer;
		sqradius = p.getSqRadius();
		if( p.texture != null ) {
			texture = new Sprite( p.texture );
		}
	}
	
	// TODO: Estalviar-se aixo per a poder afegir formes mes facilment
	public abstract int whoAmI();
	
	public abstract void Scale( float m );
	
	public abstract void WriteXML( Document doc, Element root );
	
	public abstract void Draw( final Vec2 pos, final Vec3 defColor, final Angle rot );
	
	public abstract boolean Collides( final Vec2 myPos, final Polygon p, final Vec2 hisPos, final Angle myRot, final Angle hisRot );
	
	public final void setSqRadius( float r ) {
		sqradius = r;
	}
	
	public final float getSqRadius() {
		return sqradius;
	}

	public final void setColor( final Vec3 col ) {
		color = col;
	}
	
	public final void setTexture( final String tex ) {
		if( texture == null || !tex.equals(texture.getTextureName()) ) {
			texture = new Sprite( tex );
		}		
	}
	
	protected final void writeSubShape( final Document doc, final Element root ) {
		// Color
		if( !color.equals(Vec3.white) ) {
			Element col = doc.createElement("Color");
			col.setAttribute( "r", Float.toString(color.x) );
			col.setAttribute( "g", Float.toString(color.y) );
			col.setAttribute( "b", Float.toString(color.z) );
			root.appendChild(col);
		}
		// Texture
		if(texture != null) {
			Element tex = doc.createElement("Texture");
			tex.appendChild(doc.createTextNode(texture.getTextureName()));
			root.appendChild(tex);
		}

	}
}
