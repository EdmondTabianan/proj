package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Arrows extends Projectile {

    GamePanel gp;

    public OBJ_Arrows(GamePanel gp) {
        super(gp);
        this.gp = gp;

        // FIX 1: Set correct type for projectiles
        this.type = type_pickupOnly;  // Add this for pickup behavior
        
        this.name = "Arrow";
        this.speed = 6;
        this.maxLife = 80;
        this.life = maxLife;
        this.attack = 4;
        this.useCost = 1;
        this.alive = false;
        this.value = 1;
        this.price = 10;  // Amount given when picked up
        this.knockBackPower = 1;
        this.description = "[" + name + "]\nA sharp arrow to shoot.\nYou can pick up more";
        
        getImage();
    }
    
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
    
    // FIX 2: Fix pickup amount
    public void use(Entity entity) {
        gp.ui.showMessage("You picked up " + amount + " arrows!");
        gp.player.arrow += amount;  // Changed from += 1 to += amount
        gp.playSE(2);
    }
}