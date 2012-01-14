package data;

import java.io.Serializable;

import managers.Clock;

import geometry.Vec2;
 
public class Snapshot implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4075183780321067458L;
	
	private final int											time;
	private final int											events;
	public final Vec2											position;
	public final float											rotation;
	
	public Snapshot( int t, int ev, Vec2 pos, float rot) {
		time = t;
		events = ev;
		position = pos;
		rotation = rot;
	}
	
	public Snapshot(Snapshot snp) {
		time = snp.getTime();
		events = snp.getEvent();
		position = snp.position;
		rotation = snp.rotation;
	}

	public final String toString() {
		return "(" + time + ", " + events + ", " + position + ", " + rotation + ")";
	}

	public final boolean isDone() {
		return time < Clock.getTime(Clock.game);
	}
	
	public final int getTime() {
		return time;
	}
	
	public final int getEvent() {
		return events;
	}
}
