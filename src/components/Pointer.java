package components;

import geometry.Angle;
import geometry.Circle;
import geometry.Polygon;
import geometry.Rectangle;
import geometry.Vec2;
import geometry.Vec3;
import graphics.Sprite;
import managers.Clock;
import managers.Component;
import managers.Constant;
import managers.Level;
import managers.Screen;

import org.lwjgl.input.Mouse;

public class Pointer {
	static public final int									mouse = 0;
	static public final int									addsquare = 1;
	static public final int									addcircle = 2;
	static public final int									addspawner = 3;
	static public final int									move = 4;
	//TODO: Fer boto de copy
	static public final int									copy = 5;
	
	private final int										me;
	private boolean											active = true;
	private boolean											pressed = false;
	private int												state = 0;
	// Internal data
	private Vec2											firstClick = null;
	private Vec2											secondClick = null;
	private Integer											currentID;
	private Polygon											myPoly = null;
	private int												mySequence = 0;
	private boolean											limitMouse = false;
	
	//TODO: Fer composition aqui, tenir una Tool<-AddSquare i enviar-li firstclick i secondclick
	public Pointer( int m ) {
		me = m;
	}
	
	public final boolean isActive() {
		return active;
	}
	
	public final void setActive(boolean s) {
		active = s;
	}
	
	public final Vec2 getPosition() {
		return Component.placement.get(me).position;
	}
	
	public final void SelectTool( int id, int tool ) {
		// Turn all tools on
		doTools(Clickable.on);
		// Make it active
		Component.clickable.get(id).setState(Clickable.selected);
		// Turn cursor green
		Component.drawer.get(me).setColor(Vec3.green);
		// Switch to tool
		state = tool;
		// Limit the mouse to the game zone
		limitMouse = true;
	}
	
	public final void Update() {
		Vec2 newPos = new Vec2( Mouse.getDX(), -Mouse.getDY() );
		Component.placement.get(me).position.add( Screen.decoords(newPos) );
		
		Vec2 min = new Vec2(Screen.up);
		Vec2 max = new Vec2(Screen.up);
		max.add(Vec2.ones);
		
		if(limitMouse) {
			Screen.reposition("game", min);
			Screen.reposition("game", max);
		}
		Component.placement.get(me).position.clamp(min, max);
		
		int wheelMove = Mouse.getDWheel();
		if( wheelMove != 0 ) {
			switch(state) {
			case Pointer.addcircle:
			case Pointer.addsquare:
				//TODO: Ciclar entre les textures del format adequat amb la rodeta
				break;
			case Pointer.addspawner:
				// Cycling between sequences
				if(firstClick != null) {
					int mov = wheelMove > 0 ? 1 : -1;
					mySequence = (mySequence + mov + Constant.sequenceList.size()) % Constant.sequenceList.size();
					String seqName = Constant.sequenceList.get(mySequence);
					Component.spawner.get(currentID).setSequence( Level.getSequence(seqName) );
					Component.shape.put( currentID, new Shape(Constant.getShape(seqName + "_Shape")) );
					
					// Floating text show us our selection
					Integer id = Component.getID();
					Component.placement.put( id, new Placement( new Vec2(firstClick)) );
					Component.drawer.put( id, new Drawer(id) );
					Shape shp = new Shape(Constant.getShape("FloatingText_Shape"));
					shp.getText().setText(seqName);
					Component.shape.put( id, shp );
					Component.timedObject.put( id, new Timed(id, 1, Clock.real) );
				}
				break;
			}
		}
		
		if( Mouse.isButtonDown(0) || Mouse.isButtonDown(1) ) {
			if(!pressed) {
				pressed = true;
				
				if(Mouse.isButtonDown(0)) {
					// Left click
					switch(state) {
					case Pointer.mouse:
						doMouseClick();
						break;
					case Pointer.addcircle:
						addCircle();
						break;
					case Pointer.addsquare:
						addSquare();
						break;
					case Pointer.addspawner:
						addSpawner();
						break;
					case Pointer.move:
						SelectMove();
						break;
					}
				}
				else {
					// Right click
					UnselectTool();
					myPoly = null;
					firstClick = null;
					secondClick = null;
					Component.deadObjects.add(currentID);
					setSave(Clickable.on);
					setPlay(Clickable.off);
				}
			}
		}
		else if (pressed) {
			pressed = false;
		}
		else {
			// Mouse move
			switch(state) {
			case Pointer.addcircle:
				if(firstClick != null) {
					Circle c = (Circle) myPoly;
					c.setRadius( firstClick.distance(Component.placement.get(me).position) );
				}
				break;
			case Pointer.addsquare:
				if(firstClick != null) {
					Rectangle r = (Rectangle) myPoly;
					r.size.x = (float) Math.abs(firstClick.x - Component.placement.get(me).position.x)*2;
					r.size.y = (float) Math.abs(firstClick.y - Component.placement.get(me).position.y)*2;
					
					myPoly.setTexture( Sprite.getRect(r.size) );
				}
				break;
			case Pointer.move:
				if(firstClick != null) {
					Component.placement.get(currentID).position.set( Component.placement.get(me).position );
				}
				break;
			}			
		}
	}
	
	private final void doMouseClick() {
		for(Integer him : Component.clickable.keySet()) {
			if( Component.shape.get(him).Collides(him, me) ) {
				Component.clickable.get(him).Click();
			}
		}
	}
	
	private final void SelectMove() {
		if(firstClick == null) {
			for(Integer i : Component.shape.keySet()) {
				if( me != i && Component.shape.get(i).Collides(i, me) ) {
					firstClick = new Vec2(Component.placement.get(me).position);
					currentID = i;
					break;
				}
			}
		}
		else if(secondClick == null) {
			// Leave the object
			currentID = Integer.MIN_VALUE;
			myPoly = null;
			firstClick = null;
			secondClick = null;
			setSave(Clickable.on);
			setPlay(Clickable.off);
		}
	}
	
	private final void addSpawner() {
		if(firstClick == null) {
			firstClick = new Vec2(Component.placement.get(me).position);
			
			currentID = Component.getID();
			Component.placement.put( currentID, new Placement(firstClick) );
			Component.spawner.put( currentID, new Spawner(currentID, new Angle(3.14f), 0, Level.getSequence(Constant.sequenceList.get(mySequence))) );
			Component.drawer.put( currentID, new Drawer(currentID) );
			Component.shape.put( currentID, new Shape(Constant.getShape( Constant.sequenceList.get(mySequence) + "_Shape")) );
			Component.xml.put(currentID, new Xml(currentID));
		}
		else if(secondClick == null) {
			secondClick = new Vec2(Component.placement.get(me).position);
			
			Vec2 dir = new Vec2(secondClick);
			dir.sub(firstClick);
			Angle a = new Angle();
			a.set(dir);
			Component.spawner.get(currentID).setDirection( a );
			
			// Leave the spawner
			currentID = Integer.MIN_VALUE;
			myPoly = null;
			firstClick = null;
			secondClick = null;
			setSave(Clickable.on);
			setPlay(Clickable.off);
		}
	}
	
	private final void addCircle() {
		if(firstClick == null) {
			firstClick = new Vec2(Component.placement.get(me).position);
			
			currentID = Component.getID();
			Shape shp = new Shape();
			Component.placement.put( currentID, new Placement(firstClick) );
			Component.drawer.put( currentID, new Drawer(currentID) );
			Component.shape.put( currentID, shp );
			Component.killable.put( currentID, new Killable(currentID, Killable.terrain) );
			Component.xml.put(currentID, new Xml(currentID));
			
			myPoly = new Circle(0.000001f, new Vec2(), 2);
			myPoly.setTexture("base/circle.gif");
			shp.add( myPoly );
		}
		else if(secondClick == null) {
			secondClick = new Vec2(Component.placement.get(me).position);
			// Leave the circle
			currentID = Integer.MIN_VALUE;
			myPoly = null;
			firstClick = null;
			secondClick = null;
			setSave(Clickable.on);
			setPlay(Clickable.off);
		}
	}
	
	private final void addSquare() {
		if(firstClick == null) {
			firstClick = new Vec2(Component.placement.get(me).position);
			
			currentID = Component.getID();
			Shape shp = new Shape();
			Component.placement.put( currentID, new Placement(firstClick) );
			Component.drawer.put( currentID, new Drawer(currentID) );
			Component.shape.put( currentID, shp );
			Component.killable.put( currentID, new Killable(currentID, Killable.terrain) );
			Component.xml.put(currentID, new Xml(currentID));
			
			myPoly = new Rectangle(new Vec2(0.000001f, 0.000001f), new Vec2(), 1, true);
			myPoly.setTexture("base/rect1x1.gif");
			shp.add( myPoly );
		}
		else if(secondClick == null) {
			secondClick = new Vec2(Component.placement.get(me).position);
			// Leave the Square
			currentID = Integer.MIN_VALUE;
			myPoly = null;
			firstClick = null;
			secondClick = null;
			setSave(Clickable.on);
			setPlay(Clickable.off);
		}
	}
	
	public final void UnselectTool() {
		// Turn all tools' on
		doTools(Clickable.on);		
		// Turn cursor yellow
		Component.drawer.get(me).setColor(Vec3.yellow);
		// Switch to tool
		state = 0;
		limitMouse = false;
	}
	
	static private final void doTools( int state ) {
		for(Clickable c : Component.clickable.values()) {
			if(c.getFunction().equals("ADDCIRCLE")
					|| c.getFunction().equals("ADDSQUARE")
					|| c.getFunction().equals("ADDSPAWNER")
					|| c.getFunction().equals("MOVE")
					) {
				c.setState(state);
			}
		}
	}
	
	public final void setPlay( int state ) {
		for(Clickable c : Component.clickable.values()) {
			if( c.getFunction().equals("PLAY") ) {
				c.setState(state);
			}
		}
	}
	
	public final void setSave( int state ) {
		for(Clickable c : Component.clickable.values()) {
			if( c.getFunction().equals("RELOAD") || c.getFunction().equals("SAVE") ) {
				c.setState(state);
			}
		}
	}
}
