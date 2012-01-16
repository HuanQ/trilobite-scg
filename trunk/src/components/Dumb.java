package components;

import geometry.Angle;
import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Clock;
import managers.Level;
import managers.Screen;
import managers.Sound;


public class Dumb {
	// Route types
	static public final int										normal = 0;
	static public final int										longRoute = 1;

	private final int											me;
	// Behaviour
	private final float											rotSpeed;
	private final int											route;
	private final float											rotateGoal;
	private float												health;
	// Internal data
	private Angle												movementRotation;
	private float												rotatedDistance = 0;
	private int													phase = 0;
	private final float											radius;
	private int													wait;
	private float												selfRotate;
	// 0: Go straight for a while
	// 1: Doing my route
	// 2: Stopping and spinning
	// 3: Explode and shoot bullets everywhere

	//TODO Fer un enemic diferent al Dumb (que sempre vagi disparant) i més dur de matar
	public Dumb( int m, float rspeed, final Angle rstart, int rt, float rg, float wt, float ht ) {
		me = m;
		rotSpeed = rspeed;
		movementRotation = new Angle( rstart );
		route = rt;
		rotateGoal = rg;
		radius = Component.shape.get(me) == null ? 0 : Component.shape.get(me).getRadius();
		wait = Clock.getTime(Clock.game) + (int) (wt * Constant.timerResolution);
		selfRotate = Constant.getFloat("Dumb_Rotation");
		health = ht;
	}
	
	//TODO NOW Health (hits) als dumb i spawners (amb un so al tocar) -> aixo no esta fet ja? Costen 3 hits!
	public final void Update() {
		float dt = Clock.getDelta(Clock.game);
		Component.placement.get(me).angle.add( selfRotate*dt );
		//TODO NOW Alguns Dumb no detecten els hits o passa algo que costen 5 impactes de matar
		switch(phase) {
		case 0:
			//TODO Els dumbs es veuen mes petits a 1920x1080
			Component.mover.get(me).Move( movementRotation.getDirection() );
			if( Clock.getTime(Clock.game) > wait) {
				phase = 1;
			}
			break;
		case 1:
			// Follow the route untill we reach the top of the screen
			switch(route) {
			case 0:
				doNormal(dt);
				break;
			}
			if( Component.placement.get(me).position.y-radius-Constant.getFloat("Dumb_StopDistance") < Screen.up.y ) {
				phase = 2;
				wait = Clock.getTime(Clock.game) + (int) (Constant.getFloat("Dumb_StopWait") * Constant.timerResolution);
			}
			break;
		
		case 2:
			// Stop and spin
			selfRotate += Constant.getFloat("Dumb_Acceleration") * dt;
			
			if( Clock.getTime(Clock.game) > wait) {
				phase = 3;
			}
			break;
		
		case 3:
			// Explode
			Angle a = new Angle();
			for(int i=0;i<Constant.getFloat("Dumb_NumBullets");++i) {
				a.add((float) Math.PI / (Constant.getFloat("Dumb_NumBullets")+1));
				
				Vec2 bulletDirection = a.getDirection();
				bulletDirection.scale(Constant.getFloat("Dumb_BulletSpeed"));
				Vec2 bulletPosition = new Vec2(Component.placement.get(me).position);

				Level.AddBullet(bulletDirection, bulletPosition, Killable.enemyBulletTeam);
			}
			Component.deadObjects.add(me);
			break;
		}
	}
	
	public final void Hit( float dmg ) {
		health -= dmg;
		if(health <= 0) {
			Die();
		}
	}
	
	public final void Die() {
		// Boom
		Level.AddEffect( Component.placement.get(me).position, "Explosion" );
		Sound.Play(Sound.explosion);
		Component.deadObjects.add(me);
	}
	
	private final void doNormal( float dt ) {
		float nextRotate = 0;
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
		mover.Move( movementRotation.getDirection() );
	}
	
}