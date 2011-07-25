package geometry;



import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;

import java.awt.Font;

import managers.Constant;
import managers.Screen;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import geometry.Vec2;
import geometry.Vec3;

@SuppressWarnings("deprecation")
public class Text extends Polygon {
	private String														text;
	private final Vec2													size;
	private TrueTypeFont												font;

	public Text( final String str, final Vec3 off) {
		super(off);
		text = Constant.getString("Text_" + str);
		size = new Vec2();
		sqradius = 0;
		Font awtFont = new Font("Times New Roman", Font.BOLD, 48);
		font = new TrueTypeFont(awtFont, true);
		/*
		// load font from file
		try {
			
			FileInputStream fis;
			fis = new FileInputStream(new File("myfont.ttf"));
			
			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, fis);
			awtFont2 = awtFont2.deriveFont(24f); // set font size
			font2 = new TrueTypeFont(awtFont2, antiAlias);
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	public Text(final Text t) {
		super((Polygon) t);
		text = t.text;
		size = t.size;
	}

	public int whoAmI() {
		return 3;
	}
	
	public void multSize( float f ) {}
	
	public void draw( final Vec2 pos, final Vec3 defColor ) {
		Vec2 myPos = Screen.reposition( new Vec2(pos.x + offset.x, pos.y + offset.y) );

		Color col;
	    // Draw text
		if( color == null ) {
			col = new Color(defColor.x, defColor.y, defColor.z, 1.f);
		}
		else {
			col = new Color(color.x * defColor.x, color.y * defColor.y, color.z * defColor.z, 1);
		}
		glEnable(GL_TEXTURE_2D);

		GL11.glTranslatef(0.f, 0.f, offset.z);
		font.drawString(myPos.x - font.getWidth(text)/2, myPos.y - font.getHeight()/2, text, col);
		glLoadIdentity();
	}
	
	public boolean Collides(final Vec2 myPos, final Polygon p, final Vec2 hisPos) {
		return false;
	}
}
