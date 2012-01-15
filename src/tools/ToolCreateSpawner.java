package tools;

import components.Drawer;
import components.Placement;
import components.Shape;
import components.Spawner;
import components.Timed;

import geometry.Angle;
import geometry.Vec2;
import managers.Clock;
import managers.Component;
import managers.Constant;
import managers.Level;

public class ToolCreateSpawner extends ToolCreate {
	
	private int												mySequence = 0;
	
	public ToolCreateSpawner( int m ) {
		super(m);
	}
	
	public final void LeftButton() {
		if( FirstClick() ) {			
			Component.spawner.put( currentID, new Spawner(currentID, new Angle(3.14f), 0, Level.getSequence(Constant.sequenceList.get(mySequence))) );
			Component.shape.put( currentID, new Shape(Constant.getShape( Constant.sequenceList.get(mySequence) + "_Shape")) );
		}
		else {
			SecondClick();
			
			Vec2 dir = new Vec2( Component.placement.get(pointerID).position );
			dir.sub(firstClick);
			Angle a = new Angle();
			a.set(dir);
			Component.spawner.get(currentID).setDirection( a );
			
			// Leave the object as it is
			currentID = Integer.MIN_VALUE;
			firstClick = null;
		}
	}
	
	public final void WheelButton() {}
	
	public final void WheelUp() {
		// Cycling between sequences
		if(firstClick != null) {
			mySequence = (mySequence + 1 + Constant.sequenceList.size()) % Constant.sequenceList.size();
			String seqName = Constant.sequenceList.get(mySequence);
			Component.spawner.get(currentID).setSequence( Level.getSequence(seqName) );
			Component.shape.put( currentID, new Shape(Constant.getShape(seqName + "_Shape")) );
			
			// Floating text show us our selection
			Integer id = Component.getID();
			Component.placement.put( id, new Placement( new Vec2(firstClick)) );
			Component.drawer.put( id, new Drawer(id) );
			Shape shp = new Shape(Constant.getShape("FloatingText_Shape"));
			shp.getText().setText(seqName);
			Component.shape.put( id, shp );
			Component.timedObject.put( id, new Timed(id, 1, Clock.real) );
		}
	}
	
	public final void WheelDown() {
		if(firstClick != null) {
			mySequence = (mySequence + -1 + Constant.sequenceList.size()) % Constant.sequenceList.size();
			String seqName = Constant.sequenceList.get(mySequence);
			Component.spawner.get(currentID).setSequence( Level.getSequence(seqName) );
			Component.shape.put( currentID, new Shape(Constant.getShape(seqName + "_Shape")) );
			
			// Floating text show us our selection
			Integer id = Component.getID();
			Component.placement.put( id, new Placement( new Vec2(firstClick)) );
			Component.drawer.put( id, new Drawer(id) );
			Shape shp = new Shape(Constant.getShape("FloatingText_Shape"));
			shp.getText().setText(seqName);
			Component.shape.put( id, shp );
			Component.timedObject.put( id, new Timed(id, 1, Clock.real) );
		}
	}
	
	public final void Move() {}
	
}
