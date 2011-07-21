package managers;

import javax.vecmath.Vector2f;

import org.lwjgl.opengl.Display;

public class Screen {
	static Vector2f												extraSpace;
	static float												screenSize;
	
	static public void Init() {
		extraSpace = new Vector2f( Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		screenSize = Math.min( extraSpace.x, extraSpace.y );
		extraSpace.x = ( extraSpace.x - screenSize ) / 2;
		extraSpace.y = ( extraSpace.y - screenSize ) / 2;
	}
	
	static public Vector2f rescale( final Vector2f p ) {
		Vector2f newPoint = new Vector2f( screenSize * p.x, screenSize * p.y);
		return newPoint;
	}
	
	static public Vector2f reposition( final Vector2f p ) {
		Vector2f newPoint = new Vector2f( extraSpace.x + screenSize * p.x, extraSpace.y + screenSize * p.y);
		return newPoint;
	}
	
	static public float rescale( final float f ) {
		return f * screenSize;
	}
	
	static public boolean inScreen( final Vector2f pos, final float inc ) {
		return pos.x < (1+inc) && pos.x > -inc && pos.y < (1+inc) && pos.y > -inc;
	}
}
