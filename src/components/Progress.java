package components;

import java.util.Collections;
import java.util.Vector;

import game.Start;
import geometry.Rectangle;
import geometry.Text;
import geometry.Vec2;
import geometry.Vec3;
import graphics.Sprite;

import managers.Component;
import managers.Constant;
import managers.Clock;
import managers.Level;

public class Progress {
	private final int										me;
	private final Vec3										color;
	// Internal data
	private final float										levelTime;
	private final Vector<Float>								bestTimes;
	private Rectangle										myRectangle;
	private Text											myText;
	private Rectangle										myBar;

	public Progress( int m, final Vec3 col ) {
		me = m;
		color = col;
		levelTime = (int) (Constant.getFloat(Level.lvlname + "_Time") * Constant.timerResolution);
		bestTimes = new Vector<Float>();
		
		myRectangle = Component.shape.get(me).getRectangle();
		myText = Component.shape.get(me).getText();
		
		Vec2 mySize = new Vec2( myRectangle.size.x, 0.003f);
		myBar = new Rectangle(mySize, new Vec2(), myRectangle.layer + 0.02f, true);
		myBar.setColor(Vec3.red);
		Component.shape.get(me).add(myBar);
	}
	
	public final void Update() {
		float percentCompleted = (float) Clock.getTime(Clock.game) / levelTime;
		myBar.offset.y = myRectangle.size.y/2 - percentCompleted*myRectangle.size.y;
		
		// Set percent counter's text
		percentCompleted *= 100;
		myText.setText( Integer.toString((int) percentCompleted) + "%" );
		if( percentCompleted >= 100 ) {
			Start.gameWon();
		}
	}
	
	public final void addWall( final Vec2 pos, final Shape shp ) {
		//TODO: Afegir les walls a la barra de progres
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
	
			float margin = 0.0075f;
			Shape shp = Component.shape.get(me);
			Vec2 pos = new Vec2();
			pos.x = -myRectangle.size.x / 2 + margin;
			pos.y = myRectangle.size.y / 2 - margin;
			float layer = myRectangle.layer + 0.02f;
			
			float barWidth = -2 * pos.x / bestTimes.size();
			float barMaxHeight = 2 * pos.y;
			
			for(int index = 0; index < bestTimes.size(); ++index) {
				Vec2 myPos =  new Vec2(pos);
				myPos.x += barWidth/2 + index * barWidth;
				myPos.y += -bestTimes.get(index) * barMaxHeight/2;
				Vec2 mySize =  new Vec2(barWidth, bestTimes.get(index) * barMaxHeight);
				
				Rectangle r = new Rectangle(mySize, myPos, layer, true);
				r.setColor(color);
				r.setTexture( Sprite.getRect(r.size) );
				shp.add(r);
			}
		}
	}
}
