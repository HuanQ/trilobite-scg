package game;

import managers.Constant;
import managers.Screen;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Start {
	static final int										startGame = 1;
	static final int										quitProgram = 2;
	static final int										mainMenu = 3;
	
	private boolean											active;
	static private int										option;
	static private Menu										mainScreen;
	static private Game										mainGame;
	
	public void start() {
		initGraphics();
		Constant.Init();
		Screen.Init();

		active = true;
		option = Start.mainMenu;
		
		while(active) {
			switch (option) {
				case Start.quitProgram:
					active = false;
				break;
				
				case Start.startGame:
					mainGame = new Game("decline", 1);
					mainGame.start();
					mainScreen = new Menu( "resources/data/ingame.xml" );
					mainScreen.start();
				break;
				
				case Start.mainMenu:
					mainScreen = new Menu( "resources/data/main.xml" );
					mainScreen.start();
				break;
			}
		}
		Display.destroy();
	}
	
	public static void quitProgram() {
		option = Start.quitProgram;
		mainScreen.end();
	}
	
	public static void startGame() {
		option = Start.startGame;
		mainScreen.end();
	}
	
	public static void startMenu() {
		option = Start.mainMenu;
		mainScreen.end();
	}

	public static void main(String[] argv) {
		Start displayExample = new Start();
		displayExample.start();
	}
	
	private void initGraphics() {
		//TODO: Crear un manager de graphics i potser fins i tot buidar tot el reste del codi de OpenGL
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
		//glEnable(GL_ALPHA_TEST);
		//glAlphaFunc(GL_GREATER, 0.5f);
		
		glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
		Mouse.setGrabbed(true);
		glViewport(0, 0, 1024, 800);
	}
}