package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Asteroids extends Game {
	public static int frame = 0;
	private ArrayList<Explosion> explosions;

  public Asteroids() {
    super("Asteroids",800,600);
    this.setFocusable(true);
	this.requestFocus();
	explosions = new ArrayList<Explosion>();
  }
  
  public static void main (String[] args) {
 		Asteroids a = new Asteroids();
		a.repaint();
  }
  
  public void paint(Graphics brush) {
    	brush.setColor(Color.black);
    	brush.fillRect(0,0,width,height);
    	frame++;
    	

    	explosionUpdates(brush);
    	
    	brush.setColor(Color.white);
    	brush.drawString("Frame: " + frame,10,10);
  }
  
	
	
	
	
	private void explosionUpdates(Graphics brush) {
		for(Explosion e: explosions) {
			if(e.getDeathFrame() < frame) {
				e = null;
			} else {
			e.update();
			e.draw(brush);
			}
		}
	}
	
	public void addExplosion(Explosion e) {
		explosions.add(e);
	}
}
