package components;

import org.lwjgl.input.Keyboard;

public class InputEditor extends Input {
	
	public InputEditor( int m ) {
		super(m);
	}
	
	public final void Update() {
		if( Keyboard.isKeyDown(Keyboard.KEY_UP) ) {
			//TODO fer editor controls de teclat	
			/*else if( function.equals("EDITORUP") ) {
			Screen.up.add(0, -0.25f);
		}
		else if( function.equals("EDITORDOWN") ) {
			if(Screen.up.y < 0) {
				Screen.up.add(0, 0.25f);
			}
		}*/
		}
		if( Keyboard.isKeyDown(Keyboard.KEY_DOWN) ) {
			
		}		
	}
	
}
