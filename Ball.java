/*  Ball.java
 	Phillip Pham
	Class used to generate Ball objects in the game that are bounced by the player's ship
	to destroy Walls.
*/

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class Ball {

	private Image ballImage = new ImageIcon("Sprites/ball.png").getImage(); //main image for Ball objects
	
	//SOUNDS
	private SoundEffect wallHitSoundEffect = new SoundEffect("Sounds/Wall Hit.wav");
	private SoundEffect shipHitSoundEffect = new SoundEffect("Sounds/Ship Hit.wav");
	private SoundEffect silverWallHitSoundEffect = new SoundEffect("Sounds/Silver Wall Hit.wav");

	public final double radius = ballImage.getWidth(null) / 2; //radius of Ball
	public double x; //x pos of Ball (top left corner)
	public double y; //y pos of Ball (top left corner)
	public double normalXSpeed; //normal x speed of Ball
	public double normalYSpeed; //normal y speed of Ball
	public double xSpeed; //current x speed of Ball
	public double ySpeed; //current y speed of Ball
	public boolean isRemoved; //determines if Ball is removed
	public boolean isSlow; //determines if Ball is under effect of Slow powerup
	
	//CONSTRUCTOR
	public Ball(double x, double y, double xSpeed, double ySpeed) {
		this.x = x;
		this.y = y; 
		normalXSpeed = xSpeed;
		normalYSpeed = -ySpeed;
		this.xSpeed = xSpeed;
		this.ySpeed = -ySpeed; //set speed to negative, Ball is usually moving up screen
		isRemoved = false; 
	}
	
	//move() changes pos of Ball by adding xSpeed or ySpeed to its respective x and y pos
	public void move() {
		x += xSpeed;
		y += ySpeed;	
	}

	//checkCollisionWithWall() checks if the Ball collided with the parameter Wall object
	public void checkCollisionWithWall(Wall brick) {
		
		//if the Ball did collide
		if (getRect().overlaps(brick.getRect())) {

			if (brick.wallType.equals("silver")) {
				silverWallHitSoundEffect.play();
			}
			else {
				wallHitSoundEffect.play();
			}
			
			//reduce current Wall's health by 1
			brick.health -= 1;
			
			//remove current Wall if its health drops to 0
			if (brick.health <= 0) {
				brick.isRemoved = true;
			}
			
			//get where the Ball collided in the x and y directions
			String collisionSectionX = getRect().checkCollisionSectionX(brick.getRect());
			String collisionSectionY = getRect().checkCollisionSectionY(brick.getRect());
			
			if (collisionSectionX.equals("left")) {
				//bounce Ball vertically if hit was on left
				bounceOffVertical();
				System.out.println("left brick");
			}

			else if (collisionSectionX.equals("right")) {
				//bounce Ball vertically if hit was on left	
				bounceOffVertical();
				System.out.println("right brick");
			}
			

			if (collisionSectionY.equals("above")) {
           	 	//bounce Ball horizontally if hit was from above	
				bounceOffHorizontal();
				System.out.println("above brick");
			}

			else if (collisionSectionY.equals("below")) {
				//bounce Ball horizontally if hit was from below
				bounceOffHorizontal();
				System.out.println("below brick");
			}
		
		}

	}

	//checkCollisionWithShip() checks if the Ball collided with the parameter Ship object
	public void checkCollisionWithShip(Ship paddle) {
	
		//if the Ball collided, bounce it off at an angle
		if (getRect().overlaps(paddle.getRect())) {

			shipHitSoundEffect.play();
			bounceOffAngle(paddle);
  			
		}
	}

	//checkCollisionWithBoundaries() checks if the Ball collided with the parameter boundary variables
	public void checkCollisionWithBoundaries(double xStartBoundary, double xEndBoundary, double yStartBoundary, double yEndBoundary) {
		double ballX1 = x; //top left corner of Ball
		double ballY1 = y; //top left corner of Ball
		double ballX2 = ballX1 + 2 * radius; //bottom right corner of Ball
		double ballY2 = ballY1 + 2 * radius; //bottom right corner of Ball
		
		if (ballX1 < xStartBoundary) {
			//bounce Ball vertically if it reached x start boundary 
			bounceOffVertical();
			x += 5; //offset Ball
		}
		else if (ballX2 > xEndBoundary) {
			//bounce Ball vertically if it reached x end boundary
			bounceOffVertical();
			x -= 5; //offset Ball
		}

		if (ballY1 < yStartBoundary) {
			//bounce Ball horizontally if it reached y start boundary
			bounceOffHorizontal();
			y += 5; //offset ball
		}

		else if (ballY2 > yEndBoundary) {
			//remove the ball if it falls below y end boundary
			isRemoved = true;
		}
	}
	
	//returns the Ball's image
	public Image getImage() {
		return ballImage;
	}
	
	//bounce off vertically by taking the negative of the current xSpeed
    private void bounceOffVertical() {
    	xSpeed = -xSpeed;
    }

    //bounce off horizontally by taking the negative of the current ySpeed
    private void bounceOffHorizontal() {
        ySpeed = -ySpeed;
    }
    
    //bounceOffAngle() reflects the Ball at an angle off the Ship parameter
    //if it collides with it
    private void bounceOffAngle(Ship paddle) {
	    double ballCenterX = x + radius; //center x position of Ball
	    double shipWidth = paddle.getImage().getWidth(null); //width of Ship
	    double shipCenterX = paddle.x + shipWidth / 2; //center x position of Ship
	
	    //get the total speed of the Ball by adding its x and y components using
	    //Pythagorean theorem (always positive)
		double totalSpeed = Math.sqrt(normalXSpeed * normalXSpeed + normalYSpeed * normalYSpeed);
		
	    //calculate distance from Ball center to Ship center
	    //and divide it over half the Ship width to get xRatio
	    double xRatio = (ballCenterX - shipCenterX) / (shipWidth / 2);
	
	    //get new xSpeed by multipliying the totalSpeed by xRatio
	    xSpeed = (totalSpeed * xRatio);
	    
	
	    //use Pythagorean theorem to calculate the Ball's ySpeed
	    //make negative to make Ball go up
    	ySpeed = -Math.sqrt(totalSpeed * totalSpeed - normalXSpeed * normalXSpeed);
    	y -= 5; //offset   
	
	    isSlow = false; //make ball normal speed again
    }
    
    //gets Rect of the Ball
    public Rect getRect() {
		return new Rect(x, y, radius * 2, radius * 2);
	}
	
	//slowDown() makes the Ball move slower if not slowed by setting the xSpeed and ySpeed 
	//equal to the speed divided by 2. 
	public void slowDown() {
		if (isSlow == false) {
			xSpeed *= 0.5;
			ySpeed *= 0.5;
			isSlow = true;
		}
		
	}
	
	//speedUpNormal() makes the Ball return to normal speed if slowed by setting the xSpeed and ySpeed 
	//equal to the normal speed by multiplying it by 2
	public void speedUpNormal() {
		if (isSlow == true) {
			xSpeed *= 2;
			ySpeed *= 2;
			isSlow = false;
		}
		
	}
}
