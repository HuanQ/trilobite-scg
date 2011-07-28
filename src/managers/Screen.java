package managers;

import java.util.EmptyStackException;

import geometry.Vec2;

import org.lwjgl.opengl.Display;

import components.Placement;

public class Screen {
	static float												extraSpaceLeft;
	static float												extraSpaceRight;
	static float												gameScreenSize;
	static Vec2													screenSize;;
	
	static public void Init() {
		screenSize = new Vec2( Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		if ( screenSize.y > screenSize.x ) {
			// Not supported
			throw new EmptyStackException();
		}
		
		gameScreenSize = screenSize.y;
		float totalExtraSpace = screenSize.x - gameScreenSize;
		
		// Extra space goes to the left untill it is half of it's height
		extraSpaceLeft = Math.min(totalExtraSpace, gameScreenSize/2);
		extraSpaceRight = totalExtraSpace - extraSpaceLeft;
	}
	
	static public Vec2 descale( final Vec2 p ) {
		// Proportional to full screen
		return new Vec2( p.x/screenSize.x, p.y/gameScreenSize);
	}
	
	//TODO: No retornar mai classes, transformar-les. Especialment en les matematiques de vectors
	static public void reposition( final Vec2 pos, int side ) {
		switch(side) {
		case Placement.fullScreen:
			pos.x = pos.x * screenSize.x;
			pos.y = pos.y * gameScreenSize;
			break;
			
		case Placement.gameSide:
			pos.x = pos.x * gameScreenSize + extraSpaceLeft;
			pos.y = pos.y * gameScreenSize;
			break;
			
		case Placement.leftSide:
		case Placement.leftSideFull:
			pos.x = pos.x * extraSpaceLeft;
			pos.y = pos.y * gameScreenSize;
			break;
			
		case Placement.rightSide:
		case Placement.rightSideFull:
			pos.x = pos.x * extraSpaceRight + extraSpaceLeft + gameScreenSize;
			pos.y = pos.y * gameScreenSize;
			break;
		}
	}

	static public float rescale( float f, int side ) {
		//TODO: Alinear be tots els switchos
		switch(side) {
		case Placement.fullScreen:
		case Placement.gameSide:
			return f * gameScreenSize;
			
		case Placement.leftSide:
		case Placement.leftSideFull:
			return f * extraSpaceLeft;
		
		case Placement.rightSide:
		case Placement.rightSideFull:
			return f * extraSpaceRight;
		default:
			return 0;
		}
	}
	
	static public void rescale( final Vec2 v, int side ) {
		switch(side){
		case Placement.fullScreen:
		case Placement.gameSide:
			v.scale(gameScreenSize);
			break;
			
		case Placement.leftSide:
			v.scale(extraSpaceLeft);
			break;
		
		case Placement.leftSideFull:
			v.x *= extraSpaceLeft;
			v.y *= gameScreenSize;
			break;
			
		case Placement.rightSide:
			v.scale(extraSpaceRight);
			break;
			
		case Placement.rightSideFull:
			v.x *= extraSpaceRight;
			v.y *= gameScreenSize;
			break;
		}
	}
	
	static public boolean inScreen( final Vec2 pos, float inc ) {
		return pos.x < 1+inc && pos.x > -inc && pos.y < 1+inc && pos.y > -inc;
	}
	
	static public boolean inScreenU( final Vec2 pos, float inc ) {
		return pos.x < 1+inc && pos.x > -inc && pos.y < 1+inc;
	}
}
