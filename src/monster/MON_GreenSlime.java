package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Arrows;
import object.OBJ_Bato;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;

public class MON_GreenSlime extends Entity {

    GamePanel gp;
    
    // Spawn point tracking
    private int spawnWorldX;
    private int spawnWorldY;
    private int aggroRange = 5; // Tiles - if aggroed and goes beyond this, return to spawn
    private boolean returningToSpawn = false;

    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        type = type_monster;
        name = "Green Slime";
        action = true;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 8;
        life = maxLife;
        attack = 1 ;
        defense = 0;
        exp = 3;

        projectiles = new OBJ_Bato(gp);

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
        up1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        up2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        down1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        down2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        left1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        left2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        right1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        right2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
    }
    
    public void update() {
        super.update();
        
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
                int i = new Random().nextInt(100) + 1;
                if (i > 50) {
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
            // FOLLOW PLAYER
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;
    
            searchPath(goalCol, goalRow);
            
            // Shooting logic
            shotAvailableCounter++;
            
            if (shotAvailableCounter >= 120) {
                int shootChance = new Random().nextInt(100) + 1;
                if (shootChance > 70) {
                    projectiles.set(worldX, worldY, Direction, true, this);
                    for (int ii = 0; ii < gp.projectile[1].length; ii++) {
                        if (gp.projectile[gp.currentMap][ii] == null) {
                            gp.projectile[gp.currentMap][ii] = projectiles;
                            break;
                        }
                    }
                    shotAvailableCounter = 0;
                } else {
                    shotAvailableCounter = 90;
                }
            }
        } 
        else {
            // RANDOM MOVEMENT
            actionLockCounter++;
    
            // Change direction when hitting obstacle
            if (collisionOn == true) {
                getRandomDirection();
                collisionOn = false;
                actionLockCounter = 0;
                return;
            }
    
            // Change direction every 2 seconds (120 frames)
            if (actionLockCounter >= 120) {
                getRandomDirection();
                actionLockCounter = 0;
            }
            
            // Increment cooldown when not on path
            if (shotAvailableCounter < 90) {
                shotAvailableCounter++;
            }
        }
    }
    
    // Helper method for random direction with equal probability
    private void getRandomDirection() {
        Random random = new Random();
        int i = random.nextInt(4); // 0-3
        
        switch (i) {
            case 0: Direction = "up"; break;
            case 1: Direction = "down"; break;
            case 2: Direction = "left"; break;
            case 3: Direction = "right"; break;
        }
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
    
    public void checkDrop() {
        int roll = new Random().nextInt(100) + 1;

        if (roll < 40) {
            // no drop (40%)
        }
        else if (roll < 60) {
            dropItem(new OBJ_Coin_Bronze(gp));     // 20%
        }
        else if (roll < 75) {
            dropItem(new OBJ_Arrows(gp));          // 15%
        }
        else if (roll < 85) {
            dropItem(new OBJ_Heart(gp));           // 10%
        }
        else if (roll < 93) {
            dropItem(new OBJ_ManaCrystal(gp));     // 8%
        }
        else if (roll < 98) {
            dropItem(new OBJ_Potion_Blue(gp));     // 5%
        }
        else if (roll < 100) {
            dropItem(new OBJ_Potion_Red(gp));      // 2%
        }
    }
}