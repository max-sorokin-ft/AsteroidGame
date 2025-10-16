package game;

/**
 * This Utilities class provides helper methods for game physics and geometry.
 * It contains static methods for utilities such as position updates, point transformations,
 * collision detection, and screen wrapping.
 */
public class Utilities {

    /**
     * Updates the position by adding velocity components.
     * 
     * @param position the current position
     * @param velX the horizontal velocity
     * @param velY the vertical velocity
     * @return a new point with the updated position
     */
    public static Point updatePosition(Point position, int velX, int velY) {
        Point newPos = new Point(0,0);
        newPos.setX(position.getX()+velX);
        newPos.setY(position.getY()+velY);
        return newPos;
    }
    
    /**
     * Updates the position based on angle and velocity.
     * Uses math/trig to calculate movement in a specific direction.
     * 
     * @param position the current position
     * @param angle the direction of movement in degrees
     * @param velocity the speed of movement
     * @return a new point with the updated position
     */
    public static Point updatePosition(Point position, double angle, double velocity) {
        Point newPos = new Point(0,0);
        newPos.setX(position.getX()+velocity*Math.cos(Math.toRadians(angle)));
        newPos.setY(position.getY()+velocity*Math.sin(Math.toRadians(angle)));
        return newPos;
    }
    
    /**
     * Scales an array of points by a multiplier.
     * 
     * @param points the array of points to scale
     * @param scale the scaling factor
     * @return a new array of scaled points
     */
    public static Point[] scalePoints(Point[] points, double scale) {
        Point[] scaled = new Point[points.length-1];
        for(int i = 0; i<points.length-1; i++) {
            scaled[i] = new Point((int)(points[i].getX()*scale), (int)(points[i].getY()*scale));
        }
        return scaled;
    }
    
    /**
     * Rotates an array of points around the origin.
     * 
     * @param points the array of points to rotate
     * @param angle the rotation angle in radians
     * @return a new array of rotated points
     */
    public static Point[] rotatePoints(Point[] points, double angle) {
        Point[] rotated = new Point[points.length];
        for(int i = 0; i<points.length; i++) {
            rotated[i] = new Point(
                    points[i].getX()*Math.cos(angle)-points[i].getY()*Math.sin(angle),
                    points[i].getX()*Math.sin(angle)+points[i].getY()*Math.cos(angle));
        }
        return rotated;
    }
    
    /**
     * Wraps a point around the screen edges.
     * When an object(spaceship, asteroid) moves off one edge, it re-appears on the opposite edge.
     * 
     * @param p the point to wrap
     */
    public static void wrapAround(Point p) {
        
        if(p.getX() < -10) {
            p.setX(Asteroids.width + 10);
        }
        
        if(p.getX() > Asteroids.width + 10) {
            p.setX(-10);
        }
        
        if(p.getY() < -10) {
            p.setY(Asteroids.height + 10);
        }
        
        if(p.getY() > Asteroids.height + 10) {
            p.setY(-10);
        }
    }
    
    /**
     * Checks if two polygons are colliding.
     * 
     * @param a the first polygon
     * @param b the second polygon
     * @return true if the polygons are colliding
     */
    public static boolean isColliding(Polygon a, Polygon b) {
        for(Point p: b.getPoints()) {
            if(a.contains(p)) {return true;}
        }
        return false;
    }
    
    /**
     * Calculates the average position between two polygons.
     * 
     * @param a the first polygon
     * @param b the second polygon
     * @return a Point at the average position
     */
    public static Point averagePosition(Polygon a, Polygon b) {
        int avgX = (int)(a.position.getX()+b.position.getX())/2;
        int avgY = (int)(a.position.getY()+b.position.getY())/2;
        return new Point(avgX, avgY);
    }
    
}
