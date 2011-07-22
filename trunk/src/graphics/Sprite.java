package graphics;

import java.io.IOException;

import managers.Constant;


import static org.lwjgl.opengl.GL11.*;

/**
 * Implementation of sprite that uses an OpenGL quad and a texture
 * to render a given image to the screen.
 *
 * @author Kevin Glass
 * @author Brian Matzon
 */
public class Sprite {

	/** The texture that stores the image for this sprite */
	private Texture	texture;

	/** The height and width in pixels of this sprite */
	private int			width;
	private int			height;
	
	/**
	 * Create a new sprite from a specified image.
	 *
	 * @param loader the texture loader to use
	 * @param ref A reference to the image on which this sprite should be based
	 */
	public Sprite( String ref ) {
    try {
		texture = Constant.textureLoader.getTexture("\\..\\resources\\textures\\" + ref);
    } catch (IOException ioe) {
    	ioe.printStackTrace();
    	System.exit(-1);
    }
	}

	/**
	 * Set the width of this sprite
	 *
	 */
	public void setWidth( int w ) {
		width = w;
	}

	/**
	 * Set the height of this sprite
	 *
	 */
	public void setHeight( int h ) {
		height = h;
	}

	/**
	 * Draw the sprite at the specified location
	 *
	 * @param x The x location at which to draw this sprite
	 * @param y The y location at which to draw this sprite
	 */
	public void draw(float x, float y, float z) {
		// store the current model matrix
		glPushMatrix();

		// bind to the appropriate texture for this sprite
		texture.bind();

		glColor4f(1, 1, 1, 1);
		
		// translate to the right location and prepare to draw
		glTranslatef(x, y, z);

		// draw a quad textured to match the sprite
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(-width/2, -height/2);

			glTexCoord2f(0, texture.getHeight());
			glVertex2f(-width/2, height/2);

			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex2f(width/2, height/2);

			glTexCoord2f(texture.getWidth(), 0);
			glVertex2f(width/2, -height/2);
		}
		glEnd();

		// restore the model view matrix to prevent contamination
		glPopMatrix();
	}
}