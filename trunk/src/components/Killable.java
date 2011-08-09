package components;

import managers.Component;
import managers.Level;


public class Killable {
	static public final int										terrain = 0;
	static public final int										playerTeam = 1;
	static public final int										enemyTeam = 2;
	static public final int										shieldTeam = 3;
	
	private final int											me;
	private int													type;
	
	public Killable( int m, int typ ) {
		me = m;
		type = typ;
	}
	
	public final boolean Kills( int hisType ) {
		switch(type) {
		case 0:
			return hisType == playerTeam || hisType == enemyTeam || hisType == shieldTeam;
		case 1:
			return hisType == playerTeam || hisType == enemyTeam;
		case 2:
			return hisType == playerTeam;
		case 3:
			return hisType == playerTeam || hisType == enemyTeam;
		default:
			return false;
		}
	}
	
	public final void setType( int t ) {
		type = t;
	}
	
	public final void Update() {
		Shape myShape = Component.shape.get(me);
		boolean collision = false;
		int collisionID = 0;
		for(Integer him : Component.killable.keySet()) {
			if( him != me && Component.killable.get(him).Kills(type) ) {
				Shape hisShape = Component.shape.get(him);

				if( myShape == null ) {
					// I don't have a Shape
					if( hisShape != null ) {
						if( hisShape.Collides(him, me) ) {
							collision = true;
							collisionID = him;
							break;
						}
					}
				}
				else {
					if( myShape.Collides(me, him) ) {
						collision = true;
						collisionID = him;
						break;
					}
				}

			}
		}

		if(collision) {
			if( Component.spawner.get(me) != null ) {
				// Spawner disabled
				Component.spawner.get(me).Hit();
			}
			else {
				// Boom
				Level.AddEffect( Component.placement.get(me).position, "Explosion" );
				
				Component.deadObjects.add(me);
			}
		}
	}
}
