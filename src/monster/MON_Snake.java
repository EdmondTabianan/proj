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
    private int aggroRange = 5; // Tiles - if aggroed and goes beyond this, return to spawn
    private boolean returningToSpawn = false;

    public MON_Snake(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = 2;
        name = "snake";
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
        super.update(); // This enables knockback, invincibility, and animation

        // FIX: Only access player if it exists
        if (gp.player != null) {
            int xDistance = Math.abs(worldX - gp.player.worldX);
            int yDistance = Math.abs(worldY - gp.player.worldY);
            int tileDistanceFromPlayer = (xDistance + yDistance) / gp.TileSize;
            
            // Calculate distance from spawn point
            int xSpawnDistance = Math.abs(worldX - spawnWorldX);
            int ySpawnDistance = Math.abs(worldY - spawnWorldY);
            int tileDistanceFromSpawn = (xSpawnDistance + ySpawnDistance) / gp.TileSize;

            // Check if aggroed and too far from spawn
            if (onPath == true && tileDistanceFromSpawn > aggroRange) {
                returningToSpawn = true;
                onPath = false; // Temporarily disable following to return to spawn
            }
            
            // Check if should start following player
            if (onPath == false && !returningToSpawn && tileDistanceFromPlayer < 3) {
                if (random.nextInt(100) + 1 > 50) {
                    onPath = true;
                }
            }
            
            // If we're at spawn while returning, stop returning
            if (returningToSpawn && tileDistanceFromSpawn <= 1) {
                returningToSpawn = false;
            }
        }
    }
    
    public void setAction() {
        if (returningToSpawn) {
            // Return to spawn point
            int spawnCol = spawnWorldX / gp.TileSize;
            int spawnRow = spawnWorldY / gp.TileSize;
            searchPath(spawnCol, spawnRow);
            
            // If very close to spawn, just move directly
            if (Math.abs(worldX - spawnWorldX) < gp.TileSize && 
                Math.abs(worldY - spawnWorldY) < gp.TileSize) {
                // Direct movement towards spawn
                if (worldX < spawnWorldX) worldX += speed;
                if (worldX > spawnWorldX) worldX -= speed;
                if (worldY < spawnWorldY) worldY += speed;
                if (worldY > spawnWorldY) worldY -= speed;
            }
        }
        else if (onPath == true && gp.player != null) { // FIX: Add null check here
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;
            searchPath(goalCol, goalRow);
        } 
        else {
            actionLockCounter++;
            if (collisionOn == true) {
                changeDirection();
                collisionOn = false;
                actionLockCounter = 0;
                return;
            }
            if(actionLockCounter == 120) {
                changeDirection();
                actionLockCounter = 0;
            }
        }
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
            switch (gp.player.Direction) {
                case "up":    Direction = "down";  break;
                case "down":  Direction = "up";    break;
                case "left":  Direction = "right"; break;
                case "right": Direction = "left";  break;
            }
        }
    }
}