package managers;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import geometry.Vec2;

public class Debug {
	
	static public final void draw( final Vec2 pos ) {
		Vec2 screenPos = Screen.coords(pos);
		
		// Default placeholder
		float size = Constant.getFloat("Render_DefaultUnscaledSize");
		float layer = Constant.getFloat("Render_DefaultLayer");
		glDisable(GL_TEXTURE_2D);
	    glColor4f(1, 1, 1, 1);
	    glBegin(GL_LINE_LOOP);
        glVertex3f(screenPos.x - size/2, screenPos.y - size/2, layer);
		glVertex3f(screenPos.x + size/2, screenPos.y - size/2, layer);
		glVertex3f(screenPos.x + size/2, screenPos.y + size/2, layer);
		glVertex3f(screenPos.x - size/2, screenPos.y + size/2, layer);
	    glEnd();
	}
	
	static public final void print( final String str ) {
		System.out.println(str);
	}
	
}
