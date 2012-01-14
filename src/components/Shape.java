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
import managers.Constant;


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

	// Collision with a point
	public final boolean Collides( int me, final Vec2 point ) {
		
		Vec2 myPos = Component.placement.get(me).position;
		
		// Envolving circle check
		if( myPos.distanceSquared(point) - Constant.getFloat("Performance_CollisionEpsilon") > radius*radius ) {
			return false;
		}

		// Check shape by shape
		if(radius != 0) {
			// Check his position to all my shapes
			for(Polygon p : polygons) {
				if( p.Collides(myPos, null, point, Component.placement.get(me).angle, null) ) {
					return true;
				}
			}
		}
		return false;
	}

	// Collision with a shape
	public final boolean Collides( int me, int him ) {
		if( Component.shape.get(him) == null || Component.shape.get(him).radius == 0) {
			return Collides(me, Component.placement.get(him).position);
		}
		else {
			Vec2 myPos = Component.placement.get(me).position;
			Vec2 hisPos = Component.placement.get(him).position;
			float hisRadius = Component.shape.get(him).radius;
			
			// Envolving circle check
			if( myPos.distanceSquared(hisPos) > Math.pow(radius + hisRadius, 2) ) {
				return false;
			}

			// Check shape to shape
			if(radius == 0) {
				// I am shapeless, check my position to all his shapes
				for(Polygon p : Component.shape.get(him).polygons) {
					if( p.Collides(myPos, null, hisPos, Component.placement.get(me).angle, Component.placement.get(him).angle) ) {
						return true;
					}
				}
			}
			else {
				// Check all my shapes to all his shapes
				for(Polygon myP : polygons) {
					for(Polygon hisP : Component.shape.get(him).polygons) {
						if( myP.Collides(myPos, hisP, hisPos, Component.placement.get(me).angle, Component.placement.get(him).angle) ) {
							return true;
						}
					}
				}
			}
			return false;
		}
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
	
	public final void Scale( float m ) {
		for(Polygon p : polygons) {
			p.Scale(m);
		}
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
