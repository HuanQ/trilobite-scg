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
	static public Collection<Integer>							deadObjects = new HashSet<Integer>();
	
	// Components
	static public Map<Integer, Placement>					placement = new HashMap<Integer, Placement>();
	static public Map<Integer, Gun>							gun = new HashMap<Integer, Gun>();
	static public Map<Integer, Shape>						shape = new HashMap<Integer, Shape>();
	static public Map<Integer, Killer>						canKill = new HashMap<Integer, Killer>();
	static public Map<Integer, Shield>						shield = new HashMap<Integer, Shield>();
	static public Map<Integer, Clickable>					clickable = new HashMap<Integer, Clickable>();
	// Render
	static public Map<Integer, Drawer>						drawer = new HashMap<Integer, Drawer>();
	// Update
	static public Map<Integer, KeyboardInput>				keyboard = new HashMap<Integer, KeyboardInput>();
	static public Map<Integer, Bullet>						bullet = new HashMap<Integer, Bullet>();
	static public Map<Integer, Mover>						mover = new HashMap<Integer, Mover>();
	static public Map<Integer, Dumb>						dumb = new HashMap<Integer, Dumb>();
	static public Map<Integer, Killable>					canBeKilled = new HashMap<Integer, Killable>();
	static public Map<Integer, TimedObject>					timedObject = new HashMap<Integer, TimedObject>();
	static public Map<Integer, Spawner>						spawner = new HashMap<Integer, Spawner>();
	static public Map<Integer, Record>						record = new HashMap<Integer, Record>();
	static public Map<Integer, Actor>						actor = new HashMap<Integer, Actor>();
	static public Map<Integer, Planet>						planet = new HashMap<Integer, Planet>();
	// Global
	static public Pointer									mouse;
	static public Fader										fader = new Fader();
		
	
	static public Integer getID() {
		return myIDcounter++;
	}
	
	static public void Init() {
		myIDcounter = 0;
		mouse = null;
	}
	
	static public void Release() {
		drawer.clear();
		gun.clear();
		keyboard.clear();
		placement.clear();
		bullet.clear();
		shield.clear();
		mover.clear();
		dumb.clear();
		planet.clear();
		shape.clear();
		canKill.clear();
		canBeKilled.clear();
		timedObject.clear();
		spawner.clear();
		record.clear();
		actor.clear();
		clickable.clear();
		
		fader.reset();
		mouse = null;
	}
	
	static public void Update() {
		Timer.Update();
		if(mouse != null) {
			mouse.Update();
		}
		if(fader != null) {
			fader.Update();
		}
		
		for (Map.Entry<Integer, Actor> entry : actor.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, KeyboardInput> entry : keyboard.entrySet()) {
			entry.getValue().Update();
		}		
		for (Map.Entry<Integer, Spawner> entry : spawner.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, Planet> entry : planet.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, Dumb> entry : dumb.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, Bullet> entry : bullet.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, Mover> entry : mover.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, Killable> entry : canBeKilled.entrySet()) {
			entry.getValue().Update();
		}
		for (Map.Entry<Integer, TimedObject> entry : timedObject.entrySet()) {
			entry.getValue().Update();
		}
		// Record is always updated last
		for (Map.Entry<Integer, Record> entry : record.entrySet()) {
			entry.getValue().Update();
		}
		
		DestroyObjects();
	}
	
	static public void Render() {
		// Clear the screen and depth buffer
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    glClearColor(0, 0, 0, 1);
	    
		for (Map.Entry<Integer, Drawer> entry : drawer.entrySet()) {
			entry.getValue().Render();
		}
	}
	
	static private void DestroyObjects() {

		for (Iterator<Integer> iter = deadObjects.iterator(); iter.hasNext();) {
		   Integer next = iter.next();
		   
		   // Save to file
		   Record rec = record.get(next); 
		   if( rec != null) {
			   record.get(next).save();
			   record.remove(next);
		   }
		   
		   placement.remove(next);
		   gun.remove(next);
		   shape.remove(next);
		   canKill.remove(next);
		   drawer.remove(next);
		   keyboard.remove(next);
		   planet.remove(next);
		   bullet.remove(next);
		   shield.remove(next);
		   mover.remove(next);
		   dumb.remove(next);
		   canBeKilled.remove(next);
		   timedObject.remove(next);
		   spawner.remove(next);
		   actor.remove(next);
		}
		deadObjects.clear();
	}
}
