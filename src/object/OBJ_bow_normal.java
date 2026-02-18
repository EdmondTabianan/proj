package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_bow_normal extends Entity {

    GamePanel gp;

    public OBJ_bow_normal(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_bow;
        name = "Normal bow";
        down1 = setup("/objects/bow_normal", gp.TileSize, gp.TileSize);
        attackvalue = 5;
        description = "[" + name + "]\n normal sword\n" + "attack: " + attackvalue;
        attackArea.width = 36;
        attackArea.height = 36;
        price = 100;
    }

    
}
