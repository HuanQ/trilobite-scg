package game;

import geometry.Vec3;

import java.io.File;

import managers.Component;
import managers.Constant;
import managers.Level;
import managers.Clock;
import managers.Screen;
import managers.Sound;

import org.lwjgl.opengl.Display;

import components.PanelActors;

public class Game {
	private boolean												victory = false;
	
	public Game() {
		boolean firstPlay = false;
		// Set up directory if needed
		File directory = new File( "resources/games/" + Level.lvlname + "/" );
		if( !directory.exists() ) {
			directory.mkdir();
			firstPlay = true;
		}
		
		// Initialize game
		Screen.Init();
		Constant.Init();
		Component.Init();
		Clock.Init();
		Level.Init( "resources/data/hud/game.xml" );
		Level.Init( "resources/data/level/" + Level.lvlname + ".xml" );
		Level.Init( "resources/data/hud/Background.xml" );
		
		// Helpers pop up the first time we play the Intro level 
		if( firstPlay && Level.lvlname.equals("Intro") ) {
			Level.Init( "resources/data/hud/help.xml" );
		}
		
		Component.fader.set(Vec3.black);
	}
	
	public final void start( final String shipType ) {
		Sound.Play(Sound.start);
		
		Component.fader.FadeToWhite();
		Clock.Pause(Clock.game);
		
		// Insert Player and Actors
		String nextFileName;
		int lowestAvailNumber = 0;
		for(int i=1; i<Constant.getFloat("Rules_MaxActors"); ++i) {
			String base = "resources/games/" + Level.lvlname + "/" + i;
			String extension = PanelActors.getFileName(base);
			if( extension != null ) {
				nextFileName = base + "." + extension;
				Level.AddActor(nextFileName, i, extension);
			}
			else if ( lowestAvailNumber == 0 ) {
				lowestAvailNumber = i;
			}
		}
		
		// Add the actors to the progressbar
		Component.progressbar.addActors();
		
		if( lowestAvailNumber != 0) {
			int player = Level.AddPlayer( "resources/games/" + Level.lvlname + "/", lowestAvailNumber, shipType );
	
			// Run the game
			boolean startPaused = true;
			while ( Component.placement.get(player) != null && !victory ) {
				Component.Update();
				Component.Render();
				Display.update();
				Display.sync(100);
				if(startPaused && Component.fader.isDone()) {
					Clock.Unpause(Clock.game);
					startPaused = false;
				}
			}
			
			Clock.UnmaskAll();
			if(victory) {
				Sound.Play(Sound.win);
				Component.record.get(player).Save();
			}
			
			Start.gameMenu();
		}
		else {
			// Max actors reached
			//TODO Carregar tots els arxius de la carpeta (agafant el llistat i posar spawnpoints dinamics fent screenups positius per a fer espai -> NO REAL ACTOR LIMIT
		}
		
		// Clean up
		Component.Release();
	}
	
	public final void end() {
		victory = true;
	}
	
}
