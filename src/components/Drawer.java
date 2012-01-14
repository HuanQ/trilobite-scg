package components;

import geometry.Polygon;
import geometry.Vec2;
import geometry.Vec3;

import managers.Component;
import managers.Constant;
import managers.Screen;

import static org.lwjgl.opengl.GL11.*;

public class Drawer {
	private final int											me;
	private Vec3												color;
	private boolean												visible = true;
	
	public Drawer( int m) {
		me = m;
		color = Vec3.white;
	}
	
	public Drawer( int m, final Vec3 c) {
		me = m;
		color = c;
	}
	
	public final void setVisible( boolean b ) {
		visible = b;
	}
	
	public final void setColor( Vec3 col ) {
		color = col;
	}
	
	public final void Render() {
		Shape myShape = Component.shape.get(me);
		float radius = myShape == null ? 0 : myShape.getRadius();
	
		if( visible ) {
			if(myShape == null || myShape.getRadius() == 0) {
				if(Screen.inScreen( Component.placement.get(me).position, radius )) {
					Vec2 screenPos = Screen.coords(Component.placement.get(me).position);
					
					// Default placeholder
					//TODO: tambe cal dibuixar el sprite si no te shape, pero no se on guardar-lo
					float size = Constant.getFloat("Render_DefaultUnscaledSize");
					float layer = Constant.getFloat("Render_DefaultLayer");
					glDisable(GL_TEXTURE_2D);
				    glColor4f(color.x*Component.fader.color.x, color.y*Component.fader.color.y, color.z*Component.fader.color.z, 1);
				    glBegin(GL_LINE_LOOP);
			        glVertex3f(screenPos.x - size/2, screenPos.y - size/2, layer);
					glVertex3f(screenPos.x + size/2, screenPos.y - size/2, layer);
					glVertex3f(screenPos.x + size/2, screenPos.y + size/2, layer);
					glVertex3f(screenPos.x - size/2, screenPos.y + size/2, layer);
				    glEnd();
				}
			}
			else {
				// Each shape draws itself
				for(Polygon p : myShape.polygons) {
					p.Draw(Component.placement.get(me).position, color, Component.placement.get(me).angle);
				}
			}
		}
	}

}
