package geometry;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import managers.Constant;
import managers.Screen;


public class Circle extends Polygon {
	private float														radius;

	public Circle(final float r, final Vector3f off) {
		super(off);
		radius = r;
		sqradius = r*r;
	}
	
	public Circle(final Circle c) {
		super((Polygon) c);
		radius = c.getRadius();
	}

	public float getRadius() {
		return radius;
	}
	
	public void multSize(final float m) {
		radius *= m;
		sqradius = radius*radius;
	}
	
	public int whoAmI() {
		return 0;
	}
	
	public void draw( final Vector2f pos, final Vector3f defColor ) {
		Vector2f myPos = Screen.reposition( new Vector2f(pos.x + offset.x, pos.y + offset.y) );
		float myRadius = Screen.rescale( radius );
			
		glDisable(GL_TEXTURE_2D);
		if( color == null )
			glColor4f(defColor.x, defColor.y, defColor.z, 1.f);
		else
			glColor4f(color.x * defColor.x, color.y * defColor.y, color.z * defColor.z, 1);
		
		glBegin(GL_TRIANGLE_FAN);
		glVertex3f(myPos.x, myPos.y, offset.z);
		int sect = (int) Constant.getFloat("Render_CircleSections");
		for(int i = 0; i <= sect; ++i) {
			float slice = i * (float) (2 * Math.PI) / (float) sect; 
			glVertex3f( myPos.x + (float) Math.sin(slice) * myRadius, myPos.y + (float) Math.cos(slice) * myRadius, offset.z);
		}
		glEnd();
	    
	    // Draw sprites
	    if(texture != null) {
	    	glEnable(GL_TEXTURE_2D);
	    	texture.draw(myPos.x, myPos.y, offset.z + 0.5f);
	    }
	}
	
	public boolean Collides(final Vector2f myPos, final Polygon p, final Vector2f hisPos) {
		if(p == null) {
			Vector2f myAbsPos = (Vector2f) myPos.clone();
			myAbsPos.add( new Vector2f(offset.x, offset.y));
			
			Vector2f sqdist = new Vector2f(myAbsPos.x - hisPos.x, myAbsPos.y - hisPos.y);
			if( sqdist.lengthSquared() < sqradius ) {
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
			
			Vector2f sqdist = new Vector2f(myAbsPos.x - hisAbsPos.x, myAbsPos.y - hisAbsPos.y);
			if( sqdist.lengthSquared() < Math.pow(radius + c.radius, 2) ) {
				return true;
			}
			return false;
		}
		else if( p.whoAmI() == 1 ) {
			Rectangle r = (Rectangle) p;
			Vector2f myAbsPos = (Vector2f) myPos.clone();
			myAbsPos.add( new Vector2f(offset.x, offset.y));
			Vector2f hisAbsPos = (Vector2f) hisPos.clone();
			hisAbsPos.add( new Vector2f(r.offset.x, r.offset.y));
			// Find the closest point to the circle within the rectangle
			float closestX = Math.min( Math.max( myAbsPos.x, hisAbsPos.x - r.getSize().x/2 ), hisAbsPos.x + r.getSize().x/2 );
			float closestY = Math.min( Math.max( myAbsPos.y, hisAbsPos.y - r.getSize().y/2 ), hisAbsPos.y + r.getSize().y/2 );
			// Calculate the distance between the circle's center and this closest point
			float distanceX = myAbsPos.x - closestX;
			float distanceY = myAbsPos.y - closestY;
			// If the distance is less than the circle's radius, an intersection occurs
			float distanceSquared = (float) (Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
			return distanceSquared < Math.pow(radius, 2);
		}
		else {
			return false;
		}
	}
}
