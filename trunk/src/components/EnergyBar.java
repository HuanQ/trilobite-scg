package components;

import geometry.Rectangle;
import geometry.Vec3;
import managers.Component;
import managers.Constant;
import managers.Clock;

public class EnergyBar {
	private final int										me;
	private final Vec3										emptyColor;
	private final Vec3										fullColor;
	// Internal data
	private final float										originalSize;
	private Rectangle										myRectangle;
	private float											time;
	
	public EnergyBar( int m, final Vec3 empty, final Vec3 full ) {
		me = m;
		myRectangle = Component.shape.get(me).getRectangle();
		emptyColor = empty;
		fullColor = full;
		originalSize = myRectangle.size.y;
		time = Constant.getFloat("Ship_ShieldCooldown");
	}
	
	public final void Update() {
		float shieldCD = Constant.getFloat("Ship_ShieldCooldown");
		if(time < shieldCD) {
			// Resize the bar
			time += Clock.getDelta(Clock.play);
			float percent = time / shieldCD;
			float percentDelta = Clock.getDelta(Clock.play) / shieldCD;
			
			myRectangle.size.y = originalSize * percent;
			
			myRectangle.offset.y -= percentDelta * originalSize/2;

			if(time >= shieldCD) {
				// Recharge done
				myRectangle.setColor(fullColor);
			}
		}
	}
	
	public final void Exhaust() {
		time = 0;
		myRectangle.size.y = 0;
		myRectangle.offset.y += originalSize/2;
		myRectangle.setColor(emptyColor);
	}
}
