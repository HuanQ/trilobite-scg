package components;

import managers.Component;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Xml {
	private final int										me;
		
	public Xml( int m ) {
		me = m;
	}
	
	public final void writeXml( final Document doc, final Element root ) {
		if( Component.spawner.get(me) == null ) {
			// Wall
			Element wall = doc.createElement("Wall");
			// Point
			//TODO: Passar a cada component totes les funcions aixi, posarem mes privats i menys imports ja veuras!
			Component.placement.get(me).writeXml(doc, wall);
			// Shape
			Component.shape.get(me).writeXml(doc, wall);
			root.appendChild(wall);
		}
		else {
			// Spawner
			Element spawner = doc.createElement("Spawner");
			// Point
			Component.placement.get(me).writeXml(doc, spawner);
			// Shape
			if(Component.shape.get(me) != null) {
				Component.shape.get(me).writeXml(doc, spawner);
			}
			// Shape
			Component.spawner.get(me).writeXml(doc, spawner);
			root.appendChild(spawner);
		}
	}
	
	
	
	//TODO: Fer static les privades que no utilitzin atributs
}
