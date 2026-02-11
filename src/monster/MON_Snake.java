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

public class MON_Snake extends Entity {
    
    GamePanel gp;

    public MON_Snake(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        type = 2;
        name = "snake";
        action = true;

        defaultSpeed = (int)(1.5);
        speed = defaultSpeed;
        maxLife = 12 + gp.player.level * 3;
        life = maxLife;
        attack = 2;
        defense = 1 + gp.player.level / 3;
        exp = 5 + gp.player.level * 2;

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
    
    public void update() {
        super.update();

        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance) / gp.TileSize;

        if (onPath == false && tileDistance < 5) {
            int i = new Random().nextInt(100)+1;
            if (i > 50) {
                onPath = true;
            }
        }
    }
    
    public void setAction() {
        if (onPath == true) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;

            searchPath(goalCol, goalRow);
            
            // Check if close enough to damage player
            int playerDistance = Math.abs(worldX - gp.player.worldX) + Math.abs(worldY - gp.player.worldY);
            if (playerDistance < gp.TileSize * 1.5) {
                // Try to damage player if not invincible
                if (gp.player.Invincible == false && Invincible == false) {
                    // Damage will be handled in Player.contactMonster() method
                }
            }
        } 
        else {
            actionLockCounter++;

            if (collisionOn == true) {
                Random random = new Random();
                int i = random.nextInt(4);

                switch (i) {
                    case 0: Direction = "up"; break;
                    case 1: Direction = "down"; break;
                    case 2: Direction = "left"; break;
                    case 3: Direction = "right"; break;
                }
                collisionOn = false;
                actionLockCounter = 0;
                return;
            }

            if(actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100)+1;
                
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
    }
    
    public void checkDrop() {
        int roll = new Random().nextInt(100)+1;

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

    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
        
        switch (gp.player.Direction) {
            case "up":    Direction = "down";  break;
            case "down":  Direction = "up";    break;
            case "left":  Direction = "right"; break;
            case "right": Direction = "left";  break;
        }
        
    }
}