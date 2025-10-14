package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class AsteroidField implements Updatable, Drawable{

	private static double spawningMultiplier = 1.1;
	private ArrayList<Asteroid> asteroids;
	Random random = new Random();
	private Asteroids gameReference;
	private ArrayList<Asteroid> delete = new ArrayList<Asteroid>();
	
	Point[][] shapes = {{
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

	
	public AsteroidField(Asteroids game) {
		this.gameReference = game;
		asteroids = new ArrayList<Asteroid>();
	}
	
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
	
	public class Asteroid extends Polygon implements Updatable, Drawable{

		double velocity;
		double scale;
		
		private Asteroid(Point[] shape, Point p, double angle, double velocity, double scale) {
			super(Utilities.rotatePoints(Utilities.scalePoints(shape, scale), angle), p, angle);
			this.scale = scale;
			this.velocity = velocity;
		}

		@Override
		public void update() {
			position = Utilities.updatePosition(position, rotation, velocity);
			Utilities.wrapAround(position);
		}

		@Override
		public void draw(Graphics brush) {
			this.drawPolygon(brush);
		}
	}

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

	@Override
	public void draw(Graphics brush) {
		brush.setColor(Color.white);
		for(Asteroid a: asteroids) {
			a.draw(brush);
		}
	}
	
	public void checkBulletCollisions(Ship ship) {
		for (Bullet bullet : ship.getBullets()) {
			for (Asteroid asteroid : asteroids) {
				if (asteroid.contains(bullet.getPosition())) {
					delete.add(asteroid);
					bullet.update();
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
	
	private void cleanup() {
		for(Asteroid a: delete) {
			double scale = a.scale;
			Point position = a.position.clone();
			asteroids.remove(a);
			splitAsteroid(position, scale);
		}
		delete.clear();
	}
	
	public int getAsteroidCount() {
		return asteroids.size();
	}
}
