package components;

import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Level;


public class Gun {
	private final int											me;
	private final Vec2											gunPoint;
	
	public Gun( int m, final Vec2 p ) {
		me = m;
		gunPoint = p;
	}
	
	public final void Shoot() {
		Vec2 exitPoint = new Vec2(Component.placement.get(me).position);
		exitPoint.add(gunPoint);

		//TODO: S'ha de grabar la direcció perquè cada actor tindrà direccions diferents
		// Record
		Record rec = Component.record.get(me);
		if( rec != null) {
			rec.addEvent( Record.gunShot, null );
		}
		
		// Create a bullet
		Vec2 bulletDirection = new Vec2(0, -Constant.getFloat("Ship_BulletSpeed"));
		Level.AddBullet(bulletDirection, exitPoint, Killable.playerTeam);
	}

}
