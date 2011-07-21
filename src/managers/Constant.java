package managers;

import geometry.Circle;
import geometry.Polygon;
import geometry.Rectangle;
import graphics.TextureLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.Sys;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import components.Shape;

public class Constant {
	static private Map<String, Float>							myFloats;
	static private Map<String, Vector2f>						myPoints;
	static private Map<String, Vector3f>						myVectors;
	static private Map<String, Shape>							myShapes;
	static private Map<String, String>							myStrings;
	
	static public Vector2f										gravity; 
	static public long											timerResolution;
	static public TextureLoader									textureLoader;
	
	static public void Init() {
		myFloats = new HashMap<String, Float>();
		myPoints = new HashMap<String, Vector2f>();
		myVectors = new HashMap<String, Vector3f>();
		myShapes = new HashMap<String, Shape>();
		myStrings = new HashMap<String, String>();
		textureLoader = new TextureLoader();
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc;
			
			doc = (Document) db.parse( new File("resources/gamedata/constants.xml") );
			doc.getDocumentElement().normalize();
			loadXML( doc.getDocumentElement().getChildNodes() );
			
			/*doc = (Document) db.parse( new File("bin/resources/gamedata/english.xml") );
			doc.getDocumentElement().normalize();
			loadXML( doc.getDocumentElement().getChildNodes() );*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gravity = new Vector2f(0, getFloat("Rules_Gravity"));
		timerResolution = Sys.getTimerResolution();
	}
	
	static public void loadXML(final NodeList section) {
		for (int s = 0; s < section.getLength(); ++s) {
			Node nextSection = section.item(s);
			if(nextSection.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			NodeList type = nextSection.getChildNodes();
			for (int t = 0; t < type.getLength(); ++t) {
				Node nextType = type.item(t);
				if(nextType.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
			    String typeName = nextType.getNodeName() + "_";
			    
			    NodeList property = nextType.getChildNodes();
			    for (int p = 0; p < property.getLength(); ++p) {
			    	Node nextProperty = property.item(p);
			    	if(nextProperty.getNodeType() != Node.ELEMENT_NODE)
						continue;
			    	
				    if(nextProperty.getNodeName() == "Color") {
				    	myVectors.put( typeName + nextProperty.getNodeName(), readColor(nextProperty) );
				    }
				    else if(nextProperty.getNodeName() == "Point") {
				    	myPoints.put( typeName + nextProperty.getNodeName(), readPoint(nextProperty) );
				    }
				    else if(nextProperty.getNodeName() == "Sequence" || nextType.getNodeName() == "Text" ) {
				    	myStrings.put( typeName + nextProperty.getNodeName(), nextProperty.getTextContent() );
				    }
				    else if(nextProperty.getNodeName() == "Shape") {
				    	String name = typeName + nextProperty.getNodeName();
				    	Shape myShape = new Shape();
				    	
				    	NodeList shape = nextProperty.getChildNodes();
					    for (int shp = 0; shp < shape.getLength(); ++shp) {
					    	Node nextShape = shape.item(shp);
					    	if(nextShape.getNodeType() != Node.ELEMENT_NODE)
								continue;
					    	
					    	if(nextShape.getNodeName() == "Circle") {
					    		myShape.add( readCircle(nextShape) );
					    	}
					    	
					    	if(nextShape.getNodeName() == "Rectangle") {
					    		myShape.add( readRectangle(nextShape) );
					    	}
					    }
					    
					    myShapes.put(name, myShape );
				    }
				    else {
				    	String name = typeName + nextProperty.getNodeName();
				    	myFloats.put(name, Float.valueOf(nextProperty.getTextContent()) );
				    }
			    }
			}
		}
	}
	
	static public Rectangle readRectangle(  final Node node ) {
		Vector3f myPoint = new Vector3f();
		Vector2f mySize = new Vector2f();
		NamedNodeMap attr = node.getAttributes();
    	myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
    	myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
    	myPoint.z = Float.valueOf( attr.getNamedItem("z").getTextContent() );
    	mySize.x = Float.valueOf( attr.getNamedItem("sizex").getTextContent() );
    	mySize.y = Float.valueOf( attr.getNamedItem("sizey").getTextContent() );
    	return (Rectangle) readSubShape( node, new Rectangle(mySize, myPoint) );
	}
	
	static public Polygon readSubShape(  final Node node, Polygon p ) {
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
		Vector3f myPoint = new Vector3f();
		float radius;
		NamedNodeMap attr = node.getAttributes();
		myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
		myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
		myPoint.z = Float.valueOf( attr.getNamedItem("z").getTextContent() );
		radius = Float.valueOf( attr.getNamedItem("radius").getTextContent() );
		
    	return (Circle) readSubShape( node, new Circle(radius, myPoint) );
	}
	
	static public Vector2f readPoint(  final Node node ) {
		Vector2f myPoint = new Vector2f();
    	NamedNodeMap attr = node.getAttributes();
    	myPoint.x = Float.valueOf( attr.getNamedItem("x").getTextContent() );
    	myPoint.y = Float.valueOf( attr.getNamedItem("y").getTextContent() );
    	return myPoint;
	}
	
	static public Vector3f readColor( final Node node ) {
		Vector3f myColor = new Vector3f();
    	NamedNodeMap attr = node.getAttributes();
    	myColor.x = Float.valueOf( attr.getNamedItem("r").getTextContent() );
    	myColor.y = Float.valueOf( attr.getNamedItem("g").getTextContent() );
    	myColor.z = Float.valueOf( attr.getNamedItem("b").getTextContent() );
    	return myColor;
	}
	
	static public float getFloat( final String str ) {
		return (float) myFloats.get(str);
	}
	
	static public Vector2f getPoint( final String str ) {
		return myPoints.get(str);
	}
	
	static public Vector3f getVector( final String str ) {
		return myVectors.get(str);
	}
	
	static public Shape getShape( final String str ) {
		return myShapes.get(str);
	}
	
	static public String getString( final String str ) {
		return myStrings.get(str);
	}
}
