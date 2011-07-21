package components;

import java.util.Map;

import javax.vecmath.Vector2f;

import managers.Component;
import managers.Constant;


public class CanBeKilled {
	private final int											me;
	
	public CanBeKilled(final int m) {
		me = m;
	}
	
	public void Update() {
		Shape myShape = Component.shape.get(me);
		boolean drawExplosion = false;
		
		for (Map.Entry<Integer, CanKill> entry : Component.canKill.entrySet()) {
			Integer him = entry.getKey();
			if( him != me ) {
				Shape hisShape = Component.shape.get(him);
				if( myShape == null) {
					// I don't have a Shape
					if( hisShape == null) {
						// Collision impossible
					}
					else if ( hisShape.Collides( Component.placement.get(him).getPosition(), null, Component.placement.get(me).getPosition() ) ) {
						// We have collided
						if( Component.shield.get(me) == null || !Component.shield.get(me).isUp() ) {
							// I didn't have my shields up
							drawExplosion = true;
						}
					}
				}
				else {
					// I do have a Shape
					if ( myShape.Collides( Component.placement.get(me).getPosition(), hisShape, Component.placement.get(him).getPosition() ) ) {
						// We have collided
						if( Component.shield.get(me) == null || !Component.shield.get(me).isUp() ) {
							// I didn't have my shields up
							drawExplosion = true;
						}
					}
				}
			}
		}
		
		if(drawExplosion) {
			Placement p = Component.placement.get(me);
			Integer id = Component.getID();
			
			Component.timedObject.put( id, new TimedObject(id, Constant.getFloat("Explosion_Duration")) );
			Component.placement.put( id, new Placement( new Vector2f( p.getPosition().x, p.getPosition().y ) ) );
			Component.drawer.put( id, new Drawer(id, Constant.getVector("Explosion_Color") ) );
			Component.shape.put( id, Constant.getShape("Explosion_Shape")  );
			
			Component.deadObjects.add(me);
		}
	}
}
