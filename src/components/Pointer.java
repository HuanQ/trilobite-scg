package components;

import geometry.Angle;
import geometry.Circle;
import geometry.Polygon;
import geometry.Rectangle;
import geometry.Vec2;
import geometry.Vec3;
import managers.Component;
import managers.Screen;

import org.lwjgl.input.Mouse;

import sequences.SimpleDumb;

public class Pointer {
	static public final int									mouse = 0;
	static public final int									addsquare = 1;
	static public final int									addcircle = 2;
	static public final int									addspawner = 3;
	static public final int									move = 4;
	
	private final int										me;
	private boolean											active = true;
	private boolean											pressed = false;
	private int												state = 0;
	// Internal data
	private Vec2											firstClick;
	private Vec2											secondClick;
	private Integer											currentID;
	private Polygon											myPoly;
	private boolean											limitMouse = false;
	
	public Pointer( int m ) {
		me = m;
		firstClick = null;
		secondClick = null;
		myPoly = null;
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
	
	public final void selectedTool( int id, int tool ) {
		// Turn all tools on
		setTools(Clickable.on);
		// Make it active
		Component.clickable.get(id).setState(Clickable.selected);
		// Turn cursor green
		for(Polygon p : Component.shape.get(me).polygons) {
			p.setColor(Vec3.green);
		}
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
		
		if( Mouse.isButtonDown(0) || Mouse.isButtonDown(1) ) {
			if(!pressed) {
				pressed = true;
				
				if(Mouse.isButtonDown(0)) {
					// Left click
					switch(state) {
					case Pointer.mouse:
						mouseClick();
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
						selectMove();
						break;
					}
				}
				else {
					// Right click
					unSelectTool();
					myPoly = null;
					firstClick = null;
					secondClick = null;
					Component.deadObjects.add(currentID);
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
					
					if(r.size.x > r.size.y) {
						myPoly.setTexture("rect2x1.gif");
					}
					else {
						myPoly.setTexture("rect1x2.gif");
					}
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
	
	private final void mouseClick() {
		for(Integer him : Component.clickable.keySet()) {
			if( Component.shape.get(him).Collides(him, me) ) {
				Component.clickable.get(him).Click();
			}
		}
	}
	
	private final void selectMove() {
		if(firstClick == null) {
			for(Integer i : Component.placement.keySet()) {
				if( me != i && Component.shape.get(me).Collides(me, i) ) {
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
			Component.spawner.put( currentID, new Spawner(currentID, new Angle(3.14f), 0, new SimpleDumb("LeftDumb")) );
			Component.drawer.put( currentID, new Drawer(currentID) );
			Component.xml.put(currentID, new Xml(currentID));
			
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
			
			myPoly = new Circle(0.000001f, new Vec2(), 1);
			myPoly.setTexture("circle.gif");
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
			
			myPoly = new Rectangle(new Vec2(0.000001f, 0.000001f), new Vec2(), 1, false);
			myPoly.setTexture("rect2x1.gif");
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
	
	public final void unSelectTool() {
		// Turn all tools' on
		setTools(Clickable.on);		
		// Turn cursor yellow
		for(Polygon p : Component.shape.get(me).polygons) {
			p.setColor(Vec3.yellow);
		}
		// Switch to tool
		state = 0;
		limitMouse = false;
	}
	
	private final void setTools( int state ) {
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
