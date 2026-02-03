package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity{

    public OBJ_Axe(GamePanel gp) {
        super(gp);
        
        type = type_axe;
        name = "Woodcutter axe";
        down1 = setup("/objects/axe", gp.TileSize, gp.TileSize);
        attackvalue=3;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[" + name + "]\n rusty, still can cut trees\n" + "attack: " + attackvalue;
        amount = 75;
    }
}