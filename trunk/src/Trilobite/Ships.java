package Trilobite;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import managers.Component;
import managers.Constant;

import components.*;

public class Ships {
//EnemyPosition
	static public Integer AddPlayer() {
		Integer id = Component.getID();
		Component.keyboard.put( id, new KeyboardInput(id) );
		Component.placement.put( id, new Placement( Constant.getPoint("Green_Point") ) );
		Component.drawer.put( id, new Drawer(id, Constant.getVector("Green_Color") ) );
		Component.gun.put( id, new Gun(id, Constant.getPoint("Gun_Point")) );
		Component.shield.put( id, new Shield(id) );
		Component.mover.put( id, new Mover(id, Constant.getFloat("Ship_Speed"), false, true, 0) );
		Component.shape.put( id, Constant.getShape("Ship_Shape") );
		Component.canBeKilled.put( id, new CanBeKilled(id) );
		Component.canKill.put( id, new CanKill() );
		
		Component.placement.get(id).setRotation( (float) Math.PI / 2 );
		
		return id;
	}
	
	static public Integer AddDumb(boolean leftExit, int shortRoute) {
		Integer id = Component.getID();
		
		float rotSpeed = Constant.getFloat("Dumb_RotationSpeed");
		float rotStart = Constant.getFloat("Dumb_RotationStart");

		if(!leftExit)
		{
			rotSpeed = -rotSpeed;
			rotStart += (float) Math.PI;
		}
			
		Component.dumb.put( id, new Dumb(id, rotSpeed, rotStart, shortRoute ) );
		Component.mover.put( id, new Mover(id, Constant.getFloat("Dumb_Speed"), false, false, Constant.getFloat("Dumb_Gravity") ) );
		Component.placement.put( id, new Placement( Constant.getPoint("Dumb_Point") ) );
		Component.drawer.put( id, new Drawer(id, Constant.getVector("Dumb_Color") ) );
		Component.shape.put( id, Constant.getShape("Dumb_Shape") );
		Component.canBeKilled.put( id, new CanBeKilled(id) );
		Component.canKill.put( id, new CanKill() );

		return id;
	}
	
	static public Integer AddWall(Vector2f pos, Vector2f size) {
		Integer id = Component.getID();
		
		Component.placement.put( id, new Placement( pos ) );
		Component.drawer.put( id, new Drawer( id ) );
		Component.shape.put( id, new Shape() );
		Component.canKill.put( id, new CanKill() );
		Component.mover.put( id, new Mover( id, 0, false, false, 0 ) );
		
		Component.shape.get(id).add( new geometry.Rectangle(size, new Vector3f(0,0,0) ));
		
		// TODO: Crear component Impassable (impassable terrain)
		return id;
	}
	
	static public Integer AddSpawner() {
		Integer id = Component.getID();

		Component.placement.put( id, new Placement( new Vector2f( 0.5f, 0.1f ) ) );
		Component.spawner.put( id, new Spawner(id, 0, Constant.getString("Spawner_Sequence")) );
		Component.mover.put( id, new Mover( id, 0, false, false, Constant.getFloat("Spawner_Gravity") ) );
		Component.drawer.put( id, new Drawer(id, Constant.getVector("Spawner_Color") ) );
		Component.shape.put( id, Constant.getShape("Spawner_Shape") );
		Component.canKill.put( id, new CanKill() );
		
		return id;
	}
}