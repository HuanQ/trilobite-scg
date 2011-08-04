package game;

import java.io.File;

import managers.Component;
import managers.Constant;
import managers.Level;
import managers.Clock;
import managers.Screen;

import org.lwjgl.opengl.Display;

import components.ProgressBar;

public class Game {
	private final String										directoryName;
	
	public Game( final String name, int number ) {
		// Set up directory if needed
		String firstDir = name + "/";
		File directory = new File( "resources/games/" + firstDir );
		if( !directory.exists() ) {
			directory.mkdir();
		}
		
		boolean firstPlay = false;
		directoryName = firstDir + "/" + number + "/";
		directory = new File( "resources/games/" + directoryName );
		if( !directory.exists() ) {
			directory.mkdir();
			firstPlay = number == 1;
		}
		
		// Initialize game
		Screen.Init();
		Constant.Init();
		Component.Init();
		Clock.Init();
		Level.Init( "resources/data/level/" + name + ".xml", name );
		Level.Init( "resources/data/hud/game.xml", name );
		
		// Helpers pop up the first time we play each level 
		if(firstPlay) {
			Level.Init( "resources/data/help.xml", name );
		}
	}
	
	public final void start() {
		// Insert Player and Actors
		int i = 0;
		File actorFile;
		do {
			i++;
			String nextFileName = "resources/games/" + directoryName + i + ".dat";
			actorFile = new File(nextFileName);
			if( actorFile.exists() ) {
				Level.AddActor(nextFileName, i);
			}
		} while( actorFile.exists() );
		
		// Add the actors to the progressbar
		for(ProgressBar p : Component.progressbar.values()) {
			p.addActors();
		}


		int player = Level.AddPlayer( "resources/games/" + directoryName, i-1 );
		
		// Run the game 
		while ( Component.placement.get(player) != null ) {
			Component.Update();
			Component.Render();
			Display.update();
			Display.sync(60);
		}

		// Clean up
		Component.Release();
	}
}
