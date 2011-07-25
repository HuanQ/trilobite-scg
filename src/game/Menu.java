package game;

import managers.Component;
import managers.Level;
import managers.Timer;

import org.lwjgl.opengl.Display;

public class Menu {
	private boolean											active;
	
	public Menu( final String path ) {
		active = true;
		
		// Initialize game
		Component.Init();
		Timer.Init();
		Level.AddMouse();
		Level.Init( path );
	}
	
	public void start() {
		// Run the game 
		while ( active ) {
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
	
	public void end() {
		active = false;
	}
}
