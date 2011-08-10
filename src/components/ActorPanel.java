package components;

import geometry.Rectangle;
import geometry.Vec2;

import java.io.File;
import java.util.Vector;

import managers.Component;
import managers.Constant;
import managers.Level;

public class ActorPanel {
	private final int										me;
	private final Rectangle									myRectangle;
	private final float										levelTime;
	// Internal data
	private final Vec2										nextPlaceOffset;
	private final Vector<Integer>							shownActors;
	private boolean											reload = false;
	private boolean											victory = false;

	public ActorPanel( int m ) {
		me = m;
		levelTime = Constant.getFloat(Level.lvlname + "_Time") * Constant.timerResolution;
		myRectangle = Component.shape.get(me).getRectangle();
		nextPlaceOffset = new Vec2();
		shownActors = new Vector<Integer>();
	}
	
	public final boolean isReloadTime() {
		return reload;
	}
	
	public final boolean isVictory() {
		return victory;
	}
	
	public final void Reload() {
		reload = true;
	}
	
	public final void Load() {
		reload = false;
		// Delete all actors;
		for(Integer i : shownActors) {
			Component.deadObjects.add(i);
		}
		shownActors.clear();
		
		// Prepare our insert positions
		Vec2 realSpace = new Vec2(myRectangle.size);
		realSpace.x *= ( 1-Constant.getFloat("GUI_PanelMarginX") )/2;
		realSpace.y *= ( 1-Constant.getFloat("GUI_PanelMarginY") )/2;
		nextPlaceOffset.x = -realSpace.x;
		nextPlaceOffset.y = -realSpace.y;
		
		// Load Actors
		String nextFileName;
		File actorFile;
		boolean newline = true;
		int highestLife = 0;
		for(int i=1; i<Constant.getFloat("Rules_MaxActors"); ++i) {
			nextFileName = "resources/games/" + Level.lvlname + "/" + i + ".dat";
			actorFile = new File(nextFileName);
			if( actorFile.exists() ) {
				// Load
				Actor act = new Actor(0, nextFileName, false);
				int life = (int) (100 * act.getLifeLength() / levelTime);
				life = Math.min(life, 100);
				Shape shp = new Shape(Constant.getShape("Ship_Shape"));
				shp.getText().setText(Integer.toString(i));
				if(life > highestLife) {
					highestLife = life;
				}
				
				// Write success %
				Shape succ = new Shape(Constant.getShape("Success_Shape"));
				succ.getText().setText(Integer.toString(life) + "%");
				
				// Insert indicator
				shp.add( succ );
				shp.add( new Shape(Constant.getShape("Delete_Shape")) );
				Vec2 insertPos = new Vec2(Component.placement.get(me).position);

				if(i == 1) {
					if(newline) {
						nextPlaceOffset.x += shp.getRadius()/2;
					}
					nextPlaceOffset.y += shp.getRadius()/2;
				}
				insertPos.add(nextPlaceOffset);
				shownActors.add( Level.AddActorIndicator(insertPos, shp, i) );
				
				// Move nextplace
				nextPlaceOffset.x += shp.getRadius();
				if(nextPlaceOffset.x > realSpace.x) {
					nextPlaceOffset.x = -realSpace.x;
					nextPlaceOffset.y += shp.getRadius();
					newline = true;
				}
			}
		}
		
		victory = highestLife >= 100;
	}
	
}
