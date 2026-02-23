package entity;

import main.GamePanel;
import object.OBJ_Arrows;
import object.OBJ_Axe;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;
import object.OBJ_bow_normal;

public class NPC_merchant extends Entity {

    public NPC_merchant(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;
        
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        setDialogue();
        setItem();
    }
    public void getImage() {
            up1 = setup("/npc/npc_3_front", gp.TileSize, gp.TileSize);
            up2 = setup("/npc/npc_3_front", gp.TileSize, gp.TileSize);
            down1 = setup("/npc/npc_3_back", gp.TileSize, gp.TileSize);
            down2 = setup("/npc/npc_3_back", gp.TileSize, gp.TileSize);
            left1 = setup("/npc/npc_3_left", gp.TileSize, gp.TileSize);
            left2 = setup("/npc/npc_3_left", gp.TileSize, gp.TileSize);
            right1 = setup("/npc/npc_3_right", gp.TileSize, gp.TileSize);
            right2 = setup("/npc/npc_3_right", gp.TileSize, gp.TileSize);
    }
    public void setDialogue() {

        int i = 0;

        dialogues[i][0] = "So you want to buy?";i++;
    }

    public void setItem() {
        inventory.add(new OBJ_Arrows(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Potion_Blue(gp));
        inventory.add(new OBJ_Potion_Red(gp));
        inventory.add(new OBJ_Sword_Normal(gp));
        inventory.add(new OBJ_Shield_Wood(gp));
        inventory.add(new OBJ_bow_normal(gp));
    }

    public void speak() {
        super.speak();
        gp.gameState = gp.tradeState;
        gp.ui.npc = this;
    }
}
