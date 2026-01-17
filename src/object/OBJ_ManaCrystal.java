package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {

    GamePanel gp;

    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "mana Crystal";
        image = setup("/objects/manacrystal_full", gp.TileSize, gp.TileSize);
        image2 = setup("/objects/manacrystal_blank", gp.TileSize, gp.TileSize);
        // image = setup("/projectile/arrow_up_1", gp.TileSize, gp.TileSize);
        // image2 = setup("/projectile/arrow_up_2", gp.TileSize, gp.TileSize);
    }

}
