package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_tablet extends Entity {

    GamePanel gp;

    public OBJ_tablet(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Ancient Tablet";
        down1 = setup("/objects/tablet", gp.TileSize, gp.TileSize);
        defenseValue = 1;
        // description = "You have to find something can cut it.";
        
        description = "The path is sealed \n by wood. Seek what \nbites deeper than bark.";
    }
}
