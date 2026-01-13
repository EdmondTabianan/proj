package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Arrows extends Projectile {

    GamePanel gp;

    public OBJ_Arrows(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Arrow";
        speed = 10;
        maxLife = 160;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1 = setup("/projectile/arrow_up_1", gp.TileSize, gp.TileSize);
        up2 = setup("/projectile/arrow_up_1", gp.TileSize, gp.TileSize);
        down1 = setup("/projectile/arrow_down_1", gp.TileSize, gp.TileSize);
        down2 = setup("/projectile/arrow_down_1", gp.TileSize, gp.TileSize);
        left1 = setup("/projectile/arrow_left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/projectile/arrow_left_1", gp.TileSize, gp.TileSize);
        right1 = setup("/projectile/arrow_right_1", gp.TileSize, gp.TileSize);
        right2 = setup("/projectile/arrow_right_1", gp.TileSize, gp.TileSize);

    }
    
}
