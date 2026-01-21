package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_boat extends Entity  {

    GamePanel gp;

    public OBJ_boat(GamePanel gp) {
        super(gp);
         
        type = type_transport;
        name = "Boat";
        down1 = setup("/objects/boat", gp.TileSize, gp.TileSize);
        
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
