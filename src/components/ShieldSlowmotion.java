package components;

import managers.Clock;
import managers.Component;


public class ShieldSlowmotion extends Shield {

	public ShieldSlowmotion( int m, float dur ) {
		super(m, dur);
	}
	
	protected final void Down() {
		// Slow time down unless i'm an actor
		if( Component.actor.get(me) == null ) {
			Clock.Unslow(Clock.game);
		}
	}
	
	protected final void Up() {
		// Slow time down unless i'm an actor
		if( Component.actor.get(me) == null ) {
			Clock.Slow(Clock.game);
		}
	}

}
