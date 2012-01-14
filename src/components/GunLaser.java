package components;

import data.Laser;
import geometry.Angle;
import geometry.Rectangle;
import geometry.Vec2;

import managers.Clock;
import managers.Component;
import managers.Constant;
import managers.Screen;


public class GunLaser extends Gun {
	final float													damagePerHit;
	private Laser												laser;
	// Internal data
	private int													myObj = 0;
	private Vec2												lastBegin = null;
	private Vec2												lastEnd = null;
	
	public GunLaser( int m, float gd, float dps ) {
		super(m, gd);
		damagePerHit = dps * Constant.getFloat("Performance_LaserCooldown");
	}
	
	public final void Shoot() {
		super.Shoot();
		
		Angle myRot = new Angle(Component.placement.get(me).angle);
		myRot.add((float) -Math.PI / 2);
		
		Vec2 exitPoint = myRot.getDirection();
		exitPoint.scale(gunDist);
		exitPoint.add(Component.placement.get(me).position);
		
		laser = new Laser(exitPoint, myRot, Killable.playerBulletTeam);
		
		for( Integer i : laser.getThingsHit() ) {
			Killable.Hit(me, i, damagePerHit);
		}
		
		if( lastBegin == exitPoint && lastEnd == laser.getEndPoint() && Component.shape.get(myObj) != null ) {
			// The laser trace we want to draw is the same we have on screen, extend it's duration
			Component.timedObject.get(myObj).Extend( Constant.getFloat("Performance_LaserCooldown") );
		}
		else {
			lastBegin = exitPoint;
			lastEnd = laser.getEndPoint();
			
			Integer id = Component.getID();
			Component.timedObject.put( id, new Timed(id, Constant.getFloat("Performance_LaserCooldown")*1.5f, Clock.play) );
			Component.placement.put( id, new Placement(lastBegin) );
			Component.drawer.put( id, new Drawer(id) );
			Component.shape.put( id, Constant.getShape("Laser_Shape")  );
			
			// Calculate the rectangle rotation and size with screen coordinates in mind
			Vec2 dist = new Vec2( Screen.coords(lastEnd, true) );
			dist.sub( Screen.coords(lastBegin, true) );
			dist = Screen.decoords(dist);
			Angle rot = new Angle();
			rot.set(dist);

			Rectangle myRect = Component.shape.get(id).getRectangle();
			myRect.size.x = dist.length();
			myRect.offset.x = dist.length()/2;
			Component.placement.get(id).angle.set( rot.get() );
			myObj = id;
		}
	}

}
