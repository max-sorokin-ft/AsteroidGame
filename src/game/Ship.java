package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Ship extends Polygon implements Updatable, Drawable {
    
    private double velocityX = 0;
    private double velocityY = 0;
    private double acceleration = 0.15;
    private double friction = 0.98;
    private double maxSpeed = 5;
    private double rotationSpeed = 5;
    
    private boolean thrustingForward = false;
    private boolean rotatingLeft = false;
    private boolean rotatingRight = false;
    private boolean shooting = false;
    
    private int shootCooldown = 0;
    private static final int SHOOT_DELAY = 15;
    
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    
    private static Point[] shipShape = {
        new Point(0, -10),
        new Point(-6, 10),
        new Point(0, 6),
        new Point(6, 10)
    };
    
    public Ship(Point startPosition) {
        super(shipShape, startPosition, 0); // Pointing up initially
    }
    
    @Override
    public void update() {
        if (rotatingLeft) {
            rotation -= rotationSpeed;
        }
        if (rotatingRight) {
            rotation += rotationSpeed;
        }
        if (thrustingForward) {
            double angle = rotation - 90;
            velocityX += acceleration * Math.cos(Math.toRadians(angle));
            velocityY += acceleration * Math.sin(Math.toRadians(angle));
        }
        velocityX *= friction;
        velocityY *= friction;
        
        // Limit max speed
        double speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        if (speed > maxSpeed) {
            velocityX = (velocityX / speed) * maxSpeed;
            velocityY = (velocityY / speed) * maxSpeed;
        }
        
        // Update position
        position.setX(position.getX() + velocityX);
        position.setY(position.getY() + velocityY);
        
        // Wrap the ship around screen
        Utilities.wrapAround(position);
        
        // This creates more realistic shooting
        if (shooting && shootCooldown == 0) {
            shoot();
            shootCooldown = SHOOT_DELAY;
        }
        
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        
        // Update bullets
        bullets.forEach(bullet -> bullet.update());
        
        bullets.removeIf(bullet -> bullet.isDead());
    }
    
    @Override
    public void draw(Graphics brush) {
        brush.setColor(Color.white);
        
        // Draw ship using rotated points
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
        
        if (thrustingForward) {
            brush.setColor(Color.orange);
            double angle = rotation - 90;
            int flameX = (int)(position.getX() - 8 * Math.cos(Math.toRadians(angle)));
            int flameY = (int)(position.getY() - 8 * Math.sin(Math.toRadians(angle)));
            brush.fillOval(flameX - 2, flameY - 2, 4, 4);
        }
        
        for (Bullet bullet : bullets) {
            bullet.draw(brush);
        }
    }

    
    private void shoot() {
        double bulletSpeed = 8;
        double angle = rotation - 90;
        System.out.println(rotation - 90);
        
        // Calculate bullet starting position at the tip/top of the ship.
        Point bulletStart = new Point(
            position.getX() + 0 * Math.cos(Math.toRadians(rotation)),
            position.getY() + 0 * Math.sin(Math.toRadians(rotation))
        );
        
        Bullet bullet = new Bullet(bulletStart, angle, bulletSpeed);
        bullets.add(bullet);
    }
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            thrustingForward = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            rotatingLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rotatingRight = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shooting = true;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            thrustingForward = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            rotatingLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rotatingRight = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shooting = false;
        }
    }
    
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
    
    public void reset() {
        position = new Point(Asteroids.width / 2, Asteroids.height / 2);
        velocityX = 0;
        velocityY = 0;
        rotation = 0; 
        bullets.clear();
    }
}
