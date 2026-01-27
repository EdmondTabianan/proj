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

    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        type = type_monster;
        name = "Green Slime";
        speed = 1;
        maxLife = 8 + gp.player.level * 2;
        life = maxLife;
        attack = 1 + gp.player.level / 3;
        defense = gp.player.level / 4;
        exp = 3 + gp.player.level;

        projectiles = new OBJ_Bato(gp);

        solidArea.x = 3;
        solidArea.y = 10;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
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
    public void setAction() {
        // Calculate distance to player
        int playerDistanceX = Math.abs(worldX - gp.player.worldX);
        int playerDistanceY = Math.abs(worldY - gp.player.worldY);
    
        // Check if player is within 5 TILESIZE distance (Manhattan distance)
        boolean isPlayerNear = (playerDistanceX <= gp.TileSize * 5 && playerDistanceY == 0) || 
                            (playerDistanceY <= gp.TileSize * 5 && playerDistanceX == 0);
    
        if (isPlayerNear) {
            // Move toward player (no diagonal)
            if (playerDistanceX > playerDistanceY) {
                // Horizontal movement
                if (gp.player.worldX > worldX) {
                    Direction = "right";
                } else if (gp.player.worldX < worldX) {
                    Direction = "left";
                }
            } else {
                // Vertical movement
                if (gp.player.worldY > worldY) {
                    Direction = "down";
                } else if (gp.player.worldY < worldY) {
                    Direction = "up";
                }
            }
            
            // IMPORTANT: Reset actionLockCounter when chasing player
            // So random movement doesn't interfere
            actionLockCounter = 0;
        } 
        
        // Random movement timer
        actionLockCounter++;
        
        // Check for random movement (exactly like your working version)
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // pick up number from 1 - 100
            
            if (i <= 25) {
                Direction = "up";
            }
            if (i >= 25 && i <= 50) {
                Direction = "down";
            } 
            if (i >= 50 && i <= 75) {
                Direction = "left";
            }
            if (i >= 75 && i <= 100) {
                Direction = "right";
            }
            actionLockCounter = 0;
        }
        
        // Projectile shooting logic
        int shootChance = new Random().nextInt(100) + 1;
        if (shootChance > 99 && projectiles.alive == false && shotAvailableCounter == 30) {
            projectiles.set(worldX, worldY, Direction, true, this);
            gp.projectileList.add(projectiles);
            shotAvailableCounter = 0;
        }
    }
    public void damageReaction() {
    
        actionLockCounter = 0;
        switch (gp.player.Direction) {
            case "up":    Direction = "down";  break;
            case "down":  Direction = "up";    break;
            case "left":  Direction = "right"; break;
            case "right": Direction = "left";  break;
        }

        int knockBackDistance = 40;

        // Calculate how far we can actually move without hitting solid tile OR other entities
        int actualDistance = knockBackDistance;

        // Test each possible distance from smallest to largest
        for (int testDistance = 5; testDistance <= knockBackDistance; testDistance += 5) {
            // Temporarily move to test position
            int tempX = worldX;
            int tempY = worldY;
            
            switch (gp.player.Direction) {
                case "up":    tempY -= testDistance; break;
                case "down":  tempY += testDistance; break;
                case "left":  tempX -= testDistance; break;
                case "right": tempX += testDistance; break;
            }
            
            // Store original position
            int originalX = worldX;
            int originalY = worldY;
            
            // Test collision at this distance
            worldX = tempX;
            worldY = tempY;
            collisionOn = false;
            
            // Check for tile collisions
            gp.cChecker.checkTile(this);
            
            // Check for entity collisions (with other monsters)
            if (!collisionOn) {
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                if (monsterIndex != 999) {
                    // Found collision with another monster
                    collisionOn = true;
                }
            }
            
            // Restore position
            worldX = originalX;
            worldY = originalY;
            
            if (collisionOn) {
                // Can't move this far, use previous valid distance
                actualDistance = testDistance - 5;
                break;
            } else {
                actualDistance = testDistance;
            }
        }

        // Apply the actual possible knockback distance
        switch (gp.player.Direction) {
            case "up":    worldY -= actualDistance; break;
            case "down":  worldY += actualDistance; break;
            case "left":  worldX -= actualDistance; break;
            case "right": worldX += actualDistance; break;
        }

        
    }
    public void checkDrop() {

        int roll = new Random().nextInt(100)+1; // 0–99

        if (roll < 40) {
            // no drop (40%)
        }
        else if (roll < 60) {
            dropItem(new OBJ_Coin_Bronze(gp));     // 20%
        }
        else if (roll < 75) {
            dropItem(new OBJ_Arrows(gp));      // 15%
        }
        else if (roll < 85) {
            dropItem(new OBJ_Heart(gp));         // 10%
        }
        else if (roll < 93) {
            dropItem(new OBJ_ManaCrystal(gp));   // 8%
        }
        else if (roll < 98) {
            dropItem(new OBJ_Potion_Blue(gp));   // 5%
        }
        else if (roll < 100) {
            dropItem(new OBJ_Potion_Red(gp));    // 2%
        }

    }
}

