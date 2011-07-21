package components;

import geometry.Polygon;

import java.util.Iterator;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import managers.Component;
import managers.Constant;
import managers.Screen;

import static org.lwjgl.opengl.GL11.*;



public class Drawer {
	private final Vector3f										color;
	private final int											me;
	
	public Drawer(final int m) {
		me = m;
		color = new Vector3f(1, 1, 1);
	}
	
	public Drawer(final int m, final Vector3f c) {
		me = m;
		color = c;
	}
	
	public void Render() {
		Shape myShape = Component.shape.get(me);

		if(myShape == null || myShape.getRadius() == 0) {
			// Default placeholder
			Vector2f myPos = Screen.reposition( Component.placement.get(me).getPosition() );
			float size = Screen.rescale( Constant.getFloat("Render_DefaultSize") );
			float layer = Constant.getFloat("Render_DefaultLayer");
			
			//TODO: tambe cal dibuixar el sprite si no te shape, pero no se on guardar-lo
			
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
			
			for (Iterator<Polygon> iter = myShape.getPolygons().iterator(); iter.hasNext();) {
				Polygon next = (Polygon) iter.next();
				next.draw(Component.placement.get(me).getPosition(), color);
			}
		}
	}
}
