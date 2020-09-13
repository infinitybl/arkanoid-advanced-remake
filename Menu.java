/*  Menu.java
 	Phillip Pham
	Class used to generate the main menu of the game.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

class Menu extends JFrame {
	
	//JBUTTONS
	public JButton playBtn = new JButton("Play");
	public JButton helpBtn = new JButton("Help");
	public JButton scoresBtn = new JButton("Scores");
	public JButton returnBtn1 = new JButton("Return");
	public JButton returnBtn2 = new JButton("Return");
	public JButton controlsBtn = new JButton("Controls: Keyboard");
	public JButton confirmBtn = new JButton("Confirm Name");
	
	//LOGO AND HELP JLABEL
	public JLabel logoLabel;
	public JLabel helpLabel;
	
	//ALL MENU PAGES (JLAYEREDPANES)
	public JLayeredPane mPage = new JLayeredPane(); //LayeredPane allows to control what shows on top
	public JLayeredPane hPage = new JLayeredPane();
	public JLayeredPane sPage = new JLayeredPane();
	public JLayeredPane ePage = new JLayeredPane();
	
	//JTEXTFIELD to enter name for high scores
	JTextField textInput = new JTextField("Enter Name", 20);
	
	//CONSTRUCTOR
	public Menu() {
		
		//SETTING SIZE AND LOCATION OF ALL BUTTONS
		playBtn.setSize(100, 30);
		playBtn.setLocation(260, 350);

		helpBtn.setSize(100, 30);
		helpBtn.setLocation(260, 390);

		scoresBtn.setSize(100, 30);
		scoresBtn.setLocation(260, 430);
		
		controlsBtn.setSize(150, 30);
		controlsBtn.setLocation(350, 30);
		
		returnBtn1.setSize(100, 30);
		returnBtn1.setLocation(50, 430);

		returnBtn2.setSize(100, 30);
		returnBtn2.setLocation(50, 430);
		
		confirmBtn.setSize(150, 30);
		confirmBtn.setLocation(50, 430);
		
		textInput.setSize(200, 30);
		textInput.setLocation(200, 100);
		
		//PAGES
		//******************** mPage ************************
		//central menu page
		
		mPage.setLayout(null);

		//The numbers I use when adding to the LayeredPane are just relative to one another. Higher numbers on top.

		ImageIcon logo = new ImageIcon("Menu/logo.png");

		logoLabel = new JLabel(logo);
		
		//adding logo image on the screen
		logoLabel.setSize(600, 500);
		logoLabel.setLocation(0, -100);
		mPage.add(logoLabel, 2);

		//adding buttons to mPage
		mPage.add(playBtn, 2);
		mPage.add(helpBtn, 2);
		mPage.add(scoresBtn, 2);
		mPage.add(controlsBtn, 2);
		
		//******************** hPage ************************
		//help page
		hPage.setLayout(null);
		
		ImageIcon help = new ImageIcon("Menu/Arkanoid Help.png");
		helpLabel = new JLabel(help);
		helpLabel.setSize(585, 400);
		helpLabel.setLocation(0, 0);
		
		//adds helpLabel to hPage
		hPage.add(helpLabel, 2);
		//adding buttons to hPage
		hPage.add(returnBtn1, 2);

		//******************** sPage ************************
		//high scores page
		sPage.setLayout(null);
		
		//adding buttons to sPage
		sPage.add(returnBtn2, 2);
		
		//******************** ePage ************************
		//enter score page
		ePage.setLayout(null);
		
		//adding buttons and text field to ePage
		ePage.add(textInput, 2);
		ePage.add(confirmBtn, 2);



	}

}