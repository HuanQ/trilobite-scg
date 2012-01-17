package sequences;

import geometry.Angle;
import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Clock;
import managers.Debug;

import components.Bullet;
import components.Killable;
import components.Drawer;
import components.Placement;


public class SimpleBlaster implements Sequence {
	static public final int										shooting = 0;
	static public final int										paused = 1;
	
	private final float											shootRate;
	private float												shootingTime;
	private float												pausingTime;
	private final float											shootingArc;
	private final String										name;
	private final int											numBullets;
	// Internal data
	private float												time = 0;
	private float												lastShot = 0;
	private int													phase = 0;

	public SimpleBlaster( final String nm ) {
		name = nm;
		shootRate = Constant.getFloat(name + "_Frequency");
		shootingTime = Constant.getFloat(name + "_Shooting");
		pausingTime = Constant.getFloat(name + "_Pause");
		shootingArc = Constant.getFloat(name + "_Arc");
		numBullets = (int) Constant.getFloat(name + "_NumBullets");
	}
	
	public final SimpleBlaster getCopy() {
		return new SimpleBlaster(name);
	}
	
	public final boolean Spawn( final Angle direction, final Vec2 spawnPoint ) {
		time += Clock.getDelta(Clock.game);

		switch(phase) {
			case shooting:
				if(time < shootingTime) {
					if( time > lastShot + shootRate) {
						doShoot(direction, spawnPoint);
						lastShot = time;
					}
				}
				else {
					lastShot = 0;
					phase = paused;
					time = 0;
				}
			break;
			
			case paused:
				if(time > pausingTime) {
					phase = shooting;
					time = 0;
				}
			break;
		}
		return false;
	}
	
	public final String getName() {
		return name;
	}

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
			Component.killable.put( id, new Killable(id, Killable.enemyBulletTeam) );
		
			a.add( shootingArc/(numBullets-1) );
		}
	}
}
