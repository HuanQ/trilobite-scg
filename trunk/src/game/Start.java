package game;

import managers.Constant;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Start {
	static final int										gameMenu = 1;
	static final int										quitProgram = 2;
	static final int										mainMenu = 3;
	static final int										startEditor = 4;
	static final int										startGame = 5;
	static final int										victoryMenu = 6;
	
	private boolean											active;
	static private int										option;
	static private Menu										menu;
	static private Game										game;
	static private Editor									editor;
	static public String									nextship;
	
	public final void start() {
		Start.initGraphics();
		
		active = true;
		option = Start.mainMenu;
		
		while(active) {
			switch (option) {
			case Start.quitProgram:
				active = false;
				break;
				
			case Start.gameMenu:
				nextship = Constant.GliderShip;
				menu = new Menu("ingame" );
				menu.start();
				menu = null;
				break;
				
			case Start.mainMenu:
				menu = new Menu("main");
				menu.start();
				menu = null;
				break;
			
			case Start.startEditor:
				editor = new Editor("Intro");
				editor.start();
				editor = null;
				break;
				
			case Start.startGame:
				game = new Game();
				game.start( nextship );
				game = null;
				break;
				
			case Start.victoryMenu:
				menu = new Menu("victory");
				menu.start();
				menu = null;
			}
		}
		Display.destroy();
	}
	
	static public final void quitProgram() {
		option = Start.quitProgram;
		menu.end();
	}
	
	static public final void gameMenu() {
		option = Start.gameMenu;
		if(menu != null) menu.end();
	}
	
	static public final void startEditor() {
		option = Start.startEditor;
		menu.end();
	}
	
	static public final void restartEditor() {
		option = Start.startEditor;
		editor.end();
	}
	
	static public final void mainMenu() {
		option = Start.mainMenu;
		if(menu != null) menu.end();
		if(editor != null) editor.end();
	}
	
	static public final void startGame() {
		option = Start.startGame;
		menu.end();
	}
	
	static public final void gameWon() {
		option = Start.gameMenu;
		game.end();
	}
	
	static public final void victoryMenu() {
		option = Start.victoryMenu;
		menu.end();
	}

	static public final void main(String[] argv) {
		Start displayExample = new Start();
		displayExample.start();
	}
	
	static private final void initGraphics() {
		//TODO: Crear un manager de graphics i potser fins i tot buidar tot el reste del codi de OpenGL
		DisplayMode dm = null;
		try {
			//dm = Display.getDesktopDisplayMode();
			dm = new DisplayMode(1024, 800);
			Display.setDisplayMode(dm);
			Display.setFullscreen(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		// init OpenGL here
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, dm.getWidth(), dm.getHeight(), 0, -10, 10);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.5f);
		
		//glEnable(GL_BLEND);
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
		Mouse.setGrabbed(true);
		glViewport(0, 0, dm.getWidth(), dm.getHeight());
	}
}