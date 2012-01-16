package components;

import geometry.Vec2;
import geometry.Vec3;
import managers.Component;
import managers.Screen;

import org.lwjgl.input.Mouse;

import tools.Tool;
import tools.ToolCreateCircle;
import tools.ToolEditCopy;
import tools.ToolEditMove;
import tools.ToolMouse;
import tools.ToolCreateSpawner;
import tools.ToolCreateSquare;

public class Pointer {
	static public final int									mouse = 0;
	static public final int									circle = 1;
	static public final int									square = 2;
	static public final int									spawner = 3;
	static public final int									move = 4;
	static public final int									copy = 5;

	private final int										me;
	private boolean											limitMouse = false;
	private Tool											myTool;
	
	// Internal data
	private boolean											pressed = false;

	public Pointer( int m ) {
		me = m;
		myTool = new ToolMouse(m);
	}
	
	public final Vec2 getPosition() {
		return Component.placement.get(me).position;
	}
	
	public final void SelectTool( int tool ) {
		// Turn all tools on
		Clickable.setTools(Clickable.on);
		
		//TODO Buscar "case 0:" que segur que en algun lloc no estas utilitzant les constants  
		// Switch to tool
		switch(tool) {
			case mouse:
				myTool = new ToolMouse(me);
				Component.drawer.get(me).setColor(Vec3.yellow);
				limitMouse = false;
			break;
			case circle:
				myTool = new ToolCreateCircle(me);
				Component.drawer.get(me).setColor(Vec3.green);
				limitMouse = true;
			break;
			case square:
				myTool = new ToolCreateSquare(me);
				Component.drawer.get(me).setColor(Vec3.green);
				limitMouse = true;
			break;
			case spawner:
				myTool = new ToolCreateSpawner(me);
				Component.drawer.get(me).setColor(Vec3.green);
				limitMouse = true;
			break;
			case move:
				myTool = new ToolEditMove(me);
				Component.drawer.get(me).setColor(Vec3.green);
				limitMouse = true;
			break;
			case copy:
				myTool = new ToolEditCopy(me);
				Component.drawer.get(me).setColor(Vec3.green);
				limitMouse = true;
			break;
		}
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
		if( wheelMove > 0 ) {
			myTool.WheelUp();
		}
		else if( wheelMove < 0 ) {
			myTool.WheelDown();
		}
		
		if( Mouse.isButtonDown(0) || Mouse.isButtonDown(1) ) {
			if(!pressed) {
				pressed = true;
				
				if(Mouse.isButtonDown(0)) {
					myTool.LeftButton();
				}
				else {
					myTool.RightButton();
					
					// Right click
					SelectTool(mouse);
					Clickable.setSave(Clickable.on);
				}
			}
		}
		else if (pressed) {
			pressed = false;
		}
		else {
			myTool.Move();
		}
	}
}
