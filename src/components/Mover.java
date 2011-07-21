package components;

import javax.vecmath.Vector2f;

import managers.Component;
import managers.Constant;
import managers.Timer;


public class Mover {
	private final int											me;
	private float												speed;
	private final boolean										noseRotate;
	private Vector2f											nextMovement;
	private final boolean										stayInScreeen;
	private final float											gravity;
	
	public Mover(final int m, final float s, final boolean nose, final boolean stay, final float g) {
		me = m;
		speed = s;
		noseRotate = nose;
		nextMovement = new Vector2f();
		stayInScreeen = stay;
		gravity = g;
	}
	
	public void move(final Vector2f dir) {
		nextMovement.x += dir.x;
		nextMovement.y += dir.y;
	}
	
	public void addSpeed(final float f) {
		speed += f;
	}
	
	public void Update() {
		
		float dt = Timer.getDelta();
		if(nextMovement.lengthSquared() > 0)
		{
			nextMovement.normalize();
			nextMovement.scale(speed * dt);
			Component.placement.get(me).addPosition(nextMovement);
			//TODO: posar el nas en la direcció que toca
		}
		
		nextMovement.x = 0;
		nextMovement.y = 0;
		
		// Washaway de la Gravity
		if(!stayInScreeen) {
			Vector2f grav = new Vector2f(Constant.gravity.x * dt , dt * (gravity + Constant.gravity.y));
			Component.placement.get(me).addPosition(grav);
		}
		
		// Limitar stayinscreen
		if(stayInScreeen) {
			Vector2f myPos = Component.placement.get(me).getPosition();
			float myRad = Component.shape.get(me).getRadius();
			
			if( myPos.x + myRad > 1 )
				myPos.x = 1 - myRad;
			if( myPos.x - myRad < 0 )
				myPos.x = myRad;
			if( myPos.y + myRad > 1 )
				myPos.y = 1 - myRad;
			if( myPos.y - myRad < 0 )
				myPos.y = myRad;
		}
	}
}
