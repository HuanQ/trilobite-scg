package sequences;

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
	
	public CrossedDumb( final float freq, final int count ) {
		spawnRate = freq;
		counter = count;
		lastTime = 0;
	}
	
	public void Spawn( final long time, final float rotStart, final Vec2 spawnPoint ) {
		if(counter > 0 && time > lastTime + spawnRate * Constant.timerResolution) {
			lastTime = time;
			counter--;
			Integer id = Component.getID();
			float rotSpeed = Constant.getFloat("Dumb_RotationSpeed");

/*			if(!leftExit)
			{
				rotSpeed = -rotSpeed;
				rotStart += (float) Math.PI;
			}*/
			Component.dumb.put( id, new Dumb(id, rotSpeed, rotStart, 0 ) );
			Component.mover.put( id, new Mover(id, Constant.getFloat("Dumb_Speed"), false, false, Constant.getFloat("Dumb_Gravity") ) );
			Component.placement.put( id, new Placement(spawnPoint) );
			Component.drawer.put( id, new Drawer(id, Constant.getVector("Dumb_Color") ) );
			Component.shape.put( id, Constant.getShape("Dumb_Shape") );
			Component.canBeKilled.put( id, new CanBeKilled(id) );
			Component.canKill.put( id, new CanKill() );
			
		}
	}
}
