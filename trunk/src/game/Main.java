package game;

import managers.Component;
import managers.Constant;
import managers.Level;
import managers.Screen;
import managers.Timer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Main {
	
	public void start() {
		
		try {
			Display.setDisplayMode(new DisplayMode(1024, 800));
			Display.setFullscreen(false);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		// init OpenGL here
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1024, 800, 0, -10, 10);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.5f);

//		Mouse.setGrabbed(true);
		
		glLoadIdentity();
		glViewport(0, 0, 1024, 800);
		
		Constant.Init();
		Screen.Init();
		
		Component.Init();
		Timer.Init();

		Level.Init("resources/gamedata/level1.xml");
		//Level.AddPlayer();
		Level.AddActor();
		
		while (!Display.isCloseRequested()) {
			Component.Update();
			Component.Render();
		
			// render OpenGL here
			Display.update();
			Display.sync(60);
		}

		Component.Release();
		Display.destroy();
	}
	
	public static void main(String[] argv) {
		Main displayExample = new Main();
		displayExample.start();
	}
}