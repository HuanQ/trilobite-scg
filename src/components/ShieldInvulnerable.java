package components;

import geometry.Polygon;

import managers.Clock;
import managers.Component;
import managers.Constant;


public class ShieldInvulnerable extends Shield {

	public ShieldInvulnerable( int m, float dur ) {
		super(m, dur);
	}
	
	protected final void Down() {
		Component.killable.get(me).setType( Killable.playerTeam );
	}
	
	protected final void Up() {
		Component.killable.get(me).setType( Killable.shieldTeam );
		
		Shape shp = Component.shape.get(me);
		if( shp != null ) {
			// Draw a bigger self below me
			Integer id = Component.getID();
			Component.timedObject.put( id, new Timed(id, duration, Clock.game) );
			Component.shape.put( id, new Shape(Component.shape.get(me)) );
			Component.placement.put( id, Component.placement.get(me) );
			Component.drawer.put( id, new Drawer(id) );
			
			shp = Component.shape.get(id);
			shp.Scale(Constant.getFloat("Shield_Size"));
			
			for(Polygon p : shp.polygons) {
				p.layer = Constant.getFloat("Shield_Layer");
				p.setColor( Constant.getVector("Shield_Color") );
			}
		}
	}

}
