package components;

import org.lwjgl.input.Keyboard;

import managers.Component;
import managers.Constant;
import managers.Clock;

public class Helper {
	private final int										me;
	private String											function;
	private int												wait;
	// Internal data
	private int												phase = 0;
	private float											blinkTime;
	private int												blinkWait;
	// 0: Not shown yet
	// 1: Shown but action not done
	// 2: Action done but still shown
	// 3: Done and about to selfdestroy
	
	public Helper( int m, final String func, float w) {
		me = m;
		function = func;
		wait = Clock.getTime(Clock.ui) + (int) (w * Constant.timerResolution);
		blinkTime = Constant.getFloat("Helper_BlinkTime");
		Component.drawer.get(me).setVisible(false);
	}
	
	public final boolean isActive() {
		return phase != 0;  
	}
	
	public final void Update( ) {
		
		switch(phase) {
		case 0:
			if(Clock.getTime(Clock.ui) > wait) {
				boolean otherHelpersActive = false;
				for(Helper h : Component.helper.values()) {
					otherHelpersActive |= h.isActive();
				}
				
				if( otherHelpersActive ) {
					// Wait at least one second after no other helper is left
					wait = Clock.getTime(Clock.ui) + Constant.timerResolution;
				}
				else {
					// Show the helper
					Component.drawer.get(me).setVisible(true);
					phase = 1;						
				}
			}
			break;
		
		case 1:
			{
				// Discard the helper after stayTime
				boolean next = false;
	
				if(function.equals("MOVE") && (Keyboard.isKeyDown(Keyboard.KEY_UP)
						|| Keyboard.isKeyDown(Keyboard.KEY_DOWN)
						|| Keyboard.isKeyDown(Keyboard.KEY_LEFT)
						|| Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) ) {
					next = true;
				}
				else if(function.equals("SHOOT") && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
					next = true;
				}
				else if(function.equals("SHIELD") && Keyboard.isKeyDown(Keyboard.KEY_RETURN) ) {
					next = true;
				}
				
				if(next) {
					phase = 2;
					wait += (int) (Constant.getFloat("Helper_Duration") * Constant.timerResolution);
				}
	
				if(Clock.getTime(Clock.ui) > blinkWait) {
					blinkWait = Clock.getTime(Clock.ui) + (int) (2*blinkTime * Constant.timerResolution);
					
					// Create annoying circle
					Integer id = Component.getID();
	
					Component.timedObject.put( id, new TimedObject(id, blinkTime, Clock.ui) );
					Component.placement.put( id, new Placement(Component.placement.get(me)) );
					Component.drawer.put( id, new Drawer(id, Constant.getVector("Helper_Color")) );
					Component.shape.put( id, Constant.getShape("Helper_Shape")  );
					Component.sticky.put( id, new Sticky(id) );
					
					if(blinkTime > 0.2f) {
						blinkTime *= 0.85f;
					}
				}
			}
			break;
		
		case 2:
			if(Clock.getTime(Clock.ui) > wait) {
				// Hide the helper and self destroy
				Component.drawer.get(me).setVisible(false);
				phase = 3;
				Component.deadObjects.add(me);
			}
			
			break;
		}
	}
}