package components;

import geometry.Polygon;

import java.util.Iterator;

import managers.Component;
import managers.Constant;
import managers.Timer;



public class Shield {
	private final int											me;

	public Shield(final int m) {
		me = m;
	}
	
	public boolean isUp() {
		return !Timer.isReady("ShieldTime");
	}
	
	public void Raise() {
		// Dibuixar shield amb shapes
		
		Integer id = Component.getID();
		Component.timedObject.put( id, new TimedObject(id, Constant.getFloat("Shield_Time")) );
		Component.shape.put( id, new Shape(Component.shape.get(me)) );
		Component.placement.put( id, Component.placement.get(me) );
		Component.drawer.put( id, new Drawer(id) );
		
		Shape shp = Component.shape.get(id);
		
		for (Iterator<Polygon> iter = shp.getPolygons().iterator(); iter.hasNext();) {
			Polygon next = (Polygon) iter.next();
			next.multSize( Constant.getFloat("Shield_Scale") );
			next.getOffset().z = -1;
			next.setColor( Constant.getVector("Shield_Color") );
		}
		
		Timer.exhaust("ShieldTime");
	}
}
