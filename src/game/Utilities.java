package game;

public class Utilities {

	public static Point updatePosition(Point position, int velX, int velY) {
		Point newPos = new Point(0,0);
		newPos.setX(position.getX()+velX);
		newPos.setY(position.getY()+velY);
		return newPos;
	}
	
	public static Point updatePosition(Point position, double angle, double velocity) {
		Point newPos = new Point(0,0);
		newPos.setX(position.getX()+velocity*Math.cos(angle));
		newPos.setY(position.getY()+velocity*Math.sin(angle));
		return newPos;
	}
	
	public static Point[] scalePoints(Point[] points, double scale) {
		Point[] scaled = new Point[points.length-1];
		for(int i = 0; i<points.length-1; i++) {
			scaled[i] = new Point((int)(points[i].getX()*scale), (int)(points[i].getY()*scale));
		}
		return scaled;
		
	}
	
	public static Point[] rotatePoints(Point[] points, double angle) {
		Point[] rotated = new Point[points.length];
		for(int i = 0; i<points.length; i++) {
			rotated[i] = new Point(
					points[i].getX()*Math.cos(angle)-points[i].getY()*Math.sin(angle),
					points[i].getX()*Math.sin(angle)+points[i].getY()*Math.cos(angle));
		}
		return rotated;
		
	}
	
	public static void wrapAround(Point p) {
		
		if(p.getX()<-10) {p.setX(Asteroids.width+10);}
		if(p.getX()>Asteroids.width+10) {p.setX(-10);}
		if(p.getY()<-10) {p.setY(Asteroids.height+10);}
		if(p.getY()>Asteroids.height+10) {p.setY(-10);}
		
	}
	
	public static boolean isColliding(Polygon a, Polygon b) {
		for(Point p: b.getPoints()) {
			if(a.contains(p)) {return true;}
		}
		return false;
	}
	
	public static Point averagePosition(Polygon a, Polygon b) {
		int avgX = (int)(a.position.getX()+b.position.getX())/2;
		int avgY = (int)(a.position.getY()+b.position.getY())/2;
		return new Point(avgX, avgY);
	}
	
}
