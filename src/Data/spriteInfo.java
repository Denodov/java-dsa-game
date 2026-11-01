/* This is a way to pass a sprite's key information in one entity. (x, y, tag) */

package Data;

public class spriteInfo {
	// Fields
    // private class fields to store x, y and tag (String) values 
	private Vector2D v2d;
	private String tag;
	// Constructor
	public spriteInfo(Vector2D v2d, String tag){
     // Save the constructor parameters into class fields
		this.v2d = v2d;
		this.tag = tag;
	}
	
	// Methods
	public String getTag(){
		return tag;
	}
	
	public Vector2D getCoords(){
		return v2d;
	}
	
	public void setTag(String newTag){
		this.tag = newTag;
	}
	
	public void setCoords(Vector2D newV2D){
		this.v2d = newV2D;
	}
	
	public void setCoords(int x, int y) {
		v2d.setX(x);
		v2d.setY(y);
	}
	@Override
	public String toString() {
		// "Assume an x, y of 100, 50 and a tag of "star", this should return: [100, 50, star]
		return "[" + v2d.getX() + ", " + v2d.getY() + ", " + tag + "]";
	}
}
