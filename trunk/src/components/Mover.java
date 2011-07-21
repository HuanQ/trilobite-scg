package components;

import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Timer;


public class Mover {
	private final int											me;
	private float												speed;
	private final boolean										noseRotate;
	private Vec2												nextMovement;
	private final boolean										stayInScreeen;
	private final float											gravity;
	
	public Mover( int m, float s, boolean nose, boolean stay, float g) {
		me = m;
		speed = s;
		noseRotate = nose;
		nextMovement = new Vec2();
		stayInScreeen = stay;
		gravity = g;
	}
	
	public void move(final Vec2 dir) {
		nextMovement.x += dir.x;
		nextMovement.y += dir.y;
	}
	
	public void addSpeed( float f) {
		speed += f;
	}
	
	public void Update() {
		
		float dt = Timer.getDelta();
		if(!nextMovement.isZero())
		{
			nextMovement.normalize();
			nextMovement.scale(speed * dt);
			Component.placement.get(me).addPosition(nextMovement);
			//TODO: posar el nas en la direcció que toca
		}
		
		nextMovement.zero();
		
		// Gravity pull
		if(!stayInScreeen) {
			Component.placement.get(me).addPosition( Constant.gravity.getScaled(dt) );
		}
		
		// Stay in screen
		if(stayInScreeen) {
			Vec2 myPos = Component.placement.get(me).getPosition();
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
