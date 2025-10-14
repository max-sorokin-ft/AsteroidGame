package game;

import java.awt.Graphics;

/**
 * This interface is for game objects that can be drawn to the screen.
 * Implementing classes should define their visual representation
 * using the provided Graphics object.
 */
public interface Drawable {

    /**
     * Draws the object to the screen.
     * 
     * @param brush the Graphics object used for drawing
     */
    void draw(Graphics brush);
    
}
