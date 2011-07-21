package managers;

import graphics.TextureLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import geometry.Vec2;
import geometry.Vec3;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.Sys;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import components.Shape;
import data.NodeReader;


public class Constant {
	static private Map<String, Float>							myFloats;
	static private Map<String, Vec2>							myPoints;
	static private Map<String, Vec3>							myVectors;
	static private Map<String, Shape>							myShapes;
	static private Map<String, String>							myStrings;
	
	static public float											gravity; 
	static public long											timerResolution;
	static public TextureLoader									textureLoader;
	
	static public void Init() {
		myFloats = new HashMap<String, Float>();
		myPoints = new HashMap<String, Vec2>();
		myVectors = new HashMap<String, Vec3>();
		myShapes = new HashMap<String, Shape>();
		myStrings = new HashMap<String, String>();
		textureLoader = new TextureLoader();
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc;
			
			doc = (Document) db.parse( new File("resources/gamedata/constants.xml") );
			doc.getDocumentElement().normalize();
			loadXML( doc.getDocumentElement().getChildNodes() );
			
			doc = (Document) db.parse( new File("resources/gamedata/english.xml") );
			doc.getDocumentElement().normalize();
			loadXML( doc.getDocumentElement().getChildNodes() );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gravity = getFloat("Rules_Gravity");
		timerResolution = Sys.getTimerResolution();
	}
	
	static private void loadXML(final NodeList section) {
		// Trilobite node
		for (int s = 0; s < section.getLength(); ++s) {
			Node nextSection = section.item(s);
			if(nextSection.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			// World Player Ships Effects nodes
			NodeList type = nextSection.getChildNodes();
			for (int t = 0; t < type.getLength(); ++t) {
				Node nextType = type.item(t);
				if(nextType.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
			    String typeName = nextType.getNodeName() + "_";
			    
			    // Render Wall Dumb Spawner Explosion... nodes
			    NodeList property = nextType.getChildNodes();
			    for (int p = 0; p < property.getLength(); ++p) {
			    	Node nextProperty = property.item(p);
			    	if(nextProperty.getNodeType() != Node.ELEMENT_NODE)
						continue;
			    	
				    // Add the read nodes to each Map
				    if(nextProperty.getNodeName() == "Color") {
				    	myVectors.put( typeName + nextProperty.getNodeName(), NodeReader.readColor(nextProperty) );
				    }
				    else if(nextProperty.getNodeName() == "Point") {
				    	myPoints.put( typeName + nextProperty.getNodeName(), NodeReader.readPoint(nextProperty) );
				    }
				    else if(nextProperty.getNodeName() == "Sequence" || nextType.getNodeName() == "Text" ) {
				    	myStrings.put( typeName + nextProperty.getNodeName(), NodeReader.readString(nextProperty) );
				    }
				    else if(nextProperty.getNodeName() == "Shape") {
				    	myShapes.put( typeName + nextProperty.getNodeName(), NodeReader.readShape(nextProperty) );
				    }
				    else {
				    	myFloats.put( typeName + nextProperty.getNodeName(), NodeReader.readFloat(nextProperty) );
				    }
			    }
			}
		}
	}
	
	static public float getFloat( final String str ) {
		return (float) myFloats.get(str);
	}
	
	static public Vec2 getPoint( final String str ) {
		return myPoints.get(str);
	}
	
	static public Vec3 getVector( final String str ) {
		return myVectors.get(str);
	}
	
	static public Shape getShape( final String str ) {
		return myShapes.get(str);
	}
	
	static public String getString( final String str ) {
		return myStrings.get(str);
	}
}
