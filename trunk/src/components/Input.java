package components;

import org.lwjgl.input.Keyboard;

import data.Timer;

import geometry.Vec2;

import managers.Component;
import managers.Clock;
import managers.Constant;

public class Input {
	static public final int										playerKeyboard = 0;
	static public final int										editorKeyboard = 1;
	
	private final int											me;
	private final int											type;
	// Internal data
	private final Timer											gun;
	private final Timer											shield;
	
	public Input( int m, int tp ) {
		me = m;
		type = tp;
		gun = new Timer(Constant.getFloat("Ship_GunCooldown"), Clock.play);
		shield = new Timer(Constant.getFloat("Ship_ShieldCooldown"), Clock.play);
	}
	
	//TODO: Els components han d'anar aixi. Una petita maquina d'estats amb un switch i uns defines a dalt
	public final void Update() {
		switch(type) {
		case playerKeyboard:
			doPlayer();
			break;
		case editorKeyboard:
			doEditor();
			break;
		}
	}
	
	private final void doEditor() {
		if( Keyboard.isKeyDown(Keyboard.KEY_UP) ) {

		}
		if( Keyboard.isKeyDown(Keyboard.KEY_DOWN) ) {
			
		}
	}

	private final void doPlayer() {
		if( Component.mover.get(me) != null )
		{
			if( Keyboard.isKeyDown(Keyboard.KEY_UP) )
				Component.mover.get(me).move( Vec2.down );
			if( Keyboard.isKeyDown(Keyboard.KEY_DOWN) )
				Component.mover.get(me).move( Vec2.up );
			if( Keyboard.isKeyDown(Keyboard.KEY_LEFT) )
				Component.mover.get(me).move( Vec2.left );
			if( Keyboard.isKeyDown(Keyboard.KEY_RIGHT) )
				Component.mover.get(me).move( Vec2.right );
		}
		
		if( Component.gun.get(me) != null )
		{
			if( Keyboard.isKeyDown(Keyboard.KEY_SPACE) && gun.Check() )
			{
				Component.gun.get(me).Shoot();
				gun.Refresh();
			}
		}
		
		if( Component.shield.get(me) != null )
		{
			if( Keyboard.isKeyDown(Keyboard.KEY_RETURN) && shield.Check() )
			{
				Component.shield.get(me).Raise();
				shield.Refresh();
			}
		}
	}
}
