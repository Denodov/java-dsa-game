package Data;

public class GameObject {
    private String tag;
    private int x, y, width, height;
    private String description;
    private boolean isSolid;
    private boolean isExaminable;
    
    public GameObject(String tag, int x, int y, int width, int height, String description, boolean isSolid, boolean isExaminable) {
        this.tag = tag;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.description = description;
        this.isSolid = isSolid;
        this.isExaminable = isExaminable;
    }
    
    // getters
    public String getTag() { return tag; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getDescription() { return description; }
    public boolean isSolid() { return isSolid; }
    public boolean isExaminable() { return isExaminable; }
    
    // bounding box collision detection
    public boolean intersects(int px, int py, int pWidth, int pHeight) {
        return px < x + width && px + pWidth > x && py < y + height && py + pHeight > y;
    }
    
    // check if player is nearby and facing the object
    public boolean isNearby(int px, int py, String direction) {
        int distance = 80;
        int playerCenterX = px + 50;
        int playerCenterY = py + 90;
        
        switch(direction) {
            case "right":
                return playerCenterY < y + height + 20 && playerCenterY > y - 20 && 
                       playerCenterX < x && playerCenterX + distance > x;
            case "left":
                return playerCenterY < y + height + 20 && playerCenterY > y - 20 && 
                       playerCenterX > x + width && playerCenterX < x + width + distance;
            case "up":
                return playerCenterX < x + width + 20 && playerCenterX > x - 20 && 
                       playerCenterY > y + height && playerCenterY < y + height + distance;
            case "down":
                return playerCenterX < x + width + 20 && playerCenterX > x - 20 && 
                       playerCenterY < y && playerCenterY + distance > y;
        }
        return false;
    }
}