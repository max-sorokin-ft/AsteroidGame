package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Explosion implements Updatable, Drawable{

	private Point position;
	private static final int particleLife = 30;
	private static final int numParticles = 9;
	private static final double particleVelocity = 1;
	private int deathFrame;
	
	private ArrayList<ExplosionParticle> particles = new ArrayList<ExplosionParticle>();
	
	public Explosion(int x, int y) {
		this.deathFrame = Asteroids.frame+particleLife;
		position = new Point(x, y);
		for(int i = 1; i <= numParticles; i++) {
			ExplosionParticle particle = new ExplosionParticle(this.position, i*360/numParticles);
			particles.add(particle);
		}
		
	}
	
	public int getDeathFrame() {
		return this.deathFrame;
	}
	
	private class ExplosionParticle implements Updatable, Drawable{
		
		private Point position;
		private double angle;
		
		private ExplosionParticle(Point position, double angle) {
			this.position = position;
			this.angle = angle;
		}
		
		public void update() {
			this.position = Utilities.updatePosition(position, angle, particleVelocity);
		}

		@Override
		public void draw(Graphics brush) {
			brush.setColor(Color.white);
			brush.fillRect((int)position.getX(), (int)position.getY() , 2, 2);
		}
		
	}



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



	@Override
	public void draw(Graphics brush) {
		for(ExplosionParticle p: particles) {	
				p.draw(brush);
			}
		}
		
	}
	
