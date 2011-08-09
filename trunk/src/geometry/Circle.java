package geometry;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import geometry.Vec2;
import geometry.Vec3;

import managers.Component;
import managers.Constant;
import managers.Screen;


public class Circle extends Polygon {
	private float														radius;

	public Circle( float r, final Vec2 off, float ly ) {
		super(off, ly);
		radius = r;
		sqradius = r*r;
	}
	
	public Circle(final Circle c) {
		super(c);
		radius = c.getRadius();
	}

	public final void setRadius( float r ) {
		radius = r;
	}
	
	public final float getRadius() {
		return radius;
	}
	
	public final void multSize( float m ) {
		radius *= m;
		sqradius = radius*radius;
	}
	
	public final int whoAmI() {
		return Polygon.circle;
	}
	
	public final void Draw( final Vec2 pos, final Vec3 defColor, final Angle rot ) {

		Vec2 realPos = new Vec2(pos.x+offset.x, pos.y+offset.y);
		if( Screen.inScreen(realPos, radius) ) {
			// Final position
			Vec2 screenPos = Screen.coords(realPos);
			// Final color
			Vec3 finalColor = new Vec3(defColor);
			finalColor.mult(color);
			finalColor.mult(Component.fader.color);
			glColor4f(finalColor.x, finalColor.y, finalColor.z, 1.f);

			if(texture == null) {
				float screenRadius = Screen.coords(radius);
			    // Draw placeholder
				glDisable(GL_TEXTURE_2D);
				
				glDisable(GL_TEXTURE_2D);
				glTranslatef(screenPos.x, screenPos.y, layer);
				glRotatef( (float) Math.toDegrees(rot.get()), 0, 0, 1 );

				glBegin(GL_TRIANGLE_FAN);
				glVertex2f(0, 0);
				int sect = (int) Constant.getFloat("Render_CircleSections");
				for(int i = 0; i <= sect; ++i) {
					float slice = i * (float) (2 * Math.PI) / (float) sect; 
					glVertex2f( (float) Math.sin(slice) * screenRadius, (float) Math.cos(slice) * screenRadius);
				}
				glEnd();
				
				glLoadIdentity();
			}
			else {
				//TODO: Dibuixar com a rectangle
				//Vec2 screenSize = Screen.coords( new Vec2(radius, radius), false );
				float screenRadius = Screen.coords(radius);
				// Draw sprites
		    	glEnable(GL_TEXTURE_2D);
		    	texture.setWidth( (int) screenRadius*2 );
				texture.setHeight( (int) screenRadius*2 );
		    	texture.Draw(screenPos.x, screenPos.y, layer, rot.get());
		    }
		}
	}
	
	public final boolean Collides(final Vec2 myPos, final Polygon p, final Vec2 hisPos) {
		Vec2 myAbsPos = new Vec2( myPos.x+offset.x, myPos.y+offset.y);
		if(p == null) {
			return myAbsPos.distanceSquared(hisPos) < sqradius;
		}
		else {
			Vec2 hisAbsPos = new Vec2( hisPos.x+p.offset.x, hisPos.y+p.offset.y);
			if( p.whoAmI() == Polygon.circle ) {
				// Circle to circle
				return myAbsPos.distanceSquared(hisAbsPos) < Math.pow(radius + ((Circle) p).radius, 2);
			}
			else if( p.whoAmI() == Polygon.rectangle ) {
				// Circle to rectangle
				Rectangle r = (Rectangle) p;
				// Find the closest point to the circle within the rectangle
				float closestX = Math.min( Math.max( myAbsPos.x, hisAbsPos.x - r.size.x/2 ), hisAbsPos.x + r.size.x/2 );
				float closestY = Math.min( Math.max( myAbsPos.y, hisAbsPos.y - r.size.y/2 ), hisAbsPos.y + r.size.y/2 );
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
	
	public final void WriteXML( final Document doc, final Element root ) {
		Element circle = doc.createElement("Circle");
		Vec2 myOff = new Vec2(offset);
		Screen.descale("game", myOff);
		circle.setAttribute( "x", Float.toString(myOff.x) );
		circle.setAttribute( "y", Float.toString(myOff.y) );
		circle.setAttribute( "z", Float.toString(layer) );
		float myRad = Screen.descale("game", radius);
		circle.setAttribute( "radius", Float.toString(myRad) );
		writeSubShape( doc, circle );
		root.appendChild(circle);
	}
}
