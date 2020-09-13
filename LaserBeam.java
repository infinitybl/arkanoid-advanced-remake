/*  LaserBeam.java
 	Phillip Pham
	Class used to generate laser bullet beams when player's Ship is in laser mode.
*/

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class LaserBeam {

	private Image laserBeamImage = new ImageIcon("Sprites/Spacecraft/Laser Beam.png").getImage(); //LaserBeam main image

	public double x; //x pos of LaserBeam (top left corner)
	public double y; //y pos of LaserBeam (top left corner)
	public double speed; //speed of LaserBeam
	public boolean isRemoved; //determines if LaserBeam is removed
	
	//Sounds
	private SoundEffect laserBeamHitSoundEffect = new SoundEffect("Sounds/Laser Beam Hit.wav");
	
	//CONSTRUCTOR	
	public LaserBeam(double x, double y, double speed) {
		this.x = x; 
		this.y = y;
		this.speed = speed;
		isRemoved = false;
	}
	
	//move() moves the LaserBeam up the screen and removes it if 
	//it passes the y start boundary
	public void move(double yStartBoundary) {
		y -= speed;
		if (y <= yStartBoundary) {
			isRemoved = true;
		}
	}
	
	//returns the LaserBeam's image
	public Image getImage() {
		return laserBeamImage;
	}

	//checkCollisionWithWall() checks if the LaserBeam collided with a Wall object,
	//decreases the Wall's health, and removes them both if needed
	public void checkCollisionWithWall(Wall brick) {
		if (getRect().overlaps(brick.getRect())) {
			laserBeamHitSoundEffect.play();
			isRemoved = true;
			brick.health -= 1;
			if (brick.health <= 0) {
				brick.isRemoved = true;
			}
		}
	}
	
	//gets Rect of the LaserBeam
	public Rect getRect() {
		return new Rect(x, y, getImage().getWidth(null), getImage().getHeight(null));
	}


}
