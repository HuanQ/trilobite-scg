package data;

import geometry.Vec2;

public class ScreenZone {
	public Vec2													offset;
	public Vec2													size;

	public ScreenZone( final Vec2 os, final Vec2 s ) {
		offset = os;
		size = s;
	}
}
