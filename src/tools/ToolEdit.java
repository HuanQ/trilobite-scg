package tools;

import geometry.Vec2;

public abstract class ToolEdit extends Tool {
	protected Vec2											firstClick = null;
	protected Vec2											secondClick = null;
	
	public ToolEdit( int m ) {
		super(m);
	}
	
	public abstract void LeftButton();
	
	public abstract void WheelButton();
	
	public abstract void WheelUp();
	
	public abstract void WheelDown();
	
	public abstract void Move();
	
}
