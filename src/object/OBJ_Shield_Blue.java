package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Blue extends Entity {
    public OBJ_Shield_Blue (GamePanel gp) {
        super(gp);

        type = type_shield;
        name = "Blue Shield";
        down1 = setup("/objects/shield_blue", gp.TileSize, gp.TileSize);
        defenseValue = 5;
        description = "[" + name + "]\n shiny shield\n" + "Defense: " + defenseValue;
        price = 200;
    }
}
