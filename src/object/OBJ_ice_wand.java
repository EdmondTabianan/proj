package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ice_wand extends Entity {

    GamePanel gp;

    public OBJ_ice_wand(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_wand;
        name = "Normal wand";
        down1 = setup("/objects/ice_wand", gp.TileSize, gp.TileSize);
        attackvalue = 1;
        description = "[" + name + "]\n normal wand\n" + "attack: " + attackvalue;
        attackArea.width = 36;
        attackArea.height = 36;
        amount = 100;
    }

}
