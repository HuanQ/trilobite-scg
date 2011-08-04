package components;

import managers.Constant;
import managers.Clock;
import geometry.Vec3;

public class Fader {
	// Internal states
	final private int										toWhite = 1;
	final private int										toBlack = 2;
	//
	public Vec3												color = new Vec3(Vec3.white);
	private int												direction = 0;
	
	public final void resetBlack() {
		color = new Vec3(Vec3.black);
		direction = 0;
	}
	
	public final void resetWhite() {
		color.set(Vec3.white);
		direction = 0;
	}
	
	public final boolean isDone() {
		return direction == 0;
	}
	
	public final void fadeToBlack() {
		direction = toBlack;
	}
	
	public final void fadeToWhite() {
		direction = toWhite;
	}
	
	public final void Update() {
		float dt = Clock.getDelta(Clock.ui);
		if(direction > 0 && dt > 0) {
			Vec3 speed = new Vec3(dt, dt, dt);
			speed.scale( Constant.getFloat("Fader_Speed"));
			switch(direction) {
			case toBlack:
				color.sub(speed);
				break;
			case toWhite:
				color.add(speed);
				break;
			}
			color.clamp(0, 1);
			if( color.equals(Constant.getVector("Fader_Color")) || color.isOnes() ) {
				direction = 0;
			}
		}
	}
}
