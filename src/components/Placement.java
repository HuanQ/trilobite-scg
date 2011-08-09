package components;

import managers.Screen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import geometry.Angle;

import geometry.Vec2;

public class Placement {
	public Vec2													position;
	public Angle												angle = new Angle();
	
	public Placement() {
		position = new Vec2();
	}
	
	public Placement( final Vec2 p ) {
		position = p;
	}
	
	public Placement(Placement p) {
		position = new Vec2(p.position);
		angle.set( p.angle.get() );
	}

	public final void WriteXML( Document doc, Element root ) {
		Element point = doc.createElement("Point");
		Vec2 myPos = new Vec2(position);
		Screen.deposition("game", myPos);
		point.setAttribute( "x", Float.toString(myPos.x) );
		point.setAttribute( "y", Float.toString(myPos.y) );
		root.appendChild(point);
	}
}
