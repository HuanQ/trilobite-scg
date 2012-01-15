package components;

import java.util.LinkedList;
import java.util.Queue;

import managers.Component;
import managers.Constant;
import managers.Clock;
import geometry.Vec2;

public class Planet {
	private final int											inertia;
	
	private final int											me;
	private final Vec2											originalPos;
	
	// Internal data
	private final Queue<Vec2>									lastFloats = new LinkedList<Vec2>();
	
	public Planet( int m ) {
		me = m;
		inertia = (int) Constant.getFloat("GUI_PlanetInertia");
		originalPos = new Vec2(Component.placement.get(m).position);

		while(lastFloats.size() < inertia) {
			lastFloats.add( new Vec2() );
		}
	}
	
	public final void Update() {
		float dt = Clock.getDelta(Clock.ui);
		
		doGravity(dt);
	}
	
	private final void doGravity( float dt ) {
		Vec2 myPos = Component.placement.get(me).position;
		Vec2 myFuturePos = new Vec2(myPos);
		
		// Gravity movement
		if( Component.mouse != null) {
			Vec2 mousePos = Component.mouse.getPosition();
			float dist = 1;
			Shape shp = Component.shape.get(me);
			if( shp != null && shp.getRadius() > 0 ) {
				dist = (float) Math.pow(mousePos.distance(originalPos), 0.00125f/shp.getRadius() );
			}
			myFuturePos.interpolate(mousePos, originalPos, Math.min(Math.max(dist,0),1) );
			
			//TODO Usar el mover per a fer aixo
			float speed = (float) Math.min(myFuturePos.distance(myPos), 0.4f);
			Vec2 floatMov = new Vec2(myFuturePos);
			floatMov.sub(myPos);
			if( !floatMov.isZero() ) {
				floatMov.normalize();
				floatMov.scale(dt * speed);
				lastFloats.add(floatMov);
			}
			else {
				lastFloats.add( new Vec2() );
			}
			if(lastFloats.size() > inertia) {
				lastFloats.poll();
			}
			
			Component.placement.get(me).position.add( doAverageFloat() );
		}
	}
	
	private final Vec2 doAverageFloat() {
		Vec2 avg = new Vec2();
		for(Vec2 lf : lastFloats) {
			avg.add( lf );
		}
		if(lastFloats.size() > 0) {
			avg.scale( 1.f / lastFloats.size());
		}
		
		return avg;
	}
}
