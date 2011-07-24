package components;

import geometry.Angle;

import geometry.Vec2;

import managers.Component;
import managers.Constant;
import managers.Screen;
import managers.Timer;


import sequences.CrossedDumb;
import sequences.Sequence;


public class Spawner {
	private final int											me;
	// Behaviour
	private final Angle											spawnDirection;
	private long												wait;
	private float												rotationSpeed;
	// Internal data
	private final Sequence										mySeq;
	
	public Spawner( int m, final Angle dir, final String seq, float rotSpeed, float freq, int count ) {
		me = m;
		spawnDirection = dir;
		wait = 0;
		rotationSpeed = rotSpeed;
		if(  seq.equals("CrossedDumb") ) {
			mySeq = new CrossedDumb( freq, count );
		}
		else {
			mySeq = null;
		}
	}
	
	public void Update() {
		Vec2 myPos = Component.placement.get(me).getPosition();
		
		// Check if i am in screen
		if( Screen.inScreen(myPos, 0) ) {
			if(wait == 0) {
				wait = Timer.getTime() + (int) (Constant.getFloat("Spawner_Wait") * Constant.timerResolution);
			}
			else if(Timer.getTime() > wait) {
				Vec2 spawnPoint = new Vec2();
				spawnPoint.add(Component.placement.get(me).getPosition(), Constant.getPoint("Spawner_Point"));
				mySeq.Spawn( spawnDirection, spawnPoint, rotationSpeed );
			}
		}
	}
}