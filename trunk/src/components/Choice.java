package components;

import geometry.Angle;


public class Choice {
	protected final int											me;
	protected final Angle										direction;
	protected final float										distance;
	
	// Internal data

	protected Choice( int m, float dir, float ac, float dist ) {
		me = m;
		direction = new Angle(dir - ac/2);
		distance = dist;
	}

}
