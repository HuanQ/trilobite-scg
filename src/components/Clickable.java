package components;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import managers.Clock;
import managers.Component;
import managers.Constant;
import managers.Level;
import managers.Screen;
import managers.Sound;
import data.Cleaner;
import game.Start;
import geometry.Rectangle;
import geometry.Vec3;

public class Clickable {
	// States
	static public final int									on = 0;
	static public final int									off = 1;
	static public final int									selected = 2;
	
	private final int										me;
	private final String									function;
	private final int										param;
	private int												state = on;

	//TODO NOW Millorar aquest component, no se com. Monster class!
	public Clickable( int m, final String fun ) {
		me = m;
		function = fun;
		param = 0;
	}
	
	public Clickable( int m, final String fun, int par ) {
		me = m;
		function = fun;
		param = par;
	}
	
	public final String getFunction() {
		return function;
	}
	
	public final void setState( int st ) {
		state = st;
		Vec3 col;
		switch(st) {
		case on:
			col = Vec3.white;
			break;
		case off:
			col = Vec3.gray;
			break;
		case selected:
			col = Vec3.green;
			break;
		default:
			col = Vec3.black;
			break;
		}
		
		Rectangle r = Component.shape.get(me).getRectangle();
		if( r != null ) {
			r.setColor(col);
		}
	}
	
	public final void Click( ) {
		if(state == on) {
			doClick();
		}
	}

	//TODO Comprovar si alguns dels .equals que faig servir podiren ser ==
	private final void doClick() {
		boolean clicked = true;
		if( function.equals("START") ) {
			Start.gameMenu();
		}
		else if( function.equals("QUIT") ) {
			Start.quitProgram();
		}
		else if( function.equals("STARTPLAY") ) {
			Start.startGame();
		}
		else if( function.equals("BACK") ) {
			Component.fader.FadeToBlack();
			Start.mainMenu();
		}
		else if( function.equals("RESTARTLEVEL") ) {
			Cleaner.CleanAll();
			Start.gameMenu();
		}
		else if( function.equals("EDITOR") ) {
			Start.startEditor();
		}
		else if( function.equals("PLAY") ) {
			Screen.up.zero();
			Clock.Unpause(Clock.game);
			setEditorButtons(Clickable.off);
			
			setState(Clickable.selected);
			
			for(Clickable c : Component.clickable.values()) {
				if(c.getFunction().equals("STOP")) {
					c.setState(Clickable.on);
				}
			}
		}
		else if( function.equals("STOP") ) {
			Start.restartEditor();
		}
		else if( function.equals("RELOAD") ) {
			Start.restartEditor();
			for(Clickable c : Component.clickable.values()) {
				if(c.getFunction().equals("PLAY")) {
					c.setState(Clickable.on);
				}
			}
		}
		else if( function.equals("SAVE") ) {
			Clickable.setSave(Clickable.off);
			doLevel();
			for(Clickable c : Component.clickable.values()) {
				if(c.getFunction().equals("PLAY")) {
					c.setState(Clickable.on);
				}
			}
		}
		else if( function.equals("EDITORUP") ) {
			Screen.up.add(0, -0.25f);
		}
		else if( function.equals("EDITORDOWN") ) {
			if(Screen.up.y < 0) {
				Screen.up.add(0, 0.25f);
			}
		}
		else if ( function.contains("TOOL_") ) {
			
			if( function.equals("TOOL_CIRCLE") ) {
				Component.mouse.SelectTool( Pointer.circle );
			}
			else if( function.equals("TOOL_SQUARE") ) {
				Component.mouse.SelectTool( Pointer.square );
			}
			else if( function.equals("TOOL_SPAWNER") ) {
				Component.mouse.SelectTool( Pointer.spawner );
			}
			else if( function.equals("TOOL_MOVE") ) {
				Component.mouse.SelectTool( Pointer.move );
			}
			else if( function.equals("TOOL_COPY") ) {
				Component.mouse.SelectTool( Pointer.copy );
			}
			
			// Make me active
			Component.clickable.get(me).setState(Clickable.selected);
		}
		else if( function.equals("DELETEACTOR") ) {
			Cleaner.DeleteActor(param);
			Component.actorpanel.Reload();
		}
		else if( function.contains("SHIP_") ) {
			// Reset all ships
			for(Integer i : Component.clickable.keySet()) {
				if( Component.clickable.get(i).getFunction().contains("SHIP_") ) {
					Component.drawer.get(i).setColor( Vec3.darkgray );
				}
			}
			
			Component.drawer.get(me).setColor(Vec3.white);
			if( function.contains(Constant.GliderShip) ) {
				Start.nextship = Constant.GliderShip;
			}
			else if( function.contains(Constant.AgileShip) ) {
				Start.nextship = Constant.AgileShip;
			}
			else if( function.contains(Constant.TankShip) ) {
				Start.nextship = Constant.TankShip;
			}
			else if( function.contains(Constant.DefendShip) ) {
				Start.nextship = Constant.DefendShip;
			}
		}
		else if( function.contains("LEVEL_") ) {
			// Reset all levels
			for(Integer i : Component.clickable.keySet()) {
				if( Component.clickable.get(i).getFunction().contains("LEVEL_") ) {
					Component.drawer.get(i).setColor( Vec3.darkgray );
				}
			}
			Component.drawer.get(me).setColor(Vec3.white);
			Level.lvlname = function.substring(6, function.length());
		}
		else {
			clicked = false;
		}
		
		if(clicked) {
			Sound.Play(Sound.click);
		}
	}
	
	static public final void setTools( int state ) {
		for(Clickable c : Component.clickable.values()) {
			if(c.getFunction().equals("TOOL_CIRCLE")
					|| c.getFunction().equals("TOOL_SQUARE")
					|| c.getFunction().equals("TOOL_SPAWNER")
					|| c.getFunction().equals("TOOL_MOVE")
					|| c.getFunction().equals("TOOL_COPY")
					) {
				c.setState(state);
			}
		}
	}
	
	
	static public final void setSave( int state ) {
		for(Clickable c : Component.clickable.values()) {
			if( c.getFunction().equals("RELOAD") || c.getFunction().equals("SAVE") ) {
				c.setState(state);
			}
		}
		int inverseState = state == Clickable.on ? Clickable.off : Clickable.on;
		for(Clickable c : Component.clickable.values()) {
			if( c.getFunction().equals("PLAY") ) {
				c.setState(inverseState);
			}
		}
	}
	
	static private final void setEditorButtons( int state ) {
		for(Clickable c : Component.clickable.values()) {
			if(c.getFunction().equals("TOOL_CIRCLE")
					|| c.getFunction().equals("TOOL_SQUARE")
					|| c.getFunction().equals("TOOL_SPAWNER")
					|| c.getFunction().equals("TOOL_MOVE")
					|| c.getFunction().equals("TOOL_COPY")
					|| c.getFunction().equals("EDITORUP")
					|| c.getFunction().equals("EDITORDOWN")
					|| c.getFunction().equals("SAVE")
					|| c.getFunction().equals("RELOAD")
					) {
				c.setState(state);
			}
		}
	}
	
	static private final void doLevel() {
		File file = new File( "resources/data/level/" + Level.lvlname + ".xml" );

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			//root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Trilobite");
			doc.appendChild(rootElement);
	 
			//staff elements
			Element block = doc.createElement("Generated");
			rootElement.appendChild(block);
			block.setAttribute("zone", "game");
	 
			for(Xml x : Component.xml.values()) {
				x.WriteXML( doc, block );
			}
			
			//write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
			DOMSource source = new DOMSource(doc);
			StreamResult result =  new StreamResult(file);
			transformer.transform(source, result);
		}
		catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		catch(TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
