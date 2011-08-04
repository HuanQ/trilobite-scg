package components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import geometry.Angle;

import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Screen;
import managers.Clock;

import sequences.Sequence;


public class Spawner {
	private final int											me;
	// Behaviour
	private final Angle											spawnDirection;
	private int													wait = 0;
	private float												totalWait;
	private final Sequence										mySeq;
	// Internal data
	private int													phase = 0;
	private int													hitTime;
	private int													lastPhase;
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
	
	public final void Update() {
		Vec2 myPos = Component.placement.get(me).position;
		int time = Clock.getTime(Clock.game);
		// Check if i am in screen
		switch(phase) {
		case 0:
			if( Screen.inScreen(myPos, 0) ) {
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
			if( mySeq.Spawn( spawnDirection, spawnPoint ) ) {
				phase = 3;
			}
			break;
			
		case 2:
			if(time > hitTime) {
				phase = lastPhase;
			}
			else {
				//TODO: Disabled spark
			}
			break;
		
		case 3:
			break;
		}
	}
	
	public final void Hit() {
		if(phase != 3) {
			hitTime = Clock.getTime(Clock.game) + (int) (Constant.getFloat("Spawner_DisableTime") * Constant.timerResolution);
			lastPhase = phase;
			phase = 2;
		}
	}
	
	public final void writeXml( Document doc, Element root ) {
		Element dir = doc.createElement("Direction");
		dir.appendChild( doc.createTextNode(Float.toString(spawnDirection.get())) );
		root.appendChild(dir);
		
		Element seq = doc.createElement("Sequence");
		seq.appendChild( doc.createTextNode(mySeq.getName()) );
		root.appendChild(seq);
		
		Element wt = doc.createElement("Wait");
		wt.appendChild( doc.createTextNode(Float.toString(totalWait)) );
		root.appendChild(wt);
		
		if( Component.shape.get(me) != null) {
			Component.shape.get(me).writeXml(doc, root);
		}
	}
	
}