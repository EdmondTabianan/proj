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
        maxLife = 10;
        life = maxLife;
        attack = 1;
        defense = 0;
        exp = 2;
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
        actionLockCounter++;

        if(actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100)+1; //pick up numbner from 1 - 100
            
            if (i <=25) {
                Direction = "up";
            }
            if (i >=25 && i <= 50) {
                Direction = "down";
            } 
            if (i >=50  && i <= 75) {
                Direction = "left";
            }
            if (i >= 75 && i <= 100) {
                Direction = "right";
            }
            actionLockCounter = 0;
        }
        int i = new Random().nextInt(100)+1;
        //if (i > 99 && projectile.alive == false && shotAvailableCounter == 30) {
        if (i > 99 && projectiles.alive == false && shotAvailableCounter == 30) {
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

            int knockBackDistance = 40; // Adjust this value for "strength"

            // Calculate how far we can actually move without hitting solid tile
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
                gp.cChecker.checkTile(this);
                
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

        //cast a die/dice
        int dropRate = new Random().nextInt(100)+1;
        //set the monster drop
        if (dropRate<50) {
            dropItem(new OBJ_Arrows(gp));
        } 
        if (dropRate >= 50 && dropRate < 60 ) {
            dropItem(new OBJ_Arrows(gp));
        }
        if (dropRate >=60 && dropRate < 75) {
            dropItem(new OBJ_Coin_Bronze(gp));
        }
        if (dropRate >=75 && dropRate < 85) {
            dropItem(new OBJ_Heart(gp));
        }
        if (dropRate >= 85 && dropRate < 99) {
            dropItem(new OBJ_ManaCrystal(gp));
        }
        if (dropRate == 99) {
            dropItem(new OBJ_Potion_Blue(gp));
        }
        if (dropRate == 100) {
            dropItem(new OBJ_Potion_Red(gp));
        }
    }

}

