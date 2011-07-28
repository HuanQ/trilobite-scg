package components;

import data.Cleaner;
import game.Start;

public class Clickable {
	private final String									function;
	
	public Clickable( final String fun ) {
		function = fun;
	}
	
	public void Click( ) {
		if( function.equals("START") ) {
			Start.startGame();
		}
		else if( function.equals("QUIT") ) {
			Start.quitProgram();
		}
		else if( function.equals("AGAIN") ) {
			Start.startGame();
		}
		else if( function.equals("BACK") ) {
			Start.startMenu();
		}
		else if( function.equals("CLEAN") ) {
			new Cleaner().cleanAll();
		}
	}
}
