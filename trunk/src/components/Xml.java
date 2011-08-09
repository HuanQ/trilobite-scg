package components;

import managers.Component;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Xml {
	private final int										me;
		
	public Xml( int m ) {
		me = m;
	}
	
	public final void WriteXML( final Document doc, final Element root ) {
		Element elem = null;
		// Find out which component to use 
		if( Component.spawner.get(me) != null ) {
			// Spawner
			elem = doc.createElement("Spawner");
			Component.spawner.get(me).WriteXML(doc, elem);
		}
		else if( Component.killable.get(me) != null) 
		{
			// Wall
			elem = doc.createElement("Wall");
		}
		else {
			// Decoration
			elem = doc.createElement("Decoration");
			if( Component.sticky.get(me) != null ) {
				float spd = Component.sticky.get(me).getSpeed();
				elem.setAttribute( "speed", String.valueOf(spd) );
			}
		}

		// Point
		Component.placement.get(me).WriteXML(doc, elem);
		// Shape
		if(Component.shape.get(me) != null) {
			Component.shape.get(me).WriteXML(doc, elem);
		}
		root.appendChild(elem);
	}
}
