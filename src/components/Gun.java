package components;

import javax.vecmath.Vector2f;

import managers.Component;
import managers.Constant;


public class Gun {

	private final int											me;
	private final Vector2f										gunPoint;
	
	public Gun(final int m, final Vector2f p) {
		me = m;
		gunPoint = p;
	}
	public void Shoot(Vector2f dir) {
		Vector2f myPos = Component.placement.get(me).getPosition();
		Vector2f exitPoint = new Vector2f( myPos.x + gunPoint.x, myPos.y + gunPoint.y);
		Integer id = Component.getID();
		
		Component.bullet.put( id, new Bullet(id, dir) );
		Component.placement.put( id, new Placement( exitPoint ) );
		Component.drawer.put( id, new Drawer(id, Constant.getVector("Bullet_Color") ) );
		Component.canBeKilled.put( id, new CanBeKilled(id) );
		Component.canKill.put( id, new CanKill() );
	}

}
