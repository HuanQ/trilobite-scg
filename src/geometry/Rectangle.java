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
	
	public final void Scale( float m ) {
		size.scale(m);
		sqradius = (float) Math.pow(size.length()/2, 2);
		offset.scale(m);
	}

	public final void Draw( final Vec2 pos, final Vec3 defColor, final Angle rot ) {
		//TODO Draw amb rounded edges
		Vec2 realPos = new Vec2(pos);
		Vec2 offsetSize = new Vec2(offset.x, offset.y);
		realPos.add(offsetSize);
		if( Screen.inScreen(realPos, (float) Math.sqrt(sqradius)) ) {
			// Final position
			Vec2 screenPos = Screen.coords(pos);
			//TODO Verificar aquest stretch
			Vec2 screenOffset = Screen.coords(offsetSize, stretch);
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
				glTranslatef(screenOffset.x, screenOffset.y, 0);
				
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
		    	texture.Draw(screenPos, layer, screenOffset, rot.get());
		    }
		}
	}

	//TODO Colisions amb rotacio a shape contra shape (R->R, R->C, C->R, C->C)
	public final boolean Collides( final Vec2 myPos, final Polygon p, final Vec2 hisPos, final Angle myRot, final Angle hisRot ) {
		Vec2 myAbsPos = new Vec2(myPos);
		myAbsPos.add(offset);

		if(p == null) {
			if(myRot.isZero()) {
				return Math.abs(hisPos.x - myAbsPos.x) < size.x / 2 && Math.abs(hisPos.y - myAbsPos.y) < size.y / 2;
			}
			else {
				return doFourPointsPointCollision(myPos, hisPos, myRot);
			}
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
	
	private final boolean doFourPointsPointCollision( final Vec2 myPos, final Vec2 hisPos, final Angle myRot) {
		//TODO Optimitzar els calculs
		Vec2 rotX1 = new Vec2(offset);
		rotX1.x += size.x/2;
		rotX1.y += size.y/2;
		rotX1 = rotate(rotX1, myRot);
		rotX1.add(myPos);
		
		Vec2 rotX2 = new Vec2(offset);
		rotX2.x += size.x/2;
		rotX2.y -= size.y/2;
		rotX2 = rotate(rotX2, myRot);
		rotX2.add(myPos);

		Vec2 rotY2 = new Vec2(offset);
		rotY2.x -= size.x/2;
		rotY2.y += size.y/2;
		rotY2 = rotate(rotY2, myRot);
		rotY2.add(myPos);
		
		Vec2 v1 = new Vec2(rotX2);
		v1.sub(rotX1);
		Vec2 v2 = new Vec2(rotY2);
		v2.sub(rotX1);
		Vec2 v = new Vec2(hisPos);
		v.sub(rotX1);
		
		return 0 <= v.dot(v1) && v.dot(v1) <= v1.dot(v1) && 0 <= v.dot(v2) && v.dot(v2) <= v2.dot(v2); 
	}
	
	private final Vec2 rotate( Vec2 vec, final Angle rot ) {
		
		// Do the calculations in screen coordinates
		Vec2 aux = new Vec2(vec);
		aux = Screen.coords(aux, true);
		
		float len = aux.length();
		Angle newRot = new Angle();
		newRot.set(aux);
		newRot.add(rot.get());
		
		Vec2 ret = newRot.getDirection();
		ret.scale(len);
		
		ret = Screen.decoords(ret);
		return ret;
	}
	
}
