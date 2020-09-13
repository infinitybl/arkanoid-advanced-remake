/*  MainGame.java
 	Phillip Pham
	Contains main method to create a JFrame to run the Arkanoid game, plus the GamePanel class to control overall
	game logic and to render graphics on screen. 
*/

import java.util.*;
import java.io.*; //used for file input and output
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.Timer; //used for Timer
import javax.imageio.ImageIO; //used to render high scores text
import java.awt.Graphics2D; //same reason as above

//MainGame class that holds main method
class MainGame extends JFrame implements ActionListener {
	JPanel cards;   //a panel that uses CardLayout
	CardLayout cLayout = new CardLayout(); //layout used for menu

	Timer gameTimer; //controls game's framerate
	GamePanel game; //generates GamePanel object to run the game
	Menu mainMenu; //generates a Menu object to create a menu

	String mainControls; //String that is used to determine which controls player is going to use (only "mouse" or "keyboard")

	//SOUND EFFECTS
	SoundEffect menuSoundEffect = new SoundEffect("Sounds/Menu.wav");
	SoundEffect gameStartSoundEffect = new SoundEffect("Sounds/Game Start.wav");
	SoundEffect gameOverSoundEffect = new SoundEffect("Sounds/Game Over.wav");
	SoundEffect highScoresSoundEffect = new SoundEffect("Sounds/High Scores.wav");
	
	boolean gameRunning; //used to determine if game is still going on
	
	//VARIABLES USED FOR HIGH SCORE MENU
	BufferedReader reader; //used to read data file "High Scores.txt"
	int lineNumber; //total number of lines other than the first one in "High Scores.txt"
	String userName; //used to hold the name input from the textfield in the enter score menu page
	ArrayList<Score> scoreList; //holds Score objects
	ScoreComparator comparator = new ScoreComparator();	//used in Collections.sort() to sort the scoreList
	int currentScore; //holds final score after a game
	int currentRound; //holds final round after a game
	int lowestScoreIndex; //index of the lowest score in the scoreList arrayList
	int lowestScore; //used to compare scores to see which one is the lowest
		
	//CONSTRUCTOR
	public MainGame() {
		super("Arkanoid - Phillip Pham");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 550);
		
		gameTimer = new Timer(10, this);

		mainControls = "keyboard"; //inital controls is keyboard
		gameRunning = false; //initally set to false so that user can use the menu first
		
		//Generates a new menu 
		mainMenu = new Menu();
		//adds action listeners to all the buttons from the mainMenu
		mainMenu.playBtn.addActionListener(this);
		mainMenu.helpBtn.addActionListener(this);
		mainMenu.scoresBtn.addActionListener(this);
		mainMenu.controlsBtn.addActionListener(this);
		mainMenu.returnBtn1.addActionListener(this);
		mainMenu.returnBtn2.addActionListener(this);
		mainMenu.confirmBtn.addActionListener(this);
		
		//create a new JPanel with a card layout
		cards = new JPanel(cLayout);
		//add each page (menu screens) from the mainMenu to the JPanel
		cards.add(mainMenu.mPage, "menu");
		cards.add(mainMenu.hPage, "help");
		cards.add(mainMenu.sPage, "scores");
		cards.add(mainMenu.ePage, "enter score");
		
		add(cards); //add the JPanel to the MainGame's JFrame
		
		setResizable(false);
		setVisible(true);

		menuSoundEffect.play();

	}

	// ------------ ActionListener ------------------------------------------
	// Checks for button click and Timer events
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		//when the user selects the play button, create new GamePanel instance
		//and display the game page to start the game
		if (source == mainMenu.playBtn) {
			game = new GamePanel(mainControls, 1, getHighestScore());
			cards.add(game, "game");
			cLayout.show(cards, "game");
			gameStartSoundEffect.play();
			gameRunning = true; //set to true to indicate that the game is running
			gameTimer.start(); //start the gameTimer
			game.requestFocus();
		}
		
		//display help page
		if (source == mainMenu.helpBtn) {
			cLayout.show(cards, "help");
			mainMenu.hPage.requestFocus();
		}
		
		//display high scores page
		if (source == mainMenu.scoresBtn) {
			//render high scores text
			displayScoreText();
			cLayout.show(cards, "scores");
			mainMenu.sPage.requestFocus();
			highScoresSoundEffect.play();
			
		}
		
		//changes mainControls to "mouse" or "button" and changes the text
		//on the button respectively when the user clicks on it
		if (source == mainMenu.controlsBtn) {
			if (mainControls.equals("keyboard")){
        		mainMenu.controlsBtn.setText("Controls: Mouse");
        		mainControls = "mouse";
   			}
   			else if (mainControls.equals("mouse")) {
        		mainMenu.controlsBtn.setText("Controls: Keyboard");
        		mainControls = "keyboard";
    		}
		}
		
		//returns to the central menu screen whenever user clicks on a return button
		if (source == mainMenu.returnBtn1 || source == mainMenu.returnBtn2) {
			cLayout.show(cards, "menu");
			mainMenu.mPage.requestFocus();
		}
		
		//when user is finished entering name on ePage for high scores
		if (source == mainMenu.confirmBtn) {
			userName = mainMenu.textInput.getText(); //get text from the textfield
			
			readHighScores(); //get high scores from the text file and add them to the scoresList
			
		    scoreList.add(new Score("" + userName + "," + currentRound + "," + currentScore));
		    //add the user's new Score to the scoreList
    		
    		lowestScore = Integer.MAX_VALUE;
    		
    		//find the lowest Score in the scoreList and get it's index
    		for (int j = 0; j < scoreList.size(); j++) {
    			if (scoreList.get(j).totalScore < lowestScore) {
    				lowestScoreIndex = j;
    				lowestScore = scoreList.get(j).totalScore;
    			}
    			  
    		}
    		
    		scoreList.remove(lowestScoreIndex); //remove the lowest Score
    		
    		Collections.sort(scoreList, comparator); //sort the scoreList from highest score to lowest
    		
    		try {
    			BufferedWriter output = new BufferedWriter(new FileWriter("High Scores/High Scores.txt"));
        		output.flush(); //clear the text file
        		output.write("" + scoreList.size()); //write the line number on the top line
    			for (int k = 0; k < scoreList.size(); k++) {
    				output.newLine();
        			output.write(scoreList.get(k).getString()); //write each score on a new line
    			}
    			output.close();
    		}
    		catch (IOException ex) {
				System.out.println("Error writing scores to file");
			}
				
			//render the text to be displayed
    		displayScoreText();
    		
    		//show high scores menu page
		    cLayout.show(cards, "scores");
		    
		    
		}
		
		//whenever the gameTimer is ticked, check if the game is still running
		//and update all the graphics on screen using repaint
		if (source == gameTimer) {
			if (game != null && game.ready && gameRunning) {
				gameRunning = game.updateState();
				game.repaint();
			}
			if (gameRunning == false) {
				//if the game is not running anymore, get the currentScore andn currentRound 
				//before the game ended, stop the gameTimer, and 
				//show the enter score page on the screen
				currentScore = game.getCurrentScore();
				currentRound = game.getCurrentRound();
				gameTimer.stop();
				cLayout.show(cards, "enter score");
				mainMenu.ePage.requestFocus();
				gameOverSoundEffect.play();
			}
		}

	}
	
	//getHighestScore() returns the highest score in the "High Score.txt" file
	public int getHighestScore() {
		readHighScores();
		Collections.sort(scoreList, comparator);
		return scoreList.get(0).totalScore;
	}
	
	//readHighScores() reads the "High Scores.txt" file and creates 
	//Score objects using the data and adds it to the scoreList
	public void readHighScores() {
		try {
    		reader = new BufferedReader(new FileReader("High Scores/High Scores.txt"));
    		lineNumber = Integer.parseInt(reader.readLine().trim()); //gets how much lines are in text file
    		scoreList = new ArrayList<Score>(); //clear scoreList
    		
    		//read the score file line by line and create Score objects
    		for (int i = 0; i < lineNumber; i++) {          
            	scoreList.add(new Score(reader.readLine().trim()));
    		}
    		
    		reader.close();

		} 
		catch (IOException ex) {
			System.out.println("Error reading scores from file");
		}
	}
	
	public static void main(String[] args) {
		MainGame frame = new MainGame(); //create new JFrame (window for the game)
    }
    
	//displayScoreText() renders high score text on an image so that it can be displayed
	//Got help from https://stackoverflow.com/questions/10391778/create-a-bufferedimage-from-file-and-make-it-type-int-argbs
	public void displayScoreText() {
		
		try {
			
			BufferedImage in = ImageIO.read(new File("Menu/Scores.png"));
			BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);	
			
			Graphics2D g = newImage.createGraphics();
			//g.setColor(new Color(255, 255, 255)); 
			//g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight()); //make the whole image white
	    	
	        readHighScores(); //get updated scoreList
	        
	        g.setFont(new Font("emulogic", Font.PLAIN, 10));
			g.setColor(new Color(255, 0, 0));
			
			//display all the Scores line by line by drawing on newImage
	        for (int l = 0; l < scoreList.size(); l++) {
	        	g.drawString("" + (l + 1) + ": " + scoreList.get(l).getDisplayString(), 75, 75 + 75 * l);
	        	System.out.println(scoreList.get(l).getDisplayString());
	        }
	        g.dispose();
	        
			mainMenu.sPage = new JLayeredPane();
			mainMenu.sPage.setLayout(null);
		
			//adding buttons to sPage
			mainMenu.sPage.add(mainMenu.returnBtn2, 2);
			
			mainMenu.returnBtn2.addActionListener(this);
			
	        JLabel scoreLabel = new JLabel(new ImageIcon(newImage));
	        //scoreLabel.setOpaque(true);
	        scoreLabel.setSize(585, 400);
	        scoreLabel.setLocation(0, 0);
	        mainMenu.sPage.add(scoreLabel);
	        
	        cards.add(mainMenu.sPage, "scores");
		}
		catch (IOException ex) {
			System.out.println("Error reading image from file");
		}
		
		
	}

}


//GamePanel class controls the game logic and allows to update the graphics on the screen	
class GamePanel extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
	private boolean[] keys; //array used to see which keyboard keys are currently pressed
	
	//background images for each round of the game 
	private Image background1 = new ImageIcon("Backgrounds/Stage 1.png").getImage();
	private Image background2 = new ImageIcon("Backgrounds/Stage 2.png").getImage();
	private Image background3 = new ImageIcon("Backgrounds/Stage 3.png").getImage();
	private Image background4 = new ImageIcon("Backgrounds/Stage 4.png").getImage();
	private Image background5 = new ImageIcon("Backgrounds/Stage 5.png").getImage();
	
	public boolean ready; //used to determine if the all the game's assets already loaded yet
	
	public Ship playerShip; //the main ship the user is controlling
	
	//CONSTANTS used to indicate the boundaries of the background images for each round of the game
	private final int backgroundWidth = 400;
	private final int backgroundHeight = 550;
	private final int backgroundStartx = 20;
	private final int backgroundEndx = backgroundStartx + backgroundWidth;
	private final int backgroundStarty = 0;
	private final int borderWidth = 15; //width of the border on the background
	
	//CONSTANTS for the width and height of each Wall object in the game (used so game can
	//create these Walls at certain positions on the screen)
	private final int wallWidth = 31;
	private final int wallHeight = 16;
	
	private String playerControls; //used to determine what controls user is using ("mouse" or "keyboard")

	private boolean ballMoving; //used to determine if the ball is currently moving across the screen or 
								//if it's on the playerShip ready to get sent off

	public ArrayList<Ball> ballList; //ArrayList to hold all the Ball objects of the game currently on screen
	public ArrayList<LaserBeam> laserBeamList; //ArrayList to hold all the Ball objects of the game currently on screen
	public ArrayList<Powerup> powerupList; //ArrayList to hold all the falling Powerup objects of the game currently on screen
	
	public ArrayList<Wall> wallList;//ArrayList to hold all the Wall objects of the game currently on screen
		
	public int score; //used to determine user's current score
	public int round; //used to determine the round user's is currently on
	public int lives; //used to determine how much lives playerShip has left before game over
	
	int highestScore; //holds highestScore from "High Score.txt"
	
	//SOUNDS
	private SoundEffect shipHitSoundEffect = new SoundEffect("Sounds/Ship Hit.wav");
	private SoundEffect laserBeamSoundEffect = new SoundEffect("Sounds/Laser Beam.wav");
	private SoundEffect deathSoundEffect = new SoundEffect("Sounds/Death.wav");
	private SoundEffect roundStartSoundEffect = new SoundEffect("Sounds/Game Start.wav");
	
	//Random object used to generate random numbers that are used to 
	//determine which Powerup is generated
	Random rand;
	int randomNum;
	
	//CONSTRUCTOR
	public GamePanel(String controls, int stageLevel, int highestScore){
		playerControls = controls; //sets the controls parameter from the MainGame class to playerControls
		
		this.highestScore = highestScore;
		
		//add event listeners so user can perform mouse or keyboard interactions
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		
		//resize all the backgrounds to the appropriate size determined from boundary variables
		background1 = background1.getScaledInstance(backgroundWidth, backgroundHeight, Image.SCALE_SMOOTH);
		background2 = background2.getScaledInstance(backgroundWidth, backgroundHeight, Image.SCALE_SMOOTH);
		background3 = background3.getScaledInstance(backgroundWidth, backgroundHeight, Image.SCALE_SMOOTH);
		background4 = background4.getScaledInstance(backgroundWidth, backgroundHeight, Image.SCALE_SMOOTH);
		background5 = background5.getScaledInstance(backgroundWidth, backgroundHeight, Image.SCALE_SMOOTH);
		
		keys = new boolean[KeyEvent.KEY_LAST + 1]; //creates keys array to determine which key is currently pressed

		playerShip = new Ship(200, 450, "small", 5); //creates playerShip object
		
		//set up game objects ArrayList
		laserBeamList = new ArrayList<LaserBeam>(); 
		wallList = new ArrayList<Wall>();
		powerupList = new ArrayList<Powerup>();
		
		ballList = new ArrayList<Ball>();
		ballList.add(new Ball(playerShip.x + (playerShip.getImage().getWidth(null) / 2), playerShip.y, 3.5, 3.5)); //add the first ball of the game
		
		ballMoving = false; //set to false so ball doesn't move yet at start of the game
		score = 0; //set inital score
		round = stageLevel; //set inital round through stageLevel parameter from MainGame class
		lives = 3; //set inital lives
		
		
		//initalize the randomizer
		rand = new Random();
		randomNum = -1;
		
		wallList = addWalls(round, wallList); //add the first round Walls to the game
		
		addNotify(); //calls addNotify to indicate game is done loading
		
	}
	
	//addNotify() indicates game is done loading by setting ready to true
	public void addNotify() {
        super.addNotify();
        ready = true;
    }
	
	//used to return the current score or round
	public int getCurrentScore() {
		return score;
	}
	
	public int getCurrentRound() {
		return round;
	}
	
	//updateState() updates the game everytime the GameTimer from the MainGame class ticks
	public boolean updateState() {
		//update highestScore
		if (highestScore < score) {
			highestScore = score;
		}
		
		//CHECKING THE POWERUPS
		//if a Powerup is in effect, then reset all current Powerup effects from the playerShip
		//as well as set all the Ball speeds to normal, and apply the appropriate new effects
		if (playerShip.catchPowerupInEffect) {
			playerShip.removeAllPowerupEffects();
			for (Ball b : ballList) {
				b.speedUpNormal();
			}
			ballMoving = false; //set ballMoving to false so ball moves to playerShip to get relaunched
		}
		if (playerShip.duplicatePowerupInEffect) {
			playerShip.removeAllPowerupEffects();
			//add 2 new Ball objects to game
			if (ballList.size() == 1 && ballMoving == true) {
				ballList.add(new Ball(ballList.get(0).x, ballList.get(0).y, 3.5, 3.5));
				ballList.add(new Ball(ballList.get(0).x, ballList.get(0).y, 3.5, -3.5));
			} 
			for (Ball b : ballList) {
				b.speedUpNormal();
			}
		}
		if (playerShip.enlargePowerupInEffect) {
			playerShip.removeAllPowerupEffects();
			for (Ball b : ballList) {
				b.speedUpNormal();
			}
			//set shipType to "large" to make the playerShip bigger
			playerShip.shipType = "large";
		}	
		if (playerShip.laserPowerupInEffect) {
			playerShip.removeAllPowerupEffects();
			for (Ball b : ballList) {
				b.speedUpNormal();
			}
			//set shipType to "laser" so the playerShip can shoot lasers
			playerShip.shipType = "laser";
		}
		if (playerShip.playerPowerupInEffect) {
			playerShip.removeAllPowerupEffects();
			for (Ball b : ballList) {
				b.speedUpNormal();
			}
			//increase the playerShip's lives
			lives++;
		}
		if (playerShip.slowPowerupInEffect) {
			playerShip.removeAllPowerupEffects();
			for (Ball b : ballList) {
				b.slowDown(); //slow down all the Ball objects
			}
		}
		
		//******************************************************************************	
		
		//CONTROL PLAYERSHIP WITH KEYBOARD CONTROLS
		if (playerControls.equals("keyboard")) {
			//moves the playerShip to the right when right key is pressed (if playerShip is still in boundaries)
			if (keys[KeyEvent.VK_RIGHT] && playerShip.x + playerShip.getImage().getWidth(null) + playerShip.speed + borderWidth < backgroundEndx) {
				playerShip.x += playerShip.speed;
			}
			//moves the playerShip to the left when left key is pressed (if playerShip is still in boundaries)
			if (keys[KeyEvent.VK_LEFT] && playerShip.x - playerShip.speed - borderWidth > backgroundStartx) {
				playerShip.x -= playerShip.speed;
			}
			
    		
			//sets ballMoving to true when space key is pressed
			if (keys[KeyEvent.VK_SPACE] && ballMoving == false) {
				ballMoving = true;
				shipHitSoundEffect.play();
    		}


    	}
		
		//******************************************************************************
		
		//CHECK IF THE BALL OBJECTS ARE CURRENTLY MOVING IN GAME
    	if (ballMoving == true) {
    		
    		//CHECKING ALL BALL OBJECTS
    		for (int i = ballList.size() - 1; i >= 0; i--) {
    			ballList.get(i).move(); //move the current Ball
    			
    			//checking current Ball collision with boundaries or playerShip
    			ballList.get(i).checkCollisionWithBoundaries(backgroundStartx + borderWidth, backgroundStartx + backgroundWidth - borderWidth, backgroundStarty + borderWidth, backgroundStarty + backgroundHeight);
    			ballList.get(i).checkCollisionWithShip(playerShip);
    			
    			//checking current Ball collision all the Walls 
				for (int k = wallList.size() - 1; k >= 0; k--) {
					ballList.get(i).checkCollisionWithWall(wallList.get(k));
					
					//if the current Wall is destroyed, increase score and remove that Wall
					if (wallList.get(k).isRemoved == true) {
						score += wallList.get(k).scoreValue;
						
						//1/5 chance of generating new Powerup 
						if (powerupList.size() == 0) {
    						randomNum = rand.nextInt(5);
							if (randomNum == 0) {
								powerupList.add(new Powerup(wallList.get(k).x, wallList.get(k).y, 2));
							}
						}
						wallList.remove(k);
						break;
					}
				}	
    			
    			//if current Ball falls below playerShip, remove it
    			if (ballList.get(i).isRemoved == true) {
    				ballList.remove(i);
    			}
    		}
    		
    		
    	}
    	//CHECK IF THE BALL OBJECTS ARE NOT CURRENTLY MOVING IN GAME
    	else if (ballMoving == false) {
    		//all Ball objects should be on the playerShip (change position to be on playerShip)
    		for (Ball ball : ballList) {
    			ball.x = playerShip.x + (playerShip.getImage().getWidth(null) / 2);
    			ball.y = playerShip.y - (playerShip.getImage().getHeight(null));
    		}

    	}
		
		//******************************************************************************
		
		//CHECKING ALL LASERBEAM OBJECTS
		for (int j = laserBeamList.size() - 1; j >= 0; j--) {
			laserBeamList.get(j).move(backgroundStarty + borderWidth); //move the current LaserBeam
			
			//check for Wall collision just like with the Ball objects, and remove, increase score, or 
			//add Powerup objects if needed
			for (int a = wallList.size() - 1; a >= 0; a--) {
				laserBeamList.get(j).checkCollisionWithWall(wallList.get(a));
				if (wallList.get(a).isRemoved == true) {
					score += wallList.get(j).scoreValue;
					if (powerupList.size() == 0) {
						randomNum = rand.nextInt(5);
						if (randomNum == 0) {
							powerupList.add(new Powerup(wallList.get(a).x, wallList.get(a).y, 2));
						}
					}
					wallList.remove(a);
					break;
				}
			}
				
			if (laserBeamList.get(j).isRemoved == true) {
    			laserBeamList.remove(j);
    		}
		}
		
		//******************************************************************************
		
		//CHECKING ALL POWERUP OBJECTS
		for (int b = powerupList.size() - 1; b >= 0; b--) {
			powerupList.get(b).fall(backgroundStarty + backgroundHeight); //make the Powerup fall
			powerupList.get(b).checkCollisionWithShip(playerShip); //check for playerShip collisions
			if (powerupList.get(b).isRemoved == true) { //remove the Powerup if needed
				powerupList.remove(b);
			}
			
		}
		
		//******************************************************************************
		
		//WINNING THE CURRENT ROUND (if there are no more Walls on screen or Break Powerup is used)
		if (wallList.size() == 0 || playerShip.breakPowerupInEffect) {
			if (wallList.size() == 0 && round + 1 < 6) {
				roundStartSoundEffect.play();
			}
			playerShip.removeAllPowerupEffects(); //remove all current Powerup effects on playerShip
			//remove all Walls, Balls, Powerups, and LaserBeams
			wallList.clear();
			ballList.clear();
			powerupList.clear();
			laserBeamList.clear();
			
			//stop Balls from moving
			ballMoving = false;
			
			//add new Ball for next round
			ballList.add(new Ball(playerShip.x + (playerShip.getImage().getWidth(null) / 2), playerShip.y, 3.5, 3.5));
		
			round++; //increase the round number for user to move on
			if (round < 6) {
				wallList = addWalls(round, wallList); //add new Walls for next round
			}
			//the game ends after round 5
			else if (round == 6) {
				return false;
			}
		}
		
		//******************************************************************************
		
    	//LOSING THE GAME (no more Ball objects left on screen)
    	if (ballList.size() == 0) {
    		//if the playerShip still has lives, then remove all Powerup effects, decrease life counter,
    		//set ballMoving to false, and add a new Ball to relaunch
    		if (lives > 1) {
   				playerShip.removeAllPowerupEffects();
    			deathSoundEffect.play();
    			lives--;
    			ballMoving = false;
    			ballList.add(new Ball(playerShip.x + (playerShip.getImage().getWidth(null) / 2), playerShip.y, 3.5, 3.5));
    		}
    		//otherwise game ends if playerShip has no more lives
    		else {
    			return false;
    		}

    	}

		return true; //return true if game didn't end for MainGame class
	}
	
	//paintComponent() is used to draw all the graphics on the screen
	@Override
    public void paintComponent(Graphics g){
    	g.setColor(new Color(0, 0, 0)); 
		g.fillRect(0, 0, getWidth(), getHeight()); //make the whole screen black
		
		//place respective background image for current round on the screen
		if (round == 1) {
			g.drawImage(background1, backgroundStartx, backgroundStarty, this);
		}
		else if (round == 2) {
			g.drawImage(background2, backgroundStartx, backgroundStarty, this);
		}
		else if (round == 3) {
			g.drawImage(background3, backgroundStartx, backgroundStarty, this);
		}
		else if (round == 4) {
			g.drawImage(background4, backgroundStartx, backgroundStarty, this);
		}
		else if (round == 5) {
			g.drawImage(background5, backgroundStartx, backgroundStarty, this);
		}
		
		//draw all Wall objects
		for (Wall brick : wallList) {
			g.drawImage(brick.getImage(), (int)brick.x, (int)brick.y, this);
		}
		
		//draw playerShip
		g.drawImage(playerShip.getImage(), (int)playerShip.x, (int)playerShip.y, this);
		
		//draw all Powerup objects
		for (Powerup pill : powerupList) {
			g.drawImage(pill.getImage(), (int)pill.x, (int)pill.y, this);
		}
		
		//draw all LaserBeam objects
		for (LaserBeam laser : laserBeamList) {
			g.drawImage(laser.getImage(), (int)laser.x, (int)laser.y, this);
		}
		
		//draw all Ball objects
		for (Ball ball : ballList) {
			g.drawImage(ball.getImage(), (int)ball.x, (int)ball.y, this);
		}
		
		//draws all neccessary text on screen
		g.setFont(new Font("emulogic", Font.PLAIN, 14));
		g.setColor(new Color(255, 0, 0));
		g.drawString("High Score", 430, 30);
		g.drawString("1UP", 430, 110);
		g.drawString("Lives", 430, 250);
		g.drawString("Round", 430, 430);

		g.setColor(new Color(255, 255, 255));
		g.drawString("" + highestScore, 430, 60);
		g.drawString("" + score, 430, 140);
		g.drawString("" + lives, 430, 280);
		g.drawString("" + round, 430, 460);
    }

    // ------------ KeyListener ------------------------------------------
    // Check for keyboard events
	public void keyTyped(KeyEvent e) {


	}
	
	//when a key is pressed, set the index of that respective key in the keys array to true
	//to indicate it is pressed
    public void keyPressed(KeyEvent e) {
    	keys[e.getKeyCode()] = true;

    }
	
	//when a key is released, set the index of that respective key in the keys array to false
	//to indicate it is not pressed
    public void keyReleased(KeyEvent e) {
    	//adds a LaserBeam object on screen if playerShip's shipType is "laser", the X key is pressed,
		//and ballMoving is true
		if (playerShip.shipType.equals("laser") && keys[KeyEvent.VK_X] && ballMoving == true) {
			//only a max of 2 LaserBeams can be on screen at once
    		if (laserBeamList.size() < 2) {
    			laserBeamList.add(new LaserBeam(playerShip.x + playerShip.getImage().getWidth(null) / 3, playerShip.y, 5));
    			laserBeamSoundEffect.play();
    		}
    	}
    	keys[e.getKeyCode()] = false;
    }

	// ---------- MouseMotionListener ------------------------------------------
	// Check for mouse motion events
    public void mouseDragged(MouseEvent e) {}
	
	//if the mouse is moved and the current controls is "mouse", update the playerShip's position to the mouse pointer within boundaries
    public void mouseMoved(MouseEvent e) {
    	if (playerControls.equals("mouse")) {
    		if (e.getX() - playerShip.getImage().getWidth(null) / 2 - borderWidth > backgroundStartx && e.getX() + playerShip.getImage().getWidth(null) / 2 + borderWidth < backgroundEndx) {
    			playerShip.x = e.getX() - playerShip.getImage().getWidth(null) / 2;
    		}
    	}

    }

	// ------------ MouseListener ------------------------------------------
	// Check for mouse click events
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
	
	//if the mouse button is released and the current controls is "mouse"
    public void mouseReleased(MouseEvent e) {
    	if (playerControls.equals("mouse")) {
    		//if the user left clicked, set ballMoving to true
    		if (e.getButton() == MouseEvent.BUTTON1) {
	    		if (ballMoving == false) {
	    			ballMoving = true;
	    			shipHitSoundEffect.play();
	    		}
    		}
    		if (e.getButton() == MouseEvent.BUTTON3) {
    			//if the user right clicked, add new LaserBeam
    			if (playerShip.shipType.equals("laser") && ballMoving == true) {
    				if (laserBeamList.size() < 2) {
    					laserBeamList.add(new LaserBeam(playerShip.x + playerShip.getImage().getWidth(null) / 3, playerShip.y, 5));
    					laserBeamSoundEffect.play();
    				}
    			}
    		}
    	}
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {


	}
	
	//addWalls() is used to generate an ArrayList of what Walls are supposed
	//to be on screen at the current round
	public ArrayList<Wall> addWalls(int stage, ArrayList<Wall> brickList) {
		//round 1 walls
		if (stage == 1) {
			
			for (int i = 0; i < 6; i++) { //row
				for (int j = 0; j < 12; j++) { //col
					if (i == 0) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "silver", round));
					}
					else if (i == 1) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "red", round));
					}
					else if (i == 2) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "yellow", round));
					}
					else if (i == 3) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "blue", round));
					}
					else if (i == 4) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "pink", round));
					}
					else if (i == 5) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "green", round));
					}
	
				}
			}
		}
		//round 2 walls
		if (stage == 2) {
			brickList.add(new Wall(backgroundStartx + borderWidth, 80 - wallHeight, "pink", round));
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < i + 2; j++) {
					if (j == 0) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "pink", round));	
					}
					else if (j == 1) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "lightBlue", round));
					}
					else if (j == 2) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "green", round));
					}
					else if (j == 3) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "blue", round));
					}
					else if (j == 4) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "red", round));
					}
					else if (j == 5) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "pink", round));
					}
					else if (j == 6) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "lightBlue", round));
					}
					else if (j == 7) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "green", round));
					}
					else if (j == 8) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "blue", round));
					}
					else if (j == 9) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "red", round));
					}
					else if (j == 10) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "pink", round));
					}
				}	
	
			}
			for (int k = 0; k < 12; k++) {
				if (k == 11) {
					brickList.add(new Wall(backgroundStartx + borderWidth + k * wallWidth, 80 + 10 * wallHeight, "lightBlue", round));
				}
				else {
					brickList.add(new Wall(backgroundStartx + borderWidth + k * wallWidth, 80 + 10 * wallHeight, "silver", round));
				}
					
			}
		}
		//round 3 walls
		else if (stage == 3) {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 12; j++) {
					if (i == 0) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "green", round));
					}
					else if (i == 2) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "white", round));
					}
					else if (i == 4) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "red", round));
					}
					else if (i == 6) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "white", round));
					}
					else if (i == 8) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "pink", round));
					}
					else if (i == 10) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "blue", round));
					}
					else if (i == 12) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "blue", round));
					}
					else if (i == 14) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "blue", round));
					}
				}
			}
		}
		//round 4 walls
		else if (stage == 4) {
			for (int i = 0; i < 11; i++) {
				for (int j = 0; j < 12; j++) {
					if (j == 0) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "blue", round));
					}
					else if (j == 2) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "red", round));
					}
					else if (j == 4) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "green", round));
					}
					else if (j == 6) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "green", round));
					}
					else if (j == 8) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "red", round));
					}
					else if (j == 10) {
						brickList.add(new Wall(backgroundStartx + borderWidth + j * wallWidth, 80 + i * wallHeight, "blue", round));
					}
				}
			}
		}
		//round 5 walls
		else if (stage == 5) {
			for (int i = 0; i < 5; i++) {
				brickList.add(new Wall(backgroundStartx + borderWidth + wallWidth, 200 + i * wallHeight, "silver", round));
			}
			for (int i = 0; i < 4; i++) {
				brickList.add(new Wall(backgroundStartx + borderWidth + 2 * wallWidth, 200 + i * wallHeight - 2 * wallHeight, "silver", round));
			}
			for (int i = 0; i < 9; i++) {
				brickList.add(new Wall(backgroundStartx + borderWidth + 3 * wallWidth, 200 + i * wallHeight - 4 * wallHeight, "silver", round));
			}
			for (int i = 0; i < 9; i++) {
				brickList.add(new Wall(backgroundStartx + borderWidth + 8 * wallWidth, 200 + i * wallHeight - 4 * wallHeight, "silver", round));
			}
			for (int i = 0; i < 4; i++) {
				brickList.add(new Wall(backgroundStartx + borderWidth + 9 * wallWidth, 200 + i * wallHeight - 2 * wallHeight, "silver", round));
			}
			for (int i = 0; i < 5; i++) {
				brickList.add(new Wall(backgroundStartx + borderWidth + 10 * wallWidth, 200 + i * wallHeight, "silver", round));
			}
			
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 4; j++) {
					brickList.add(new Wall(backgroundStartx + borderWidth + (4 + j) * wallWidth, 120 + i * wallHeight - 4 * wallHeight, "silver", round));
				}
			}
		
		}
		return brickList;
	}

}
