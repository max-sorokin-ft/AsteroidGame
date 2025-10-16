package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Main game class for Asteroids. Manages game state, rendering,
 * collision detection, score, lives, and everything else.
 * Extends Game to inherit the game loop and window management.
 */
class Asteroids extends Game {
    public static int frame;
    public static int width;
    public static int height;
    private ArrayList<Explosion> explosions;
    private AsteroidField asteroidField;
    private Ship ship;
    private int score;
    private boolean gameOver;
    private int lives;

    /**
     * Constructor for the Asteroids game.
     * Initializes the game window, ship, asteroid field, keyboard controls, and other important properties.
     */
    public Asteroids() {
        super("Asteroids", 800, 600);
        width = 800;
        height = 600;
        frame = 0;
        score = 0;
        gameOver = false;
        lives = 3;
        this.setFocusable(true);
        this.requestFocus();
        
        asteroidField = new AsteroidField(this);
        ship = new Ship(new Point(width / 2, height / 2));
        explosions = new ArrayList<Explosion>();
        
        this.addKeyListener(ship);
        
        // Anonymous class that handles game level controls(like restart)
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
                    resetGame();
                }
            }
        });
    }
    
    /**
     * Main method to start the game.
     * 
     * @param args default main java arguments
     */
    public static void main(String[] args) {
        Asteroids a = new Asteroids();
        a.repaint();
    }
    
    /**
     * Main game loop. Updates and draws all objects.
     * Handles collisions, score display, etc.
     * 
     * @param brush the Graphics object for drawing
     */
    public void paint(Graphics brush) {
        brush.setColor(Color.black);
        brush.fillRect(0, 0, width, height);
        frame++;
        
        if (!gameOver) {
            ship.update();
            asteroidField.update();
            asteroidField.checkBulletCollisions(ship);
            
            if (asteroidField.checkShipCollision(ship)) {
                lives--;
                newExplosion(ship.position);
                ship.reset();
                
                if (lives <= 0) {
                    gameOver = true;
                }
            }
            
            ship.draw(brush);
            asteroidField.draw(brush);
        }
        
        explosionUpdates(brush);
        
        brush.setColor(Color.white);
        brush.drawString("Score: " + score, 10, 20);
        brush.drawString("Lives: " + lives, 10, 40);
        brush.drawString("Asteroids: " + asteroidField.getAsteroidCount(), 10, 60);
        
        if (gameOver) {
            brush.drawString("GAME OVER", width / 2 - 150, height / 2);
            brush.drawString("Press R to Restart", width / 2 - 120, height / 2 + 50);
        }
    }
    
    /**
     * Updates and removes expired explosions using lambda functions.
     * 
     * @param brush the Graphics object for drawing
     */
    private void explosionUpdates(Graphics brush) {
        // Lambda expression that uses removeIf to filter expired explosions
        explosions.removeIf(e -> e.getDeathFrame() < frame);
        
        for (Explosion e : explosions) {
            e.update();
            e.draw(brush);
        }
    }
    
    /**
     * Creates a new explosion at the specified position when asteroid gets hit and also,
     * increases the players score by 10 points.
     * 
     * @param p the position where the explosion occurs
     */
    public void newExplosion(Point p) {
        Explosion e = new Explosion((int)p.getX(), (int)p.getY());
        explosions.add(e);
        score += 10;
    }
    
    /**
     * Resets the game.
     * And its other aspects like lives, score, etc.
     */
    private void resetGame() {
        gameOver = false;
        lives = 3;
        score = 0;
        ship.reset();
        asteroidField = new AsteroidField(this);
        explosions.clear();
    }
}
