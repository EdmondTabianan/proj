package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Blue extends Entity {

    GamePanel gp;
    public OBJ_Potion_Blue(GamePanel gp) {
        super(gp);
        
        this.gp = gp;
        
        type = type_consumable;
        name = "Blue potion";
        value = 2;
        down1 = setup("/objects/potion_blue", gp.TileSize, gp.TileSize);
        description = "[Blue Potion]\n your mana by " + value + ".";
        amount = 30;
    }
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You Drink the " + name + "!\n"
            + "your mana has been recovered by " + value + ".";
        entity.mana += value;
        gp.playSE(2);
    }
}
