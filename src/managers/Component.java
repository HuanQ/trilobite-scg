package managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;


import components.*;

public class Component {

	static Integer												myIDcounter;
	static public Collection<Integer>							deadObjects;
	
	// Components
		static public Map<Integer, Placement>					placement;
		static public Map<Integer, Gun>							gun;
		static public Map<Integer, Shape>						shape;
		static public Map<Integer, CanKill>						canKill;
		static public Map<Integer, Shield>						shield;
		// Render
		static public Map<Integer, Drawer>						drawer;
		// Update
		static public Map<Integer, KeyboardInput>				keyboard;
		static public Map<Integer, Bullet>						bullet;
		static public Map<Integer, Mover>						mover;
		static public Map<Integer, Dumb>						dumb;
		static public Map<Integer, CanBeKilled>					canBeKilled;
		static public Map<Integer, TimedObject>					timedObject;
		static public Map<Integer, Spawner>						spawner;
	
	static public Integer getID() {
		return myIDcounter++;
	}
	
	static public void Init() {
		myIDcounter = 0;
		drawer = new HashMap<Integer, Drawer>();
		gun = new HashMap<Integer, Gun>();
		keyboard = new HashMap<Integer, KeyboardInput>();
		placement = new HashMap<Integer, Placement>();
		bullet = new HashMap<Integer, Bullet>();
		shield = new HashMap<Integer, Shield>();
		mover = new HashMap<Integer, Mover>();
		dumb = new HashMap<Integer, Dumb>();
		shape = new HashMap<Integer, Shape>();
		canKill = new HashMap<Integer, CanKill>();
		canBeKilled = new HashMap<Integer, CanBeKilled>();
		timedObject = new HashMap<Integer, TimedObject>();
		spawner = new HashMap<Integer, Spawner>();
		
		deadObjects = new HashSet<Integer>();
	}
	
	static public void Release() {
		drawer = null;
		gun = null;
		keyboard = null;
		placement = null;
		bullet = null;
		shield = null;
		mover = null;
		dumb = null;
		shape = null;
		canKill = null;
		canBeKilled = null;
		timedObject = null;
		spawner = null;
	}
	
	static public void Update() {
		Timer.Update();

		for (Map.Entry<Integer, Spawner> entry : spawner.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, TimedObject> entry : timedObject.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, CanBeKilled> entry : canBeKilled.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, Dumb> entry : dumb.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, Mover> entry : mover.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, KeyboardInput> entry : keyboard.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, Bullet> entry : bullet.entrySet()) {
			entry.getValue().Update();
		}
		
		DestroyObjects();
	}
	
	static public void Render() {
		// Clear the screen and depth buffer
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    glClearColor(0.4f, 0.5f, 0.8f, 1);
	    
		for (Map.Entry<Integer, Drawer> entry : drawer.entrySet()) {
			entry.getValue().Render();
		}
	}
	
	static private void DestroyObjects() {

		for (Iterator<Integer> iter = deadObjects.iterator(); iter.hasNext();) {
		   Integer next = (Integer) iter.next();
		   placement.remove(next);
		   gun.remove(next);
		   shape.remove(next);
		   canKill.remove(next);
		   drawer.remove(next);
		   keyboard.remove(next);
		   bullet.remove(next);
		   shield.remove(next);
		   mover.remove(next);
		   dumb.remove(next);
		   canBeKilled.remove(next);
		   timedObject.remove(next);
		   spawner.remove(next);
		}
		deadObjects.clear();
	}
}
