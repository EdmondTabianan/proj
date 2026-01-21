package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_snik extends Entity {
    GamePanel gp;

    public MON_snik(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        type = 2;
        name = "snik";
        speed = 1;
        maxLife = 10;
        life = maxLife;
        attack = 1;
        defense = 0;
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
        up1 = setup("/snik/snik_left_1", gp.TileSize, gp.TileSize);
        up2 = setup("/snik/snik_left_2", gp.TileSize, gp.TileSize);
        down1 = setup("/snik/snik_righ_1", gp.TileSize, gp.TileSize);
        down2 = setup("/snik/snik_righ_2", gp.TileSize, gp.TileSize);
        left1 = setup("/snik/snik_left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/snik/snik_left_2", gp.TileSize, gp.TileSize);
        right1 = setup("/snik/snik_righ_1", gp.TileSize, gp.TileSize);
        right2 = setup("/snik/snik_righ_2", gp.TileSize, gp.TileSize);
        
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
    public void damageReaction() {
        actionLockCounter = 0;
        Direction = gp.player.Direction;
    }
}
