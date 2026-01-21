package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {

    GamePanel gp;

    public OBJ_Heart(GamePanel gp) {

        super(gp);
        this.gp = gp;
        name = "Heart";
        type = type_pickupOnly;
        value = 2;
        down1 = setup("/objects/heart_full", gp.TileSize, gp.TileSize);
        image  = setup("/objects/heart_full", gp.TileSize, gp.TileSize);
        image2 = setup("/objects/heart_half", gp.TileSize, gp.TileSize);
        image3 = setup("/objects/heart_blank", gp.TileSize, gp.TileSize);
        
    }
    public void use(Entity entity) {

        gp.ui.showMessage("+" + value + " Life!");
        entity.life += value;
        gp.playSE(2);
    }
}
