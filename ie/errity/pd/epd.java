package ie.errity.pd;

import ie.errity.pd.graphics.*;
//Import GUI components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;


/**
 *MAIN class. Contains code which launches an instance of the Evolutionary 
 *Prisoner's Dilemma application.
 *@author Andrew Errity 99086921
 */
public class epd
{	
	/**
	 *MAIN class. Contains code which launches an instance of the Evolutionary 
	 *Prisoner's Dilemma application.
	 *@param args 	command line agruments (not processed)
	 */
	public static void main(String[] args)
	{
	  	//Make sure we have window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
			
		//Create a frame and container for the game panels.
		MenuFrame progFrame = new MenuFrame("Evolutionary Prisoner's Dilemma");
       		
       	// THEMES
		// user selected theme
		MetalTheme theme = new EmeraldTheme();  
		// set the chosen theme
		MetalLookAndFeel.setCurrentTheme(theme);	
		try
		{
		    UIManager.setLookAndFeel(
			UIManager.getCrossPlatformLookAndFeelClassName());
		    SwingUtilities.updateComponentTreeUI(progFrame);
		}
		catch(Exception e){}			
	
	    // Exit when the window is closed.
	    progFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		// Show the program
		progFrame.pack();
		progFrame.setVisible(true);

	}
	
}

