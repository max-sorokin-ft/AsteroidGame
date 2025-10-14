package game;

/*
CLASS: Asteroids
DESCRIPTION: Extending Game, Asteroids is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
      
*/
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Asteroids extends Game {
	public static int frame = 0;
	public static int width;
	public static int height;
	private ArrayList<Explosion> explosions;
	private AsteroidField asteroidField;
	private Ship ship;
	private int score = 0;
	private boolean gameOver = false;
	private int lives = 3;

	public Asteroids() {
		super("Asteroids", 800, 600);
		width = 800;
		height = 600;
		this.setFocusable(true);
		this.requestFocus();
		
		asteroidField = new AsteroidField(this);
		ship = new Ship(new Point(width / 2, height / 2));
		explosions = new ArrayList<Explosion>();
		
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
					resetGame();
				} else if (!gameOver) {
					ship.keyPressed(e);
				}
			}
			
			public void keyReleased(KeyEvent e) {
				if (!gameOver) {
					ship.keyReleased(e);
				}
			}
		});
	}
	
	public static void main(String[] args) {
		Asteroids a = new Asteroids();
		a.repaint();
	}
	
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
	
	private void explosionUpdates(Graphics brush) {
		// Uses lambda to remove expired explosions
		explosions.removeIf(e -> e.getDeathFrame() < frame);
		
		for (Explosion e : explosions) {
			e.update();
			e.draw(brush);
		}
	}
	
	public void newExplosion(Point p) {
		Explosion e = new Explosion((int)p.getX(), (int)p.getY());
		explosions.add(e);
		score += 10;
	}
	
	private void resetGame() {
		gameOver = false;
		lives = 3;
		score = 0;
		ship.reset();
		asteroidField = new AsteroidField(this);
		explosions.clear();
	}
}
