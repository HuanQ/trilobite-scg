package managers;

import geometry.Vec2;

import org.lwjgl.opengl.Display;

public class Screen {
	static Vec2													extraSpace;
	static float												screenSize;
	
	static public void Init() {
		extraSpace = new Vec2( Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		screenSize = Math.min( extraSpace.x, extraSpace.y );
		extraSpace.x = ( extraSpace.x - screenSize ) / 2;
		extraSpace.y = ( extraSpace.y - screenSize ) / 2;
	}
	
	static public Vec2 descale( final Vec2 p ) {
		Vec2 newPoint = new Vec2( p.x/screenSize, p.y/screenSize);
		return newPoint;
	}
	
	static public Vec2 rescale( final Vec2 p ) {
		Vec2 newPoint = new Vec2( screenSize * p.x, screenSize * p.y);
		return newPoint;
	}
	
	static public Vec2 reposition( final Vec2 p ) {
		Vec2 newPoint = new Vec2( extraSpace.x + screenSize * p.x, extraSpace.y + screenSize * p.y);
		return newPoint;
	}
	
	static public float rescale( final float f ) {
		return f * screenSize;
	}
	
	static public boolean inScreen( final Vec2 pos, final float inc ) {
		return pos.x < 1+inc && pos.x > -inc && pos.y < 1+inc && pos.y > -inc;
	}
	
	static public boolean inScreenU( final Vec2 pos, final float inc ) {
		return pos.x < 1+inc && pos.x > -inc && pos.y < 1+inc;
	}
}
