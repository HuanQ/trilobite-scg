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
	
	public Drawer( int m) {
		me = m;
		color = new Vec3(1.f, 1.f, 1.f);
	}
	
	public Drawer( int m, final Vec3 c) {
		me = m;
		color = c;
	}
	
	public void Render() {
		Shape myShape = Component.shape.get(me);
		float radius = myShape == null ? 0 : myShape.getRadius();
		
		if( Screen.inScreen( Component.placement.get(me).getPosition(), radius ) ) {
			if(myShape == null || myShape.getRadius() == 0) {
				Vec2 myPos =  Screen.reposition( new Vec2( Component.placement.get(me).getPosition() ) );
				// Default placeholder
				//TODO: tambe cal dibuixar el sprite si no te shape, pero no se on guardar-lo
				float size = Screen.rescale( Constant.getFloat("Render_DefaultSize") );
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
				// Every shape draws itself
				for (Iterator<Polygon> iter = myShape.getPolygons().iterator(); iter.hasNext();) {
					Polygon next = (Polygon) iter.next();
					next.draw( Component.placement.get(me).getPosition(), color );
				}
			}
		}
	}
}
