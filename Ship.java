/*  Ship.java
 	Phillip Pham
	Class used to generate the Ship the player controls in the game.
*/

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class Ship {
	
	//Images for the different Ship types
	private Image smallShipImage = new ImageIcon("Sprites/Spacecraft/VausSpacecraft.png").getImage();
	private Image largeShipImage = new ImageIcon("Sprites/Spacecraft/VausSpacecraftLarge.png").getImage();
	private Image laserShipImage = new ImageIcon("Sprites/Spacecraft/LaserVausSpacecraft.png").getImage();

	public double x; //x pos of the Ship (top left corner)
	public double y; //y pos of the Ship (top left corner)
	public String shipType; //the type of the Ship ("small", "large", "laser")
	public double speed; //speed of the Ship
	
	//POWERUPINEFFECT BOOLEAN FIELDS to determine if a Powerup is applied on the Ship
	public boolean catchPowerupInEffect;
	public boolean duplicatePowerupInEffect;
	public boolean enlargePowerupInEffect;	
	public boolean laserPowerupInEffect;
	public boolean playerPowerupInEffect;	
	public boolean slowPowerupInEffect;
	public boolean breakPowerupInEffect;
	
	//CONSTRUCTOR
	public Ship(double x, double y, String shipType, double speed) {
		this.x = x;
		this.y = y;
		this.shipType = shipType;
		this.speed = speed;
		
		catchPowerupInEffect = false;
		duplicatePowerupInEffect = false;
		enlargePowerupInEffect = false;	
		laserPowerupInEffect = false;
		playerPowerupInEffect = false;	
		slowPowerupInEffect = false;
		breakPowerupInEffect = false;
		
	}
	
	//returns the respective Ship's image
	public Image getImage() {
		if (shipType.equals("large")) {
			return largeShipImage;
		}
		else if (shipType.equals("laser")) {
			return laserShipImage;
		}
		return smallShipImage;
	}
	
	//gets the Rect of the Ship
	public Rect getRect() {
		return new Rect(x, y, getImage().getWidth(null), getImage().getHeight(null));
	}
	
	//removeAllPowerupEffects() sets all PowerupInEffect boolean fields to false and shipType to "small"
	//to remove all special effects on the Ship
	public void removeAllPowerupEffects() {
		catchPowerupInEffect = false;
		duplicatePowerupInEffect = false;
		enlargePowerupInEffect = false;	
		laserPowerupInEffect = false;
		playerPowerupInEffect = false;	
		slowPowerupInEffect = false;
		breakPowerupInEffect = false;
		shipType = "small";
	}
}
