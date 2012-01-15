package tools;

import components.Clickable;

import geometry.Vec2;
import managers.Component;

public class ToolEditCopy extends ToolEdit {
	
	public ToolEditCopy( int m ) {
		super(m);
	}
	
	public final void LeftButton() {
		//TODO NOW Els objectes molt grans nomes colisionen amb el mouse al bell mig de la figura
		if(firstClick == null) {
			for(Integer i : Component.shape.keySet()) {
				if( pointerID != i && Component.shape.get(i).Collides(i, pointerID) ) {
					firstClick = new Vec2(Component.placement.get(pointerID).position);
					currentID = i;
					break;
				}
			}
			currentID = Component.CopyObject(currentID);
		}
		else if(secondClick == null) {
			// Leave the object
			currentID = Integer.MIN_VALUE;
			firstClick = null;
			secondClick = null;
			//TODO NOW Arreglar redundancies setSave i setPlay
			Clickable.setSave(Clickable.on);
			Clickable.setPlay(Clickable.off);
		}
	}
	
	public final void WheelButton() {}
	
	public final void WheelUp() {}
	
	public final void WheelDown() {}
	
	public final void Move() {
		if(firstClick != null) {
			Component.placement.get(currentID).position.set( Component.placement.get(pointerID).position );
		}
	}
	
}
