package components;

import java.util.Map;

import geometry.Vec2;

import org.lwjgl.input.Keyboard;

import managers.Component;
import managers.Constant;
import managers.Timer;

public class Helper {
	private final int										me;
	private String											function;
	private int												wait;
	// Internal data
	private int												phase;
	private float											blinkTime;
	private int												blinkWait;
	// 0: Not shown yet
	// 1: Shown but action not done
	// 2: Action done but still shown
	// 3: Done and about to selfdestroy
	
	public Helper( int m, final String func, float w) {
		me = m;
		function = func;
		wait = Timer.getTime() + (int) (w * Constant.timerResolution);
		phase = 0;
		blinkTime = Constant.getFloat("Helper_BlinkTime");
		Component.drawer.get(me).setVisible(false);
	}
	
	public boolean isActive() {
		return phase != 0;  
	}
	
	public void Update( ) {
		
		switch(phase) {
			case 0:
				if(Timer.getTime() > wait) {
					boolean otherHelpersActive = false;
					for (Map.Entry<Integer, Helper> entry : Component.helper.entrySet()) {
						otherHelpersActive |= entry.getValue().isActive();
					}
					
					if( otherHelpersActive ) {
						// Wait at least one second after no other helper is left
						wait = Timer.getTime() + Constant.timerResolution;
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

				if(Timer.getTime() > blinkWait) {
					blinkWait = Timer.getTime() + (int) (2*blinkTime * Constant.timerResolution);
					
					// Create annoying circle
					Placement p = Component.placement.get(me);
					Integer id = Component.getID();

					Component.timedObject.put( id, new TimedObject(id, blinkTime) );
					Component.placement.put( id, new Placement( new Vec2( p.getPosition().x, p.getPosition().y ), p.getScreenSide() ) );
					Component.drawer.put( id, new Drawer(id, Constant.getVector("Helper_Color")) );
					Component.shape.put( id, Constant.getShape("Helper_Shape")  );
					
					if(blinkTime > 0.2f) {
						blinkTime *= 0.85f;
					}
				}

			}
			break;
			
			case 2:
				if(Timer.getTime() > wait) {
					// Hide the helper and self destroy
					Component.drawer.get(me).setVisible(false);
					phase = 3;
					Component.deadObjects.add(me);
				}
				
			break;
		}
	}
}