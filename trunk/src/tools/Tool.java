package tools;

import managers.Component;

public abstract class Tool {
	protected final int										pointerID;
	protected Integer										currentID = Integer.MIN_VALUE;
	
	public Tool( int id ) {
		pointerID = id;
	}

	public abstract void LeftButton();
	
	public void RightButton() {
		Component.deadObjects.add(currentID);
	}
	
	public abstract void WheelButton();
	
	public abstract void WheelUp();
	
	public abstract void WheelDown();
	
	public abstract void Move();
	
}
