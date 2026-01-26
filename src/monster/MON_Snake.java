package monster;

import main.GamePanel;
import object.OBJ_Arrows;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;

import java.util.Random;

import entity.Entity;

public class MON_Snake extends Entity{
    
    GamePanel gp;

    public MON_Snake(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        type = 2;
        name = "snake";
        speed = 1;
        maxLife = 15;
        life = maxLife;
        attack = 1;
        defense = 2;
        exp = 2;

        solidArea.x = 3;
        solidArea.y = 10;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
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
    public void damageReaction() {
        
        

        int knockBackDistance = 40; 

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
}
