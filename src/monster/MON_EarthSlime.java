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

public class MON_EarthSlime extends Entity {

    GamePanel gp;

    public MON_EarthSlime(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        action = true;
        type = type_monster;
        name = "Earth Slime";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 8;

        
        life = maxLife;
        attack = 1;
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

    public void getImage() {
        up1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        up2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        down1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        down2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        left1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        left2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        right1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        right2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        slowEffectImage = setup("/effects/slow_effect", gp.TileSize, gp.TileSize);
    }

    public void setAction() {
    
        if (onPath == true) {

            //check if stop chasing
            checkStopChasingOrNot(gp.player, 15, 100);
        
            // search direction to go
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
            
           checkShootOrNot(200, 30);
        }
        else {
            //check if start chasing 
            checkStartChasingOrNot(gp.player, 5, 100);
    
            getRandomDirection();
            
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