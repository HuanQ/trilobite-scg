package components;

import geometry.Polygon;

import java.util.Iterator;

import geometry.Vec2;
import geometry.Vec3;

import managers.Component;
import managers.Constant;
import managers.Screen;

import static org.lwjgl.opengl.GL11.*;


public class Drawer {
	private final Vec3											color;
	private final int											me;
	private boolean												visible;
	
	public Drawer( int m) {
		me = m;
		color = Vec3.white;
		visible = true;
	}
	
	public Drawer( int m, final Vec3 c) {
		me = m;
		color = c;
		visible = true;
	}
	
	public void setVisible( boolean b ) {
		visible = b;
	}
	
	public void Render() {
		Shape myShape = Component.shape.get(me);
		float radius = myShape == null ? 0 : myShape.getRadius();
		//System.out.println(Component.shape.get(me).getText());		
		if( Screen.inScreen( Component.placement.get(me).getPosition(), radius ) && visible ) {
			Placement place = Component.placement.get(me); 
			if(myShape == null || myShape.getRadius() == 0) {
				Vec2 myPos = new Vec2( place.getPosition() );
				Screen.reposition(myPos, place.getScreenSide());
				
				// Default placeholder
				//TODO: tambe cal dibuixar el sprite si no te shape, pero no se on guardar-lo
				float size = Constant.getFloat("Render_DefaultUnscaledSize");
				float layer = Constant.getFloat("Render_DefaultLayer");
				glDisable(GL_TEXTURE_2D);
			    glColor4f(color.x, color.y, color.z, 1);
			    glBegin(GL_LINE_LOOP);
		        glVertex3f(myPos.x - size/2, myPos.y - size/2, layer);
				glVertex3f(myPos.x + size/2, myPos.y - size/2, layer);
				glVertex3f(myPos.x + size/2, myPos.y + size/2, layer);
				glVertex3f(myPos.x - size/2, myPos.y + size/2, layer);
			    glEnd();
			}
			else {
				// Each shape draws itself
				for (Iterator<Polygon> iter = myShape.getPolygons().iterator(); iter.hasNext();) {
					Polygon next = iter.next();
					Vec2 myPos = new Vec2(place.getPosition().x + next.getOffset().x, place.getPosition().y + next.getOffset().y);
					Screen.reposition( myPos, place.getScreenSide() );
					next.draw( myPos, color, place.getScreenSide() );
				}
			}
		}
	}
}
