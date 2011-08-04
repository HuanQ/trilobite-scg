package components;

import java.util.Collections;
import java.util.Vector;

import geometry.Polygon;
import geometry.Rectangle;
import geometry.Vec2;
import geometry.Vec3;

import managers.Component;
import managers.Constant;
import managers.Clock;

public class ProgressBar {
	private final int										me;
	private final Vec3										color;
	// Internal data
	private final int										levelTime;	
	private final Vector<Float>								bestTimes;
	private Rectangle										myRectangle;
	private Rectangle										myBar;

	public ProgressBar( int m, final Vec3 col, final String lvlname ) {
		me = m;
		color = col;
		levelTime = (int) Constant.getFloat(lvlname + "_Time") * Constant.timerResolution;
		bestTimes = new Vector<Float>();
		
		for(Polygon p : Component.shape.get(me).polygons) {
			if(p.whoAmI() == Polygon.rectangle) {
				myRectangle = (Rectangle) p;
				break;
			}
		}
		
		Vec2 mySize = new Vec2( myRectangle.size.x, 0.003f);
		myBar = new Rectangle(mySize, new Vec2(), myRectangle.layer + 0.02f, true);
		myBar.setColor(Vec3.red);
		Component.shape.get(me).add(myBar);
	}
	
	public final void Update() {
		float percentCompleted = (float) Clock.getTime(Clock.game) / levelTime;
		myBar.offset.y = myRectangle.size.y/2 - percentCompleted*myRectangle.size.y;
	}
	
	public final void addActors() {
		for(Actor a : Component.actor.values()) {
			float life = (float) a.getLifeLength() / levelTime;
			life = Math.min(life, 1);
			bestTimes.add(life);
		}
			
		if( bestTimes.size() > 0 ) {
			Collections.sort(bestTimes);
			Collections.reverse(bestTimes);
	
			float margin = 0.01f;
			Shape shp = Component.shape.get(me);
			Vec2 pos = new Vec2();
			pos.x = -myRectangle.size.x / 2 + margin;
			pos.y = myRectangle.size.y / 2 - margin;
			float layer = myRectangle.layer + 0.01f;
			
			float barWidth = -2 * pos.x / bestTimes.size();
			float barMaxHeight = 2 * pos.y;
			
			for(int index = 0; index < bestTimes.size(); ++index) {
				Vec2 myPos =  new Vec2(pos);
				myPos.x += barWidth/2 + index * barWidth;
				myPos.y += -bestTimes.get(index) * barMaxHeight/2;
				
				Vec2 mySize =  new Vec2(barWidth, bestTimes.get(index) * barMaxHeight);
				Rectangle r = new Rectangle(mySize, myPos, layer, true);
				r.setColor(color);
				
				shp.add(r);
			}
		}
		else {
			// Die unless we have some actors's life to show
			//Component.deadObjects.add(me);
		}
	}
}
