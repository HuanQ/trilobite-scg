package components;

import managers.Component;
import managers.Constant;
import managers.Timer;

public class TimedObject {

	private final int											me;
	private final float											duration;
	
	public TimedObject( int m, float dur) {
		me = m;
		duration = Timer.getTime() + dur * Constant.timerResolution;
	}
	
	public void Update() {
		if( duration < Timer.getTime()  ) {
			Component.deadObjects.add(me);
		}
	}
}
