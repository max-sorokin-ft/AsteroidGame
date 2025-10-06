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
	
}