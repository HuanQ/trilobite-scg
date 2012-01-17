package components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import geometry.Angle;

import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Level;
import managers.Screen;
import managers.Clock;

import sequences.Sequence;


public class Spawner {
	private final int											me;
	// Behaviour
	private Angle												spawnDirection;
	private int													wait = 0;
	private float												totalWait;
	private Sequence											mySeq;
	// Internal data
	private int													phase = 0;
	private int													hitTime;
	private int													lastPhase;
	private int													lastSpark = 0;
	// 0: Wait until we are on screen
	// 1: Shoot away
	// 2: Disabled
	// 3: Done
	
	public Spawner( int m, final Angle dir, float w, final Sequence seq ) {
		me = m;
		spawnDirection = dir;
		totalWait = w;
		mySeq = seq;
	}
	
	public Spawner( int m, final Spawner s  ) {
		me = m;
		spawnDirection = new Angle( s.getDirection() );
		totalWait = s.getWait();
		mySeq = s.getSequence().getCopy();
	}
	
	public final void Update() {
		Vec2 myPos = Component.placement.get(me).position;
		int time = Clock.getTime(Clock.game);
		// Check if i am in screen
		switch(phase) {
		case 0:
			float inScreenRadius = 0;
			if(totalWait < 0) {
				// Negative wait means we start doing our job at a certain radius
				inScreenRadius = 0.5f;
			}
			
			if( Screen.inScreen(myPos, inScreenRadius) ) {
				if(wait == 0) {
					wait = time + (int) (totalWait * Constant.timerResolution);
				}
				else if(time > wait) {
					phase = 1;
				}
			}
			break;
			
		case 1:
			Vec2 spawnPoint = Component.placement.get(me).position;
			if( mySeq.Spawn(spawnDirection, spawnPoint) ) {
				phase = 3;
			}
			break;
			
		case 2:
			if(time > hitTime) {
				phase = lastPhase;
			}
			else {
				if(time > lastSpark + (Constant.getFloat("Spark_Duration") * Constant.timerResolution)) {
					// Random positioning
					float rad = Constant.getShape("Spark_Shape").getRadius() * Constant.rnd.nextFloat();
					Angle alpha = new Angle( (float) (2*Math.PI*Constant.rnd.nextFloat()) );
					Vec2 sparkPos = alpha.getDirection();
					sparkPos.scale(rad);
					sparkPos.add(myPos);
					
					// Disabled spark
					Level.AddEffect( sparkPos, "Spark" );
					
					lastSpark = time;
				}
			}
			break;
		
		case 3:
			break;
		}
	}

	public final float getWait() {
		return totalWait;
	}
	
	public final Sequence getSequence() {
		return mySeq;
	}
	
	public final void setSequence( Sequence s ) {
		mySeq = s;
	}

	public final Angle getDirection() {
		return spawnDirection;
	}
	
	public final void setDirection( Angle d ) {
		spawnDirection = d;
	}
	
	public final void Hit() {
		if(phase != 3) {
			if(phase != 2) {
				lastPhase = phase;
			}
			hitTime = Clock.getTime(Clock.game) + (int) (Constant.getFloat("Spawner_DisableTime") * Constant.timerResolution);
			phase = 2;
		}
	}
	
	public final void WriteXML( Document doc, Element root ) {
		Element dir = doc.createElement("Direction");
		dir.appendChild( doc.createTextNode(Float.toString(spawnDirection.get())) );
		root.appendChild(dir);
		
		Element seq = doc.createElement("Sequence");
		seq.appendChild( doc.createTextNode(mySeq.getName()) );
		root.appendChild(seq);
		
		Element wt = doc.createElement("Wait");
		wt.appendChild( doc.createTextNode(Float.toString(totalWait)) );
		root.appendChild(wt);
	}
	
}