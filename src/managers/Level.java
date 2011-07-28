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
import geometry.Vec3;

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
	
	static public void Release() {
		// Level just creates components so we don't have to release anything yet
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
			    
			    //TODO: Podem fer un tag d'afegir altres XML aixi un nivell carrega els seus backgrounds, etc
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
				else if(nextType.getNodeName() == "Button") {
					AddButton( NodeReader.readPoint( readNodes.get("Point") ),
							NodeReader.readShape( readNodes.get("Shape") ),
							NodeReader.readString( readNodes.get("Function") )
							);
				}
				else if(nextType.getNodeName() == "Image") {
							AddImage( NodeReader.readPoint( readNodes.get("Point") ),
							NodeReader.readShape( readNodes.get("Shape") )
							);
				}
				else if(nextType.getNodeName() == "Helper") {
					AddHelper( NodeReader.readPoint( readNodes.get("Point") ),
					NodeReader.readShape( readNodes.get("Shape") ),
					NodeReader.readString( readNodes.get("Function") ),
					NodeReader.readFloat( readNodes.get("Wait") )
					);
				}
			}
		}
	}
	
	static public Vec2 getSpawnPoint( int sp ) {
		Vec2 pos = new Vec2();
		int spawnsRow = (int) Constant.getFloat("Rules_SpawnpointsPerRow");
		int counterX = sp % spawnsRow;
		int place; 
		if( (counterX & 1) > 0) {
			place = (spawnsRow/2) + (counterX+1)/2;
		}
		else {
			place = (spawnsRow/2) - (counterX+1)/2;
		}
		place %= spawnsRow;

		pos.x = (float) place / (spawnsRow - 1);
		pos.y = 1 - (float) (sp / spawnsRow) / (spawnsRow + 1); 
		pos.x = pos.x * 0.90f + 0.05f;
		pos.y -= 0.075f;
		return pos;
	}
	
	static public Integer AddPlayer( final String path, int number ) {
		Integer id = Component.getID();

		Component.keyboard.put( id, new KeyboardInput(id) );
		Component.placement.put( id, new Placement( getSpawnPoint(number), Placement.gameSide ) );
		Component.drawer.put( id, new Drawer(id, Vec3.white ) );
		Component.gun.put( id, new Gun(id, Constant.getPoint("Gun_Point")) );
		Component.shield.put( id, new Shield(id) );
		Component.mover.put( id, new Mover(id, Constant.getFloat("Ship_Speed"), true, 0) );
		Component.shape.put( id, Constant.getShape("Ship_Shape") );
		Component.canBeKilled.put( id, new Killable(id) );
		Component.canKill.put( id, new Killer() );
		Component.record.put( id, new Record(id, path) );

		Component.placement.get(id).setRotation( (float) Math.PI / 2 );
		Component.shield.get(id).Raise();
		Component.shape.get(id).setText(Integer.toString(number+1));

		return id;
	}
	
	static public void AddMouse() {
		Integer id = Component.getID();
		Component.mouse = new Pointer(id);
		Component.placement.put(id, new Placement(Placement.fullScreen) );
		Component.drawer.put( id, new Drawer(id) );
		Component.shape.put( id, Constant.getShape("Mouse_Shape") );
		
		Component.placement.get(id).interpPosition( Vec2.topLeft, Vec2.bottomRight, 0.5f);
	}
	
	static public Integer AddActor( final String path, int number ) {
		Integer id = Component.getID();

		Component.actor.put( id, new Actor(id, path) );
		Component.placement.put( id, new Placement(Placement.gameSide) );
		Component.drawer.put( id, new Drawer(id, Vec3.gray ) );
		Component.gun.put( id, new Gun(id, Constant.getPoint("Gun_Point")) );
		Component.shield.put( id, new Shield(id) );
		Component.mover.put( id, new Mover(id, Constant.getFloat("Ship_Speed"), true, 0) );
		Component.shape.put( id, new Shape(Constant.getShape("Ship_Shape")) );
		Component.canBeKilled.put( id, new Killable(id) );
		Component.canKill.put( id, new Killer() );

		Component.placement.get(id).setRotation( (float) Math.PI / 2 );
		Component.shape.get(id).setText(Integer.toString(number));

		return id;
	}
	
	static private Integer AddImage( final Placement pos, final Shape shp ) {
		Integer id = Component.getID();

		Component.placement.put( id, pos );
		Component.drawer.put( id, new Drawer( id ) );
		Component.shape.put( id, shp );

		return id;
	}
	
	static private Integer AddHelper( final Placement pos, final Shape shp, final String func, float wait ) {
		Integer id = Component.getID();

		Component.placement.put( id, pos );
		Component.drawer.put( id, new Drawer( id ) );
		Component.helper.put( id, new Helper( id, func, wait ) );
		Component.shape.put( id, shp );

		return id;
	}
	
	static private Integer AddButton( final Placement pos, final Shape shp, final String func ) {
		Integer id = Component.getID();

		Component.placement.put( id, pos );
		Component.drawer.put( id, new Drawer(id) );
		Component.planet.put( id, new Planet(id) );
		Component.shape.put( id, shp );
		Component.clickable.put(id, new Clickable(func) );

		return id;
	}
	
	static private Integer AddWall( final Placement pos, final Shape shp ) {
		Integer id = Component.getID();

		Component.placement.put( id, pos );
		Component.drawer.put( id, new Drawer(id) );
		Component.shape.put( id, shp );
		Component.canKill.put( id, new Killer() );
		Component.mover.put( id, new Mover(id, 0, false, 0) );

		return id;
	}
	
	static private Integer AddSpawner( final Placement pos, final Angle shootDirection, final String sequence, float rotSpeed, final Shape shp, float freq, int count ) {
		Integer id = Component.getID();

		Component.placement.put( id, pos );
		Component.spawner.put( id, new Spawner(id, shootDirection, sequence, rotSpeed, freq, count) );
		Component.mover.put( id, new Mover(id, 0, false, Constant.getFloat("Spawner_Gravity")) );
		Component.drawer.put( id, new Drawer(id) );
		Component.shape.put( id, shp );
		Component.canKill.put( id, new Killer() );
		
		return id;
	}
}
