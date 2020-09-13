/*  Rect.java
 	Phillip Pham
	Class used to create Rect objects used to model rectangles for 
	objects in the game, and can check if they collide.
*/
import java.util.*;

public class Rect {
	private double x; //x pos of the Rect (top left corner)
	private double y; //x pos of the Rect (top left corner)
	private double width; //width of the Rect
	private double height; //height of the Rect
	
	//methods to return properties about the Rect
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	//checkCollisionSectionX() determines whether the Rect collided
	//with a parameter Rect from the left, right, or not at all, and returns a String
	public String checkCollisionSectionX(Rect rectangle) {
		double intersectionDifferenceLeft = 0;
		
		if (x + width > rectangle.x && x < rectangle.x) {
			//Hit was from left
			return "left";
		}

		if (x < rectangle.x + rectangle.width && x + width > rectangle.x + rectangle.width) {
			//Hit was from right
			return "right";
		}
		
		return "false";
	}
	
	//checkCollisionSectionY() determines whether the Rect collided
	//with a parameter Rect from the above, below, or not at all, and returns a String
	public String checkCollisionSectionY(Rect rectangle) {
		
		if (y + height > rectangle.y && y < rectangle.y) {
			//Hit was from above
			return "above";
		}

		else if (y < rectangle.y + rectangle.height && y + height > rectangle.y + rectangle.height) {
			//Hit was from below
			return "below";
		}
		
		return "false";
	}
	
	//checks if the Rect collided with the parameter Rect
	public boolean overlaps(Rect rectangle) {
		if (x < rectangle.x + rectangle.width &&
			x + width > rectangle.x &&
			y < rectangle.y + rectangle.height &&
			y + height > rectangle.y) {
			return true;
		}
		return false;
	}


	//CONSTRUCTOR
	public Rect(double xCoor, double yCoor, double widthValue, double heightValue) {
		x = xCoor;
		y = yCoor;
		width = widthValue;
		height = heightValue;
		
	}
}