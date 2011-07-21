package components;

import geometry.Circle;
import geometry.Polygon;
import geometry.Rectangle;

import java.util.Iterator;
import java.util.Vector;

import javax.vecmath.Vector2f;


public class Shape implements Cloneable {
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
			if( next.whoAmI() == 0 )
				polygons.add( new Circle( (Circle) next ) );
			else if( next.whoAmI() == 1 )
				polygons.add( new Rectangle( (Rectangle) next ) );
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
		
		//TODO: En serio, aixo no pot ser xD
		float newMaxRadius = (float) ( Math.sqrt( Math.pow(p.getOffset().x,2) + Math.pow(p.getOffset().y,2) ) + Math.sqrt(p.getSqRadius()) );
		if(radius < newMaxRadius) {
			radius = newMaxRadius;
		}
	}
	
	public boolean Collides(final Vector2f myPos, final Shape him, final Vector2f hisPos) {

		float hisRadius;
		
		if(him == null)
			hisRadius = 0;
		else
			hisRadius = him.radius;

		// Envolving circle check
		Vector2f sqdist = new Vector2f(myPos.x - hisPos.x, myPos.y - hisPos.y);
		if( sqdist.lengthSquared() > Math.pow(radius + hisRadius, 2) ) {
			return false;
		}
		
		// Check shape to shape
		if(radius == 0) {
			// I am shapeless
			if(hisRadius == 0) {
				// Collision impossible
			}
			else {
				// Check my position to all his shapes
				for (Iterator<Polygon> iter = him.polygons.iterator(); iter.hasNext();) {
					Polygon next = (Polygon) iter.next();
					if( next.Collides(hisPos, (Polygon) null, myPos) ) {
						return true;
					}
				}
			}
		}
		else {
			if(hisRadius == 0) {
				// Check his position to all my shapes
				for (Iterator<Polygon> iter = polygons.iterator(); iter.hasNext();) {
					Polygon next = (Polygon) iter.next();
					if( next.Collides(myPos, (Polygon) null, hisPos) ) {
						return true;
					}
				}
			}
			else {
				// Check all my shapes to all his shapes
				for (Iterator<Polygon> iter = polygons.iterator(); iter.hasNext();) {
					Polygon nextMine = (Polygon) iter.next();
					for (Iterator<Polygon> iter2 = him.polygons.iterator(); iter2.hasNext();) {
						Polygon nextHis = (Polygon) iter2.next();
						if( nextMine.Collides(myPos, nextHis, hisPos) ) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
