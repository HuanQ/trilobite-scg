package geometry;

import static org.lwjgl.opengl.GL11.*;

import managers.Component;
import managers.Constant;

import org.newdawn.slick.Color;

import geometry.Vec2;
import geometry.Vec3;

public class Text extends Polygon {
	public static final int												bigFont = 0;
	public static final int												mediumFont = 1;
	public static final int												smallFont = 2;
	private String														text;
	private int															size;
	
	public Text( final String str, final Vec3 off, int s) {
		super(off);
		text = Constant.getString("Text_" + str);
		size = s;
	}
	
	public Text(final Text t) {
		super(t);
		text = t.text;
		size = t.size;
	}

	public int whoAmI() {
		return Polygon.text;
	}
	
	public void multSize( float f ) {}
	
	@SuppressWarnings("deprecation")
	public void draw( final Vec2 pos, final Vec3 defColor, int side ) {
		//TODO: Resize segons side, no fer-ho per pixels si es pot
	    // Draw text
		Vec3 finalColor = new Vec3(defColor);
		finalColor.mult(color);
		finalColor.mult(Component.fader.getColor());
		Color col = new Color(finalColor.x, finalColor.y, finalColor.z);

		glTranslatef(0.f, 0.f, offset.z);
		glEnable(GL_TEXTURE_2D);
		
		Constant.font[size].drawString(pos.x - Constant.font[size].getWidth(text)/2, pos.y - Constant.font[size].getHeight()/2, text, col);
		glLoadIdentity();
	}
	
	public void setText( final String str ) {
		text = str;
	}
	
	public boolean Collides(final Vec2 myPos, final Polygon p, final Vec2 hisPos) {
		return false;
	}
	
}
