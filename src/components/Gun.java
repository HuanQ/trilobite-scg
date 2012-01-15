package components;

import managers.Component;

public abstract class Gun {
	protected final int											me;
	// Internal data
	protected float												gunDist;
	
	//TODO I si fem qye els components no tinguin mai herencia, pero algun membre en composicio si? Pot simplificar-ne la manipulacio
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
