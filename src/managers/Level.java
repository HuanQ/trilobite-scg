package managers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sequences.Sequence;
import sequences.SimpleBlaster;
import sequences.SimpleDumb;

import geometry.Angle;
import geometry.Vec2;
import geometry.Vec3;

import components.*;
import data.NodeReader;


public class Level {
	static public String									lvlname;
	
	static public void Init( final String str, final String lvln ) {
		lvlname = lvln;
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc;
			
			doc = (Document) db.parse( new File(str) );
			doc.getDocumentElement().normalize();
			loadXML( doc.getDocumentElement().getChildNodes() );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static private final void loadXML(final NodeList section) {
		// Trilobite node
		for(int s = 0; s < section.getLength(); ++s) {
			Node nextSection = section.item(s);
			if(nextSection.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			NamedNodeMap attr = nextSection.getAttributes();
			String zone = attr.getNamedItem("zone").getTextContent();

			// Intro ... nodes
			NodeList type = nextSection.getChildNodes();
			for(int t = 0; t < type.getLength(); ++t) {
				Node nextType = type.item(t);
				if(nextType.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
				Map<String, Node> readNodes = new HashMap<String, Node>();
				
				// Wall ... nodes
			    NodeList property = nextType.getChildNodes();
			    for(int p = 0; p < property.getLength(); ++p) {
			    	Node nextProperty = property.item(p);
			    	if(nextProperty.getNodeType() != Node.ELEMENT_NODE)
						continue;
			    	
			    	readNodes.put(nextProperty.getNodeName(), nextProperty);
			    }

			    //TODO: Podem fer un tag d'afegir altres XML aixi un nivell carrega els seus backgrounds, etc
			    // Add the items with the read nodes they need 
				if(nextType.getNodeName() == "Wall") {
					AddWall( NodeReader.readPlace( readNodes.get("Point"), zone ),
							NodeReader.readShape( readNodes.get("Shape"), zone )
							);
				}
				else if(nextType.getNodeName() == "Spawner") {
					Sequence seq = getSequence( NodeReader.readString(readNodes.get("Sequence")) );
					Shape shp = NodeReader.readShape( readNodes.get("Shape"), zone );
					
					AddSpawner( NodeReader.readPlace( readNodes.get("Point"), zone ),
							new Angle( NodeReader.readFloat(readNodes.get("Direction")) ),
							NodeReader.readFloat( readNodes.get("Wait") ),
							seq,
							shp
							);
				}
				else if(nextType.getNodeName() == "Button") {
					AddButton( NodeReader.readPlace( readNodes.get("Point"), zone ),
							NodeReader.readShape( readNodes.get("Shape"), zone ),
							NodeReader.readString( readNodes.get("Function") )
							);
				}
				else if(nextType.getNodeName() == "Planet") {
					AddPlanet( NodeReader.readPlace( readNodes.get("Point"), zone ),
							NodeReader.readShape( readNodes.get("Shape"), zone ),
							NodeReader.readString( readNodes.get("Function") )
							);
				}
				else if(nextType.getNodeName() == "Image") {
							AddImage( NodeReader.readPlace( readNodes.get("Point"), zone ),
							NodeReader.readShape( readNodes.get("Shape"), zone )
							);
				}
				else if(nextType.getNodeName() == "Helper") {
					AddHelper( NodeReader.readPlace( readNodes.get("Point"), zone ),
					NodeReader.readShape( readNodes.get("Shape"), zone ),
					NodeReader.readString( readNodes.get("Function") ),
					NodeReader.readFloat( readNodes.get("Wait") )
					);
				}
				else if(nextType.getNodeName() == "EnergyBar") {
					AddEnergyBar( NodeReader.readPlace( readNodes.get("Point"), zone ),
					NodeReader.readShape( readNodes.get("Shape"), zone ),
					NodeReader.readColor( readNodes.get("Empty") ),
					NodeReader.readColor( readNodes.get("Full") )
					);
				}
				else if(nextType.getNodeName() == "ProgressBar") {
					AddProgressBar( NodeReader.readPlace( readNodes.get("Point"), zone ),
					NodeReader.readShape( readNodes.get("Shape"), zone ),
					NodeReader.readColor( readNodes.get("Color") )
					);
				}
			}
		}
	}
	
	static public final Vec2 getSpawnPoint( int sp ) {
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
	
	static public final Integer AddPlayer( final String path, int number) {
		
		// Look for an energy bar to latch onto 
		int energyBarID = -1;
		for(EnergyBar e : Component.energybar.values()) {
			energyBarID = e.latch();
			if(energyBarID != -1) {
				break;
			}
		} 
		
		Integer playerID = Component.getID();
		Component.input.put( playerID, new Input(playerID, Input.playerKeyboard) );
		Component.placement.put( playerID, new Placement(getSpawnPoint(number)) );
		Component.drawer.put( playerID, new Drawer(playerID ) );
		Component.gun.put( playerID, new Gun(playerID, Constant.getPoint("Ship_Point")) );
		Component.shield.put( playerID, new Shield(playerID, energyBarID) );
		Component.mover.put( playerID, new Mover(playerID, Constant.getFloat("Ship_Speed"), true, Clock.play) );
		Component.shape.put( playerID, Constant.getShape("Ship_Shape") );
		Component.killable.put( playerID, new Killable(playerID, Killable.playerTeam) );
		Component.record.put( playerID, new Record(playerID, path) );
		Component.sticky.put( playerID, new Sticky(playerID) );

		Component.placement.get(playerID).setRotation( (float) Math.PI / 2 );
		Component.shield.get(playerID).Raise();
		Component.shape.get(playerID).setText(Integer.toString(number+1));

		return playerID;
	}
	
	static public final void AddMouse() {
		Integer id = Component.getID();
		Component.mouse = new Pointer(id);
		Component.placement.put(id, new Placement() );
		Component.drawer.put( id, new Drawer(id) );
		Component.shape.put( id, Constant.getShape("Interface_Shape") );
		Component.sticky.put( id, new Sticky(id) );
		
		Component.placement.get(id).interpPosition( Vec2.topLeft, Vec2.bottomRight, 0.5f);
	}
	
	static public final void AddEditorControls() {
		Integer id = Component.getID();
		Component.input.put(id, new Input(id, Input.editorKeyboard) );
	}
	
	static public final void AddActor( final String path, int number ) {
		Integer id = Component.getID();

		Component.actor.put( id, new Actor(id, path) );
		Component.placement.put( id, new Placement() );
		Component.drawer.put( id, new Drawer(id, Constant.getVector("Actor_Color") ) );
		Component.gun.put( id, new Gun(id, Constant.getPoint("Ship_Point")) );
		Component.shield.put( id, new Shield(id) );
		Component.shape.put( id, new Shape(Constant.getShape("Ship_Shape")) );
		Component.killable.put( id, new Killable(id, Killable.playerTeam) );

		Component.placement.get(id).setRotation( (float) Math.PI / 2 );
		Component.shape.get(id).setText(Integer.toString(number));
	}
	
	static public final void AddProgressBar( final Vec2 pos, final Shape shp, final Vec3 color ) {
		Integer id = Component.getID();
		
		Component.placement.put( id, new Placement(pos) );
		Component.shape.put( id, shp );
		Component.progressbar.put( id, new ProgressBar(id, color, lvlname) );
		Component.drawer.put( id, new Drawer(id) );
		Component.sticky.put( id, new Sticky(id) );
	}
	
	static public final void AddEnergyBar( final Vec2 pos, final Shape shp, final Vec3 empty, final Vec3 full ) {
		Integer id = Component.getID();
		
		Component.placement.put( id, new Placement(pos) );
		Component.shape.put( id, shp );
		Component.energybar.put( id, new EnergyBar(id, empty, full) );
		Component.drawer.put( id, new Drawer(id) );
		Component.sticky.put( id, new Sticky(id) );
	}
	
	// Private stuff
	static private final void AddImage( final Vec2 pos, final Shape shp ) {
		Integer id = Component.getID();

		Component.placement.put( id, new Placement(pos) );
		Component.drawer.put( id, new Drawer(id) );
		Component.shape.put( id, shp );
		Component.sticky.put( id, new Sticky(id) );
	}
	
	static private final void AddHelper( final Vec2 pos, final Shape shp, final String func, float wait ) {
		Integer id = Component.getID();

		Component.placement.put( id, new Placement(pos) );
		Component.drawer.put( id, new Drawer(id) );
		Component.helper.put( id, new Helper(id, func, wait) );
		Component.shape.put( id, shp );
		Component.sticky.put( id, new Sticky(id) );
	}
	
	static private final void AddButton( final Vec2 pos, final Shape shp, final String func ) {
		Integer id = Component.getID();

		Component.placement.put( id, new Placement(pos) );
		Component.drawer.put( id, new Drawer(id) );
		Component.shape.put( id, shp );
		Component.clickable.put(id, new Clickable(id, func) );
		Component.sticky.put( id, new Sticky(id) );
	}
	
	static private final void AddPlanet( final Vec2 pos, final Shape shp, final String func ) {
		Integer id = Component.getID();

		Component.placement.put( id, new Placement(pos) );
		Component.drawer.put( id, new Drawer(id) );
		Component.planet.put( id, new Planet(id) );
		Component.shape.put( id, shp );
		Component.clickable.put(id, new Clickable(id, func) );
	}
	
	static private final void AddWall( final Vec2 pos, final Shape shp ) {
		Integer id = Component.getID();

		Component.placement.put( id, new Placement(pos) );
		Component.drawer.put( id, new Drawer(id) );
		Component.shape.put( id, shp );
		Component.killable.put( id, new Killable(id, Killable.terrain) );
		Component.xml.put( id, new Xml(id) );
	}
	
	static private final Sequence getSequence( final String seqName ) {
		Sequence ret = null;
		if( Constant.getString(seqName + "_Type").equals("SimpleDumb") ) {
			ret = new SimpleDumb(seqName);
		}
		else if( Constant.getString(seqName + "_Type").equals("SimpleBlaster") ) {
			ret = new SimpleBlaster(seqName);
		}
		return ret;
	}
	
	static private final void AddSpawner( final Vec2 pos, final Angle shootDirection, float wait, final Sequence seq, final Shape shp ) {
		Integer id = Component.getID();

		Component.placement.put( id, new Placement(pos) );
		Component.spawner.put( id, new Spawner(id, shootDirection, wait, seq) );
		Component.drawer.put( id, new Drawer(id) );
		Component.xml.put(id, new Xml(id));
		
		if(shp != null) {
			Component.killable.put( id, new Killable(id, Killable.enemyTeam) );
			Component.shape.put( id, shp );
		}
	}
}
