package managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;


import components.*;

public class Component {

	static Integer											myIDcounter;
	static public Collection<Integer>						deadObjects = new HashSet<Integer>();
	
	// Components
	static public Map<Integer, Placement>					placement = new HashMap<Integer, Placement>();
	static public Map<Integer, Gun>							gun = new HashMap<Integer, Gun>();
	static public Map<Integer, Shape>						shape = new HashMap<Integer, Shape>();
	static public Map<Integer, Shield>						shield = new HashMap<Integer, Shield>();
	static public Map<Integer, Clickable>					clickable = new HashMap<Integer, Clickable>();
	static public Map<Integer, Xml>							xml = new HashMap<Integer, Xml>();
	// Render
	static public Map<Integer, Drawer>						drawer = new HashMap<Integer, Drawer>();
	// Update
	static public Map<Integer, Input>						input = new HashMap<Integer, Input>();
	static public Map<Integer, Bullet>						bullet = new HashMap<Integer, Bullet>();
	static public Map<Integer, Mover>						mover = new HashMap<Integer, Mover>();
	static public Map<Integer, Dumb>						dumb = new HashMap<Integer, Dumb>();
	static public Map<Integer, Killable>					killable = new HashMap<Integer, Killable>();
	static public Map<Integer, TimedObject>					timedObject = new HashMap<Integer, TimedObject>();
	static public Map<Integer, Spawner>						spawner = new HashMap<Integer, Spawner>();
	static public Map<Integer, Record>						record = new HashMap<Integer, Record>();
	static public Map<Integer, Actor>						actor = new HashMap<Integer, Actor>();
	static public Map<Integer, Planet>						planet = new HashMap<Integer, Planet>();
	static public Map<Integer, Helper>						helper = new HashMap<Integer, Helper>();
	static public Map<Integer, EnergyBar>					energybar = new HashMap<Integer, EnergyBar>();
	static public Map<Integer, ProgressBar>					progressbar = new HashMap<Integer, ProgressBar>();
	static public Map<Integer, Sticky>						sticky = new HashMap<Integer, Sticky>();
	// Global
	static public Pointer									mouse;
	static public Fader										fader = new Fader();
		
	
	static public final Integer getID() {
		return myIDcounter++;
	}
	
	static public final void Init() {
		myIDcounter = 0;
		mouse = null;
	}
	
	static public final void Release() {
		drawer.clear();
		gun.clear();
		input.clear();
		placement.clear();
		bullet.clear();
		shield.clear();
		mover.clear();
		dumb.clear();
		planet.clear();
		shape.clear();
		killable.clear();
		timedObject.clear();
		spawner.clear();
		record.clear();
		actor.clear();
		clickable.clear();
		helper.clear();
		energybar.clear();
		progressbar.clear();
		sticky.clear();
		xml.clear();
		
		fader.resetWhite();
		mouse = null;
	}
	
	static public final void Update() {
		Clock.Update();
		
		// Move the screen
		Screen.up.add(0.f, Clock.getDelta(Clock.game) * Constant.getFloat("Rules_ScreenSpeed"));
		
		// Update the components
		if(mouse != null) {
			mouse.Update();
		}
		fader.Update();
				
		for(Actor a : actor.values()) {
			a.Update();
		}
		for(Input k : input.values()) {
			k.Update();
		}		
		for(Spawner s : spawner.values()) {
			s.Update();
		}
		for(Planet p : planet.values()) {
			p.Update();
		}
		for(Dumb d : dumb.values()) {
			d.Update();
		}
		for(Bullet b : bullet.values()) {
			b.Update();
		}
		for(Helper h : helper.values()) {
			h.Update();
		}
		for(Mover m : mover.values()) {
			m.Update();
		}
		for(EnergyBar e : energybar.values()) {
			e.Update();
		}
		for(ProgressBar p : progressbar.values()) {
			p.Update();
		}
		for(Sticky s : sticky.values()) {
			s.Update();
		}
		for(Killable k : killable.values()) {
			k.Update();
		}
		for(TimedObject t : timedObject.values()) {
			t.Update();
		}
		// Record is always updated last
		for(Record r : record.values()) { 
			r.Update();
		}
		
		DestroyObjects();
	}
	
	static public final void Render() {
		// Clear the screen and depth buffer
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    glClearColor(0, 0, 0, 1);
	    
	    for(Drawer e : drawer.values()) {
	        e.Render();
	    }
	}
	
	static private final void DestroyObjects() {
		//TODO: Destruir d'ofici els elements que estiguin fora de pantalla (U tots, O bullets, etc...) Ojo, pero nomes en game, en editor no!
		
		for(Integer id : deadObjects) {
		   // Save to file
		   Record rec = record.get(id); 
		   if( rec != null) {
			   record.get(id).save();
			   record.remove(id);
		   }
		   
		   placement.remove(id);
		   gun.remove(id);
		   shape.remove(id);
		   drawer.remove(id);
		   input.remove(id);
		   planet.remove(id);
		   bullet.remove(id);
		   shield.remove(id);
		   mover.remove(id);
		   dumb.remove(id);
		   killable.remove(id);
		   timedObject.remove(id);
		   spawner.remove(id);
		   actor.remove(id);
		   helper.remove(id);
		   clickable.remove(id);
		   energybar.remove(id);
		   progressbar.remove(id);
		   sticky.remove(id);
		   xml.remove(id);
		}
		deadObjects.clear();
	}
}
