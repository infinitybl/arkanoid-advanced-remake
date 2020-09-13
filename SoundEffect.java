/*  SoundEffect.java
 	Phillip Pham
	This class was NOT MADE by me. Found the code on this website and made some minor changes to it to understand how it works
	https://jnorthr.wordpress.com/2013/01/01/sample-java-code-to-play-sound-effect/.
	Making an audio system for each sound effect is complicated in Java and it's much easier to use a premade class
	to play sounds, as well as make the main class less cluttered.
*/

//
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import java.net.*;

public class SoundEffect
{
 	AudioInputStream ais;
 	Clip clip;
 	
 	// simple audio file name like snick7.wav works if in same folder as this.class
 	// but full filenames like /Volumes/PENDRIVE/groovy/snick7.wav don't
 	
 	//CONSTRUCTOR
 	public SoundEffect(String fn)
 	{
   		try
   		{
   			File soundFile = new File(fn);
			URL soundUrl = soundFile.toURI().toURL();	
		    ais = AudioSystem.getAudioInputStream(soundUrl);
		    clip = AudioSystem.getClip();
		    clip.open(ais);
   		}
   		catch (Exception e) {
   			e.printStackTrace();
   		}
 	} // end of constructor
 	
	//play() resets the clip position to 0 and plays it
	public void play() {
		clip.setMicrosecondPosition(0);
		clip.start();
	}

 	//one command line argument must be simple audio file name in one of the formats noted above (mp3 NOT supported)
 	public static void main(String args[]) throws Exception
 	{
 		SoundEffect se = new SoundEffect(args[0]);
 	}
}