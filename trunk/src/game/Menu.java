package game;

import managers.Component;
import managers.Constant;
import managers.Level;
import managers.Clock;
import managers.Screen;

import org.lwjgl.opengl.Display;

public class Menu {
	private boolean											active = true;
	
	public Menu( final String name ) {
		// Initialize menu
		Screen.Init();
		Constant.Init();
		Component.Init();
		Clock.Init();
		Level.AddMouse();
		Level.Init( "resources/data/menu/" + name + ".xml" );
		Clock.pause(Clock.game);
	}
	
	public final void start() {
		// Run the game 
		while ( active || !Component.fader.isDone() ) {
			Component.Update();
			Component.Render();
			Display.update();
			Display.sync(100);
		}
		
		// Clean up
		Component.Release();
	}
	
	public final void end() {
		Component.fader.FadeToBlack();
		active = false;
	}
}
