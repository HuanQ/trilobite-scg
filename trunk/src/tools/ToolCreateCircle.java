package tools;

import components.Killable;
import components.Shape;

import geometry.Circle;
import geometry.Polygon;
import geometry.Vec2;
import managers.Component;

public class ToolCreateCircle extends ToolCreate {
	private Polygon											myPoly = null;
	
	public ToolCreateCircle( int m ) {
		super(m);
	}
	
	public final void LeftButton() {
		if( FirstClick() ) {
			Component.killable.put( currentID, new Killable(currentID, Killable.terrain) );
			
			Shape shp = new Shape();
			myPoly = new Circle(0.000001f, new Vec2(), 2);
			myPoly.setTexture("base/circle.gif");
			shp.add( myPoly );
			Component.shape.put( currentID, shp );
		}
		else {
			SecondClick();
			
			// Overwrite the temporary Shape
			Shape shp = new Shape();
			shp.add( myPoly );
			
			Component.shape.remove(currentID);
			Component.shape.put( currentID, shp );
			

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
		//TODO Ciclar entre les textures del format adequat amb la rodeta
	}
	
	public final void WheelDown() {}
	
	public final void Move() {
		if(firstClick != null) {
			Circle c = (Circle) myPoly;
			c.setRadius( firstClick.distance(Component.placement.get(pointerID).position) );
		}
	}
	
}
