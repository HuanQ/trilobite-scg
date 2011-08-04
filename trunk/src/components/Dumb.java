package components;

import geometry.Angle;
import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Clock;
import managers.Screen;


public class Dumb {
	// Route types
	static public final int										normal = 0;
	static public final int										longRoute = 1;

	private final int											me;
	// Behaviour
	private final float											rotSpeed;
	private final int											route;
	private final float											rotateGoal;
	// Internal data
	private Angle												movementRotation;
	private float												rotatedDistance = 0;
	private int													phase = 0;
	private final float											radius;
	private int													wait;
	// 0: Go straight for a while
	// 1: Doing my route
	// 2: Stopping and spinning
	// 3: Explode and shoot bullets everywhere

	public Dumb( int m, float rspeed, final Angle rstart, int rt, float rg, float wt ) {
		me = m;
		rotSpeed = rspeed;
		movementRotation = new Angle( rstart );
		route = rt;
		rotateGoal = rg;
		radius = Component.shape.get(me) == null ? 0 : Component.shape.get(me).getRadius();
		wait = Clock.getTime(Clock.game) + (int) (wt * Constant.timerResolution);
	}
	
	public final void Update() {
		//TODO: Sempre ha de donar voltes
		switch(phase) {
		case 0:
			Component.mover.get(me).move( movementRotation.getDirection() );
			if( Clock.getTime(Clock.game) > wait) {
				phase = 1;
			}
			break;
		case 1:
			// Follow the route untill we reach the top of the screen
			switch(route) {
			case 0:
				doNormal();
				break;
			}
			if( Component.placement.get(me).position.y-radius-Constant.getFloat("Dumb_StopDistance") < Screen.up.y ) {
				phase = 2;
				wait = Clock.getTime(Clock.game) + (int) (Constant.getFloat("Dumb_StopWait") * Constant.timerResolution);
			}
			break;
		
		case 2:
			// Stop and spin
			if( Clock.getTime(Clock.game) > wait) {
				phase = 3;
			}
			break;
		
		case 3:
			// Explode
			Angle a = new Angle((float) Math.PI);
			for(int i=0;i<Constant.getFloat("Dumb_NumBullets");++i) {
				a.add((float) -Math.PI / (Constant.getFloat("Dumb_NumBullets")+1));
				
				Vec2 bulletDirection = a.getDirection();
				bulletDirection.scale(Constant.getFloat("Dumb_BulletSpeed"));
				
				Vec2 bulletPosition = new Vec2(Component.placement.get(me).position);

				Integer id = Component.getID();
				Component.bullet.put( id, new Bullet(id, bulletDirection) );
				Component.placement.put( id, new Placement(bulletPosition) );
				Component.drawer.put( id, new Drawer(id, Constant.getVector("Bullet_Color") ) );
				Component.killable.put( id, new Killable(id, Killable.enemyTeam) );
			}
			Component.deadObjects.add(me);
			break;
		}
	}
	
	private final void doNormal() {
		float nextRotate = 0;
		float dt = Clock.getDelta(Clock.game);
		Mover mover = Component.mover.get(me);

		if( Math.abs(rotatedDistance) < rotateGoal ) {
			float rotAccel = 0.75f + Math.abs(rotatedDistance) / rotateGoal / 2;
			nextRotate = rotSpeed * dt * rotAccel;
			rotatedDistance += nextRotate;
			movementRotation.add( nextRotate );
		}
		else {
			nextRotate = 0;
			mover.addSpeed( Constant.getFloat("Dumb_Acceleration") * dt );
		}
		mover.move( movementRotation.getDirection() );
	}
}