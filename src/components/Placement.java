package components;

import managers.Screen;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import geometry.Angle;

import geometry.Vec2;

public class Placement {
	public Vec2													position;
	public Angle												angle = new Angle();
	
	//TODO: Afegir rotations a tots els objectes (draw polygons i poster gunpoint i poc mes)
	public Placement() {
		position = new Vec2();
	}
	
	public Placement( final Vec2 p ) {
		position = p;
	}
	
	public Placement(Placement p) {
		position = new Vec2(p.position);
		angle = p.angle;
	}

	public final void interpPosition( final Vec2 start, final Vec2 end, float step ) {
		position.interpolate(start, end, step);
	}

	public final void setRotation( float r ) {
		angle.set(r);
	}
	
	public final void addRotation( float r ) {
		angle.add(r);
	}
	
	public final void writeXml( Document doc, Element root ) {
		Element point = doc.createElement("Point");
		Vec2 myPos = new Vec2(position);
		Screen.deposition("game", myPos);
		point.setAttribute( "x", Float.toString(myPos.x) );
		point.setAttribute( "y", Float.toString(myPos.y) );
		root.appendChild(point);
	}
}
