package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Bato extends Projectile{

    GamePanel gp;

    public OBJ_Bato(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Bato";
        speed = 4;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
        
    }

    public void getImage() {
        up1 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        up2 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        up3 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        down1 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        down2 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        down3 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        left1 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        left2 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        left3 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        right1 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        right2 = setup("/projectile/bato", gp.TileSize, gp.TileSize);
        right3 = setup("/projectile/bato", gp.TileSize, gp.TileSize);

    }
}
