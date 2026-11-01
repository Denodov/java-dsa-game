package Graphics;

import java.awt.BufferCapabilities;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import FileIO.EZFileWrite;

@SuppressWarnings("serial")
public class Graphic extends JFrame{
	// Fields
	private int _x, _y;
	
	// Constructor
	public Graphic(){
		init();
	}
	
	// Methods
	public int getHeight(){
		return _y;
	}
	
	public int getWidth(){
		return _x;
	}
	
	public Graphics getGraph(){
		return this.getBufferStrategy().getDrawGraphics();
	}
	
	public void setMouseListener(MouseListener m){
		if(m == null)	errorClose("Null pointer in Mouse Listener...exiting"); //$NON-NLS-1$
		addMouseListener(m);
	}
	
	public void setKeyListener(KeyListener k){
		if(k == null)	errorClose("Null pointer in Key Listener...exiting"); //$NON-NLS-1$
		addKeyListener(k);
	}
	
	public void hideCursor(){
		BufferedImage cursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, 
				new Point(0,0), "blank cursor"); //$NON-NLS-1$
		getContentPane().setCursor(blankCursor);
	}
	
	public void errorClose(String msg){
		dispose();
		JOptionPane.showMessageDialog(null, msg);
		System.exit(0);
	}
	
	public void normalClose(){
		dispose();
		System.exit(0);
	}
	
	private void init(){
		_x = 1280;
		_y = 720;
		// Keep drawing coordinates aligned with the window without changing monitor modes.
		setUndecorated(true);
		setTitle("Data Structures Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setIgnoreRepaint(true);
		setSize(_x, _y);
		setLocationRelativeTo(null);
		setVisible(true);
        // Create double buffering
		createBufferStrategy(2);						
		BufferCapabilities bc = this.getBufferStrategy().getCapabilities();
		String graphLog = "";
		if(bc.isPageFlipping() == false)
			graphLog = "Page flipping not supported...";
		else
			graphLog = "Page flipping working!";
		// Save to graphics log...
		EZFileWrite ezw = new EZFileWrite("Scripts/logs/pf.txt");
		ezw.writeLine(graphLog);
		ezw.saveFile();
	}
	
	/* New image scaling algorithm */
	public static BufferedImage scale(BufferedImage sbi, double scale) {
	    BufferedImage dbi = null;
	    if(sbi != null) {
	        dbi = new BufferedImage(sbi.getWidth(), sbi.getHeight(), sbi.getType());
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
	        g.drawRenderedImage(sbi, at);
	    }
	    return dbi;
	}
	
	/* A method to return a rotated BufferedImage */
	public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        //at.translate((newWidth - w) / 2, (newHeight - h) / 2); // Expand canvas

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);

        return rotated;
    }
	
	/* Get a piece of another image */
	public static BufferedImage getCroppedImage(BufferedImage image, int x, int y, int width, int height){
		BufferedImage ret = image.getSubimage(x, y, width, height);
		return ret;
	}
}
