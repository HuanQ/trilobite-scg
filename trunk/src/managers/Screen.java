package managers;

import geometry.Vec2;

import org.lwjgl.opengl.Display;

public class Screen {
	static Vec2													extraSpace;
	static float												gameScreenSize;
	static Vec2													screenSize;;
	
	static public void Init() {
		screenSize = new Vec2( Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		gameScreenSize = Math.min( screenSize.x, screenSize.y );
		extraSpace = new Vec2( screenSize.x - gameScreenSize, screenSize.y - gameScreenSize );
		extraSpace.scale(0.5f);
	}
	
	static public Vec2 descale( final Vec2 p ) {
		return new Vec2( p.x/gameScreenSize, p.y/gameScreenSize);
	}
	
	static public Vec2 screenRescale( final Vec2 p ) {
		return new Vec2( screenSize.x * p.x, screenSize.y * p.y);
	}

	static public Vec2 gameRescale( final Vec2 p ) {
		return new Vec2( gameScreenSize * p.x, gameScreenSize * p.y);
	}
	
	static public Vec2 gameReposition( final Vec2 p ) {
		Vec2 newPoint = new Vec2( extraSpace.x + gameScreenSize * p.x, extraSpace.y + gameScreenSize * p.y);
		return newPoint;
	}
	
	static public float rescale( final float f ) {
		return f * gameScreenSize;
	}
	
	static public boolean inScreen( final Vec2 pos, final float inc ) {
		return pos.x < 1+inc && pos.x > -inc && pos.y < 1+inc && pos.y > -inc;
	}
	
	static public boolean inScreenU( final Vec2 pos, final float inc ) {
		return pos.x < 1+inc && pos.x > -inc && pos.y < 1+inc;
	}
}
