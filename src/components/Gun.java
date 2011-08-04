package components;

import geometry.Vec2;

import managers.Component;
import managers.Constant;


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

		Integer id = Component.getID();

		//TODO: S'ha de grabar la direcció perquè cada actor tindrà direccions diferents
		// Record
		Record rec = Component.record.get(me);
		if( rec != null) {
			rec.event( Record.gunShot, null );
		}
		
		// Create a bullet
		Component.bullet.put( id, new Bullet(id,  new Vec2(0, -Constant.getFloat("Bullet_Speed")) ) );
		Component.placement.put( id, new Placement(exitPoint) );
		Component.drawer.put( id, new Drawer(id, Constant.getVector("Bullet_Color") ) );
		Component.killable.put( id, new Killable(id, Killable.playerTeam) );
	}

}
