package components;

import geometry.Vec2;

import managers.Clock;
import managers.Component;
import managers.Constant;


public class Killable {
	static public final int										terrain = 0;
	static public final int										playerTeam = 1;
	static public final int										enemyTeam = 2;
	
	private final int											me;
	private final int											type;
	
	public Killable( int m, int typ ) {
		me = m;
		type = typ;
	}
	
	public final boolean Kills( int hisType ) {
		switch(type) {
		case 0:
		case 1:
			return hisType == playerTeam || hisType == enemyTeam;
		case 2:
			return hisType == playerTeam;
		default:
			return false;
		}
	}
	
	public final void Update() {
		Shape myShape = Component.shape.get(me);
		boolean collision = false;
		int collisionID = 0;
		for(Integer him : Component.killable.keySet()) {
			if( him != me && Component.killable.get(him).Kills(type) ) {
				Shape hisShape = Component.shape.get(him);

				if( myShape == null) {
					// I don't have a Shape
					if( hisShape != null && hisShape.Collides(him, me) ) {
						collision = true;
						collisionID = him;
					}
				}
				else if( myShape.Collides(me, him) ) {
					collision = true;
					collisionID = him;
				}

			}
		}
		
		if(collision) {
			// We have collided
			if( Component.shield.get(me) != null && Component.shield.get(me).isUp() ) {
				// Shield is up, hit ignored
				if( Component.shape.get(collisionID) != null ) {
					// TODO: Impassable terrain
				}
			}
			else if( Component.spawner.get(me) != null ) {
				// Spawner disabled
				Component.spawner.get(me).Hit();
			}
			else {
				// Boom
				Placement p = Component.placement.get(me);
				Integer id = Component.getID();
				
				Component.timedObject.put( id, new TimedObject(id, Constant.getFloat("Explosion_Duration"), Clock.game) );
				Component.placement.put( id, new Placement( new Vec2(p.position.x, p.position.y)) );
				Component.drawer.put( id, new Drawer(id) );
				Component.shape.put( id, Constant.getShape("Explosion_Shape")  );
				
				Component.deadObjects.add(me);
			}
		}
	}
}
