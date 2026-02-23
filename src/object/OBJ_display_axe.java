package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_display_axe extends Entity {

    GamePanel gp;

    public OBJ_display_axe(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "display_axe";
        down1 = setup("/displayed_objects/axe", 48, 48);
        type = type_door;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
    }

}
