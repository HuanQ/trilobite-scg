package tools;

import managers.Component;

import components.Clickable;
import components.Drawer;
import components.Placement;
import components.Xml;

import geometry.Vec2;

public abstract class ToolCreate extends Tool {
	protected Vec2											firstClick = null;
	
	public ToolCreate( int m ) {
		super(m);
	}
		
	public final boolean FirstClick() {
		if(firstClick == null) {
			firstClick = new Vec2(Component.placement.get(pointerID).position);
			
			currentID = Component.getID();
			Component.placement.put( currentID, new Placement(firstClick) );
			Component.drawer.put( currentID, new Drawer(currentID) );
			Component.xml.put(currentID, new Xml(currentID));
			return true;
		}
		return false;
	}
	
	public final void SecondClick() {
		Clickable.setSave(Clickable.on);
		Clickable.setPlay(Clickable.off);
	}
	
	public abstract void LeftButton();
	
	public void RightButton() {
		super.RightButton();
		firstClick = null;
	}
	
	public abstract void WheelButton();
	
	public abstract void WheelUp();
	
	public abstract void WheelDown();
	
	public abstract void Move();
	
}
