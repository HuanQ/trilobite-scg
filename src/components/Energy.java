package components;

import geometry.Rectangle;
import geometry.Vec3;
import managers.Component;

import managers.Clock;

public class Energy {
	private final int										me;
	private final Vec3										emptyColor;
	private final Vec3										fullColor;
	// Internal data
	private final float										originalSize;
	private Rectangle										myRectangle;
	private float											time;
	private float											shieldCooldown;
	
	public Energy( int m, final Vec3 empty, final Vec3 full ) {
		me = m;
		myRectangle = Component.shape.get(me).getRectangle();
		emptyColor = empty;
		fullColor = full;
		originalSize = myRectangle.size.y;
		time = 0;
		shieldCooldown = 0;
	}
	
	public final void Update() {
		if(shieldCooldown != 0 && time < shieldCooldown) {
			// Resize the bar
			time += Clock.getDelta(Clock.game);
			float percent = time / shieldCooldown;
			float percentDelta = Clock.getDelta(Clock.game) / shieldCooldown;
			
			myRectangle.size.y = originalSize * percent;
			
			myRectangle.offset.y -= percentDelta * originalSize/2;

			if(time >= shieldCooldown) {
				// Recharge done
				myRectangle.setColor(fullColor);
			}
		}
	}
	
	public final void Exhaust( float cd ) {
		shieldCooldown = cd;
		time = 0;
		myRectangle.size.y = 0;
		myRectangle.offset.y += originalSize/2;
		myRectangle.setColor(emptyColor);
	}
}
