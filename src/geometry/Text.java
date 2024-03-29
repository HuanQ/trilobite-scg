package geometry;

import static org.lwjgl.opengl.GL11.*;

import managers.Component;
import managers.Constant;
import managers.Screen;

import org.newdawn.slick.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import geometry.Vec3;

public class Text extends Polygon {
	public static final int												hugeFont = 0;
	public static final int												bigFont = 1;
	public static final int												mediumFont = 2;
	public static final int												smallFont = 3;
	public static final int												littleFont = 4;
	private String														text;
	private int															size;
	
	public Text( final String str, final Vec2 off, float ly, int s ) {
		super(off, ly);
		text = str;
		size = s;
	}
	
	public Text(final Text t) {
		super(t);
		text = t.text;
		size = t.size;
	}

	public final int whoAmI() {
		return Polygon.text;
	}
	
	public final String getText() {
		return text;
	}
	
	public final void Scale( float m ) {
		offset.scale(m);
	}
	
	@SuppressWarnings("deprecation")
	public final void Draw( final Vec2 pos, final Vec3 defColor, final Angle rot ) {
		Vec2 realPos = new Vec2(pos);
		Vec2 offsetSize = new Vec2(offset.x, offset.y);
		realPos.add(offsetSize);
		if( Screen.inScreen(realPos, 0) ) {
			// Final position
			Vec2 screenPos = Screen.coords(pos);
			Vec2 screenOffset = Screen.coords(offsetSize, false);
			// Final color
			Vec3 finalColor = new Vec3(defColor);
			finalColor.mult(color);
			finalColor.mult(Component.fader.color);
			Color col = new Color(finalColor.x, finalColor.y, finalColor.z);
			glLoadIdentity();
			//glTranslatef(screenPos.x - Constant.font[size].getWidth(text)/2, screenPos.y - Constant.font[size].getHeight()/2, layer);
			glTranslatef(screenPos.x, screenPos.y, layer);
			glRotatef( (float) Math.toDegrees(rot.get()), 0, 0, 1 );
			glTranslatef(screenOffset.x, screenOffset.y, 0);
			glTranslatef(-Constant.font[size].getWidth(text)/2, -Constant.font[size].getHeight()/2, 0);

			glEnable(GL_TEXTURE_2D);
			
			Constant.font[size].drawString(0, 0, text, col);
			glLoadIdentity();
		}
	}
	
	public final void setText( final String str ) {
		text = str;
	}
	
	public final boolean Collides( final Vec2 myPos, final Polygon p, final Vec2 hisPos, final Angle myRot, final Angle hisRot ) {
		return false;
	}
	
	public final void WriteXML( final Document doc, final Element root ) {
		Element txt = doc.createElement("Text");
		Vec2 myOff = new Vec2(offset);
		Screen.descale("game", myOff);
		txt.setAttribute( "x", Float.toString(myOff.x) );
		txt.setAttribute( "y", Float.toString(myOff.y) );
		txt.setAttribute( "z", Float.toString(layer) );
		txt.setAttribute( "txt", text );
		txt.setAttribute( "size", Float.toString(size) );
		writeSubShape( doc, txt );
		root.appendChild(txt);
	}
}
