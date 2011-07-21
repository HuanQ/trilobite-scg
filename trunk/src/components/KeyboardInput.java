package components;

import javax.vecmath.Vector2f;

import managers.Component;
import managers.Constant;
import managers.Timer;

import org.lwjgl.input.Keyboard;


public class KeyboardInput {
	private final int											me;
	
	public KeyboardInput(final int m) {
		me = m;
	}
	
	public void Update() {
		if( Component.mover.get(me) != null )
		{
			if( Keyboard.isKeyDown(Keyboard.KEY_UP) )
				Component.mover.get(me).move( new Vector2f(0, -1) );
			if( Keyboard.isKeyDown(Keyboard.KEY_DOWN) )
				Component.mover.get(me).move( new Vector2f(0, 1) );
			if( Keyboard.isKeyDown(Keyboard.KEY_LEFT) )
				Component.mover.get(me).move( new Vector2f(-1, 0) );
			if( Keyboard.isKeyDown(Keyboard.KEY_RIGHT) )
				Component.mover.get(me).move( new Vector2f(1, 0) );
		}
		
		if( Component.gun.get(me) != null )
		{
			if( Keyboard.isKeyDown(Keyboard.KEY_SPACE) && Timer.isReady("GunCooldown"))
			{
				Component.gun.get(me).Shoot( new Vector2f(0, -Constant.getFloat("Bullet_Speed")) );
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
