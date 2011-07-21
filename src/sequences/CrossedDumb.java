package sequences;

import geometry.Angle;
import geometry.Vec2;

import managers.Component;
import managers.Constant;

import components.CanBeKilled;
import components.CanKill;
import components.Dumb;
import components.Mover;
import components.Drawer;
import components.Placement;


public class CrossedDumb implements Sequence {
	private long												lastTime;
	private final float											spawnRate;
	private int													counter;
	// Internal data
	private boolean												shortRoute;
	
	public CrossedDumb( final float freq, final int count ) {
		spawnRate = freq;
		counter = count;
		lastTime = 0;
		shortRoute = false;
	}
	
	public void Spawn( final long time, final Angle rotStart, final Vec2 spawnPoint, float rotSpeed ) {
		if(counter > 0 && time > lastTime + spawnRate * Constant.timerResolution) {
			lastTime = time;
			counter--;
			
			Integer id = Component.getID();
			Component.dumb.put( id, new Dumb(id, rotSpeed, rotStart, shortRoute ? 0 : 1 ) );
			Component.mover.put( id, new Mover(id, Constant.getFloat("Dumb_Speed"), false, Constant.getFloat("Dumb_Gravity") ) );
			Component.placement.put( id, new Placement(spawnPoint) );
			Component.drawer.put( id, new Drawer(id, Constant.getVector("Dumb_Color") ) );
			Component.shape.put( id, Constant.getShape("Dumb_Shape") );
			Component.canBeKilled.put( id, new CanBeKilled(id) );
			Component.canKill.put( id, new CanKill() );
			
			shortRoute = !shortRoute;
		}
	}
}
