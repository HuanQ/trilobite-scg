package sequences;

import geometry.Angle;
import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Clock;

import components.Bullet;
import components.Killable;
import components.Drawer;
import components.Placement;


public class SimpleBlaster implements Sequence {
	private final int											shootRate;
	private int													shootingTime;
	private final float											shootingArc;
	private final String										name;
	private final int											numBullets;
	// Internal data
	private int													lastTime = 0;
	private int													phase = 0;
	// 0: Not shooting yet
	// 1: Shooting


	public SimpleBlaster( final String nm ) {
		name = nm;
		shootRate = (int) (Constant.getFloat(name + "_Frequency") * Constant.timerResolution);;
		shootingTime = (int) (Constant.getFloat(name + "_Time") * Constant.timerResolution);
		shootingArc = Constant.getFloat(name + "_Arc");
		numBullets = (int) Constant.getFloat(name + "_NumBullets");
	}
	
	public final boolean Spawn( final Angle direction, final Vec2 spawnPoint ) {
		int time = Clock.getTime(Clock.game);
		switch(phase) {
		case 0:
			phase = 1;
			shootingTime += time;
			return false;
			
		case 1:
			
			if(time < shootingTime) {
				if( time > lastTime + shootRate) {
					doShoot(direction, spawnPoint);
					lastTime = time;
				}
			}
			else {
				return true;
			}
			return false;
			
		default:
			return true;
		}
	}
	
	public final String getName() {
		return name;
	}

	//TODO: Posar tots els metodes en majuscules (gets, sets i do tambe?)
	private final void doShoot( final Angle direction, final Vec2 spawnPoint ) {
		Angle a = new Angle(direction);
		a.add(-shootingArc/2);
		for(int i=0;i<numBullets;++i) {
			Vec2 bulletDirection = a.getDirection();
			bulletDirection.scale(Constant.getFloat("Blaster_BulletSpeed"));
			
			Integer id = Component.getID();
			Component.bullet.put( id, new Bullet(id, bulletDirection) );
			Component.placement.put( id, new Placement( new Vec2(spawnPoint) ) );
			Component.drawer.put( id, new Drawer(id, Constant.getVector("Bullet_Color") ) );
			Component.killable.put( id, new Killable(id, Killable.enemyTeam) );
		
			a.add( shootingArc/(numBullets-1) );
		}
	}
}
