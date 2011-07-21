package geometry;


import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import managers.Screen;

import static org.lwjgl.opengl.GL11.*;

public class Rectangle extends Polygon {
	private final Vector2f											size;
	
	public Rectangle( final Vector2f s, final Vector3f off ) {
		super(off);
		size = s;
		sqradius = (float) Math.pow(size.length() / 2,2);
	}
	
	public Rectangle( final Rectangle r ) {
		super((Polygon) r);
		size = new Vector2f();
		size.x = r.size.x;
		size.y = r.size.y;
	}
	
	public Vector2f getSize() {
		return size;
	}
	
	public int whoAmI() {
		return 1;
	}
	
	public void multSize( final float m ) {
		size.x *= m;
		size.y *= m;
		sqradius = (float) Math.pow(size.length() / 2,2);
	}

	public void draw( final Vector2f pos, final Vector3f defColor ) {
		Vector2f myPos = Screen.reposition( new Vector2f(pos.x + offset.x, pos.y + offset.y) );
		Vector2f mySize = Screen.rescale( new Vector2f(size.x, size.y) );

		glDisable(GL_TEXTURE_2D);
		if( color == null )
			glColor4f(defColor.x, defColor.y, defColor.z, 1.f);
		else
			glColor4f(color.x * defColor.x, color.y * defColor.y, color.z * defColor.z, 1);
		glBegin(GL_QUADS);
	    glVertex3f(myPos.x - mySize.x/2, myPos.y - mySize.y/2, offset.z);
		glVertex3f(myPos.x + mySize.x/2, myPos.y - mySize.y/2, offset.z);
		glVertex3f(myPos.x + mySize.x/2, myPos.y + mySize.y/2, offset.z);
		glVertex3f(myPos.x - mySize.x/2, myPos.y + mySize.y/2, offset.z);
	    glEnd();
	    
	    // Draw sprites
	    if(texture != null) {
	    	glEnable(GL_TEXTURE_2D);
	    	texture.draw(myPos.x, myPos.y, offset.z + 0.5f);
	    }
	}
    
	public boolean Collides( final Vector2f myPos, final Polygon p, final Vector2f hisPos ) {
		if(p == null) {
			Vector2f myAbsPos = (Vector2f) myPos.clone();
			myAbsPos.add( new Vector2f(offset.x, offset.y));
			
			if( Math.abs(hisPos.x - myAbsPos.x) < size.x / 2 && Math.abs(hisPos.y - myAbsPos.y) < size.y / 2 ) {
				return true;
			}
			return false;
		}
		else if( p.whoAmI() == 0 ) {
			Circle c = (Circle) p;
			Vector2f myAbsPos = (Vector2f) myPos.clone();
			myAbsPos.add( new Vector2f(offset.x, offset.y));
			Vector2f hisAbsPos = (Vector2f) hisPos.clone();
			hisAbsPos.add( new Vector2f(c.offset.x, c.offset.y));

			// Find the closest point to the circle within the rectangle
			float closestX = Math.min( Math.max( hisAbsPos.x, myAbsPos.x - size.x/2 ), myAbsPos.x + size.x/2 );
			float closestY = Math.min( Math.max( hisAbsPos.y, myAbsPos.y - size.y/2 ), myAbsPos.y + size.y/2 );
			// Calculate the distance between the circle's center and this closest point
			float distanceX = hisAbsPos.x - closestX;
			float distanceY = hisAbsPos.y - closestY;
			// If the distance is less than the circle's radius, an intersection occurs
			float distanceSquared = (float) (Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
			return distanceSquared < Math.pow(c.getRadius(), 2);
		}
		else if( p.whoAmI() == 1 ) {
			Rectangle r = (Rectangle) p;
			Vector2f myAbsPos = (Vector2f) myPos.clone();
			myAbsPos.add( new Vector2f(offset.x, offset.y));
			Vector2f hisAbsPos = (Vector2f) hisPos.clone();
			hisAbsPos.add( new Vector2f(r.offset.x, r.offset.y));
			
			if( Math.abs(hisAbsPos.x - myAbsPos.x) < (size.x + r.size.x) / 2 && Math.abs(hisAbsPos.y - myAbsPos.y) < (size.y + r.size.y) / 2 ) {
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
}
