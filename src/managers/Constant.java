package managers;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import geometry.Vec2;
import geometry.Vec3;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.TrueTypeFont;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import components.Shape;
import data.NodeReader;


@SuppressWarnings("deprecation")
public class Constant {
	static private Map<String, Float>							myFloats;
	static private Map<String, Vec2>							myPoints;
	static private Map<String, Vec3>							myVectors;
	static private Map<String, Shape>							myShapes;
	static private Map<String, String>							myStrings;
	
	static public int											timerResolution;
	static public Random										rnd;
	static public TrueTypeFont									font[];
	
	static public final void Init() {
		myFloats = new HashMap<String, Float>();
		myPoints = new HashMap<String, Vec2>();
		myVectors = new HashMap<String, Vec3>();
		myShapes = new HashMap<String, Shape>();
		myStrings = new HashMap<String, String>();
		rnd = new Random();
		font = new TrueTypeFont[4];
		font[0] = loadFont("HEMIHEAD.TTF", 64);
		font[1] = loadFont("HEMIHEAD.TTF", 48);
		font[2] = loadFont("HEMIHEAD.TTF", 18);
		font[3] = loadFont("Stereofunk.ttf", 14);
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc;
			
			doc = (Document) db.parse( new File("resources/data/constants.xml") );
			doc.getDocumentElement().normalize();
			loadXML( doc.getDocumentElement().getChildNodes() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		timerResolution = (int) getFloat("Performance_TimerResolution");
	}
	
	static private final void loadXML(final NodeList section) {
		// Trilobite node
		for(int s = 0; s < section.getLength(); ++s) {
			Node nextSection = section.item(s);
			if(nextSection.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			// World Player Ships Effects nodes
			NodeList type = nextSection.getChildNodes();
			for(int t = 0; t < type.getLength(); ++t) {
				Node nextType = type.item(t);
				if(nextType.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
			    String typeName = nextType.getNodeName() + "_";
			    
			    // Render Wall Dumb Spawner Explosion... nodes
			    NodeList property = nextType.getChildNodes();
			    for(int p = 0; p < property.getLength(); ++p) {
			    	Node nextProperty = property.item(p);
			    	if(nextProperty.getNodeType() != Node.ELEMENT_NODE)
						continue;
			    	
				    // Add the read nodes to each Map
				    if(nextProperty.getNodeName().equals("Color")) {
				    	myVectors.put( typeName + nextProperty.getNodeName(), NodeReader.readColor(nextProperty) );
				    }
				    else if(nextProperty.getNodeName().equals("Point")) {
				    	myPoints.put( typeName + nextProperty.getNodeName(), NodeReader.readPoint(nextProperty) );
				    }
				    else if(nextProperty.getNodeName().equals("Sequence") || nextProperty.getNodeName().equals("Type")) {
				    	myStrings.put( typeName + nextProperty.getNodeName(), NodeReader.readString(nextProperty) );
				    }
				    else if(nextProperty.getNodeName().equals("Shape")) {
				    	myShapes.put( typeName + nextProperty.getNodeName(), NodeReader.readShape(nextProperty, "full") );
				    }
				    else {
				    	myFloats.put( typeName + nextProperty.getNodeName(), NodeReader.readFloat(nextProperty) );
				    }
			    }
			}
		}
	}
	
	static public final float getFloat( final String str ) {
		return (float) myFloats.get(str);
	}
	
	static public final Vec2 getPoint( final String str ) {
		return myPoints.get(str);
	}
	
	static public final Vec3 getVector( final String str ) {
		return myVectors.get(str);
	}
	
	static public final Shape getShape( final String str ) {
		return myShapes.get(str);
	}
	
	static public final String getString( final String str ) {
		return myStrings.get(str);
	}
	
	static private final TrueTypeFont loadFont( final String str, float size ) {
		// Load font from file
		try {
			FileInputStream fis = new FileInputStream(new File("resources/fonts/" + str));
			Font awtFont = Font.createFont(Font.TRUETYPE_FONT, fis );
			return new TrueTypeFont(awtFont.deriveFont(size), true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
