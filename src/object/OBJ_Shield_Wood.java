package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {
    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);

        type = type_shield;
        name = "Wood Shield";
        down1 = setup("/objects/shield_wood", gp.TileSize, gp.TileSize);
        defenseValue = 1;
        description = "[" + name + "]\nAn old wooden shield\nDefense: " + defenseValue;
        price = 150;
        
        // Optional: Add these if you want more shield functionality
        knockBackPower = 1;      // Small knockback when blocking
        stackable = false;       // Shields shouldn't stack
    }
}