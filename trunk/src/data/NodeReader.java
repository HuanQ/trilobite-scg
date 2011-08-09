package data;

import geometry.Circle;
import geometry.Polygon;
import geometry.Rectangle;
import geometry.Text;
import geometry.Vec2;
import geometry.Vec3;

import managers.Screen;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import components.Shape;

public class NodeReader {

	static public final Shape readShape(  final Node node, final String zone ) {
		Shape myShape = null;
		
		if(node != null) {
			myShape = new Shape();
			NodeList shape = node.getChildNodes();
		    for(int shp = 0; shp < shape.getLength(); ++shp) {
		    	Node nextShape = shape.item(shp);
		    	if(nextShape.getNodeType() != Node.ELEMENT_NODE)
					continue;
		    	
		    	if(nextShape.getNodeName() == "Circle") {
		    		myShape.add( NodeReader.readCircle(nextShape, zone) );
		    	}
		    	else if(nextShape.getNodeName() == "Rectangle") {
		    		myShape.add( NodeReader.readRectangle(nextShape, zone) );
		    	}
		    	else if(nextShape.getNodeName() == "Text") {
		    		myShape.add( NodeReader.readText(nextShape, zone) );
		    	}
		    }
		}
		
	    return myShape;
	}
	
	static public final String readString( final Node node ) {
		return node.getTextContent();
	}
	
	static public final float readFloat( final Node node ) {
		return Float.valueOf(node.getTextContent());
	}
	
	static public final Text readText( final Node node, final String zone ) {
		Vec2 myPoint = new Vec2();
		NamedNodeMap attr = node.getAttributes();
    	myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
    	myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
    	float layer = Float.valueOf( attr.getNamedItem("z").getTextContent() );
    	String myText = attr.getNamedItem("txt").getTextContent();
    	int size = (int) (float) Float.valueOf(attr.getNamedItem("size").getTextContent());
    	
    	Screen.rescale(zone, myPoint, false);
    	
    	return (Text) readSubShape( node, new Text(myText, myPoint, layer, size) );
	}
	
	static public final Rectangle readRectangle( final Node node, final String zone ) {
		Vec2 myPoint = new Vec2();
		Vec2 mySize = new Vec2();
		NamedNodeMap attr = node.getAttributes();
    	myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
    	myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
    	float layer = Float.valueOf( attr.getNamedItem("z").getTextContent() );
    	mySize.x = Float.valueOf( attr.getNamedItem("sizex").getTextContent() );
    	mySize.y = Float.valueOf( attr.getNamedItem("sizey").getTextContent() );
    	
    	
    	Node str = attr.getNamedItem("stretch");
    	boolean stretch = true;
    	if(str != null) {
    		stretch = Boolean.valueOf(str.getTextContent());
    	}
    	Screen.rescale(zone, mySize, stretch);
    	Screen.rescale(zone, myPoint, false);
    	
    	return (Rectangle) readSubShape( node, new Rectangle(mySize, myPoint, layer, stretch) );
	}
	
	static public final Polygon readSubShape( final Node node, Polygon p ) {
		NodeList color = node.getChildNodes();
	    for(int col = 0; col < color.getLength(); ++col) {
	    	Node nextCol = color.item(col);
	    	if(nextCol.getNodeType() != Node.ELEMENT_NODE)
				continue;
	    	
	    	if(nextCol.getNodeName() == "Color") {
		    	p.setColor( readColor(nextCol) );
		    }
	    	else if(nextCol.getNodeName() == "Texture" ) {
	    		p.setTexture(nextCol.getTextContent());
		    }
	    }
	    return p;
	}
	
	static public final Circle readCircle( final Node node, final String zone ) {
		Vec2 myPoint = new Vec2();
		NamedNodeMap attr = node.getAttributes();
		myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
		myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
		float layer = Float.valueOf( attr.getNamedItem("z").getTextContent() );
		float radius = Float.valueOf( attr.getNamedItem("radius").getTextContent() );
		
		radius = Screen.rescale(zone, radius);
		Screen.rescale(zone, myPoint, false);
    	return (Circle) readSubShape( node, new Circle(radius, myPoint, layer) );
	}
	
	static public final Vec2 readPoint( final Node node ) {
		NamedNodeMap attr = node.getAttributes();
    	Vec2 pos = new Vec2();
    	pos.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
    	pos.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
    	return pos;
	}
	
	static public final Vec2 readPlace( final Node node, final String zone ) {
    	NamedNodeMap attr = node.getAttributes();
    	Vec2 pos = new Vec2();
    	pos.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
    	pos.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
    	Screen.reposition(zone, pos);

    	return pos;
	}
	
	static public final Vec3 readColor( final Node node ) {
		Vec3 myColor = new Vec3();
    	NamedNodeMap attr = node.getAttributes();
    	myColor.x = Float.valueOf( attr.getNamedItem("r").getTextContent() );
    	myColor.y = Float.valueOf( attr.getNamedItem("g").getTextContent() );
    	myColor.z = Float.valueOf( attr.getNamedItem("b").getTextContent() );
    	return myColor;
	}
	
}
