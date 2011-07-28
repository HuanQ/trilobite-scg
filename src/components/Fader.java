package components;

import managers.Constant;
import managers.Timer;
import geometry.Vec3;

public class Fader {
	// Internal states
	final private int										toWhite = 1;
	final private int										toBlack = 2;
	//
	private Vec3											color;
	private int												direction;
	
	public Fader() {
		color = new Vec3(Vec3.white);
		direction = 0;
	}
	
	public void reset() {
		color.set(Vec3.white);
		direction = 0;
	}
	
	public Vec3 getColor() {
		return color;
	}
	
	public boolean isDone() {
		return direction == 0;
	}
	
	public void fadeToBlack() {
		direction = toBlack;
	}
	
	public void fadeToWhite() {
		direction = toWhite;
	}
	
	public void Update() {
		if(direction > 0) {
			float dt = Timer.getDelta();
			Vec3 speed = new Vec3(dt, dt, dt);
			speed.scale( Constant.getFloat("Fader_Speed"));
			switch(direction) {
				case toBlack:
					color.add(speed);
				break;
				case toWhite:
					color.sub(speed);
				break;
			}
			color.clamp(0, 1);
			if( color.isZero() || color.isOnes() ) {
				direction = 0;
			}
		}
	}
}
