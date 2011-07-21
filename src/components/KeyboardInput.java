package components;

import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Timer;

import org.lwjgl.input.Keyboard;


public class KeyboardInput {
	private final int											me;
	
	public KeyboardInput( int m) {
		me = m;
	}
	
	public void Update() {
		if( Component.mover.get(me) != null )
		{
			if( Keyboard.isKeyDown(Keyboard.KEY_UP) )
				Component.mover.get(me).move( Vec2.Down() );
			if( Keyboard.isKeyDown(Keyboard.KEY_DOWN) )
				Component.mover.get(me).move( Vec2.Up() );
			if( Keyboard.isKeyDown(Keyboard.KEY_LEFT) )
				Component.mover.get(me).move( Vec2.Left() );
			if( Keyboard.isKeyDown(Keyboard.KEY_RIGHT) )
				Component.mover.get(me).move( Vec2.Right() );
		}
		
		if( Component.gun.get(me) != null )
		{
			if( Keyboard.isKeyDown(Keyboard.KEY_SPACE) && Timer.isReady("GunCooldown"))
			{
				Component.gun.get(me).Shoot( new Vec2(0, -Constant.getFloat("Bullet_Speed")) );
				Timer.exhaust("GunCooldown");
			}
		}
		
		if( Component.shield.get(me) != null )
		{
			if( Keyboard.isKeyDown(Keyboard.KEY_RETURN) && Timer.isReady("ShieldCooldown") )
			{
				Component.shield.get(me).Raise();
				Timer.exhaust("ShieldCooldown");
			}
		}
	}
}
