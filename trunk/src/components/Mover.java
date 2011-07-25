package components;

import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Screen;
import managers.Timer;


public class Mover {
	private final int											me;
	private float												speed;
	private Vec2												nextMovement;
	private final boolean										stayInScreeen;
	private final float											gravity;
	
	public Mover( int m, float s, boolean stay, float g) {
		me = m;
		speed = s;
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
		float myRadius = 0;
		if(Component.shape.get(me) != null) {
			myRadius += Component.shape.get(me).getRadius();
		}
		
		if(!nextMovement.isZero())
		{
			nextMovement.normalize();
			nextMovement.scale(speed * dt);
			Component.placement.get(me).addPosition(nextMovement);
			
			// Record
			Record rec = Component.record.get(me);
			if( rec != null) {
				rec.event( Record.movement, null );
			}
		}
		
		nextMovement.zero();
		
		// Gravity pull
		if(!stayInScreeen && Screen.inScreen(Component.placement.get(me).getPosition(), myRadius)) {
			Vec2 grav = new Vec2();
			grav.y = (gravity + Constant.gravity) * dt;
			Component.placement.get(me).addPosition( grav );
		}
		
		Vec2 myPos = Component.placement.get(me).getPosition();
		
		// Stay in screen
		if(stayInScreeen) {
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
		
		// Selfkill at a certain distance from the screen limit
		float killDist = Constant.getFloat("Render_DefaultKillDistance") + myRadius;
		if( !Screen.inScreenU(myPos, killDist) ) {
			Component.deadObjects.add(me);
		}
	}
}
