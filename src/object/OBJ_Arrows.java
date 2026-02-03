package object;


import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Arrows extends Projectile {

    GamePanel gp;

    public OBJ_Arrows(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickupOnly;
        name = "Arrow";
        speed = 6;
        maxLife = 80;
        life = maxLife;
        attack = 4;
        useCost = 1;
        alive = false;
        value = 1;
        amount = 10;
        description = "[" + name + "]\nA sharp arrow to shoot.\nYou can pick up more";
        getImage();
    }
    // edmond tabianan
    public void getImage() {
        up1 = setup("/projectile/arrow_up_1", gp.TileSize, gp.TileSize);
        up2 = setup("/projectile/arrow_up_2", gp.TileSize, gp.TileSize);
        up3 = setup("/projectile/arrow_up_1", gp.TileSize, gp.TileSize);
        down1 = setup("/projectile/arrow_down_1", gp.TileSize, gp.TileSize);
        down2 = setup("/projectile/arrow_down_2", gp.TileSize, gp.TileSize);
        down3 = setup("/projectile/arrow_down_1", gp.TileSize, gp.TileSize);
        left1 = setup("/projectile/arrow_left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/projectile/arrow_left_2", gp.TileSize, gp.TileSize);
        left3 = setup("/projectile/arrow_left_1", gp.TileSize, gp.TileSize);
        right1 = setup("/projectile/arrow_right_1", gp.TileSize, gp.TileSize);
        right2 = setup("/projectile/arrow_right_2", gp.TileSize, gp.TileSize);
        right3 = setup("/projectile/arrow_right_1", gp.TileSize, gp.TileSize);

    }
    public boolean haveResource(Entity user) {
        boolean haveResource = false;
        if (user.arrow >= useCost) {
            haveResource = true;
        }
        return haveResource;
    }
    public void SubtractResource(Entity user) {
        user.arrow -= useCost;
    }
    public void use(Entity entity) {
        gp.ui.showMessage("You pick " + value + " arrows");
        gp.player.arrow += 1;
        gp.playSE(2);
    }
}
