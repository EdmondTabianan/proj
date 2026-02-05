package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door_Right extends Entity {

    public OBJ_Door_Right(GamePanel gp) {
        super(gp);
        name = "Door";
        down1 = setup("/objects/door_right", gp.TileSize, gp.TileSize);
        
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
