package sequences;

import geometry.Angle;
import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Timer;

import components.Killable;
import components.Killer;
import components.Dumb;
import components.Mover;
import components.Drawer;
import components.Placement;


public class CrossedDumb implements Sequence {
	private int													lastTime;
	private final float											spawnRate;
	private int													counter;
	// Internal data
	private boolean												shortRoute;
	
	public CrossedDumb( float freq, int count ) {
		spawnRate = freq;
		counter = count;
		lastTime = 0;
		shortRoute = false;
	}
	
	public void Spawn( final Angle rotStart, final Vec2 spawnPoint, float rotSpeed ) {
		int time = Timer.getTime();
		if(counter > 0 && time > lastTime + spawnRate * Constant.timerResolution) {
			lastTime = time;
			counter--;
			
			Integer id = Component.getID();
			Component.dumb.put( id, new Dumb(id, rotSpeed, rotStart, shortRoute ? Dumb.shortRoute : Dumb.longRoute ) );
			Component.mover.put( id, new Mover(id, Constant.getFloat("Dumb_Speed"), false, Constant.getFloat("Dumb_Gravity") ) );
			Component.placement.put( id, new Placement(spawnPoint, Placement.gameSide) );
			Component.drawer.put( id, new Drawer(id) );
			Component.shape.put( id, Constant.getShape("Dumb_Shape") );
			Component.canBeKilled.put( id, new Killable(id) );
			Component.canKill.put( id, new Killer() );
			
			shortRoute = !shortRoute;
		}
	}
}
