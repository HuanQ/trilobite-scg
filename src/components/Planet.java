package components;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import managers.Component;
import managers.Timer;
import geometry.Vec2;

public class Planet {
	private final int											inertia;
	
	private final int											me;
	private final Vec2											originalPos;
	private final Queue<Vec2>									lastFloats;
	
	public Planet( int m ) {
		me = m;
		inertia = 150;
		originalPos = new Vec2(Component.placement.get(m).getPosition());
		lastFloats = new LinkedList<Vec2>();
		while(lastFloats.size() < inertia) {
			lastFloats.add( new Vec2() );
		}
	}
	
	public void Update() {
		Vec2 myPos = Component.placement.get(me).getPosition();
		Vec2 myFuturePos = new Vec2(myPos);
		float dt = Timer.getDelta();
		
		// Gravity movement
		if( Component.mouse != null) {
			Vec2 mousePos = Component.mouse.getPosition();
			float mass = Component.shape.get(me).getRadius();
			//TODO: Weight by radius
			float dist = (float) Math.pow(mousePos.distance(originalPos), 0.0015f/Math.pow(mass,1.2f) );
			myFuturePos.interpolate(mousePos, originalPos, Math.min(Math.max(dist,0),1) );
			
			float speed = (float) Math.min(myFuturePos.distance(myPos), 0.75f);
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
			
			Component.placement.get(me).addPosition( averageFloat() );
		}
	}
	
	private Vec2 averageFloat() {
		Vec2 avg = new Vec2();
		for (Iterator<Vec2> iter = lastFloats.iterator(); iter.hasNext();) {
			avg.add( iter.next() );
		}
		if(lastFloats.size() > 0) {
			avg.scale( 1.f / lastFloats.size());
		}
		
		return avg;
	}
}
