package managers;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class Clock {
	static private final int									numTypes = 4;
	
	static public final int										real = 0;
	static public final int										ui = 1;
	static public final int										play = 2;
	static public final int										game = 3;
	
	// Internal counters
	static private int											lastFrame;
	static private int											lastFPS;
	static private int											fps;
	static private long											startTime;

	// Internal data
	static private int[]										time;
	static private float[]										delta;
	static private int											pausedMask;
	
	static public final void Init() {
		startTime = Sys.getTime() * Constant.timerResolution / Sys.getTimerResolution();
		lastFrame = 0;
		fps = 0;
		lastFPS = 0;
		
		time = new int[numTypes];
		delta = new float[numTypes];
		for(int i=0;i<numTypes;++i) {
			time[i] = 0;
			delta[i] = 0;
		}

		pausedMask = 0;
	}

	static public final boolean isPaused( int type ) {
		return (pausedMask & (1 << type)) > 0;
	}
	
	static public final void pause( int type ) {
		pausedMask |= 1 << type;
	}
	
	static public final void unpause( int type ) {
		pausedMask &= 1 << ~type;
	}
	
	static public final int getTime( int type ) {
		return time[type];
	}
	
	static public final float getDelta( int type ) {
	    return delta[type];
	}
	
	static public final void Update() {
		// Real time
		time[real] = (int) (Sys.getTime() * Constant.timerResolution / Sys.getTimerResolution() - startTime);
		int dt = time[real] - lastFrame;
		delta[real] = (float) dt / Constant.timerResolution;
		lastFrame = time[real]; 
		
		for(int i=1;i<numTypes;++i) {
		    if( (pausedMask & (1<<i)) == 0 ) {
		    	time[i] += dt;
		    	delta[i] = delta[real];
		    }
		    else {
		    	delta[i] = 0;
		    }
		}
		
	    // Calculate FPS
	    if(time[real] - lastFPS > Constant.timerResolution) {
	        Display.setTitle("FPS: " + fps); 
	        fps = 0;
	        lastFPS += Constant.timerResolution;
	    }
	    fps++;
	}
}
