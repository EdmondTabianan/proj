package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_ice extends Projectile {

    GamePanel gp;

    public OBJ_ice(GamePanel gp) {
        
        super(gp);
        this.gp = gp;

        name = "Ice";
        speed = 6;
        maxLife = 80;
        life = maxLife;
        attack = 3;
        useCost = 1;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1 = setup("/projectile/ice_up_down", gp.TileSize, gp.TileSize);
        up2 = setup("/projectile/ice_up_down", gp.TileSize, gp.TileSize);
        down1 = setup("/projectile/ice_up_down", gp.TileSize, gp.TileSize);
        down2 = setup("/projectile/ice_up_down", gp.TileSize, gp.TileSize);
        left1 = setup("/projectile/ice_left_right", gp.TileSize, gp.TileSize);
        left2 = setup("/projectile/ice_left_right", gp.TileSize, gp.TileSize);
        right1 = setup("/projectile/ice_left_right", gp.TileSize, gp.TileSize);
        right2 = setup("/projectile/ice_left_right", gp.TileSize, gp.TileSize);

    }
    public boolean haveResource(Entity user) {
        boolean haveResource = false;
        if (user.mana >= useCost) {
            haveResource = true;
        }
        return haveResource;
    }
    public void SubtractResource(Entity user) {
        user.mana -= useCost;
    }
    public void use(Entity entity) {
        
        gp.ui.showMessage("+" + value + " life!");
        entity.mana += value;
        gp.playSE(2);
    }
}
