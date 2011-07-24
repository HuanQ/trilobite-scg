package components;

import geometry.Angle;

import managers.Component;
import managers.Constant;
import managers.Screen;
import managers.Timer;


public class Dumb {
	// Route types
	static public final int										shortRoute = 0;
	static public final int										longRoute = 1;

	private final int											me;
	// Behaviour
	private final float											rotSpeed;
	private final int											route;
	// Internal data
	Angle														movementRotation;
	float														rotatedDistance;

	public Dumb( int m, float rspeed, final Angle rstart, int rt ) {
		me = m;
		rotSpeed = rspeed;
		movementRotation = new Angle( rstart );
		rotatedDistance = 0;
		route = rt;
	}
	
	public void Update() {
		//TODO: Sempre ha de donar voltes
		
		float dt = Timer.getDelta();
		Mover mover = Component.mover.get(me);
		float nextRotate;
		switch(route) {
			case 0:
				// Short route
				if( Math.abs(rotatedDistance) < 1.57f ) {
					nextRotate = rotSpeed * dt / 2.f;
					
					rotatedDistance += nextRotate;
					movementRotation.addRotation( nextRotate );
					
					mover.move( movementRotation.getDirection() );
				}
				else if( Math.abs(rotatedDistance) < 4.71f ) {
					nextRotate = rotSpeed * dt;
					
					rotatedDistance += nextRotate;
					movementRotation.addRotation( nextRotate );
					
					mover.move( movementRotation.getDirection() );
				}
				else if( Math.abs(rotatedDistance) < 4.71f + 0.78f ) {
					nextRotate = rotSpeed * dt / 2.f;
					
					rotatedDistance += nextRotate;
					movementRotation.addRotation( nextRotate );
					
					mover.move( movementRotation.getDirection() );
					mover.addSpeed( Constant.getFloat("Dumb_Acceleration") * dt );
				}
				else if( Math.abs(rotatedDistance) < 6.28f ) {
					nextRotate = -rotSpeed * dt / 1.5f;
					
					rotatedDistance -= nextRotate;
					movementRotation.addRotation( nextRotate );
					
					mover.move( movementRotation.getDirection() );
					mover.addSpeed( Constant.getFloat("Dumb_Acceleration") * dt );
				}
				else {
					mover.move( movementRotation.getDirection() );
					mover.addSpeed( Constant.getFloat("Dumb_Acceleration") * dt );
				}
			break;
			
			case 1:
				// Long route
				if( Math.abs(rotatedDistance) < 1.57f ) {
					nextRotate = rotSpeed * dt / 1.25f;
					
					rotatedDistance += nextRotate;
					movementRotation.addRotation( nextRotate );
					
					mover.move( movementRotation.getDirection() );
				}
				else if( Math.abs(rotatedDistance) < 4.71f - 1.57f ) {
					nextRotate = rotSpeed * dt;
					
					rotatedDistance += nextRotate;
					movementRotation.addRotation( nextRotate );
					
					mover.move( movementRotation.getDirection() );
				}
				else if( Math.abs(rotatedDistance) < 4.71f ) {
					nextRotate = rotSpeed * dt / 2.f;
					
					rotatedDistance += nextRotate;
					movementRotation.addRotation( nextRotate );
					
					mover.move( movementRotation.getDirection() );
					mover.addSpeed( Constant.getFloat("Dumb_Acceleration") * dt );
				}
				else if( Math.abs(rotatedDistance) < 4.71f + 1.57f ) {
					nextRotate = -rotSpeed * dt / 5.f;
					
					rotatedDistance -= nextRotate;
					movementRotation.addRotation( nextRotate );
					
					mover.move( movementRotation.getDirection() );
					mover.addSpeed( Constant.getFloat("Dumb_Acceleration") * dt );
				}
				else {
					mover.move( movementRotation.getDirection() );
					mover.addSpeed( Constant.getFloat("Dumb_Acceleration") * dt );
				}
			break;
		}
		
		// Selfkill at a certain distance from the screen limit
		float killDist = Constant.getFloat("Dumb_KillDistance");
		if(Component.shape.get(me) != null) {
			killDist += Component.shape.get(me).getRadius();
		}
		if( !Screen.inScreen(Component.placement.get(me).getPosition(), killDist) ) {
			Component.deadObjects.add(me);
		}
	}
}