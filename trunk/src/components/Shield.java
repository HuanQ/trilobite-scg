package components;

import data.Timer;
import geometry.Polygon;

import managers.Clock;
import managers.Component;
import managers.Constant;


public class Shield {
	private final int											me;
	private final float											duration;
	private final Timer											shieldDuration;

	public Shield( int m, float dur ) {
		me = m;
		duration = dur;
		shieldDuration = new Timer( dur, Clock.game );
	}
	// TODO: Shield ha de saber la seva duracio i cooldown
	
	public final void Update() {
		if( shieldDuration.Check() ) {
			Component.killable.get(me).setType( Killable.playerTeam );
		}
	}
	
	public final void Raise() {
		shieldDuration.Refresh();
		Component.killable.get(me).setType( Killable.shieldTeam );
		
		//TODO: S'ha de grabar la duració perquè cada actor tindrà duracions diferents		
		// Record
		Record rec = Component.record.get(me);
		if( rec != null) {
			rec.addEvent( Record.shield, null );
		}
		
		Shape shp = Component.shape.get(me);
		if( shp == null || shp.polygons.isEmpty()) {
			//TODO: Shield for shapeless objects
		}
		else {
			// Draw a bigger self below me
			Integer id = Component.getID();
			Component.timedObject.put( id, new TimedObject(id, duration, Clock.game) );
			Component.shape.put( id, new Shape(Component.shape.get(me)) );
			Component.placement.put( id, Component.placement.get(me) );
			Component.drawer.put( id, new Drawer(id) );
			
			shp = Component.shape.get(id);
			
			for(Polygon p : shp.polygons) {
				p.multSize( Constant.getFloat("Ship_ShieldScale") );
				p.layer = Constant.getFloat("Ship_ShieldLayer");
				p.setColor( Constant.getVector("Ship_Color") );
			}
		}
	}
}
