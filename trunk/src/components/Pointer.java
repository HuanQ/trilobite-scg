package components;

import java.util.Map;

import geometry.Vec2;
import managers.Component;
import managers.Screen;
import managers.Timer;

import org.lwjgl.input.Mouse;

public class Pointer {
	private final int										me;
	private boolean											active;
	
	public Pointer( int m ) {
		me = m;
		active = true;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean s) {
		active = s;
	}
	
	public void Update() {
		Vec2 newPos = new Vec2( Mouse.getDX(), -Mouse.getDY() );
		Component.placement.get(me).addPosition( Screen.descale(newPos) );
		
		Vec2 myPos = Component.placement.get(me).getPosition();
		
		if( Mouse.isButtonDown(0) && Timer.isReady("ClickCooldown") ) {
			Timer.exhaust("ClickCooldown");
			for (Map.Entry<Integer, Clickable> entry : Component.clickable.entrySet()) {
				Integer him = entry.getKey();
				if( Component.shape.get(him).Collides(Component.placement.get(him).getPosition(), null, myPos) ) {
					Component.clickable.get(him).Click();
				}
			}
			
		}
	}
}
