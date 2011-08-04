package components;

import geometry.Vec2;

import managers.Component;
import managers.Clock;
import managers.Screen;


public class Mover {
	private final int											me;
	private float												speed;
	private Vec2												nextMovement = new Vec2();
	private final boolean										stayInScreeen;
	private final int											timeType; 

	public Mover( int m, float s, boolean stay, int timeTy ) {
		me = m;
		speed = s;
		stayInScreeen = stay;
		timeType = timeTy;
	}
	
	public final void move( final Vec2 dir ) {
		nextMovement.add(dir);
	}
	
	public final void addSpeed( float f) {
		speed += f;
	}
	
	public final void Update() {
		
		float dt = Clock.getDelta(timeType);
		float myRadius = 0;
		if(Component.shape.get(me) != null) {
			myRadius += Component.shape.get(me).getRadius();
		}
		
		if(!nextMovement.isZero())
		{
			nextMovement.normalize();
			nextMovement.scale(speed * dt);
			Component.placement.get(me).position.add(nextMovement);
			
			// Record
			Record rec = Component.record.get(me);
			if( rec != null) {
				rec.event( Record.movement, null );
			}
		}
		nextMovement.zero();
		
		
		//TODO: Repassar tots els news i veure quins no fan falta (potser nomes new Vec2)
		// Stay in screen
		if(stayInScreeen) {
			float myRad = Component.shape.get(me).getRadius();
			
			Vec2 myTopLeft = new Vec2(Vec2.topLeft); 
			Screen.reposition("game", myTopLeft);
			myTopLeft.add(myRad, myRad);
			myTopLeft.add(Screen.up);
			
			Vec2 myBotRight = new Vec2(Vec2.bottomRight); 
			Screen.reposition("game", myBotRight);
			myBotRight.add(-myRad, -myRad);
			myBotRight.add(Screen.up);
			
			Vec2 myPos = Component.placement.get(me).position;
			
			// Lateral
			myPos.clamp(myTopLeft, myBotRight);
		}
	}
}
