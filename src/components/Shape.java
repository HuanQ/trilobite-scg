package components;

import geometry.Circle;
import geometry.Polygon;
import geometry.Rectangle;

import java.util.Iterator;
import java.util.Vector;

import geometry.Vec2;


public class Shape {
	private Vector<Polygon>										polygons;
	// Internal data
	private float												radius;

	public Shape() {
		polygons = new Vector<Polygon>();
		radius = 0;
	}
	
	public Shape(final Shape shp) {
		polygons = new Vector<Polygon>();
		for (Iterator<Polygon> iter = shp.polygons.iterator(); iter.hasNext();) {
			Polygon next = (Polygon) iter.next();
			if( next.whoAmI() == 0 ) {
				polygons.add( new Circle( (Circle) next ) );
			}
			else if( next.whoAmI() == 1 ) {
				polygons.add( new Rectangle( (Rectangle) next ) );
			}
		}
		radius = shp.radius;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public Vector<Polygon> getPolygons() {
		return polygons;
	}
	
	public void add(final Polygon p) {
		polygons.add(p);
		
		float newMaxRadius = p.getOffset().XYlength() + (float) Math.sqrt(p.getSqRadius());
		if(radius < newMaxRadius) {
			radius = newMaxRadius;
		}
	}
	
	public boolean Collides(final Vec2 myPos, final Shape him, final Vec2 hisPos) {

		float hisRadius = him == null ? 0 : him.radius;
		
		// Envolving circle check
		if( myPos.distanceSquared(hisPos) > Math.pow(radius + hisRadius, 2) ) {
			return false;
		}
		
		// Check shape to shape
		if(radius == 0) {
			// I am shapeless
			if(hisRadius != 0) {
				// Check my position to all his shapes
				for (Iterator<Polygon> iter = him.polygons.iterator(); iter.hasNext();) {
					if( iter.next().Collides(hisPos, (Polygon) null, myPos) ) {
						return true;
					}
				}
			}
		}
		else {
			if(hisRadius == 0) {
				// Check his position to all my shapes
				for (Iterator<Polygon> iter = polygons.iterator(); iter.hasNext();) {
					if( iter.next().Collides(myPos, (Polygon) null, hisPos) ) {
						return true;
					}
				}
			}
			else {
				// Check all my shapes to all his shapes
				for (Iterator<Polygon> iter = polygons.iterator(); iter.hasNext();) {
					Polygon myPoly = (Polygon) iter.next();
					for (Iterator<Polygon> iter2 = him.polygons.iterator(); iter2.hasNext();) {
						if( myPoly.Collides(myPos, iter2.next(), hisPos) ) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
