package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_display_sword extends Entity {

    GamePanel gp;

    public OBJ_display_sword(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "display_sword";
        down1 = setup("/displayed_objects/sword", 48, 48);
        type = type_door;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
    }
}
