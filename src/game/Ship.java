package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * This class represents a player-controlled spaceship in our Asteroids game.
 * The ship can move forward, rotate left/right, and shoot bullet.
 * Uses momentum-based physics with friction for realistic and easy-to-use movement.
 */
public class Ship extends Polygon implements Updatable, Drawable, KeyListener {
    
    private double velocityX;
    private double velocityY;
    private double acceleration;
    private double friction;
    private double maxSpeed;
    private double rotationSpeed;
    
    private boolean movingForward;
    private boolean rotatingLeft;
    private boolean rotatingRight;
    private boolean shooting;
    
    private int shootCooldown;
    private static final int SHOOT_DELAY = 15;
    
    private ArrayList<Bullet> bullets;
    
    private static Point[] shipShape = {
        new Point(0, -10),
        new Point(-6, 10),
        new Point(0, 6),
        new Point(6, 10)
    };
    
    /**
     * This constructs a new ship at the specified position.
     * The ship initially points upward.
     * 
     * @param startPosition the initial position of the ship
     */
    public Ship(Point startPosition) {
        super(shipShape, startPosition, 0);
        velocityX = 0;
        velocityY = 0;
        acceleration = 0.15;
        friction = 0.98;
        maxSpeed = 5;
        rotationSpeed = 5;
        movingForward = false;
        rotatingLeft = false;
        rotatingRight = false;
        shooting = false;
        shootCooldown = 0;
        bullets = new ArrayList<Bullet>();
    }
    
    /**
     * Updates the ship's position, rotation, and bullets each frame.
     * Handles rotation, thrust, friction, speed limiting, screen wrapping, etc.
     */
    @Override
    public void update() {
        if (rotatingLeft) {
            rotation -= rotationSpeed;
        }
        if (rotatingRight) {
            rotation += rotationSpeed;
        }
        if (movingForward) {
            double angle = rotation - 90;
            velocityX += acceleration * Math.cos(Math.toRadians(angle));
            velocityY += acceleration * Math.sin(Math.toRadians(angle));
        }
        velocityX *= friction;
        velocityY *= friction;
        
        double speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        if (speed > maxSpeed) {
            velocityX = (velocityX / speed) * maxSpeed;
            velocityY = (velocityY / speed) * maxSpeed;
        }
        
        position.setX(position.getX() + velocityX);
        position.setY(position.getY() + velocityY);
        
        Utilities.wrapAround(position);
        
        if (shooting && shootCooldown == 0) {
            shoot();
            shootCooldown = SHOOT_DELAY;
        }
        
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        
        // REQUIREMENT: Lambda expression - Using forEach with lambda to update all bullets
        bullets.forEach(bullet -> bullet.update());
        // REQUIREMENT: Lambda expression - Using removeIf with lambda to remove dead bullets
        bullets.removeIf(bullet -> bullet.isDead());
    }
    
    /**
     * Draws the ship and its bullets to the screen.
     * @param brush the Graphics object used for drawing
     */
    @Override
    public void draw(Graphics brush) {
        brush.setColor(Color.white);
        
        Point[] rotatedPoints = getPoints();
        for(int i = 0; i < rotatedPoints.length-1; i++) {
            brush.drawLine(
                (int)(rotatedPoints[i].getX()), 
                (int)(rotatedPoints[i].getY()),
                (int)(rotatedPoints[i+1].getX()),
                (int)(rotatedPoints[i+1].getY())); 
        }
        brush.drawLine(
            (int)(rotatedPoints[rotatedPoints.length-1].getX()), 
            (int)(rotatedPoints[rotatedPoints.length-1].getY()),
            (int)(rotatedPoints[0].getX()),
            (int)(rotatedPoints[0].getY()));
        
        for (Bullet bullet : bullets) {
            bullet.draw(brush);
        }
    }

    /**
     * Creates and fires a bullet from the top/tip of the ship, and bullet 
     * goes in that same direction
     */
    private void shoot() {
        double bulletSpeed = 8;
        double angle = rotation - 90;
        
        Point bulletStart = new Point(
            position.getX() + 12 * Math.cos(Math.toRadians(angle)),
            position.getY() + 12 * Math.sin(Math.toRadians(angle))
        );
        
        Bullet bullet = new Bullet(bulletStart, angle, bulletSpeed);
        bullets.add(bullet);
    }
    
    /**
     * Handles key press events for ship controls.
     * @param e the KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            movingForward = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            rotatingLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rotatingRight = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shooting = true;
        }
    }
    
    /**
     * Handles key release events for ship controls.
     * @param e the KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            movingForward = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            rotatingLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rotatingRight = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shooting = false;
        }
    }
    
    /**
     * Required by KeyListener interface - left empty as per assignment instructions.
     * @param e the KeyEvent
     */
    @Override
    public void keyTyped(KeyEvent e) {}
    
    /**
     * Returns the list of bullets fired by this ship(can fire whilst others are active).
     * 
     * @return ArrayList of Bullet objects
     */
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
    
    /**
     * Resets the ship to its initial state at center screen.
     */
    public void reset() {
        position = new Point(Asteroids.width / 2, Asteroids.height / 2);
        velocityX = 0;
        velocityY = 0;
        rotation = 0; 
        bullets.clear();
    }
}
