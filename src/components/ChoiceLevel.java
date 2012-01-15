package components;

import java.io.File;

import managers.Component;
import geometry.Angle;
import geometry.Circle;
import geometry.Text;
import geometry.Vec2;
import geometry.Vec3;

public class ChoiceLevel extends Choice {

	public ChoiceLevel( int m, float dir, float ac, float dist ) {
		super(m, dir, ac, dist);

		File directory = new File("resources/data/level/");
		File files[] = directory.listFiles();
		
		for(File f : files) {
			if(f.getName().equals(".svn"))
				continue;
			addLevel( direction, dist, f.getName().substring(0, f.getName().length()-4) );
			direction.add( ac / (files.length-1) );
		}
		
	}

	private final void addLevel( Angle dir, float dist, final String name ) {
		float radius = name.length() * 0.002f + 0.03f;
		String texture = "Procedural/planet" + (name.hashCode() % 19 + 1) + ".png";
		
		Vec2 butPos = dir.getDirection();
		butPos.scale(dist);
		butPos.add(Component.placement.get(me).position);
		
		Integer id = Component.getID();
		Component.placement.put( id, new Placement(butPos) );
		Component.drawer.put( id, new Drawer(id) );
		Component.planet.put( id, new Planet(id) );
		
		Shape shp = new Shape();
		Circle crc = new Circle(radius, new Vec2(), 6.2f);
		crc.setTexture(texture);
		shp.add( crc );
		Text txt = new Text(name, new Vec2(), 6.3f, Text.mediumFont);
		txt.setColor( new Vec3(1, 0.5f, 0) );
		shp.add( txt );
		
		Component.shape.put( id, shp );
		Component.clickable.put(id, new Clickable(id, "LEVEL_" + name) );
		
		if( name.equals("Intro") ) {
			Component.drawer.get(id).setColor(Vec3.white);
		}
		else {
			Component.drawer.get(id).setColor(Vec3.darkgray);
		}
	}
}
