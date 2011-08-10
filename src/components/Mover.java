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
		speed = Screen.rescale("game", s);
		stayInScreeen = stay;
		timeType = timeTy;
	}
	
	public final void Move( final Vec2 dir ) {
		nextMovement.add(dir);
	}
	
	public final void addSpeed( float f) {
		speed += f;
	}
	
	public final void Update() {
		float dt = Clock.getDelta(timeType);
		float myRadius = 0;
		Shape shp = Component.shape.get(me);
		if( shp != null) {
			myRadius = shp.getRadius();
		}
		
		if(!nextMovement.isZero())
		{
			//TODO: Ship es mou clarament mes lent en fullscreen (prova d'anar enrrere, o es screenup que va mes rapid?)
			nextMovement.normalize();
			nextMovement.scale(speed * dt);
			Component.placement.get(me).position.add(nextMovement);
			
			// Record
			Record rec = Component.record.get(me);
			if( rec != null) {
				rec.addEvent( Record.movement, null );
			}
		}
		nextMovement.zero();
		
		
		//TODO: Repassar tots els news i veure quins no fan falta (potser nomes new Vec2)
		// Stay in screen
		if(stayInScreeen) {
			Vec2 myTopLeft = new Vec2(Vec2.topLeft); 
			Screen.reposition("game", myTopLeft);
			myTopLeft.add(myRadius, myRadius);
			myTopLeft.add(Screen.up);
			
			Vec2 myBotRight = new Vec2(Vec2.bottomRight); 
			Screen.reposition("game", myBotRight);
			myBotRight.add(-myRadius, -myRadius);
			myBotRight.add(Screen.up);
			
			Component.placement.get(me).position.clamp(myTopLeft, myBotRight);
		}
	}
}
