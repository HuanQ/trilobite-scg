package components;

import javax.vecmath.Vector2f;

import managers.Component;
import managers.Constant;
import managers.Screen;
import managers.Timer;



public class Bullet {
	private final Vector2f												direction;
	private final int													me;

	public Bullet(final int m, final Vector2f v) {
		me = m;
		direction = v;
	}
	
	public void Update() {
		//TODO: Fer-me el meu propi vector2 i fer-me les meves funcions (return amb operació, etc...)
		//TODO: Mirar totes les variables creades a cada mètode i veure si s'hi pot posar final (cal lo anterior)
		Vector2f travelDistance = new Vector2f(direction);
		travelDistance.scale( Timer.getDelta() );
		
		Component.placement.get(me).addPosition(travelDistance);
		
		// Selfkill a certa distància de la screen
		float killDist = Constant.getFloat("Bullet_KillDistance");
		if(Component.shape.get(me) != null) {
			killDist += Component.shape.get(me).getRadius();
		}
		if( !Screen.inScreen( Component.placement.get(me).getPosition(), killDist ) ) {
			Component.deadObjects.add(me);
		}
	}
}
