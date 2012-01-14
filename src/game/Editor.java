package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import managers.Component;
import managers.Constant;
import managers.Level;
import managers.Clock;
import managers.Screen;

import org.lwjgl.opengl.Display;

import components.Clickable;

public class Editor {
	private boolean												active = true;
	private boolean												play = false;
	// Move the screen
	
	//TODO: Mostrar un contador de temps per l'editor (per posar el final i les musiques!)
	public Editor( final String name ) {
		// Create the file if it does not exist
		File file = new File( "resources/data/level/" + name + ".xml" );
		if( !file.exists() ) {
			try {
				file.createNewFile();
				FileWriter fw = new FileWriter(file);
				fw.write("<Trilobite></Trilobite>");
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Initialize game
		Screen.Init();
		Constant.Init();
		Component.Init();
		Clock.Init();
		
		Level.AddMouse();
		Level.Init( "resources/data/level/" + name + ".xml" );
		Level.Init( "resources/data/hud/editor.xml" );
		Level.AddEditorControls();
		
		// Menus
		Component.mouse.UnselectTool();
		for(Clickable c : Component.clickable.values()) {
			if( c.getFunction().equals("STOP") || c.getFunction().equals("RELOAD") || c.getFunction().equals("SAVE") ) {
				c.setState(Clickable.off);
			}
		}
		
		Clock.Pause(Clock.game);
	}
	
	public final void start() {
		// Run the game 
		while ( active || !Component.fader.isDone() ) {
			if(play) {
				Screen.up.add(0.f, Clock.getDelta(Clock.game) * Constant.getFloat("Rules_ScreenSpeed"));
			}
			
			Component.Update();
			Component.Render();
			Display.update();
			Display.sync(100);
		}

		// Clean up
		Component.Release();
	}
	
	public final void end() {
		active = false;
	}
	
	public final void setPlay( boolean p ) {
		play = p;
	}
}
