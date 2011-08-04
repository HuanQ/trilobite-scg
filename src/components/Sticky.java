package components;

import geometry.Vec2;

import managers.Component;
import managers.Screen;


public class Sticky {
	private final int											me;
	private final Vec2											lastUp = new Vec2();

	public Sticky( int m ) {
		me = m;
		lastUp.set(Screen.up);
	}
	
	public final void Update() {
		Vec2 movUp = new Vec2(Screen.up);
		movUp.sub(lastUp);
		Component.placement.get(me).position.add(movUp);
		lastUp.set(Screen.up);
	}
}
