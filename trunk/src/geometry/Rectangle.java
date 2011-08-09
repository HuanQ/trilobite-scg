package geometry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import geometry.Vec2;
import geometry.Vec3;

import managers.Component;
import managers.Screen;

import static org.lwjgl.opengl.GL11.*;

public class Rectangle extends Polygon {
	public final Vec2											size;
	public final boolean										stretch;
	
	public Rectangle( final Vec2 sz, final Vec2 off, float ly, boolean str ) {
		super(off, ly);
		size = sz;
		stretch = str;
		sqradius = (float) Math.pow(size.length()/2, 2);
	}
	
	public Rectangle( final Rectangle r ) {
		super(r);
		size = new Vec2( r.size );
		stretch = r.stretch;
	}

	public final int whoAmI() {
		return Polygon.rectangle;
	}
	
	public final void multSize( float m ) {
		size.scale(m);
		sqradius = (float) Math.pow(size.length()/2, 2);
	}

	public final void Draw( final Vec2 pos, final Vec3 defColor, final Angle rot ) {
		//TODO: Draw amb rounded edges
		Vec2 realPos = new Vec2(pos.x+offset.x, pos.y+offset.y);
		if( Screen.inScreen(realPos, (float) Math.sqrt(sqradius)) ) {
			// Final position
			Vec2 screenPos = Screen.coords(realPos);
			Vec2 screenSize = Screen.coords( size, stretch );
			// Final color
			Vec3 finalColor = new Vec3(defColor);
			finalColor.mult(color);
			finalColor.mult(Component.fader.color);
			glColor4f(finalColor.x, finalColor.y, finalColor.z, 1.f);
			
			if(texture == null) {
			    // Draw placeholder
				glDisable(GL_TEXTURE_2D);
				glTranslatef(screenPos.x, screenPos.y, layer);
				glRotatef( (float) Math.toDegrees(rot.get()), 0, 0, 1 );
				
				glBegin(GL_QUADS);
			    glVertex2f(- screenSize.x/2, - screenSize.y/2);
				glVertex2f(screenSize.x/2, - screenSize.y/2);
				glVertex2f(screenSize.x/2, screenSize.y/2);
				glVertex2f(- screenSize.x/2, screenSize.y/2);
			    glEnd();
			    
			    glLoadIdentity();
			}
			else {
				// Draw sprites
		    	glEnable(GL_TEXTURE_2D);
		    	texture.setWidth( (int) screenSize.x );
				texture.setHeight( (int) screenSize.y );
		    	texture.Draw(screenPos.x, screenPos.y, layer, rot.get());
		    }
		}
	}
    
	public final boolean Collides( final Vec2 myPos, final Polygon p, final Vec2 hisPos ) {
		Vec2 myAbsPos = new Vec2(myPos);
		myAbsPos.add(offset);

		if(p == null) {
			return Math.abs(hisPos.x - myAbsPos.x) < size.x / 2 && Math.abs(hisPos.y - myAbsPos.y) < size.y / 2;
		}
		else {
			Vec2 hisAbsPos = new Vec2(hisPos);
			hisAbsPos.add(p.offset);
			
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
	
	public final void WriteXML( final Document doc, final Element root ) {
		Element rectangle = doc.createElement("Rectangle");
		Vec2 myOff = new Vec2(offset);
		Vec2 mySz = new Vec2(size);
		Screen.descale("game", myOff);
		Screen.descale("game", mySz);
		rectangle.setAttribute( "x", Float.toString(myOff.x) );
		rectangle.setAttribute( "y", Float.toString(myOff.y) );
		rectangle.setAttribute( "z", Float.toString(layer) );
		rectangle.setAttribute( "sizex", Float.toString(mySz.x) );
		rectangle.setAttribute( "sizey", Float.toString(mySz.y) );
		writeSubShape( doc, rectangle );
		root.appendChild(rectangle);
	}
}
