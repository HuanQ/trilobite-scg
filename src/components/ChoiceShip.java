package components;

import managers.Component;
import managers.Constant;
import geometry.Angle;
import geometry.Text;
import geometry.Vec2;
import geometry.Vec3;


public class ChoiceShip extends Choice {

	public ChoiceShip( int m, float dir, float ac, float dist ) {
		super(m, dir, ac, dist);

		// Glider
		addShip(direction, dist, Constant.GliderShip);
		
		// Agile
		direction.add(ac/3);
		addShip(direction, dist, Constant.AgileShip);
		
		// Tank
		direction.add(ac/3);
		addShip(direction, dist, Constant.TankShip);
		
		// Defend
		direction.add(ac/3);
		addShip(direction, dist, Constant.DefendShip);
	}

	private final void addShip( Angle dir, float dist, final String name ) {
		Vec2 butPos = dir.getDirection();
		butPos.scale(dist);
		butPos.add(Component.placement.get(me).position);
		
		Integer id = Component.getID();
		Component.placement.put( id, new Placement(butPos) );
		Component.drawer.put( id, new Drawer(id) );
		Component.shape.put( id, Constant.getShape(name + "_Shape") );
		Component.clickable.put(id, new Clickable(id, "SHIP_" + name) );
		
		if(name == Constant.GliderShip) {
			Component.drawer.get(id).setColor(Vec3.white);
		}
		else {
			Component.drawer.get(id).setColor(Vec3.darkgray);
		}
		
		Text t = Component.shape.get(id).getText();
		t.setText("");
	}
}
