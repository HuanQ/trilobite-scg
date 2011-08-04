package components;

import geometry.Vec2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Vector;

import managers.Component;
import managers.Constant;
import managers.Clock;

import data.Snapshot;

public class Actor {
	private final int											me;
	// Internal data
	private Vector<Snapshot>									recordedData;
	private Iterator<Snapshot>									iter;
	private Snapshot											prevSnap;
	private Snapshot											nextSnap;

	@SuppressWarnings("unchecked")
	public Actor( int m, final String file ) {
		me = m;
	    
		ObjectInputStream inputStream = null;
		try {
	        FileInputStream fileinputstream = new FileInputStream( new File(file) );
	        inputStream = new ObjectInputStream( fileinputstream );
			recordedData = (Vector<Snapshot>) inputStream.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
            //Close the ObjectOutputStream
            try {
                if(inputStream != null) {
                	inputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		iter = recordedData.iterator();
		prevSnap = iter.next();
		nextSnap = prevSnap;
	}
	
	public final Integer getLifeLength() {
		return recordedData.get(recordedData.size() - 1).getTime();
	}
	
	public final void Update() {
		if( nextSnap.isDone()) {
			if( iter.hasNext() ) {
				prevSnap = nextSnap;
				nextSnap = iter.next();
				
				// Do events
				int event = prevSnap.getEvent();
				if( (event & Record.gunShot) > 0 && Component.gun.get(me) != null ) {
					Component.gun.get(me).Shoot();
				}
				else if( (event & Record.shield) > 0 && Component.shield.get(me) != null ) {
					Component.shield.get(me).Raise();
				}
			}
			else {
				// Draw dephasing
				Placement p = Component.placement.get(me);
				Integer id = Component.getID();
				
				Component.timedObject.put( id, new TimedObject(id, Constant.getFloat("Phase_Duration"), Clock.game) );
				Component.placement.put( id, new Placement( new Vec2( p.position.x, p.position.y ) ) );
				Component.drawer.put( id, new Drawer(id) );
				Component.shape.put( id, Constant.getShape("Phase_Shape")  );
				
				// Die
				Component.deadObjects.add(me);
			}
		}
		
		// Move our actor according to our data
		Component.placement.get(me).interpPosition( prevSnap.position, nextSnap.position, Actor.interpolateNow( prevSnap.getTime(), nextSnap.getTime() ) );
	}
	
	static private final float interpolateNow( int prev, int next ) {
		if( prev == next ) {
			return 1;
		}
		else {
			return (float) (Clock.getTime(Clock.game) - prev) / (next - prev);
		}
	}
}
