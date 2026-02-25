package entity;

import main.GamePanel;

public class NPC_Ding extends Entity {

    public NPC_Ding(GamePanel gp) {
        super(gp);
        
        name = "Ding";
        type = type_npc;
        Direction = "down";
        speed = 1;
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 38; 
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        down1 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize);
    }

    private void prepareDialoguePages() {
        dialogues = new String[10][10];
        int i = 0;
        dialogues[i][0] = "Hello there, I'm Ding!";
        dialogues[i][1] = "I am a humble NPC in this world.";
        dialogues[i][2] = "please kill 3 slime and 3 snake";
         ;
    }

}
