package components;

import geometry.Circle;
import geometry.Polygon;
import geometry.Rectangle;
import geometry.Text;
import geometry.Vec2;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import managers.Component;


public class Shape {
	Vector<Polygon>												polygons;
	// Internal data
	private float												radius;

	public Shape() {
		polygons = new Vector<Polygon>();
		radius = 0;
	}
	
	public Shape( final Shape shp ) {
		polygons = new Vector<Polygon>();
		for(Polygon p : shp.polygons) {
			if( p.whoAmI() == Polygon.circle ) {
				polygons.add( new Circle((Circle) p) );
			}
			else if( p.whoAmI() == Polygon.rectangle ) {
				polygons.add( new Rectangle((Rectangle) p) );
			}
			else if( p.whoAmI() == Polygon.text ) {
				polygons.add( new Text((Text) p) );
			}
		}
		radius = shp.radius;
	}

	public final float getRadius() {
		return radius;
	}
	
	public final void add( final Shape shp ) {
		for(Polygon p : shp.polygons) {
			add(p);
			float newMaxRadius = p.offset.length() + (float) Math.sqrt(p.getSqRadius());
			if(radius < newMaxRadius) {
				radius = newMaxRadius;
			}
		}
	}
	
	public final void add( final Polygon p ) {
		polygons.add(p);
		
		float newMaxRadius = p.offset.length() + (float) Math.sqrt(p.getSqRadius());
		if(radius < newMaxRadius) {
			radius = newMaxRadius;
		}
	}

	public final boolean Collides( int me, int him ) {
		float hisRadius = Component.shape.get(him) == null ? 0 : Component.shape.get(him).radius;
		
		Vec2 myPos = Component.placement.get(me).position;
		Vec2 hisPos = Component.placement.get(him).position;
		
		// Envolving circle check
		if( myPos.distanceSquared(hisPos) > Math.pow(radius + hisRadius, 2) ) {
			return false;
		}


		// Check shape to shape
		if(radius == 0) {
			// I am shapeless
			if(hisRadius != 0) {
				// Check my position to all his shapes
				for(Polygon p : Component.shape.get(him).polygons) {
					if( p.Collides(myPos, null, hisPos) ) {
						return true;
					}
				}
			}
		}
		else {
			if(hisRadius == 0) {
				// Check his position to all my shapes
				for(Polygon p : polygons) {
					if( p.Collides(myPos, null, hisPos) ) {
						return true;
					}
				}
			}
			else {
				// Check all my shapes to all his shapes
				for(Polygon myP : polygons) {
					for(Polygon hisP : Component.shape.get(him).polygons) {
						if( myP.Collides(myPos, hisP, hisPos) ) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public final void WriteXML( Document doc, Element root ) {
		Element shape = doc.createElement("Shape");
		for( Polygon p : polygons ) {
			p.WriteXML(doc, shape);
		}
		root.appendChild(shape);
	}
	
	public final Rectangle getRectangle() {
		for(Polygon p : polygons) {
			if(p.whoAmI() == Polygon.rectangle) {
				return (Rectangle) p;
			}
		}
		return null;
	}
	
	public final Text getText() {
		for(Polygon p : polygons) {
			if(p.whoAmI() == Polygon.text) {
				return (Text) p;
			}
		}
		return null;
	}
}
