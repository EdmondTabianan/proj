package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_piramid extends Entity {

    public OBJ_piramid(GamePanel gp) {
        super(gp);
         
        name = "piramid";
        down1 = setup("/piramid/piramid", gp.TileSize, gp.TileSize);
        
        collision = true;
    
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}