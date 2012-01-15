package components;

import java.util.Collection;
import java.util.HashSet;

import managers.Component;
import managers.Constant;
import managers.Level;
import managers.Sound;


public class Killable {
	static public final int										terrain = 0;
	static public final int										playerTeam = 1;
	static public final int										playerBulletTeam = 2;
	static public final int										enemyTeam = 3;
	static public final int										enemyBulletTeam = 4;
	static public final int										shieldTeam = 5;
	static public final int										explosionTeam = 6;
	static public final int										bulletShieldTeam = 7;
	
	private final int											me;
	private int													type;
	private Collection<Integer>									noKill = new HashSet<Integer>();
	
	public Killable( int m, final Killable k) {
		me = m;
		type = k.getType();
	}
	
	public Killable( int m, int typ ) {
		me = m;
		type = typ;
	}
	
	public final void setType( int ty ) {
		type = ty;
	}
	
	public final int getType() {
		return type;
	}
	
	static public final boolean Kills( int attacker, int victim ) {
		switch(attacker) {
		case terrain:
			return victim == playerTeam || victim == enemyTeam || victim == shieldTeam || victim == enemyBulletTeam || victim == playerBulletTeam;
		case playerTeam:
			return victim == playerTeam || victim == enemyTeam || victim == enemyBulletTeam || victim == playerBulletTeam;
		case playerBulletTeam:
			return victim == playerTeam || victim == enemyTeam;
		case enemyTeam:
			return victim == playerTeam || victim == playerBulletTeam;
		case enemyBulletTeam:
			return victim == playerTeam;
		case shieldTeam:
			return false;
		case explosionTeam:
			return victim == playerTeam || victim == enemyTeam;
		case bulletShieldTeam:
			return victim == enemyBulletTeam;
		default:
			return false;
		}
	}
	
	public final void addNoKill( int i ) {
		noKill.add(i);
	}
	
	public final void Update() {
		float bulletDmg = Constant.getFloat("Bullet_Damage");
		
		Shape myShape = Component.shape.get(me);
		for(Integer him : Component.killable.keySet()) {
			
			if( him != me && Killable.Kills(Component.killable.get(him).getType(), type) && !noKill.contains(him) ) {
				Shape hisShape = Component.shape.get(him);
				
				if( myShape == null ) {
					// I don't have a Shape
					if( hisShape != null ) {
						if( hisShape.Collides(him, me) ) {
							Killable.Hit(him, me, bulletDmg);
							break;
						}
					}
				}
				else {
					if( myShape.Collides(me, him) ) {
						Killable.Hit(him, me, bulletDmg);
						break;
					}
				}

			}
		}

	}
	
	static public final void Hit( int attacker, int victim, float dmg ) {
		if( Component.spawner.get(victim) != null ) {
			// Spawner disabled
			Component.spawner.get(victim).Hit();
		}
		else if( Component.dumb.get(victim) != null ) {
			// Dumb hit
			if( Component.bullet.get(attacker) != null ) {
				// By a bullet
				Component.dumb.get(victim).Hit(dmg);
			}
			else if( Component.gun.get(attacker) != null ) {
				// By a laser
				Component.dumb.get(victim).Hit(dmg);
			}
			else {
				Component.dumb.get(victim).Die();
			}
			
		}
		else {
			// Boom
			Level.AddEffect( Component.placement.get(victim).position, "Explosion" );
			Sound.Play(Sound.explosion);
			Component.deadObjects.add(victim);
		}
	}
}
