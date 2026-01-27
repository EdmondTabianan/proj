package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {

    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);

        type = type_sword;
        name = "Normal Sword";
        down1 = setup("/objects/sword_normal", gp.TileSize, gp.TileSize);
        attackvalue = 2;
        description = "[" + name + "]\n Dull sword\n" + "attack: " + attackvalue;
        attackArea.width = 36;
        attackArea.height = 36;
    }
}
