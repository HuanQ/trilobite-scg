package components;

import org.lwjgl.input.Keyboard;

import data.Timer;

import managers.Component;
import managers.Clock;

public abstract class InputShip extends Input {
	// Internal data
	private final float											shieldCooldown; 
	private final Timer											gun;
	private final Timer											shield;
	
	protected InputShip( int m, float guncd, float shieldcd ) {
		super(m);
		shieldCooldown = shieldcd;
		gun = new Timer(guncd, Clock.play);
		shield = new Timer(shieldcd, Clock.play);
	}
	
	public final void Update() {
		if( Component.mover.get(me) != null )
		{
			Move();
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
				Component.energybar.Exhaust(shieldCooldown);
				shield.Refresh();
			}
		}
	}
	
	protected abstract void Move();

}
