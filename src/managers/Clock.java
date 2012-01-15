package managers;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class Clock {
	static private final int								numTypes = 4;

	static public final int									real = 0;
	static public final int									ui = 1;
	static public final int									play = 2;
	static public final int									game = 3;

	// Internal counters
	static private int										lastFrame;
	static private int										lastFPS;
	static private int										fps;
	static private long										startTime;

	// Internal data
	static private int[]									time;
	static private float[]									delta;
	static private float[]									lostRoundingTime;
	static private int										pausedMask;
	static private int										slowMask;

	static public final void Init() {
		startTime = Sys.getTime() * Constant.timerResolution
				/ Sys.getTimerResolution();
		lastFrame = 0;
		fps = 0;
		lastFPS = 0;

		time = new int[numTypes];
		delta = new float[numTypes];
		lostRoundingTime = new float[numTypes];
		
		for (int i = 0; i < numTypes; ++i) {
			time[i] = 0;
			delta[i] = 0;
			lostRoundingTime[i] = 0;
		}

		pausedMask = 0;
		slowMask = 0;
	}

	static public final void UnmaskAll() {
		pausedMask = 0;
		slowMask = 0;
	}
	
	static public final boolean isSlow(int type) {
		return (slowMask & (1 << type)) > 0;
	}

	static public final void Slow(int type) {
		slowMask |= 1 << type;
	}

	static public final void Unslow(int type) {
		slowMask &= 1 << ~type;
	}

	static public final boolean isPaused(int type) {
		return (pausedMask & (1 << type)) > 0;
	}

	static public final void Pause(int type) {
		pausedMask |= 1 << type;
	}

	static public final void Unpause(int type) {
		pausedMask &= 1 << ~type;
	}

	static public final int getTime(int type) {
		return time[type];
	}

	static public final float getDelta(int type) {
		return delta[type];
	}

	static public final void Update() {
		
		//TODO Fer sistema de fixedframes on tot passa 50 cops per segon independentment de framerate. Si baixa n'esquipejem un (potser error) o simplement anulem la partida. Un sistema dinamic podria canviar 50 per un numero adecuat.
		float slowmo = Constant.getFloat("Rules_SlowMotion");
		// Real time
		time[real] = (int) (Sys.getTime() * Constant.timerResolution
				/ Sys.getTimerResolution() - startTime);
		int dt = time[real] - lastFrame;
		delta[real] = (float) dt / Constant.timerResolution;
		lastFrame = time[real];

		for (int i = 1; i < numTypes; ++i) {
			if ((pausedMask & (1 << i)) > 0) {
				// Pause
				delta[i] = 0;
			}
			else if ((slowMask & (1 << i)) > 0) {
				int slowedDownTime = (int) (dt * slowmo);
				
				// Slow motion
				time[i] += slowedDownTime;
				delta[i] = delta[real] * slowmo;
				
				// Slow motion skips time due to rounding
				lostRoundingTime[i] += dt * slowmo - slowedDownTime; 
				if(lostRoundingTime[i] > 1) {
					++time[i];
					--lostRoundingTime[i];
				}
				
			}
			else {
				// Normal
				time[i] += dt;
				delta[i] = delta[real];				
			}
		}

		// Calculate FPS
		if (time[real] - lastFPS > Constant.timerResolution) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += Constant.timerResolution;
		}
		fps++;
		
		//TODO Calibrar la consistencia del temps amb aquest codi i screenshots 
		/*if ( time[game] > 13000 && !isPaused(game) ) {
			Pause(game);
			System.out.println(time[game]);
		}*/
	}
}
