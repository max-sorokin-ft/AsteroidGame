package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class represents a projectile/bullet fired by the player's ship.
 * Bullets travel in a straight line and disappear after a set time not wrapping screen like other elements.
 */
public class Bullet implements Updatable, Drawable {
    
    private Point position;
    private double angle;
    private double velocity;
    private int lifespan;
    private static final int MAX_LIFESPAN = 60;
    
    /**
     * Constructs a new Bullet at the specified position.
     * 
     * @param startPosition the initial spawn position
     * @param angle the direction of travel in degrees
     * @param velocity the speed in pixels per frame
     */
    public Bullet(Point startPosition, double angle, double velocity) {
        this.position = startPosition.clone();
        this.angle = angle;
        this.velocity = velocity;
        this.lifespan = 0;
    }
    
    /**
     * Updates the bullet's position each frame.
     * Moves the bullet and increments its lifespan.
     */
    @Override
    public void update() {
        position = Utilities.updatePosition(position, angle, velocity);
        lifespan++;
    }
    
    /**
     * Draws the bullet to be a white circle.
     * 
     * @param brush the Graphics object used for drawing
     */
    @Override
    public void draw(Graphics brush) {
        brush.setColor(Color.white);
        brush.fillOval((int)position.getX() - 2, (int)position.getY() - 2, 4, 4);
    }
    
    /**
     * Checks if the bullet should be removed from the game.
     * 
     * @return true if the bullet has exceeded its lifespan
     */
    public boolean isDead() {
        return lifespan > MAX_LIFESPAN;
    }
    
    /**
     * Returns the current position for collision detection.
     * 
     * @return the current Point position
     */
    public Point getPosition() {
        return position;
    }
}
