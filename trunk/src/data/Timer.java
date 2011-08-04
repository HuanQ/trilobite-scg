package data;

import managers.Clock;
import managers.Constant;

public class Timer {
	private int												timerType;
	private int												dueTime;
	private final float										refresh;
	
	public Timer( float rfsh, int ttyp ) {
		dueTime = Clock.getTime(ttyp) + (int) (rfsh * Constant.timerResolution);
		refresh = rfsh;
		timerType = ttyp;
	}
	
	public boolean Check() {
		return Clock.getTime(timerType) > dueTime;
	}
	
	public void Refresh() {
		dueTime = Clock.getTime(timerType) + (int) (refresh * Constant.timerResolution);
	}
}