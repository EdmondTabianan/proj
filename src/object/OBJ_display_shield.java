package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_display_shield extends Entity {

    GamePanel gp;

    public OBJ_display_shield(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "display_shield";
        down1 = setup("/displayed_objects/shield", 48, 48);
        type = type_door;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
    }

}
