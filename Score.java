/*  Score.java
 	Phillip Pham
	Class used to create Score objects that are used in the high score menu.
	Got help from http://forum.codecall.net/topic/50071-making-a-simple-high-score-system/
*/

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.image.*;

public class Score {
	String name; //the score holder's name
	int highestRound; //the score holder's highest round
	int totalScore; //the score holder's total score
	
	//CONSTRUCTOR
	public Score(String scoreString) {
		String[] dataString = scoreString.split(",");
		name = dataString[0];
		highestRound = Integer.parseInt(dataString[1]);
		totalScore = Integer.parseInt(dataString[2]);
	}
	
	//returns a data display string of the Score for text rendering
	public String getDisplayString() {
		return ("Name: " + name + "," + " Highest Round: " + highestRound + "," + " Score: " + totalScore);
		
	}
	
	//returns a data string of the Score
	public String getString() {
		return (name + "," + highestRound + "," + totalScore);
		
	}
		
}

