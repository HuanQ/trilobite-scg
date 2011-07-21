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
	// Internal data
	private final Sequence										mySeq;
	
	public Spawner( int m, float dir, final String seq ) {
		me = m;
		spawnDirection = new Angle(dir);
		wait = 0;
		if(  seq.equals("CrossedDumb") ) {
			mySeq = new CrossedDumb(Constant.getFloat("Spawner_Frequency"), (int) Constant.getFloat("Spawner_Count") );
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
				wait = Timer.getTime() + (int) Constant.getFloat("Spawner_Wait") * Constant.timerResolution;
			}
			else if(Timer.getTime() > wait) {
				Vec2 spawnPoint = new Vec2();
				spawnPoint.add(Component.placement.get(me).getPosition(), Constant.getPoint("Spawner_Point"));
				mySeq.Spawn( Timer.getTime(), Constant.getFloat("Spawner_RotationStart"), spawnPoint );
			}
		}
		
		// Selfkill at a certain distance from the screen limit
		float killDist = Constant.getFloat("Spawner_KillDistance");
		if(Component.shape.get(me) != null) {
			killDist += Component.shape.get(me).getRadius();
		}
		if( myPos.x > 1+killDist && myPos.x < -killDist && myPos.y < 1+killDist ) {
			Component.deadObjects.add(me);
		}
	}
}