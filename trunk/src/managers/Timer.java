package managers;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class Timer {
	
	static private class Counter {
		private long											dueTime;
		private final float										refreshTime;
		
		Counter(final long now, final float refresh) {
			refreshTime = refresh;
			dueTime = now + (long) (refresh * Sys.getTimerResolution() );
		}
		
		private void ding() {
			dueTime = 0;
		}
	}
	
	// Internal counters
	static private long											lastFrame;
	static private long											lastFPS;
	static private int											fps;
	static private long											startTime;
	// Saved data 
	static private int											time;
	static private float										delta;
	// Game timers
	static private Map<String, Counter>							timers;
	
	static public void Init() {
		startTime = Sys.getTime() * Constant.timerResolution / Sys.getTimerResolution();
		lastFrame = 0;
		fps = 0;
		lastFPS = lastFrame;
		timers = new HashMap<String, Counter>();
		
		addTimer("GunCooldown", Constant.getFloat("Gun_Cooldown"));
		ding("GunCooldown");
		addTimer("ShieldTime", Constant.getFloat("Shield_Time"));
		ding("ShieldTime");
		addTimer("ShieldCooldown", Constant.getFloat("Shield_Cooldown"));
		ding("ShieldCooldown");
	}
	static public void addTimer(final String str, final float refreshTime) {
		timers.put( str, new Counter(time, refreshTime) );
	}

	static public boolean isReady(final String str) {
		if (timers.get(str).dueTime < time)
			return true;
		else 
			return false;
	}
	
	static public void ding(final String str) {
		timers.get(str).ding();
	}
	
	static public void exhaust(final String str) {
		timers.put(str, new Counter(time, timers.get(str).refreshTime));
	}
	
	static public void exhaust(final String str, final float newRefresh) {
		timers.put(str, new Counter(time, newRefresh));
	}
	
	static public int getTime() {
		return time;
	}
	
	static public float getDelta() {
	    return delta;
	}
	
	static public void Update() {
		time = (int) (Sys.getTime() * Constant.timerResolution / Sys.getTimerResolution() - startTime);
	    delta = (float) (time - lastFrame) / Constant.timerResolution;
	    lastFrame = time;
	    if (time - lastFPS > Constant.timerResolution) {
	        Display.setTitle("FPS: " + fps); 
	        fps = 0;
	        lastFPS += Constant.timerResolution;
	    }
	    fps++;
	}
}
