package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class AsteroidField implements Updatable, Drawable{

	private static double spawningMultiplier = 5;
	private ArrayList<Asteroid> asteroids;
	Random random = new Random();
	private Asteroids gameReference;
	
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
	
	public void splitAsteroid(Point p) {
		Asteroid a = new Asteroid(shapes[random.nextInt(3)], p, random.nextDouble(360), 2, 1);
		asteroids.add(a);
		a.position = Utilities.updatePosition(a.position, a.rotation, 15);
		
		
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
			System.out.println(random.nextInt(3));
			Asteroid a = new Asteroid(
					shapes[random.nextInt(3)], 
					new Point(random.nextInt(500), random.nextInt(500)),
					(double)(random.nextInt(360)), 
					1.0, (Math.random()*3)+2);
			this.asteroids.add(a);
			System.out.println("asteroid");
		}
		
		for(Asteroid a: asteroids) {
			a.update();
		}
		collidingAsteroids();
		
	}

	@Override
	public void draw(Graphics brush) {
		brush.setColor(Color.white);
		for(Asteroid a: asteroids) {
			a.draw(brush);
		}
		
	}
	
	public void collidingAsteroids() {
		ArrayList<Polygon> delete = new ArrayList<Polygon>();
		for(Polygon p: asteroids) {
			for(Polygon a: asteroids) {
				if(a==p){continue;}
				if(Utilities.isColliding(a, p)){
					gameReference.newExplosion(Utilities.averagePosition(a, p));
					delete.add(a);
					delete.add(p);
				}
				
			}
		
		}
		for(Polygon p: delete) {
			asteroids.remove(p);
			if(((Asteroid)p).scale > 1.5) {
			splitAsteroid(p.position);
			}
		}
		
	}
	
}
