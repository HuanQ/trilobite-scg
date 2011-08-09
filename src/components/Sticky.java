package components;

import geometry.Vec2;

import managers.Component;
import managers.Screen;


public class Sticky {
	private final int											me;
	private final Vec2											lastUp = new Vec2();
	private final float											speed;

	{
		lastUp.set(Screen.up);
	}
	
	public Sticky( int m ) {
		me = m;
		speed = 1;
		
	}
	
	public Sticky( int m, float spd ) {
		me = m;
		speed = spd;
	}
	
	public final float getSpeed() {
		return speed;
	}
	
	public final void Update() {
		Vec2 movUp = new Vec2(Screen.up);
		movUp.sub(lastUp);
		movUp.scale(speed);
		Component.placement.get(me).position.add(movUp);
		lastUp.set(Screen.up);
	}
}
