package tools;

import components.Clickable;

import geometry.Vec2;
import managers.Component;

public class ToolEditMove extends ToolEdit {
	
	public ToolEditMove( int m ) {
		super(m);
	}
	
	public final void LeftButton() {
		if(firstClick == null) {
			for(Integer i : Component.shape.keySet()) {
				if( pointerID != i && Component.shape.get(i).Collides(i, pointerID) ) {
					firstClick = new Vec2(Component.placement.get(pointerID).position);
					currentID = i;
					break;
				}
			}
		}
		else if(secondClick == null) {
			// Leave the object
			currentID = Integer.MIN_VALUE;
			firstClick = null;
			secondClick = null;
			Clickable.setSave(Clickable.on);
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
