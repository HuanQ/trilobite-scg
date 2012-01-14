package components;

import geometry.Vec2;

import managers.Component;
import managers.Clock;
import managers.Screen;


public class Bullet {
	private final int													me;
	private final Vec2													direction;

	public Bullet( int m, final Vec2 v ) {
		me = m;
		direction = v;
		Screen.rescale("game", v, false);
	}
	
	//TODO: Bullet strength per a cada ship
	public final void Update() {
		Vec2 mov = new Vec2(direction);
		mov.scale(Clock.getDelta(Clock.game));
		Component.placement.get(me).position.add( mov );
	}
}
