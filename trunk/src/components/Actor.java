package components;

import geometry.Angle;
import geometry.Polygon;
import geometry.Rectangle;
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
import managers.Level;
import managers.Sound;

import data.Snapshot;

public class Actor {
	private final int											me;
	// Internal data
	private Vector<Snapshot>									recordedData;
	private Iterator<Snapshot>									iter;
	private Snapshot											prevSnap;
	private Snapshot											nextSnap;
	
	private Iterator<Snapshot>									traceIter;
	private Snapshot											prevTrace;
	private Snapshot											nextTrace;

	@SuppressWarnings("unchecked")
	public Actor( int m, final String file, boolean loadTraces ) {
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
		traceIter = recordedData.iterator();
		prevSnap = iter.next();
		nextSnap = prevSnap;
		
		if( loadTraces ) {
			// Add traces
			prevTrace = traceIter.next();
			nextTrace = prevTrace;
			addTrace();
		}
	}
	
	public final Integer getLifeLength() {
		return recordedData.get(recordedData.size() - 1).getTime();
	}
	
	public final void Update() {
		if( Component.placement.get(me) != null ) {
			if( nextSnap.isDone()) {
				if( iter.hasNext() ) {
					prevSnap = nextSnap;
					nextSnap = iter.next();
					
					// Do events
					int event = prevSnap.getEvent();
					if( (event & Record.gunShot) > 0 && Component.gun.get(me) != null ) {
						Component.gun.get(me).Shoot();
					}
					if( (event & Record.shield) > 0 && Component.shield.get(me) != null ) {
						Component.shield.get(me).Raise();
					}
					if( (event & Record.rotation) > 0 ) {
						Component.placement.get(me).angle.set( prevSnap.rotation );
					}
				}
				else {
					// Dephasing
					Level.AddEffect( Component.placement.get(me).position, "Phase" );
					Sound.Play(Sound.phase);
					// Die
					Component.deadObjects.add(me);
				}
			}
			
			//TODO: final a les locals
			// Move our actor according to our data
			float alpha = Actor.interpolateNow( prevSnap.getTime(), nextSnap.getTime() );
			Component.placement.get(me).position.interpolate( prevSnap.position, nextSnap.position, alpha );
			
			// Add trace
			addTrace();
		}
	}
	
	static private final float interpolateNow( int prev, int next ) {
		if( prev == next ) {
			return 1;
		}
		else {
			return (float) (Clock.getTime(Clock.game) - prev) / (next - prev);
		}
	}
	
	private final void addTrace() {
		int time = Clock.getTime(Clock.game);
		int ahead = time + (int) (Constant.getFloat("Trace_Ahead") * Constant.timerResolution);
		
		while( nextTrace.getTime() < ahead && traceIter.hasNext() ) {
			// Draw prev-next trace			
			Vec2 pos = new Vec2();
			pos.interpolate(prevTrace.position, nextTrace.position, 0.5f);
			
			Shape shp = new Shape(Constant.getShape("Trace_Shape"));
			Vec2 direction = new Vec2(prevTrace.position);
			direction.sub(nextTrace.position);
			
			if( !direction.isZero() ) {
				Angle rot = new Angle();
				rot.set(direction);			
				
				for(Polygon p : shp.polygons) {
					if(p.whoAmI() == Polygon.rectangle) {
						Rectangle r = (Rectangle) p;
						r.size.y = Constant.getFloat("Trace_Width");
						r.size.x = prevTrace.position.distance(nextTrace.position);
						break;
					}
				}
				
				float duration = (float) (prevTrace.getTime() - time) / Constant.timerResolution;
				if(duration > 0) {
					Level.AddTrace(pos, duration, shp, rot.get());
				}
			}
			
			prevTrace = nextTrace;
			nextTrace = traceIter.next();
		}
	}
	
}
