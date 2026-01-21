package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {

    GamePanel gp;

    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickupOnly;
        name = "mana Crystal";
        value = 1;
        down1 = setup("/objects/manacrystal_full", gp.TileSize, gp.TileSize);
        image = setup("/objects/manacrystal_full", gp.TileSize, gp.TileSize);
        image2 = setup("/objects/manacrystal_blank", gp.TileSize, gp.TileSize);
        // image = setup("/projectile/arrow_up_1", gp.TileSize, gp.TileSize);
        // image2 = setup("/projectile/arrow_up_2", gp.TileSize, gp.TileSize);
    }
    public void use(Entity entity) {

        gp.ui.showMessage("+" + value + " Mana!");
        entity.mana += value;
        gp.playSE(2);
    }
}
