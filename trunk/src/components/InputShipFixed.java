package components;

import geometry.Angle;
import geometry.Vec2;

import org.lwjgl.input.Keyboard;

import managers.Component;

public class InputShipFixed extends InputShip {
	
	public InputShipFixed( int m, float guncd, float shieldcd ) {
		super(m, guncd, shieldcd);
	}
	
	protected final void Move() {
		if( Keyboard.isKeyDown(Keyboard.KEY_UP) ) {
			Angle rot = new Angle(Component.placement.get(me).angle);
			rot.add((float) -Math.PI / 2);
			Vec2 next = rot.getDirection();
			Component.mover.get(me).Move( next );
		}
		if( Keyboard.isKeyDown(Keyboard.KEY_DOWN) ) {
			Angle rot = new Angle(Component.placement.get(me).angle);
			rot.add((float) -Math.PI / 2);
			Vec2 next = rot.getDirection();
			next.scale(-1);
			Component.mover.get(me).Move( next );
		}
		if( Keyboard.isKeyDown(Keyboard.KEY_LEFT) )
			Component.mover.get(me).RotateCCW();
		if( Keyboard.isKeyDown(Keyboard.KEY_RIGHT) )
			Component.mover.get(me).RotateCW();
	}

}
