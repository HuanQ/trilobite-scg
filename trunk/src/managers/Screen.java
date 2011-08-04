package managers;

import java.util.HashMap;
import java.util.Map;

import geometry.Vec2;

import org.lwjgl.opengl.Display;

import data.ScreenZone;

public class Screen {
	static private Vec2									screenSize = new Vec2();
	static private Map<String, ScreenZone>				zones = new HashMap<String, ScreenZone>();
	static public Vec2									up = new Vec2();
	
	static public final void Init() {
		zones.clear();
		screenSize.set( Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		up.zero();
		
		if( screenSize.y > screenSize.x ) {
			// Not supported
		}
		else {
			// Create different screen zones
			float totalExtraSpace = screenSize.x - screenSize.y;

			// Extra space goes to the left untill it is half of it's height
			float spaceMiddle = screenSize.y;
			float spaceLeft = Math.min(totalExtraSpace, screenSize.y/2);
			float spaceRight = totalExtraSpace - spaceLeft;
			
			zones.put( "full", new ScreenZone(Vec2.zero, Vec2.ones) );
			zones.put( "left", new ScreenZone(Vec2.zero, new Vec2((float) spaceLeft/screenSize.x, 1)) );
			zones.put( "game", new ScreenZone(new Vec2((float) spaceLeft / screenSize.x, 0), new Vec2(spaceMiddle/screenSize.x, 1)) );
			zones.put( "right", new ScreenZone(new Vec2((float) (spaceLeft+spaceMiddle) / screenSize.x, 0), new Vec2(spaceRight/screenSize.x, 1)) );
			
		}
	}
	
	static public final Vec2 decoords( final Vec2 p ) {
		// Proportional to full screen
		return new Vec2( p.x/screenSize.x, p.y/screenSize.y);
	}
	
	static public final void deposition( final String zone, final Vec2 pos ) {
		ScreenZone z = zones.get(zone);
		pos.x = (pos.x - z.offset.x) / z.size.x;
		pos.y = (pos.y - z.offset.y) / z.size.y;
	}
	
	static public final void reposition( final String zone, final Vec2 pos ) {
		ScreenZone z = zones.get(zone);
		pos.x = z.offset.x + pos.x * z.size.x;
		pos.y = z.offset.y + pos.y * z.size.y;
	}
	
	static public final float descale( final String zone, float radius ) {
		ScreenZone z = zones.get(zone);
		return radius / Math.min(z.size.x, z.size.y);
	}

	static public final void descale( final String zone, final Vec2 sz ) {
		ScreenZone z = zones.get(zone);
		sz.x = sz.x / Math.min(z.size.x, z.size.y);
		sz.y = sz.y / Math.min(z.size.x, z.size.y);
	}
	
	static public final float rescale( final String zone, float radius ) {
		ScreenZone z = zones.get(zone);
		return radius * Math.min(z.size.x, z.size.y);
	}
	
	static public final void rescale( final String zone, final Vec2 sz, boolean stretch ) {
		ScreenZone z = zones.get(zone);
		if( stretch ) {
			sz.x = sz.x * z.size.x;
			sz.y = sz.y * z.size.y;
		}
		else {
			sz.x = sz.x * Math.min(z.size.x, z.size.y);
			sz.y = sz.y * Math.min(z.size.x, z.size.y);
		}
	}
	
	static public final float coords( float radius ) {
		return radius * Math.min(screenSize.x, screenSize.y);
	}
	
	static public final Vec2 coords( final Vec2 pos ) {
		// Returns the screen position of a pair of coodinates
		Vec2 ret = new Vec2(pos);
		ret.sub(up);
		ret.mult(screenSize);
		return ret;
	}
	
	static public final Vec2 coords( final Vec2 sz, boolean stretch ) {
		// Returns the size of a rectangle streched or not
		if(stretch) {
			return new Vec2( sz.x*screenSize.x, sz.y*screenSize.y );
		}
		else {
			return new Vec2( sz.x*screenSize.x, sz.y*screenSize.x );
		}
	}
	
	static public final boolean inScreen( final Vec2 pos, float inc ) {
		Vec2 movedPos = new Vec2(pos);
		movedPos.sub(up);
		return movedPos.x <= 1+inc && movedPos.x >= -inc && movedPos.y <= 1+inc && movedPos.y >= -inc;
	}
}
