package geometry;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

import geometry.Vec2;
import geometry.Vec3;

import managers.Component;
import managers.Constant;
import managers.Screen;


public class Circle extends Polygon {
	private float														radius;

	public Circle( float r, final Vec3 off) {
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
	
	public void multSize( float m) {
		radius *= m;
		sqradius = radius*radius;
	}
	
	public int whoAmI() {
		return Polygon.circle;
	}
	
	public void draw( final Vec2 pos, final Vec3 defColor ) {
		float myRadius = Screen.rescale( radius );
		Vec3 finalColor = new Vec3(defColor);
		finalColor.mult(color);
		finalColor.mult(Component.fader.getColor());
		glColor4f(finalColor.x, finalColor.y, finalColor.z, 1.f);
		
		if(texture == null) {
		    // Draw placeholder
			glDisable(GL_TEXTURE_2D);
			
			glBegin(GL_TRIANGLE_FAN);
			glVertex3f(pos.x, pos.y, offset.z);
			int sect = (int) Constant.getFloat("Render_CircleSections");
			for(int i = 0; i <= sect; ++i) {
				float slice = i * (float) (2 * Math.PI) / (float) sect; 
				glVertex3f( pos.x + (float) Math.sin(slice) * myRadius, pos.y + (float) Math.cos(slice) * myRadius, offset.z);
			}
			glEnd();
		}
		else {
			// Draw sprites
	    	glEnable(GL_TEXTURE_2D);
	    	texture.setWidth( (int) Screen.rescale(radius*2) );
			texture.setHeight( (int) Screen.rescale(radius*2) );
	    	texture.draw(pos.x, pos.y, offset.z + 0.5f);
	    }
	}
	
	public boolean Collides(final Vec2 myPos, final Polygon p, final Vec2 hisPos) {
		Vec2 myAbsPos = new Vec2(myPos.x + offset.x, myPos.y + offset.y);
		
		if(p == null) {
			return myAbsPos.distanceSquared(hisPos) < sqradius;
		}
		else {
			Vec2 hisAbsPos = new Vec2(hisPos.x + p.offset.x, hisPos.y + p.offset.y);
			
			if( p.whoAmI() == Polygon.circle ) {
				// Circle to circle
				return myAbsPos.distanceSquared(hisAbsPos) < Math.pow(radius + ((Circle) p).radius, 2);
			}
			else if( p.whoAmI() == Polygon.rectangle ) {
				// Circle to rectangle
				Rectangle r = (Rectangle) p;
				// Find the closest point to the circle within the rectangle
				float closestX = Math.min( Math.max( myAbsPos.x, hisAbsPos.x - r.getSize().x/2 ), hisAbsPos.x + r.getSize().x/2 );
				float closestY = Math.min( Math.max( myAbsPos.y, hisAbsPos.y - r.getSize().y/2 ), hisAbsPos.y + r.getSize().y/2 );
				// Calculate the distance between the circle's center and this closest point
				float distanceX = myAbsPos.x - closestX;
				float distanceY = myAbsPos.y - closestY;
				// If the distance is less than the circle's radius, an intersection occurs
				float distanceSquared = distanceX*distanceX + distanceY*distanceY;
				return distanceSquared < radius*radius;
			}
			else {
				return false;
			}
		}
	}
}
