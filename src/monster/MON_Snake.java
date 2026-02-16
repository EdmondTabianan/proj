package monster;

import main.GamePanel;
import object.OBJ_Arrows;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;
import entity.Entity;

import java.util.Random;

public class MON_Snake extends Entity {
    
    GamePanel gp;
    private Random random = new Random();
    
    // Spawn point tracking
    private int spawnWorldX;
    private int spawnWorldY;
    private int aggroRange = 8; // Tiles - increased for better gameplay
    private boolean returningToSpawn = false;
    
    // Pathfinding variables
    private int pathUpdateCounter = 0;
    private final int PATH_UPDATE_DELAY = 60; // Update path every 60 frames (1 second)
    private int aggroCheckCounter = 0;
    private final int AGGRO_CHECK_DELAY = 30; // Check aggro every 30 frames

    public MON_Snake(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_monster;
        name = "Snake";
        action = true;
        defaultSpeed = 2;
        speed = defaultSpeed;
        maxLife = 12;
        life = maxLife;
        attack = 2;
        defense = 1;
        exp = 5;
        
        solidArea.x = 3;
        solidArea.y = 10;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    // Call this after setting worldX/worldY to record spawn point
    public void setSpawnPoint(int worldX, int worldY) {
        this.spawnWorldX = worldX;
        this.spawnWorldY = worldY;
    }

    public void getImage() {
        up1 = setup("/monster/snik_left_1", gp.TileSize, gp.TileSize);
        up2 = setup("/monster/snik_left_2", gp.TileSize, gp.TileSize);
        down1 = setup("/monster/snik_righ_1", gp.TileSize, gp.TileSize);
        down2 = setup("/monster/snik_righ_2", gp.TileSize, gp.TileSize);
        left1 = setup("/monster/snik_left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/monster/snik_left_2", gp.TileSize, gp.TileSize);
        right1 = setup("/monster/snik_righ_1", gp.TileSize, gp.TileSize);
        right2 = setup("/monster/snik_righ_2", gp.TileSize, gp.TileSize);
    }
    
    public void update() {
        // Check aggro FIRST before movement
        checkAggro();
        
        // Then do normal update (which calls setAction() and moves)
        super.update();
    }
    
    private void checkAggro() {
        if (gp.player == null) return;
        
        aggroCheckCounter++;
        if (aggroCheckCounter < AGGRO_CHECK_DELAY) return;
        aggroCheckCounter = 0;
        
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistanceFromPlayer = (xDistance + yDistance) / gp.TileSize;
        
        // Calculate distance from spawn point
        if (spawnWorldX != 0 || spawnWorldY != 0) {
            int xSpawnDistance = Math.abs(worldX - spawnWorldX);
            int ySpawnDistance = Math.abs(worldY - spawnWorldY);
            int tileDistanceFromSpawn = (xSpawnDistance + ySpawnDistance) / gp.TileSize;
        
            // Check if too far from spawn while following
            if (onPath == true && tileDistanceFromSpawn > aggroRange) {
                returningToSpawn = true;
                onPath = false;
                System.out.println("Snake returning to spawn - too far!");
            }
            
            // Check if should start following player
            if (onPath == false && !returningToSpawn && tileDistanceFromPlayer < 4) { // Snake has longer range
                if (random.nextInt(100) + 1 > 40) { // Higher chance to aggro
                    onPath = true;
                    System.out.println("Snake aggroed on player!");
                }
            }
            
            // If we're at spawn while returning, stop returning
            if (returningToSpawn && tileDistanceFromSpawn <= 1) {
                returningToSpawn = false;
                System.out.println("Snake returned to spawn");
            }
        }
    }
    
    public void setAction() {
        if (returningToSpawn && (spawnWorldX != 0 || spawnWorldY != 0)) {
            // Return to spawn point
            int spawnCol = spawnWorldX / gp.TileSize;
            int spawnRow = spawnWorldY / gp.TileSize;
            
            // Only update path periodically
            pathUpdateCounter++;
            if (pathUpdateCounter > PATH_UPDATE_DELAY) {
                boolean pathFound = searchPath(spawnCol, spawnRow);
                if (!pathFound) {
                    // If no path found, move directly towards spawn
                    moveTowards(spawnWorldX, spawnWorldY);
                }
                pathUpdateCounter = 0;
            }
            
            // If very close to spawn, stop returning
            if (Math.abs(worldX - spawnWorldX) < gp.TileSize && 
                Math.abs(worldY - spawnWorldY) < gp.TileSize) {
                returningToSpawn = false;
                onPath = false;
            }
        }
        else if (onPath == true && gp.player != null) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;
            
            // Only update path periodically
            pathUpdateCounter++;
            if (pathUpdateCounter > PATH_UPDATE_DELAY) {
                boolean pathFound = searchPath(goalCol, goalRow);
                if (!pathFound) {
                    // If no path found, move directly towards player
                    moveTowards(gp.player.worldX, gp.player.worldY);
                }
                pathUpdateCounter = 0;
            }
        } 
        else {
            actionLockCounter++;
            
            // Change direction when hitting obstacle
            if (collisionOn == true) {
                changeDirection();
                collisionOn = false;
                actionLockCounter = 0;
                return;
            }
            
            // Change direction every 2 seconds (120 frames)
            if (actionLockCounter >= 120) {
                changeDirection();
                actionLockCounter = 0;
            }
        }
    }
    
    // Helper method to move directly towards a target
    private void moveTowards(int targetX, int targetY) {
        int dx = targetX - worldX;
        int dy = targetY - worldY;
        
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                Direction = "right";
            } else {
                Direction = "left";
            }
        } else {
            if (dy > 0) {
                Direction = "down";
            } else {
                Direction = "up";
            }
        }
    }
    
    @Override
    public boolean searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / gp.TileSize;
        int startRow = (worldY + solidArea.y) / gp.TileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);

        if (gp.pFinder.search() == true) {
            // Get the next node from the path
            if (gp.pFinder.pathList != null && !gp.pFinder.pathList.isEmpty()) {
                int nextX = gp.pFinder.pathList.get(0).col * gp.TileSize;
                int nextY = gp.pFinder.pathList.get(0).row * gp.TileSize;
                
                // Get entity's center position
                int enCenterX = worldX + solidArea.x + solidArea.width/2;
                int enCenterY = worldY + solidArea.y + solidArea.height/2;
                
                // Determine direction based on next node position
                if (Math.abs(enCenterX - (nextX + gp.TileSize/2)) > 5) {
                    if (enCenterX < nextX + gp.TileSize/2) {
                        Direction = "right";
                    } else {
                        Direction = "left";
                    }
                } else if (Math.abs(enCenterY - (nextY + gp.TileSize/2)) > 5) {
                    if (enCenterY < nextY + gp.TileSize/2) {
                        Direction = "down";
                    } else {
                        Direction = "up";
                    }
                } else {
                    // Reached the node, remove it from path
                    gp.pFinder.pathList.remove(0);
                }
                return true;
            }
        }
        return false;
    }
    
    private void changeDirection() {
        int i = random.nextInt(100) + 1;
        if (i <= 25) { 
            Direction = "up"; 
        } else if (i <= 50) { 
            Direction = "down";
        } else if (i <= 75) { 
            Direction = "left";
        } else { 
            Direction = "right";
        }
    }
    
    public void checkDrop() {
        int roll = random.nextInt(100) + 1;
        if (roll < 40) {}
        else if (roll < 60) { dropItem(new OBJ_Coin_Bronze(gp)); }
        else if (roll < 75) { dropItem(new OBJ_Arrows(gp)); }
        else if (roll < 85) { dropItem(new OBJ_Heart(gp)); }
        else if (roll < 93) { dropItem(new OBJ_ManaCrystal(gp)); }
        else if (roll < 98) { dropItem(new OBJ_Potion_Blue(gp)); }
        else { dropItem(new OBJ_Potion_Red(gp)); }
    }

    public void damageReaction() {
        // FIX: Only access player if it exists
        if (gp.player != null) {
            actionLockCounter = 0;
            onPath = true;
            returningToSpawn = false; // Cancel return if damaged
            System.out.println("Snake damaged - aggro!");
            
            // Face away from player when damaged (like a snake recoil)
            switch (gp.player.Direction) {
                case "up":    Direction = "down";  break;
                case "down":  Direction = "up";    break;
                case "left":  Direction = "right"; break;
                case "right": Direction = "left";  break;
            }
        }
    }
}