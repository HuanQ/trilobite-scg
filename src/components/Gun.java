package components;

import managers.Component;

public abstract class Gun {
	protected final int											me;
	// Internal data
	protected float												gunDist;
	
	protected Gun( int m, float gd ) {
		me = m;
		gunDist = gd;
	}
	
	protected void Shoot() {
	
		// Record
		Record rec = Component.record.get(me);
		if( rec != null) {
			rec.addEvent(Record.gunShot);
		}
	}
	
}
