package components;

import geometry.Angle;

import javax.vecmath.Vector2f;

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
	
	public Spawner(final int m, final float dir, final String seq ) {
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
		Vector2f myPos = Component.placement.get(me).getPosition();
		
		// Check if i am in screen
		if( Screen.inScreen(myPos, 0) ) {
			if(wait == 0) {
				wait = Timer.getTime() + (int) Constant.getFloat("Spawner_Wait") * Constant.timerResolution;
			}
			else if(Timer.getTime() > wait) {
				Vector2f spawnPoint = new Vector2f();
				spawnPoint.x += Component.placement.get(me).getPosition().x + Constant.getPoint("Spawner_Point").x;
				spawnPoint.y += Component.placement.get(me).getPosition().y + Constant.getPoint("Spawner_Point").y;
				mySeq.Spawn( Timer.getTime(), Constant.getFloat("Dumb_RotationStart"), spawnPoint );
			}
		}
		
		// Selfkill a certa distància de la screen
		float killDist = Constant.getFloat("Spawner_KillDistance");
		if(Component.shape.get(me) != null) {
			killDist += Component.shape.get(me).getRadius();
		}
		if( !Screen.inScreen(Component.placement.get(me).getPosition(), killDist) ) {
			Component.deadObjects.add(me);
		}
	}
}