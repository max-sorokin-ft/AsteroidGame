package game;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet implements Updatable, Drawable {
    
    private Point position;
    private double angle;
    private double velocity;
    private int lifespan;
    private static final int MAX_LIFESPAN = 60;
    
    public Bullet(Point startPosition, double angle, double velocity) {
        this.position = startPosition.clone();
        this.angle = angle;
        this.velocity = velocity;
        this.lifespan = 0;
    }
    
    @Override
    public void update() {
        position = Utilities.updatePosition(position, angle, velocity);
        // Bullets disappear off screen, rather than wrapping like ship.
        lifespan++;
    }
    
    @Override
    public void draw(Graphics brush) {
        brush.setColor(Color.white);
        brush.fillOval((int)position.getX() - 2, (int)position.getY() - 2, 4, 4);
    }
    
    public boolean isDead() {
        return lifespan > MAX_LIFESPAN;
    }
    
    public Point getPosition() {
        return position;
    }
}
