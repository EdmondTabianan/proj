package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Bato;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Red;

public class MON_GreenSlime extends Entity {

    GamePanel gp;

    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        type = type_monster;
        name = "Green Slime";
        speed = 1;
        maxLife = 5;
        life = maxLife;
        attack = 2;
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
        if (i > 99 && projectiles.alive == false && gp.player.shotAvailableCounter == 30) {
            projectiles.set(worldX, worldY, Direction, true, this);
            gp.projectileList.add(projectiles);
            shotAvailableCounter = 0;
        }
    }
    public void damageReaction() {
        actionLockCounter = 0;
        Direction = gp.player.Direction;
    }
    public void checkDrop() {

        //cast a die 
        int i = new Random().nextInt(100)+1;
        //set the monster drop
        if (i<50) {
            dropItem(new OBJ_Coin_Bronze(gp));
        }
        if (i >=50 && i < 75) {
            dropItem(new OBJ_Heart(gp));
        }
        if (i >=75 && i < 100) {
            dropItem(new OBJ_ManaCrystal(gp));
        }
        if (i == 100) {
            dropItem(new OBJ_Potion_Red(gp));
        }
    }
}

