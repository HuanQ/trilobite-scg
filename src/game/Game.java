package game;

import java.io.File;

import managers.Component;
import managers.Level;
import managers.Timer;

import org.lwjgl.opengl.Display;

public class Game {
	private final String										directoryName;
	
	public Game( final String name, int number ) {
		// Set up directory if needed
		String firstDir = name + "/";
		File directory = new File( "resources/games/" + firstDir );
		if( !directory.exists() ) {
			directory.mkdir();
		}
		directoryName = firstDir + "/" + number + "/";
		directory = new File( "resources/games/" + directoryName );
		if( !directory.exists() ) {
			directory.mkdir();
		}
		
		// Initialize game
		Component.Init();
		Timer.Init();
		Level.Init( "resources/data/" + name + ".xml" );
	}
	
	public void start() {
		// Insert Player and Actors
		int i = 0;
		File actorFile;
		do {
			i++;
			String nextFileName = "resources/games/" + directoryName + i + ".dat";
			actorFile = new File(nextFileName);
			if( actorFile.exists() ) {
				Level.AddActor(nextFileName);
			}
		} while( actorFile.exists() );
		
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
		Timer.Release();
		Level.Release();
	}
}
