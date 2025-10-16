package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * This class represents an explosion effect that occurs when asteroids are destroyed.
 * Explosions consist of multiple particles that radiate outward from a center point
 * and disappear after a certain time.
 */
public class Explosion implements Updatable, Drawable{

    private Point position;
    private static final int particleLife = 30;
    private static final int numParticles = 9;
    private static final double particleVelocity = 1;
    private int deathFrame;
    
    private ArrayList<ExplosionParticle> particles;
    
    /**
     * Constructor for a new Explosion at given coordinates.
     * Creates multiple particles that go outward in all directions.
     * 
     * @param x the x-coordinate of the explosion center
     * @param y the y-coordinate of the explosion center
     */
    public Explosion(int x, int y) {
        this.deathFrame = Asteroids.frame + particleLife;
        this.position = new Point(x, y);
        this.particles = new ArrayList<ExplosionParticle>();
        for(int i = 1; i <= numParticles; i++) {
            ExplosionParticle particle = new ExplosionParticle(this.position, i*360/numParticles);
            particles.add(particle);
        }
    }
    
    /**
     * Returns the frame when this explosion should be removed.
     * 
     * @return the frame of expiration
     */
    public int getDeathFrame() {
        return this.deathFrame;
    }
    
    /**
     * Inner class representing a particle in an explosion.
     * Particles move outward.
     */
    private class ExplosionParticle implements Updatable, Drawable{
        
        private Point position;
        private double angle;
        
        /**
         * Constructs a new ExplosionParticle.
         * 
         * @param position the starting position
         * @param angle the direction of travel in degrees
         */
        private ExplosionParticle(Point position, double angle) {
            this.position = position;
            this.angle = angle;
        }
        
        /**
         * Updates the particle's position during each frame.
         */
        public void update() {
            this.position = Utilities.updatePosition(position, angle, particleVelocity);
        }

        /**
         * Draws the particle as a white square.
         * 
         * @param brush the Graphics object used for drawing
         */
        @Override
        public void draw(Graphics brush) {
            brush.setColor(Color.white);
            brush.fillRect((int)position.getX(), (int)position.getY() , 2, 2);
        }
        
    }

    /**
     * Updates all particles in the explosion, and marks for deletion.
     */
    @Override
    public void update() {
        boolean delete = false;
        if(Asteroids.frame >= deathFrame) {delete = true;}
        for(ExplosionParticle p: particles) {    
            if(delete) {
                p = null;
                System.out.println("Delete");
            } else {
                p.update();
            }
        }
    }

    /**
     * Draws all particles in the explosion.
     * 
     * @param brush the Graphics object used for drawing
     */
    @Override
    public void draw(Graphics brush) {
        for(ExplosionParticle p: particles) {    
            p.draw(brush);
        }
    }
    
}
