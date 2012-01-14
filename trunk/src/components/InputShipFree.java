package components;

import org.lwjgl.input.Keyboard;

import geometry.Vec2;

import managers.Component;

public class InputShipFree extends InputShip {
	
	public InputShipFree( int m, float guncd, float shieldcd ) {
		super(m, guncd, shieldcd);
	}
	
	protected final void Move() {
		if( Keyboard.isKeyDown(Keyboard.KEY_UP) )
			Component.mover.get(me).Move( Vec2.down );
		if( Keyboard.isKeyDown(Keyboard.KEY_DOWN) )
			Component.mover.get(me).Move( Vec2.up );
		if( Keyboard.isKeyDown(Keyboard.KEY_LEFT) )
			Component.mover.get(me).Move( Vec2.left );
		if( Keyboard.isKeyDown(Keyboard.KEY_RIGHT) )
			Component.mover.get(me).Move( Vec2.right );
	}

}
