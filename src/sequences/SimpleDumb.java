package sequences;

import geometry.Angle;
import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Clock;

import components.Killable;
import components.Dumb;
import components.Mover;
import components.Drawer;
import components.Placement;

public class SimpleDumb implements Sequence {
	private final float											spawnRate;
	private int													counter;
	private float												rotationSpeed;
	private float												rotateGoal;
	private float												wait;
	private String												name;
	// Internal data
	private int													lastTime = 0;

	public SimpleDumb( final String nm ) {
		name = nm;
		spawnRate = Constant.getFloat(name + "_Frequency");
		counter = (int) Constant.getFloat(name + "_Count");
		rotationSpeed = Constant.getFloat(name + "_RotationSpeed");
		rotateGoal = Constant.getFloat(name + "_MaxRotate");
		wait = Constant.getFloat(name + "_Wait");
	}
	
	public final SimpleDumb getCopy() {
		return new SimpleDumb(name);
	}
	
	public final boolean Spawn( final Angle direction, final Vec2 spawnPoint ) {
		int time = Clock.getTime(Clock.game);
		if(counter > 0 && time > lastTime + spawnRate * Constant.timerResolution) {
			lastTime = time;
			counter--;
			
			Integer id = Component.getID();
			Component.mover.put( id, new Mover(id, Constant.getFloat("Dumb_Speed"), 0, false, Clock.game ) );
			Component.placement.put( id, new Placement( new Vec2(spawnPoint) ) );
			Component.drawer.put( id, new Drawer(id) );
			Component.shape.put( id, Constant.getShape("Dumb_Shape") );
			Component.dumb.put( id, new Dumb(id, rotationSpeed, direction, Dumb.normal, rotateGoal, wait, Constant.getFloat("Dumb_Health") ) );
			Component.killable.put( id, new Killable(id, Killable.enemyTeam) );
		}
		
		return counter == 0;
	}
	
	public final String getName() {
		return name;
	}
}
