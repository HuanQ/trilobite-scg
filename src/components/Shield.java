package components;

import geometry.Polygon;

import java.util.Iterator;

import managers.Component;
import managers.Constant;
import managers.Timer;


public class Shield {
	private final int											me;

	public Shield( int m) {
		me = m;
	}
	
	public boolean isUp() {
		return !Timer.isReady("ShieldTime");
	}
	
	public void Raise() {
		
		// Record
		Record rec = Component.record.get(me);
		if( rec != null) {
			rec.event( Record.shield, null );
		}
		
		Shape shp = Component.shape.get(me);
		if( shp == null || shp.getPolygons().isEmpty()) {
			//TODO: Shield for shapeless objects
		}
		else {
			// Draw a bigger self below me
			Integer id = Component.getID();
			Component.timedObject.put( id, new TimedObject(id, Constant.getFloat("Shield_Time")) );
			Component.shape.put( id, new Shape(Component.shape.get(me)) );
			Component.placement.put( id, Component.placement.get(me) );
			Component.drawer.put( id, new Drawer(id) );
			
			shp = Component.shape.get(id);
			
			for (Iterator<Polygon> iter = shp.getPolygons().iterator(); iter.hasNext();) {
				Polygon next = iter.next();
				next.multSize( Constant.getFloat("Shield_Scale") );
				next.getOffset().z = Constant.getFloat("Shield_Layer");
				next.setColor( Constant.getVector("Shield_Color") );
			}
		}
		Timer.exhaust("ShieldTime");
	}
}
