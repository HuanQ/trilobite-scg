package components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Vector;

import managers.Component;
import managers.Timer;

import data.Snapshot;

public class Actor {
	private final int											me;
	// Internal data
	private Vector<Snapshot>									recordedData;
	private Iterator<Snapshot>									iter;
	private Snapshot											prevSnap;
	private Snapshot											nextSnap;

	@SuppressWarnings("unchecked")
	public Actor( int m ) {
		me = m;
	    
		ObjectInputStream inputStream = null;
		try {
			String file = "file.dat";
	        FileInputStream fileinputstream = new FileInputStream( new File(file) );
		    //ByteArrayOutputStream b = new ByteArrayOutputStream();
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
                if (inputStream != null) {
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
	
	public void Update() {
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
				Component.deadObjects.add(me);				
			}
		}
		
		// Move our actor according to our data
		Component.placement.get(me).interpPosition( prevSnap.getPosition(), nextSnap.getPosition(), interpolateNow( prevSnap.getTime(), nextSnap.getTime() ) );
	}
	
	private float interpolateNow( int prev, int next ) {
		if( prev == next ) {
			return 1;
		}
		else {
			return (float) (Timer.getTime() - prev) / (next - prev);
		}
	}
}