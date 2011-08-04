package components;

import geometry.Polygon;

import managers.Clock;
import managers.Component;
import managers.Constant;


public class Shield {
	private final int											me;
	private int													lastShieldID = -1;
	// Internal data
	private final int											energyBarID;

	public Shield( int m ) {
		me = m;
		energyBarID = -1;
	}
	// Shield ha de saber la seva duracio i cooldown
	public Shield( int m, int eID ) {
		me = m;
		energyBarID = eID;
	}
	
	public final boolean isUp() {
		return Component.timedObject.get(lastShieldID) != null;
	}
	
	public final void Raise() {
		// Exhaust energy bar
		if(energyBarID >= 0) {
			Component.energybar.get(energyBarID).exhaust();
		}
		
		//TODO: S'ha de grabar la duració perquè cada actor tindrà duracions diferents		
		// Record
		Record rec = Component.record.get(me);
		if( rec != null) {
			rec.event( Record.shield, null );
		}
		
		Shape shp = Component.shape.get(me);
		if( shp == null || shp.polygons.isEmpty()) {
			//TODO: Shield for shapeless objects
		}
		else {
			// Draw a bigger self below me
			Integer id = Component.getID();
			Component.timedObject.put( id, new TimedObject(id, Constant.getFloat("Ship_ShieldTime"), Clock.game) );
			Component.shape.put( id, new Shape(Component.shape.get(me)) );
			Component.placement.put( id, Component.placement.get(me) );
			Component.drawer.put( id, new Drawer(id) );
			
			shp = Component.shape.get(id);
			
			for(Polygon p : shp.polygons) {
				p.multSize( Constant.getFloat("Ship_ShieldScale") );
				p.layer = Constant.getFloat("Ship_ShieldLayer");
				p.setColor( Constant.getVector("Ship_Color") );
			}
			
			lastShieldID = id;
		}
	}
}
