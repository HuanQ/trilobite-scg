package data;

import java.io.Serializable;

import managers.Timer;

import geometry.Vec2;
 
public class Snapshot implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4075183780321067458L;
	
	private final int											time;
	private final int											events;
	private final Vec2											position;
	
	public Snapshot( int t, int ev, Vec2 pos) {
		time = t;
		events = ev;
		position = pos;
	}
	
	public Snapshot(Snapshot snp) {
		time = snp.getTime();
		events = snp.getEvent();
		position = snp.getPosition();
	}

	public String toString() {
		return "(" + time + ", " + events + ", " + position + ")";
	}

	public boolean isDone() {
		return time < Timer.getTime();
	}
	
	public int getTime() {
		return time;
	}
	
	public Vec2 getPosition() {
		return position;
	}
	
	public int getEvent() {
		return events;
	}
}
