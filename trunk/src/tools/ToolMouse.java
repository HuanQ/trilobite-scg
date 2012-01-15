package tools;

import managers.Component;

public class ToolMouse extends Tool {
	
	public ToolMouse( int m ) {
		super(m);
	}
	
	public final void LeftButton() {
		for(Integer him : Component.clickable.keySet()) {
			if( Component.shape.get(him).Collides(him, pointerID) ) {
				Component.clickable.get(him).Click();
			}
		}
	}
	
	public final void RightButton() {}
	
	public final void WheelButton() {}
	
	public final void WheelUp() {}
	
	public final void WheelDown() {}
	
	public final void Move() {}
	
}
