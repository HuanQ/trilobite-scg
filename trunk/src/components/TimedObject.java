package components;

import managers.Component;
import managers.Constant;
import managers.Clock;

public class TimedObject {

	private final int											me;
	private final int											duration;
	private final int											timerType;
	
	public TimedObject( int m, float dur, int timerty) {
		me = m;
		duration = Clock.getTime(timerty) + (int) (dur * Constant.timerResolution );
		timerType = timerty;
	}
	
	public final void Update() {
		if( duration < Clock.getTime(timerType)  ) {
			Component.deadObjects.add(me);
		}
	}
}
