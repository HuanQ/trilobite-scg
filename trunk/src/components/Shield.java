package components;

import data.Timer;

import managers.Clock;
import managers.Component;


public abstract class Shield {
	protected final int											me;
	protected final float										duration;
	private final Timer											shieldDuration;
	// Internal data
	private boolean												shieldUp = false;

	protected Shield( int m, float dur) {
		me = m;
		duration = dur;
		shieldDuration = new Timer( duration, Clock.game );
	}
	

	public final void Update() {
		if( shieldDuration.Check() && shieldUp ) {
			shieldUp = false;
			Down();
		}
	}
	
	protected abstract void Up();
	protected abstract void Down();
	
	public final void Raise() {
		shieldUp = true;
		
		shieldDuration.Refresh();
		
		// Record
		Record rec = Component.record.get(me);
		if( rec != null ) {
			rec.addEvent(Record.shield);
		}
		
		Up();		
	}
	
}
