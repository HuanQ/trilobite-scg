package managers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import geometry.Angle;
import geometry.Vec2;

import components.*;
import data.NodeReader;


public class Level {
	
	static private Map<String, Node>						readNodes;
	
	static public void Init( final String levelName ) {
		try {
			
			//TODO: Carregar diferents xml per a fer un nivell, aixi tenim un que es diu background que es pot reutilitzar i podem separar-los per parts.
			//... Intro, Lasers, etc (podrem canviar-ne l'ordre etc), caldra canviar la coordenada y amb un offset per a empalmar-los
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc;
			
			doc = (Document) db.parse( new File(levelName) );
			doc.getDocumentElement().normalize();
			loadXML( doc.getDocumentElement().getChildNodes() );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static private void loadXML(final NodeList section) {
		// Trilobite node
		for (int s = 0; s < section.getLength(); ++s) {
			Node nextSection = section.item(s);
			if(nextSection.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			// Intro ... nodes
			NodeList type = nextSection.getChildNodes();
			for (int t = 0; t < type.getLength(); ++t) {
				Node nextType = type.item(t);
				if(nextType.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
				readNodes = new HashMap<String, Node>();
				
				// Wall ... nodes
			    NodeList property = nextType.getChildNodes();
			    for (int p = 0; p < property.getLength(); ++p) {
			    	Node nextProperty = property.item(p);
			    	if(nextProperty.getNodeType() != Node.ELEMENT_NODE)
						continue;
			    	
			    	readNodes.put(nextProperty.getNodeName(), nextProperty);
			    }
			    
			    // Add the items with the read nodes they need 
				if(nextType.getNodeName() == "Wall") {
					AddWall( NodeReader.readPoint( readNodes.get("Point") ),
							NodeReader.readShape( readNodes.get("Shape") )
							);
				}
				else if(nextType.getNodeName() == "Spawner") {
					AddSpawner( NodeReader.readPoint( readNodes.get("Point") ),
							new Angle( NodeReader.readFloat(readNodes.get("Direction")) ),
							NodeReader.readString( readNodes.get("Sequence") ),
							NodeReader.readFloat(readNodes.get("RotationSpeed")),
							NodeReader.readShape( readNodes.get("Shape") ),
							NodeReader.readFloat( readNodes.get("Frequency") ),
							(int) NodeReader.readFloat( readNodes.get("Count") )
							);
				}
			}
		}
	}
	
	static public Integer AddPlayer() {
		Integer id = Component.getID();

		Component.keyboard.put( id, new KeyboardInput(id) );
		Component.placement.put( id, new Placement( Constant.getPoint("Green_Point") ) );
		Component.drawer.put( id, new Drawer(id, Constant.getVector("Green_Color") ) );
		Component.gun.put( id, new Gun(id, Constant.getPoint("Gun_Point")) );
		Component.shield.put( id, new Shield(id) );
		Component.mover.put( id, new Mover(id, Constant.getFloat("Ship_Speed"), true, 0) );
		Component.shape.put( id, Constant.getShape("Ship_Shape") );
		Component.canBeKilled.put( id, new CanBeKilled(id) );
		Component.canKill.put( id, new CanKill() );

		Component.placement.get(id).setRotation( (float) Math.PI / 2 );

		return id;
	}
	
	static private Integer AddWall( final Vec2 pos, final Shape shp ) {
		Integer id = Component.getID();

		Component.placement.put( id, new Placement( pos ) );
		Component.drawer.put( id, new Drawer( id ) );
		Component.shape.put( id, shp );
		Component.canKill.put( id, new CanKill() );
		Component.mover.put( id, new Mover( id, 0, false, 0 ) );

		return id;
	}
	
	static private Integer AddSpawner( final Vec2 pos, final Angle shootDirection, final String sequence, float rotSpeed, final Shape shp, float freq, int count ) {
		Integer id = Component.getID();

		Component.placement.put( id, new Placement( pos ) );
		Component.spawner.put( id, new Spawner(id, shootDirection, sequence, rotSpeed, freq, count) );
		Component.mover.put( id, new Mover( id, 0, false, Constant.getFloat("Spawner_Gravity") ) );
		Component.drawer.put( id, new Drawer(id, Constant.getVector("Spawner_Color") ) );
		Component.shape.put( id, shp );
		Component.canKill.put( id, new CanKill() );
		
		return id;
	}
}
