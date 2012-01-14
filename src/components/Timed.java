package components;

import managers.Component;
import managers.Constant;
import managers.Clock;

public class Timed {

	private final int											me;
	private int													duration;
	private final int											timerType;
	
	public Timed( int m, float dur, int timerty) {
		me = m;
		duration = Clock.getTime(timerty) + (int) (dur * Constant.timerResolution );
		timerType = timerty;
	}
	
	public final void Extend( float ext ) {
		duration += (int) (ext * Constant.timerResolution );
	}
	
	public final void Update() {
		if( duration < Clock.getTime(timerType)  ) {
			Component.deadObjects.add(me);
		}
	}
}
