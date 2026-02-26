package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_ice extends Projectile {

    GamePanel gp;

    public OBJ_ice(GamePanel gp) {
        super(gp);
        this.gp = gp;
    
        this.type = type_wand;
        this.name = "Ice";
        this.speed = defaultSpeed;
        this.maxLife = 80;
        this.life = maxLife;
        this.defaultSpeed = 5;
        this.attack = gp.player.attack + 2;
        this.useCost = 1;
        this.alive = false;
        this.knockBackPower = 0;
        this.value = 2;  // Mana restoration value
    
        // Slow effect properties
        this.slowDuration = 180;  // 3 seconds at 60 FPS
        this.slowAmount = 2;      // Reduce speed by 2
    
        // --- SHRINK COLLISION BOX ---
        solidArea = new java.awt.Rectangle();
        solidArea.x = 12;           // offset inside sprite
        solidArea.y = 12;
        solidArea.width = 18;      // smaller than full tile
        solidArea.height = 18;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    
        getImage();
    }
    

    public void getImage() {
        up1 = setup("/projectile/ice_up", gp.TileSize, gp.TileSize);
        up2 = setup("/projectile/ice_up_2", gp.TileSize, gp.TileSize);
        up3 = setup("/projectile/ice_up_3", gp.TileSize, gp.TileSize);
        down1 = setup("/projectile/ice_down", gp.TileSize, gp.TileSize);
        down2 = setup("/projectile/ice_down_2", gp.TileSize, gp.TileSize);
        down3 = setup("/projectile/ice_down_3", gp.TileSize, gp.TileSize);
        left1 = setup("/projectile/ice_left", gp.TileSize, gp.TileSize);
        left2 = setup("/projectile/ice_left_2", gp.TileSize, gp.TileSize);
        left3 = setup("/projectile/ice_left_3", gp.TileSize, gp.TileSize);
        right1 = setup("/projectile/ice_right", gp.TileSize, gp.TileSize);
        right2 = setup("/projectile/ice_right_2", gp.TileSize, gp.TileSize);
        right3 = setup("/projectile/ice_right_3", gp.TileSize, gp.TileSize);
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
    
    // This is for picking up ice crystals
    public void use(Entity entity) {
        gp.ui.showMessage("+" + value + " mana!");
        entity.mana += value;
        
        if (entity.mana > entity.maxMana) {
            entity.mana = entity.maxMana;
        }
        
        gp.playSE(2);
    }
}