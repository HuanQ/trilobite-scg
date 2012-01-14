package components;

import managers.Clock;
import managers.Component;
import managers.Constant;


public class ShieldWings extends Shield {

	public ShieldWings( int m, float dur ) {
		super(m, dur);
	}
	
	protected final void Down() {}
	
	protected final void Up() {
		Shape shp = Component.shape.get(me);
		if( shp != null ) {
			// Draw a bigger self below me
			Integer id = Component.getID();
			Component.timedObject.put( id, new Timed(id, duration, Clock.game) );
			Component.shape.put( id, Constant.getShape("Wings_Shape") );
			Component.placement.put( id, Component.placement.get(me) );
			Component.drawer.put( id, new Drawer(id) );
			Component.killable.put( id, new Killable(id, Killable.bulletShieldTeam ) );
			
			Component.killable.get(me).addNoKill(id);
		}
	}

}
