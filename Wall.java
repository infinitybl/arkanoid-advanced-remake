/*  Wall.java
 	Phillip Pham
	Class used to generate Wall objects in the game that the ball can destroy for points.
*/

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class Wall {
	//Images for the different Wall types
	private Image blueWallImage = new ImageIcon("Sprites/Walls/BlueWall.png").getImage();
	private Image greenWallImage = new ImageIcon("Sprites/Walls/GreenWall.png").getImage();
	private Image lightBlueWallImage = new ImageIcon("Sprites/Walls/LightBlueWall.png").getImage();
	private Image orangeWallImage = new ImageIcon("Sprites/Walls/OrangeWall.png").getImage();
	private Image pinkWallImage = new ImageIcon("Sprites/Walls/PinkWall.png").getImage();
	private Image redWallImage = new ImageIcon("Sprites/Walls/RedWall.png").getImage();
	private Image silverWallImage = new ImageIcon("Sprites/Walls/SilverWall.png").getImage();
	private Image whiteWallImage = new ImageIcon("Sprites/Walls/WhiteWall.png").getImage();
	private Image yellowWallImage = new ImageIcon("Sprites/Walls/YellowWall.png").getImage();
	
	public final double width = 31; //width of the Wall Rect
	public final double height = 16; //height of the Wall Rect
	
	public double x; //x pos of the Wall (top left corner)
	public double y; //y pos of the Wall (top left corner)
	public String wallType; //the type of the Wall ("blue", "green", "lightBlue", ...)
	public int health; //current health of the Wall
	public int scoreValue; //how much points does the Wall add to user's score if destroyed
	public boolean isRemoved; //determines if Wall is removed
	
	//CONSTUCTOR
	public Wall(double x, double y, String wallType, int roundNumber) {
		this.x = x;
		this.y = y;
		this.wallType = wallType;

		health = 1; //all Walls except for silver take one hit to destroy
		
		//sets scoreValue depending on wallType
		if (wallType.equals("blue")) {
			scoreValue = 100;
		}
		else if (wallType.equals("green")) {
  			scoreValue = 80;
		}
		else if (wallType.equals("lightBlue")) {
			scoreValue = 70;
		}
		else if (wallType.equals("orange")) {
			scoreValue = 60;
		}
		else if (wallType.equals("pink")) {
			scoreValue = 110;
		}
		else if (wallType.equals("red")) {
			scoreValue = 90;
		}
		else if (wallType.equals("silver")) {
			scoreValue = 50 * roundNumber;
			health = 2; //silver Walls take two hits to destroy
		}
		else if (wallType.equals("white")) {
			scoreValue = 50;
		}
		else if (wallType.equals("yellow")) {
			scoreValue = 120;
		}
		
		isRemoved = false;
	}
	
	//returns the respective Wall's image
	public Image getImage() {
		if (wallType.equals("blue")) {
			return blueWallImage;
		}
		else if (wallType.equals("green")) {
			return greenWallImage;
		}
		else if (wallType.equals("lightBlue")) {
			return lightBlueWallImage;
		}
		else if (wallType.equals("orange")) {
			return orangeWallImage;
		}
		else if (wallType.equals("pink")) {
			return pinkWallImage;
		}
		else if (wallType.equals("red")) {
			return redWallImage;
		}
		else if (wallType.equals("silver")) {
			return silverWallImage;
		}
		else if (wallType.equals("white")) {
			return whiteWallImage;
		}
		return yellowWallImage;
	}
	
	//gets the Rect of the Wall
	public Rect getRect() {
		return new Rect(x, y, width, height);
	}
	

}
