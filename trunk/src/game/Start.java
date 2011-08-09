package game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Start {
	static final int										startGame = 1;
	static final int										quitProgram = 2;
	static final int										startMain = 3;
	static final int										startEditor = 4;
	static final int										startPlaying = 5;
	
	private boolean											active;
	static private int										option;
	static private Menu										menu;
	static private Game										game;
	static private Editor									editor;
	
	public final void start() {
		Start.initGraphics();
		
		active = true;
		option = Start.startMain;
		
		while(active) {
			switch (option) {
			case Start.quitProgram:
				active = false;
				break;
				
			case Start.startGame:
				menu = new Menu("ingame" );
				menu.start();
				menu = null;
				break;
				
			case Start.startMain:
				menu = new Menu("main");
				menu.start();
				menu = null;
				break;
			
			case Start.startEditor:
				editor = new Editor("Intro");
				editor.start();
				editor = null;
				break;
				
			case Start.startPlaying:
				game = new Game();
				game.start();
				game = null;
				break;
			}
		}
		Display.destroy();
	}
	
	static public final void quitProgram() {
		option = Start.quitProgram;
		menu.end();
	}
	
	static public final void startGame() {
		option = Start.startGame;
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
	
	static public final void startMenu() {
		option = Start.startMain;
		if(menu != null) menu.end();
		if(editor != null) editor.end();
	}
	
	static public final void startPlaying() {
		option = Start.startPlaying;
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
			Display.setFullscreen(false);
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