/*  Powerup.java
 	Phillip Pham
	Class used for generating Powerup objects in the game that help give
	the player's ship an advantage.
*/

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class Powerup {
	//Images for the all the Powerups
	private Image catchImage = new ImageIcon("Sprites/Powerups/Catch.png").getImage();
	private Image duplicateImage = new ImageIcon("Sprites/Powerups/Duplicate.png").getImage();
	private Image enlargeImage = new ImageIcon("Sprites/Powerups/Enlarge.png").getImage();
	private Image laserImage = new ImageIcon("Sprites/Powerups/Laser.png").getImage();
	private Image playerImage = new ImageIcon("Sprites/Powerups/Player.png").getImage();
	private Image slowImage = new ImageIcon("Sprites/Powerups/Slow.png").getImage();
	private Image breakImage = new ImageIcon("Sprites/Powerups/Break.png").getImage();

	public double x; //x pos of Powerup (top left corner)
	public double y; //y pos of Powerup (top left corner)
	public String powerupType; //type of Powerup ("catch", "duplicate", ...)
	public double width = 31; //width of the Powerup Rect
	public double height = 16; //height of the Powerup Rect
	public double fallingSpeed; //falling speed of the Powerup
	public boolean isRemoved; //determines if Powerup is removed
	
	//Sounds
	private SoundEffect playerSoundEffect = new SoundEffect("Sounds/Player Powerup.wav");
	private SoundEffect enlargeSoundEffect = new SoundEffect("Sounds/Enlarge Powerup.wav");
	private SoundEffect breakSoundEffect = new SoundEffect("Sounds/Break Powerup.wav");
	
	//CONSTRUCTOR
	public Powerup(double x, double y, double fallingSpeed) {
		this.x = x;
		this.y = y;
		this.fallingSpeed = fallingSpeed;
		//randomly generate the Powerup type
		String[] powerupTypes = {"catch", "duplicate", "enlarge", "laser", "player", "slow", "break"};
		Random rand = new Random();
		powerupType = powerupTypes[rand.nextInt(powerupTypes.length)];
		isRemoved = false;
	
	}
	
	//fall() moves the Powerup down the screen and removes it if 
	//it passes the y end boundary
	public void fall(double yEndBoundary) {
		y += fallingSpeed;
		if (y >= yEndBoundary) {
			isRemoved = true;
		}
	}
	
	//checkCollisionWithShip() checks if the Powerup collided with the Ship parameter
	//and sets the appropriate PowerupInEffect boolean field on the Ship to true if it does,
	//as well as remove the Powerup
	public void checkCollisionWithShip(Ship paddle) {
		if (getRect().overlaps(paddle.getRect())) {
				
			if (powerupType.equals("catch")) {
				paddle.catchPowerupInEffect = true;
			}
			else if (powerupType.equals("duplicate")) {
				paddle.duplicatePowerupInEffect = true;
			}
			else if (powerupType.equals("enlarge")) {
				paddle.enlargePowerupInEffect = true;
				enlargeSoundEffect.play();
			}
			else if (powerupType.equals("laser")) {
				paddle.laserPowerupInEffect = true;
			}
			else if (powerupType.equals("player")) {
				paddle.playerPowerupInEffect = true;
				playerSoundEffect.play();
			}
			else if (powerupType.equals("slow")) {
				paddle.slowPowerupInEffect = true;
			}
			else if (powerupType.equals("break")) {
				paddle.breakPowerupInEffect = true;
				breakSoundEffect.play();
			}
			
			isRemoved = true;
		}
		
	}
	
	//returns the respective Powerup's image
	public Image getImage() {
		if (powerupType.equals("catch")) {
			return catchImage;
		}
		else if (powerupType.equals("duplicate")) {
			return duplicateImage;
		}
		else if (powerupType.equals("enlarge")) {
			return enlargeImage;
		}
		else if (powerupType.equals("laser")) {
			return laserImage;
		}
		else if (powerupType.equals("player")) {
			return playerImage;
		}
		else if (powerupType.equals("slow")) {
			return slowImage;
		}
		//Break powerup
		else {
			return breakImage;
		}

	}
	
	//gets the Rect of the Powerup
	public Rect getRect() {
		return new Rect(x, y, width, height);
	}
}
