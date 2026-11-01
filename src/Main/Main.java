package Main;

import java.awt.Color;
import java.util.ArrayList;
import timer.stopWatchX;
import logic.Control;
import Data.GameObject;

public class Main {
    public static stopWatchX timer = new stopWatchX(200);
    public static int currentSpriteIndex = 0;
    public static int characterX = 100; 
    public static int characterY = 300;  
    public static int moveSpeed = 5; 
    
    public static String trigger = "";
    
    // four-direction animation ArrayLists
    public static ArrayList<String> rightAnimTags = new ArrayList<>();
    public static ArrayList<String> leftAnimTags = new ArrayList<>();
    public static ArrayList<String> idleAnimTags = new ArrayList<>();
    public static ArrayList<String> upAnimTags = new ArrayList<>();
    public static ArrayList<String> downAnimTags = new ArrayList<>();
    public static ArrayList<String> currentAnimation = new ArrayList<>();
    
    public static String currentDirection = "idle";
    public static String lastDirection = "right"; 
    
    public static String backgroundTag = "background"; 

    // game objects, collection of bounding boxes
    public static ArrayList<GameObject> gameObjects = new ArrayList<>();
    public static String examinationText = "";
    public static stopWatchX examineTimer = new stopWatchX(3000);
    public static boolean isExamining = false;
    
    // End Static fields...

    public static void main(String[] args) {
        Control ctrl = new Control();
        ctrl.gameLoop();
    }

    public static void start(){
        // clear any existing data
        rightAnimTags.clear();
        leftAnimTags.clear();
        idleAnimTags.clear();
        upAnimTags.clear();
        downAnimTags.clear();
        gameObjects.clear();
        currentSpriteIndex = 0;
        
        // animation arrays for each direction
        String[] rightTags = {"a", "b", "c", "d", "e", "f", "g"};
        String[] leftTags = {"h", "i", "j", "k", "l", "m", "n"};
        String[] idleTags = {"o"};
        String[] upTags = {"p", "q", "r"};
        String[] downTags = {"s", "t", "u", "v", "w", "x"};
        
        // populate animation ArrayLists
        for(String tag : rightTags) rightAnimTags.add(tag);
        for(String tag : leftTags) leftAnimTags.add(tag);
        for(String tag : idleTags) idleAnimTags.add(tag);
        for(String tag : upTags) upAnimTags.add(tag);
        for(String tag : downTags) downAnimTags.add(tag);
        
        // start game with idle sprite
        currentAnimation = idleAnimTags;
        currentDirection = "idle";
        lastDirection = "right";
        
        characterX = 100;
        characterY = 300;
        
        backgroundTag = "bg";
        
        // add game objects with bounding box collision
        // format: tag, x, y, width, height, description, isSolid, isExaminable
        gameObjects.add(new GameObject("chest", 490, 236, 43, 32, 
            "A wooden chest with gold bindings. It appears to be locked.", true, true));
        gameObjects.add(new GameObject("barrel", 800, 400, 32, 40, 
            "There must be a dwarf in here...", true, true));
        
        timer.resetWatch();
        examinationText = "";
        isExamining = false;
    }
    
    // method to examine nearby objects when space bar is pressed
    public static void examineNearbyObject() {
        for(GameObject obj : gameObjects) {
            if(obj.isExaminable() && obj.isNearby(characterX, characterY, lastDirection)) {
                examinationText = obj.getDescription();
                isExamining = true;
                examineTimer.resetWatch();
                return;
            }
        }
    }

    public static void update(Control ctrl) {
        // draw background FIRST
        ctrl.addSpriteToFrontBuffer(0, 0, backgroundTag);
        
        // draw all game objects
        for(GameObject obj : gameObjects) {
            ctrl.addSpriteToFrontBuffer(obj.getX(), obj.getY(), obj.getTag());
        }
        
        // store previous position for collision detection
        int previousX = characterX;
        int previousY = characterY;
        
        // handle WASD movement and set appropriate animation
        switch(trigger) {
            case "w is triggered":
                characterY -= moveSpeed;
                if(!currentDirection.equals("up")) {
                    currentAnimation = upAnimTags;
                    currentDirection = "up";
                    lastDirection = "up";
                    currentSpriteIndex = 0;
                }
                break;
            case "a is triggered":
                characterX -= moveSpeed;
                if(!currentDirection.equals("left")) {
                    currentAnimation = leftAnimTags;
                    currentDirection = "left";
                    lastDirection = "left";
                    currentSpriteIndex = 0;
                }
                break;
            case "s is triggered":
                characterY += moveSpeed;
                if(!currentDirection.equals("down")) {
                    currentAnimation = downAnimTags;
                    currentDirection = "down";
                    lastDirection = "down";
                    currentSpriteIndex = 0;
                }
                break;
            case "d is triggered":
                characterX += moveSpeed;
                if(!currentDirection.equals("right")) {
                    currentAnimation = rightAnimTags;
                    currentDirection = "right";
                    lastDirection = "right";
                    currentSpriteIndex = 0;
                }
                break;
            default:
                if(!currentDirection.equals("idle")) {
                    currentAnimation = idleAnimTags;
                    currentDirection = "idle";
                    currentSpriteIndex = 0;
                }
                break;
        }
        
        // character hitbox for collision detection
        int characterHitboxX = characterX + 50;
        int characterHitboxY = characterY + 90;
        int characterHitboxWidth = 30;
        int characterHitboxHeight = 20;
        
        // check collision with objects
        for(GameObject obj : gameObjects) {
            if(obj.isSolid() && obj.intersects(characterHitboxX, characterHitboxY, characterHitboxWidth, characterHitboxHeight)) {
                // Collision detected - revert to previous position
                characterX = previousX;
                characterY = previousY;
                break;
            }
        }
        
        // keep character within screen bounds (wall collision)
        if(characterX < -6) characterX = -6;
        if(characterX > 1160) characterX = 1160;
        if(characterY < -55) characterY = -55;
        if(characterY > 610) characterY = 610;
        
        // draw the current sprite from current animation
        if(currentAnimation != null && currentAnimation.size() > 0) {
            if(currentSpriteIndex >= currentAnimation.size()) {
                currentSpriteIndex = 0;
            }
            
            if(currentSpriteIndex >= 0 && currentSpriteIndex < currentAnimation.size()) {
                String currentTag = currentAnimation.get(currentSpriteIndex);
                ctrl.addSpriteToFrontBuffer(characterX, characterY, currentTag);
            }
        }
        
        // animate sprite cycling based on timer
        if(timer.isTimeUp()) {
            if(currentAnimation != null && currentAnimation.size() > 0) {
                currentSpriteIndex++;
                
                if(currentSpriteIndex >= currentAnimation.size()) {
                    currentSpriteIndex = 0;
                }
            }
            
            timer.resetWatch();
        }
        
        // display examination text (follows character above his head)
        if(isExamining) {
            int examineX = characterX - 100;
            int examineY = characterY - 5;
            
            ctrl.drawString(examineX, examineY, examinationText, Color.YELLOW);
            
            if(examineTimer.isTimeUp()) {
                isExamining = false;
                examinationText = "";
            }
        }
    }
}
