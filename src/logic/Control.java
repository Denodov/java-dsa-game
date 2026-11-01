package logic;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Data.Sprite;
import Data.gameString;
import FileIO.EZFileRead;
import Graphics.Graphic;
import Graphics.Sprites;
import Input.Keyb;
import Main.KeyProcessor;
import Main.Main;
import gameloop.gameLoop;

public class Control{
	// Fields
	private Graphic graphic;
	private Sprites backbuffer;
	private Sprites frontbuffer;
	private Sprites overlaybuffer;
	private ArrayList<gameString> gs;
	private Keyb kb;
	private gameLoop gl;
	private Font font;
	public static boolean isMouseCoordsDisplayed;
	
	// Constructor
	public Control(){
		graphic = new Graphic();				// Create the 1280 x 720 game window
		backbuffer = new Sprites();				// Create place to store game ALL game sprites needed for project (images)
		frontbuffer = new Sprites();			// This is the "front buffer" that goes to the renderer every frame
		overlaybuffer = new Sprites();			// Allows things above the frontbuffer (overlays or draws on top of...things like custom mouse cursors, etc.)
		gs = new ArrayList<>();					// Set up for gamestrings (graphical text printing; System.out.println doesn't work in graphics mode)
		setupFont();							// Set up program to use a font custom (stored in "Font" subfolder)
		loadArtIntoBackBuffer();				// Loads the art referenced in "Art.txt" into the backbuffer of sprites
		kb = new Keyb();						// Initialize the keyboard handler
		gl = new gameLoop(graphic, gs, frontbuffer, overlaybuffer);		// Sets up render loop
		graphic.setKeyListener(kb);										// Sets graphics handler up to listen for keyboard input (Asynchronous)
		graphic.addFocusListener(kb);
		Main.start();
	}
	
	// Methods
	public void gameLoop(){
		final long frameNanos = 1_000_000_000L / 60;
		long nextFrame = System.nanoTime();
		while(!Thread.currentThread().isInterrupted()){
			char key = kb.getInputCodeX();
			KeyProcessor.processKey(key);										// Check for keyboard key press
			Main.update(this);													
			if(isMouseCoordsDisplayed)
				drawString(1110, 20, getMouseCoords(), Color.white);
			gl.run(); 															// Render the graphical data for the frame
			frontbuffer.clearSprites(); 										// Clear the front sprite buffer for the next frame
			overlaybuffer.clearSprites(); 										// Clears the overlay buffer for the next frame
			gs.clear(); 														// Clears all of the gameStrings for that frame
			nextFrame += frameNanos;
			long remaining = nextFrame - System.nanoTime();
			if(remaining > 0){
				try {
					Thread.sleep(remaining / 1_000_000L, (int)(remaining % 1_000_000L));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			} else {
				// Avoid a burst of catch-up frames after a slow frame.
				nextFrame = System.nanoTime();
			}
		}
	}
	
	public void drawString(int x, int y, String text, Color c){
		gs.add(new gameString(c, x, y, text, font, 1.0f));
	}
	
	public void addSpriteToFrontBuffer(int x, int y, String spriteTag){
		Sprite s = backbuffer.getSpriteByTag(spriteTag);
		s.moveXAbsolute(x);
		s.moveYAbsolute(y);
		frontbuffer.addSprite(s);
	}
	
	private void loadArtIntoBackBuffer(){
		EZFileRead ezr = new EZFileRead("Art.txt");
		for(int i = 0; i < ezr.getNumLines(); i++){
			String raw = ezr.getLine(i);
			StringTokenizer st = new StringTokenizer(raw, "*");
			String file = st.nextToken();
			String tag = st.nextToken();
			backbuffer.addSprite(new Sprite(0, 0, file, tag));
		}
	}
	
	private void setupFont(){
		String fontFile = "Font/Jipatha-Regular.ttf";
		float fontSize = 32f;
		font = null;
		try {
	           font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFile)).deriveFont(fontSize);
	           GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	           ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontFile)));
	       }catch (Exception e) {e.printStackTrace();}
	}
	
	private static String getMouseCoords(){
		Point p = MouseInfo.getPointerInfo().getLocation();
		int x1 = (int) p.getX();
		int y1 = (int) p.getY();
		return "X: " + x1 + " , Y: " + y1;
	}
}
