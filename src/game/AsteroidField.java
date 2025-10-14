package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class manages all asteroids in the field in the game including spawning, updating,etc.
 *  Asteroids spawn from screen edges, not middle, and split
 * into smaller asteroids when destroyed(only once).
 */
public class AsteroidField implements Updatable, Drawable{

    private static double spawningMultiplier;
    private ArrayList<Asteroid> asteroids;
    private Random random;
    private Asteroids gameReference;
    private ArrayList<Asteroid> delete;
    
    private Point[][] shapes = {{
        new Point(9, -8),
        new Point(6, -2),
        new Point(10, 1),
        new Point(3, 4),
        new Point(7, 9),
        new Point(0, 6),
        new Point(-4, 2),
        new Point(-9, 5),
        new Point(-6, -3),
        new Point(-2, -10)
    },
    {
        new Point(10, -9),
        new Point(7, -3),
        new Point(9, 2),
        new Point(4, 6),
        new Point(6, 10),
        new Point(-1, 7),
        new Point(-5, 3),
        new Point(-10, 0),
        new Point(-7, -6),
        new Point(-3, -10)
    },
    {
        new Point(8, -10),
        new Point(10, -2),
        new Point(5, 1),
        new Point(9, 7),
        new Point(2, 10),
        new Point(-4, 8),
        new Point(-9, 4),
        new Point(-6, -1),
        new Point(-10, -5),
        new Point(-2, -9)
    }};

    /**
     * Constructs a new AsteroidField.
     * 
     * @param game reference to the main Asteroids game instance
     */
    public AsteroidField(Asteroids game) {
        this.gameReference = game;
        this.asteroids = new ArrayList<Asteroid>();
        this.random = new Random();
        this.delete = new ArrayList<Asteroid>();
        this.spawningMultiplier = 1.1;
    }
    
    /**
     * Creates smaller asteroids when a larger asteroid is destroyed.
     * Asteroids split into two smaller pieces that move faster than bigger ones before.
     * Stops splitting when asteroids become too small.
     * 
     * @param p the position where the asteroid was destroyed
     * @param scale the scale of the destroyed asteroid
     */
    public void splitAsteroid(Point p, double scale) {
        double newScale = scale * 0.65; 
        if (newScale > 2.5) {
            for (int i = 0; i < 2; i++) {
                Asteroid a = new Asteroid(
                    shapes[random.nextInt(3)], 
                    p.clone(), 
                    random.nextDouble() * 360, 
                    2.1, 
                    newScale
                );
                asteroids.add(a);
                a.position = Utilities.updatePosition(a.position, a.rotation + (i * 180), 15);
            }
        }
    }
    
    /**
     * Tis inner class represents a single asteroid.
     * Asteroids go around screen and a across edges.
     */
    public class Asteroid extends Polygon implements Updatable, Drawable{

        private double velocity;
        private double scale;
        
        /**
         * Constructs a new Asteroid with specified properties.
         * 
         * @param shape array of points defining the asteroid's shape
         * @param p initial position
         * @param angle initial rotation angle
         * @param velocity movement speed
         * @param scale size multiplier
         */
        private Asteroid(Point[] shape, Point p, double angle, double velocity, double scale) {
            super(Utilities.rotatePoints(Utilities.scalePoints(shape, scale), angle), p, angle);
            this.scale = scale;
            this.velocity = velocity;
        }

        /**
         * Updates the asteroid's position each frame.
         */
        @Override
        public void update() {
            position = Utilities.updatePosition(position, rotation, velocity);
            Utilities.wrapAround(position);
        }

        /**
         * Draws the asteroid as a white polygon.
         * 
         * @param brush the Graphics object used for drawing
         */
        @Override
        public void draw(Graphics brush) {
            this.drawPolygon(brush);
        }
    }

    /**
     * Updates all asteroids and spawns new ones randomly.
     */
    @Override
    public void update() {
        if(Math.random()*100 < spawningMultiplier) {
            Point spawnPoint = getEdgeSpawnPoint();
            Asteroid a = new Asteroid(
                    shapes[random.nextInt(3)], 
                    spawnPoint,
                    (double)(random.nextInt(360)), 
                    1.0, 
                    (Math.random()*2)+2.5);
            this.asteroids.add(a);
        }
        
        for(Asteroid a: asteroids) {
            a.update();
        }
    }
    
    /**
     * Generates a random spawn point along screen edges.
     * 
     * @return a Point just outside one of the four screen edges
     */
    private Point getEdgeSpawnPoint() {
        int edge = random.nextInt(4); 
        
        if (edge == 0) {
            return new Point(random.nextInt(Asteroids.width), -20);
        } else if (edge == 1) {
            return new Point(Asteroids.width + 20, random.nextInt(Asteroids.height));
        } else if (edge == 2) {
            return new Point(random.nextInt(Asteroids.width), Asteroids.height + 20);
        } else {
            return new Point(-20, random.nextInt(Asteroids.height));
        }
    }

    /**
     * Draws all asteroids to the screen.
     * 
     * @param brush the Graphics object used for drawing
     */
    @Override
    public void draw(Graphics brush) {
        brush.setColor(Color.white);
        for(Asteroid a: asteroids) {
            a.draw(brush);
        }
    }
    
    /**
     * Checks for collisions between bullets and asteroids.
     * Destroys asteroids when hit and splits them.
     * 
     * @param ship the player's ship containing bullets to check
     */
    public void checkBulletCollisions(Ship ship) {
        for (Bullet bullet : ship.getBullets()) {
            for (Asteroid asteroid : asteroids) {
                if (asteroid.contains(bullet.getPosition())) {
                    delete.add(asteroid);
                    for (int i = 0; i < 60; i++) {
                        bullet.update();
                    }
                    gameReference.newExplosion(asteroid.position);
                    break;
                }
            }
        }
        cleanup();
    }
    
    /**
     * Checks for collisions between the ship and asteroids.
     * 
     * @param ship the players ship
     * @return true if the ship collides with any asteroid
     */
    public boolean checkShipCollision(Ship ship) {
        for (Asteroid asteroid : asteroids) {
            Point[] shipPoints = ship.getPoints();
            for (Point p : shipPoints) {
                if (asteroid.contains(p)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Removes destroyed asteroids and creates split asteroids.
     */
    private void cleanup() {
        for(Asteroid a: delete) {
            double scale = a.scale;
            Point position = a.position.clone();
            asteroids.remove(a);
            splitAsteroid(position, scale);
        }
        delete.clear();
    }
    
    /**
     * Returns the number of asteroids currently on screen.
     * 
     * @return the asteroid count
     */
    public int getAsteroidCount() {
        return asteroids.size();
    }
}
