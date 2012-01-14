package data;

import java.util.Collection;
import java.util.HashSet;

import components.Killable;

import managers.Component;
import managers.Constant;
import managers.Screen;
import geometry.Angle;
import geometry.Vec2;


public class Laser {
	private final Vec2											finalPoint;
	private final int											killType;
	// Internal data
	private final Collection<Integer>							thingsHit = new HashSet<Integer>();
	
	public Laser( final Vec2 exit, final Angle dir, int kt ) {
		killType = kt;
		finalPoint = new Vec2(exit);
		
		Vec2 step = dir.getDirection();
		step.scale( Constant.getFloat("Performance_LaserEpsilon") );
		
		float margin = Constant.getFloat("Laser_Margin");
		boolean done = false;
		while(!done && Screen.inScreen(finalPoint, margin)) {
			// Find out if we hit something
			for( Integer i : Component.killable.keySet() ) {
				if( Component.shape.get(i) != null && Component.shape.get(i).Collides(i, finalPoint) ) {
					
					int hisType = Component.killable.get(i).getType();
					// Something has been hit
					if( Killable.Kills(killType, hisType) ) {
						// Kill it
						//TODO: Sempre toca? Segur? No sembla que faci 5 dmg/sec
						thingsHit.add(i);
					}
					else if( hisType == Killable.terrain ) {
						// Laser stops
						done = true;
						break;
					}
				}
				
			}
			
			finalPoint.add(step);
		}
		
		
	}

	public final Collection<Integer> getThingsHit() {
		return thingsHit;
	}
	
	public final Vec2 getEndPoint() {
		return finalPoint;
	}

}
