package data;

import geometry.Circle;
import geometry.Polygon;
import geometry.Rectangle;
import geometry.Text;
import geometry.Vec2;
import geometry.Vec3;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import components.Shape;

public class NodeReader {
	

	static public Shape readShape(  final Node node ) {
		Shape myShape = new Shape();
		
		NodeList shape = node.getChildNodes();
	    for (int shp = 0; shp < shape.getLength(); ++shp) {
	    	Node nextShape = shape.item(shp);
	    	if(nextShape.getNodeType() != Node.ELEMENT_NODE)
				continue;
	    	
	    	if(nextShape.getNodeName() == "Circle") {
	    		myShape.add( NodeReader.readCircle(nextShape) );
	    	}
	    	
	    	if(nextShape.getNodeName() == "Rectangle") {
	    		myShape.add( NodeReader.readRectangle(nextShape) );
	    	}
	    	
	    	if(nextShape.getNodeName() == "Text") {
	    		myShape.add( NodeReader.readText(nextShape) );
	    	}
	    }
	    
	    return myShape;
	}
	
	static public String readString( final Node node ) {
		return node.getTextContent();
	}
	
	static public float readFloat( final Node node ) {
		return Float.valueOf(node.getTextContent());
	}
	
	static public Text readText( final Node node ) {
		Vec3 myPoint = new Vec3();
		NamedNodeMap attr = node.getAttributes();
    	myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
    	myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
    	myPoint.z = Float.valueOf( attr.getNamedItem("z").getTextContent() );
    	String myText = attr.getNamedItem("txt").getTextContent();
    	return (Text) readSubShape( node, new Text(myText, myPoint) );
	}
	
	static public Rectangle readRectangle( final Node node ) {
		Vec3 myPoint = new Vec3();
		Vec2 mySize = new Vec2();
		NamedNodeMap attr = node.getAttributes();
    	myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
    	myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
    	myPoint.z = Float.valueOf( attr.getNamedItem("z").getTextContent() );
    	mySize.x = Float.valueOf( attr.getNamedItem("sizex").getTextContent() );
    	mySize.y = Float.valueOf( attr.getNamedItem("sizey").getTextContent() );
    	return (Rectangle) readSubShape( node, new Rectangle(mySize, myPoint) );
	}
	
	static public Polygon readSubShape( final Node node, Polygon p ) {
		NodeList color = node.getChildNodes();
	    for (int col = 0; col < color.getLength(); ++col) {
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
	
	static public Circle readCircle(  final Node node ) {
		Vec3 myPoint = new Vec3();
		float radius;
		NamedNodeMap attr = node.getAttributes();
		myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
		myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
		myPoint.z = Float.valueOf( attr.getNamedItem("z").getTextContent() );
		radius = Float.valueOf( attr.getNamedItem("radius").getTextContent() );
		
    	return (Circle) readSubShape( node, new Circle(radius, myPoint) );
	}
	
	static public Vec2 readPoint(  final Node node ) {
		Vec2 myPoint = new Vec2();
    	NamedNodeMap attr = node.getAttributes();
    	myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
    	myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
    	return myPoint;
	}
	
	static public Vec3 readColor( final Node node ) {
		Vec3 myColor = new Vec3();
    	NamedNodeMap attr = node.getAttributes();
    	myColor.x = Float.valueOf( attr.getNamedItem("r").getTextContent() );
    	myColor.y = Float.valueOf( attr.getNamedItem("g").getTextContent() );
    	myColor.z = Float.valueOf( attr.getNamedItem("b").getTextContent() );
    	return myColor;
	}
	
}
