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
	public void Shoot( final Vec2 dir ) {
		Vec2 myPos = Component.placement.get(me).getPosition();
		Vec2 exitPoint = new Vec2( myPos.x + gunPoint.x, myPos.y + gunPoint.y);
		Integer id = Component.getID();
		
		// Create a bullet
		Component.bullet.put( id, new Bullet(id, dir) );
		Component.placement.put( id, new Placement( exitPoint ) );
		Component.drawer.put( id, new Drawer(id, Constant.getVector("Bullet_Color") ) );
		Component.canBeKilled.put( id, new CanBeKilled(id) );
		Component.canKill.put( id, new CanKill() );
	}

}
