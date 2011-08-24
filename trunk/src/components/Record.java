package components;

import geometry.Vec2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import data.Snapshot;

import managers.Component;
import managers.Constant;
import managers.Clock;


public class Record {
	// Event types
	static final int													positionChange = 1;
	static final int													movement = 1;
	static final int													gunShot = 2;
	static final int													shield = 4;
	
	private final int													me;
	// Internal data
	private final Vector<Snapshot>										recordedData = new Vector<Snapshot>();
	private int															eventsThisFrame;
	private boolean														needUpdate = true;
	private Vec2														lastRecordedPosition = null;
	private Vec2														lastRecordedDirection = null;
	private float														tickAccumulatedTime = 0;
	private File														file;
	
	public Record( int m, final String path ) {
		me = m;
		int i = 1;
		do {
			file = new File(path + i + ".dat");
			i++;
		} while( file.exists() );		
	}
	
	public final void addEvent( int event, int[] data ) {
		eventsThisFrame = eventsThisFrame | event;
		switch(event) {
		case movement:
			break;
			
		case gunShot:
		case shield:
			needUpdate = true;
			break;
		}
	}
	
	public final void Save() {
		// Save last position
		recordedData.add( new Snapshot(Clock.getTime(Clock.game), eventsThisFrame, new Vec2(Component.placement.get(me).position)) );
		ObjectOutputStream outputStream = null;
		try {
	        FileOutputStream fileoutputstream = new FileOutputStream( file );
		    //ByteArrayOutputStream b = new ByteArrayOutputStream();
	        outputStream = new ObjectOutputStream( fileoutputstream );
	        outputStream.writeObject(recordedData);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            //Close the ObjectOutputStream
            try {
                if(outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}

	public final void Update() {
		Vec2 myPos = Component.placement.get(me).position;
		float dt = Clock.getDelta(Clock.game);
		tickAccumulatedTime += dt;
		
		if( tickAccumulatedTime > Constant.getFloat("Performance_RecordTick") ) {
			// Decide if we need to update
			if( !needUpdate ) {
				if( lastRecordedDirection == null ) {
					// Second update, this always needs to be recorded too
					lastRecordedDirection = new Vec2();
					needUpdate = true;
				}
				else if( (eventsThisFrame & movement) > 0 ) {
					// We calculate our speed
					Vec2 newDirection = new Vec2( lastRecordedPosition );
					newDirection.sub( myPos );
					newDirection.scale( 1 / tickAccumulatedTime );
					if( !newDirection.epsilonEquals( lastRecordedDirection, Constant.getFloat("Performance_RecordEpsilon") ) ) {
						// If we had moved the same direction and speed as the last frame we wouldn't need to record, interpolation would predict it correctly
						needUpdate = true;
					}
				}
			}
			
			if( needUpdate && dt > 0) {
				// TODO: Quan has fet un canvi el que has de grabar, en realitat, és lastposition, no myPos! (ojo que si tens event has de grabar també mypos amb l'event)
				recordedData.add( new Snapshot(Clock.getTime(Clock.game), eventsThisFrame, new Vec2(myPos) ) );
				
				// Prepare the data for the next frame
				eventsThisFrame = 0;
				if( lastRecordedPosition == null ) {
					lastRecordedPosition = new Vec2();
				}
				else {
					lastRecordedDirection = new Vec2( lastRecordedPosition );
					lastRecordedDirection.sub( myPos );
					lastRecordedDirection.scale( 1 / tickAccumulatedTime );
				}
				
				lastRecordedPosition.set( myPos );
				needUpdate = false;
			}
			
			tickAccumulatedTime = 0;
		}
	}
}
