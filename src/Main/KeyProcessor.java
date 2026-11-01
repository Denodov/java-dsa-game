/* This will handle the "Hot Key" system. */

package Main;

import logic.Control;
import timer.stopWatchX;

public class KeyProcessor{
	// Static Fields
	private static char last = ' ';			// for debouncing purposes
	private static stopWatchX sw = new stopWatchX(250);
	
	// Static Method(s)
	public static void processKey(char key){
		if (key == ' ') {
			Main.trigger = "";     
			last = ' ';            // reset debounce 
			return;
		}
		// debounce 
		if (key == last && !sw.isTimeUp()) return;
		last = key;
		sw.resetWatch();
		switch(key){
		case '%':								// ESC key
			System.exit(0);
			break;
			
		case 'w':
			Main.trigger = "w is triggered";
			break;
		case 'a':
			Main.trigger = "a is triggered";
			break;
		case 's':
			Main.trigger = "s is triggered";
			break;
		case 'd':
			Main.trigger = "d is triggered";
			break;
		case '$': 								// space
			Main.trigger = "space bar is triggered";
		    Main.examineNearbyObject();  
		    break;

		case 'm':
			// For mouse coordinates
			Control.isMouseCoordsDisplayed = !Control.isMouseCoordsDisplayed;
			break;

		}
	}
}
