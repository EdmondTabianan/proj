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

public class MON_MOMMY extends Entity {

    GamePanel gp;

    public MON_MOMMY(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        type = type_monster;
        name = "Mummy";
        action = true;

        defaultSpeed = 1;
        speed = defaultSpeed;
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
        up1 = setup("/monster/mummy_up_1", gp.TileSize, gp.TileSize);
        up2 = setup("/monster/mummy_up_2", gp.TileSize, gp.TileSize);
        down1 = setup("/monster/mummy_down_1", gp.TileSize, gp.TileSize);
        down2 = setup("/monster/mummy_down_2", gp.TileSize, gp.TileSize);
        left1 = setup("/monster/mummy_left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/monster/mummy_left_2", gp.TileSize, gp.TileSize);
        right1 = setup("/monster/mummy_right_1", gp.TileSize, gp.TileSize);
        right2 = setup("/monster/mummy_right_2", gp.TileSize, gp.TileSize);
        
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
            
            // Increment cooldown counter
            shotAvailableCounter++;
            
            // Check if cooldown is ready (2 seconds = 120 frames at 60 FPS)
            if (shotAvailableCounter >= 120) {
                
                int shootChance = new Random().nextInt(100) + 1;
                if (shootChance > 70) {
                    projectiles.set(worldX, worldY, Direction, true, this);
                    // check vacancy before adding
                    for (int ii = 0; ii < gp.projectile[1].length; ii++) {
                        if (gp.projectile[gp.currentMap][ii] == null) {
                            gp.projectile[gp.currentMap][ii] = projectiles;
                            break;
                        }
                    }
                    shotAvailableCounter = 0; // Reset cooldown
                } else {
                    // If chance fails, wait a bit before next check
                    shotAvailableCounter = 90; // Give 0.5 second pause
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
            
            // Also increment cooldown when not on path
            if (shotAvailableCounter < 90) {
                shotAvailableCounter++;
            }
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
