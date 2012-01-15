package tools;

import components.Killable;
import components.Shape;

import geometry.Polygon;
import geometry.Rectangle;
import geometry.Vec2;
import graphics.Sprite;
import managers.Component;

public class ToolCreateSquare extends ToolCreate {
	private Polygon											myPoly = null;
	
	public ToolCreateSquare( int m ) {
		super(m);
	}
	
	public final void LeftButton() {
		if( FirstClick() ) {
			Component.killable.put( currentID, new Killable(currentID, Killable.terrain) );
			
			Shape shp = new Shape();
			myPoly = new Rectangle(new Vec2(0.000001f, 0.000001f), new Vec2(), 1, true);
			myPoly.setTexture("base/rect1x1.gif");
			shp.add( myPoly );
			Component.shape.put( currentID, shp );
		}
		else {
			SecondClick();
			// Leave the object as it is
			currentID = Integer.MIN_VALUE;
			firstClick = null;
			myPoly = null;
		}
	}
	
	public final void RightButton() {
		super.RightButton();
		myPoly = null;
	}
	
	public final void WheelButton() {}
	
	public final void WheelUp() {
	}
	
	public final void WheelDown() {}
	
	public final void Move() {
		if(firstClick != null) {
			Rectangle r = (Rectangle) myPoly;
			r.size.x = (float) Math.abs(firstClick.x - Component.placement.get(pointerID).position.x)*2;
			r.size.y = (float) Math.abs(firstClick.y - Component.placement.get(pointerID).position.y)*2;
			
			myPoly.setTexture( Sprite.getRect(r.size) );
		}
	}
	
}
