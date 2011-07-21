package components;

import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Screen;
import managers.Timer;


public class Bullet {
	private final Vec2													direction;
	private final int													me;

	public Bullet( int m, final Vec2 v ) {
		me = m;
		direction = v;
	}
	
	public void Update() {
		Component.placement.get(me).addPosition( direction.getScaled( Timer.getDelta() ) );
		
		// Selfkill at a certain distance from the screen limit
		float killDist = Constant.getFloat("Bullet_KillDistance");
		if(Component.shape.get(me) != null) {
			killDist += Component.shape.get(me).getRadius();
		}
		if( !Screen.inScreen( Component.placement.get(me).getPosition(), killDist ) ) {
			Component.deadObjects.add(me);
		}
	}
}
