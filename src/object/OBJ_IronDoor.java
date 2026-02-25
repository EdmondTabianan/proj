package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_IronDoor extends Entity {

    GamePanel gp;
    public final static String objName = "Iron Door";

    public OBJ_IronDoor(GamePanel gp) {
        super(gp);
        
        name = objName;
        down1 = setup("/objects/door_iron", gp.TileSize, gp.TileSize);
        type = type_door;
        collision = true;
        
        // Set solid area for collision
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.TileSize;
        solidArea.height = gp.TileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
