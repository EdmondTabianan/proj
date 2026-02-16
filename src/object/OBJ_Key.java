package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {

    GamePanel gp;

    public OBJ_Key(GamePanel gp) {
        super(gp);
         
        type = type_key;
        name = "Key";
        down1 = setup("/objects/key", gp.TileSize, gp.TileSize);
        description = "[" + name + "]\n opens door";
        price = 100;
    }

}
