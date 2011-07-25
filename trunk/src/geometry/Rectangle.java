package geometry;


import geometry.Vec2;
import geometry.Vec3;

import managers.Screen;

import static org.lwjgl.opengl.GL11.*;

public class Rectangle extends Polygon {
	private final Vec2											size;
	
	public Rectangle( final Vec2 s, final Vec3 off ) {
		super(off);
		size = s;
		sqradius = (float) Math.pow(size.length()/2, 2);
	}
	
	public Rectangle( final Rectangle r ) {
		super((Polygon) r);
		size = new Vec2( r.size );
	}
	
	public Vec2 getSize() {
		return size;
	}
	
	public int whoAmI() {
		return Polygon.rectangle;
	}
	
	public void multSize( float m ) {
		size.scale(m);
		sqradius = (float) Math.pow(size.length()/2, 2);
	}

	public void draw( final Vec2 pos, final Vec3 defColor ) {
		Vec2 myPos = Screen.reposition( new Vec2(pos.x + offset.x, pos.y + offset.y) );
		Vec2 mySize = Screen.rescale( new Vec2( size ) );
		
		if(texture == null) {
		    // Draw placeholder
			glDisable(GL_TEXTURE_2D);
			if( color == null ) {
				glColor4f(defColor.x, defColor.y, defColor.z, 1.f);
			}
			else {
				glColor4f(color.x * defColor.x, color.y * defColor.y, color.z * defColor.z, 1);
			}
			glBegin(GL_QUADS);
		    glVertex3f(myPos.x - mySize.x/2, myPos.y - mySize.y/2, offset.z);
			glVertex3f(myPos.x + mySize.x/2, myPos.y - mySize.y/2, offset.z);
			glVertex3f(myPos.x + mySize.x/2, myPos.y + mySize.y/2, offset.z);
			glVertex3f(myPos.x - mySize.x/2, myPos.y + mySize.y/2, offset.z);
		    glEnd();
		}
		else {
			// Draw sprites
	    	glEnable(GL_TEXTURE_2D);
	    	texture.setWidth( (int) Screen.rescale(size.x) );
			texture.setHeight( (int) Screen.rescale(size.y) );
	    	texture.draw(myPos.x, myPos.y, offset.z + 0.5f);
	    }
	}
    
	public boolean Collides( final Vec2 myPos, final Polygon p, final Vec2 hisPos ) {
		Vec2 myAbsPos = new Vec2(myPos.x + offset.x, myPos.y + offset.y);
		
		if(p == null) {
			return Math.abs(hisPos.x - myAbsPos.x) < size.x / 2 && Math.abs(hisPos.y - myAbsPos.y) < size.y / 2;
		}
		else {
			Vec2 hisAbsPos = new Vec2(hisPos.x + p.offset.x, hisPos.y + p.offset.y);
			
			if( p.whoAmI() == Polygon.circle ) {
				// Rectangle to circle
				Circle c = (Circle) p;
				// Find the closest point to the circle within the rectangle
				float closestX = Math.min( Math.max( hisAbsPos.x, myAbsPos.x - size.x/2 ), myAbsPos.x + size.x/2 );
				float closestY = Math.min( Math.max( hisAbsPos.y, myAbsPos.y - size.y/2 ), myAbsPos.y + size.y/2 );
				// Calculate the distance between the circle's center and this closest point
				float distanceX = hisAbsPos.x - closestX;
				float distanceY = hisAbsPos.y - closestY;
				// If the distance is less than the circle's radius, an intersection occurs
				float distanceSquared = distanceX*distanceX + distanceY*distanceY;
				return distanceSquared < c.getRadius()*c.getRadius();
			}
			else if( p.whoAmI() == Polygon.rectangle ) {
				// Rectangle to rectangle
				Rectangle r = (Rectangle) p;
				return Math.abs(hisAbsPos.x - myAbsPos.x) < (size.x + r.size.x) / 2 && Math.abs(hisAbsPos.y - myAbsPos.y) < (size.y + r.size.y) / 2;
			}
			else {
				return false;
			}
		}
	}
}
