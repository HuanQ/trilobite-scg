package geometry;


import geometry.Vec2;
import geometry.Vec3;

import managers.Component;
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
		super(r);
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

	public void draw( final Vec2 pos, final Vec3 defColor, int side ) {
		Vec2 mySize = new Vec2(size); 
		Screen.rescale( mySize, side );
		Vec3 finalColor = new Vec3(defColor);
		finalColor.mult(color);
		finalColor.mult(Component.fader.getColor());
		glColor4f(finalColor.x, finalColor.y, finalColor.z, 1.f);
		
		if(texture == null) {
		    // Draw placeholder
			glDisable(GL_TEXTURE_2D);

			glBegin(GL_QUADS);
		    glVertex3f(pos.x - mySize.x/2, pos.y - mySize.y/2, offset.z);
			glVertex3f(pos.x + mySize.x/2, pos.y - mySize.y/2, offset.z);
			glVertex3f(pos.x + mySize.x/2, pos.y + mySize.y/2, offset.z);
			glVertex3f(pos.x - mySize.x/2, pos.y + mySize.y/2, offset.z);
		    glEnd();
		}
		else {
			// Draw sprites
	    	glEnable(GL_TEXTURE_2D);
	    	texture.setWidth( (int) mySize.x );
			texture.setHeight( (int) mySize.y );
	    	texture.draw(pos.x, pos.y, offset.z + 0.5f);
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
