package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity{

    GamePanel gp;
    int value = 5;

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);
        
        this.gp = gp;
        
        type = type_consumable;
        name = "Red potion";
        value = 4;
        down1 = setup("/objects/potion_red", gp.TileSize, gp.TileSize);
        description = "[Red Potion]\n your life by " + value + ".";

    }
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You Drink the " + name + "!\n"
            + "your life has been recovered by " + value + ".";
        entity.life += value;
        gp.playSE(2);
    }
}
