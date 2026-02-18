package entity;

import java.util.Random;

import main.GamePanel;
import object.OBJ_tablet;

public class NPC_sailor extends Entity {

    GamePanel gp;

    public NPC_sailor(GamePanel gp) {
        super(gp);
        this.gp = gp;

        Direction = "down";
        speed = 1;

        getImage();

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32; 
    }
    
    public void getImage() {
        up1 = setup("/npc/npc_3_back", gp.TileSize, gp.TileSize);
        up2 = setup("/npc/npc_3_back", gp.TileSize, gp.TileSize);
        down1 = setup("/npc/npc_3_front", gp.TileSize, gp.TileSize);
        down2 = setup("/npc/npc_3_front", gp.TileSize, gp.TileSize);
        left1 = setup("/npc/npc_3_left", gp.TileSize, gp.TileSize);
        left2 = setup("/npc/npc_3_left", gp.TileSize, gp.TileSize);
        right1 = setup("/npc/npc_3_right", gp.TileSize, gp.TileSize);
        right2 = setup("/npc/npc_3_right", gp.TileSize, gp.TileSize);
    }
    
    public void setDialogue() {
        // Only access player if it exists
        if (gp.player != null) {
            dialogues[0] = "Hello adventurer! Welcome to the island.";
            dialogues[1] = "if you want to go to other islands, you need to find the key.";
        } else {
            // Default dialogue when player doesn't exist yet
            dialogues[0] = "Hello adventurer! Welcome to the island.";
            dialogues[1] = "if you want to go to other islands, you need to find the key.";
        }
    }
    
    public void setAction(){
        if (onPath == true) {
            int goalCol = 7;
            int goalRow = 10;
            searchPath(goalCol, goalRow);
        } 
        else {
            actionLockCounter++;

            if (collisionOn == true) {
                Random random = new Random();
                int i = random.nextInt(4);

                switch (i) {
                    case 0: Direction = "up"; break;
                    case 1: Direction = "down"; break;
                    case 2: Direction = "left"; break;
                    case 3: Direction = "right"; break;
                }
                collisionOn = false;
                actionLockCounter = 0;
                return;
            }

            if(actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;
                
                if (i <= 25) {
                    Direction = "up";
                } else if (i <= 50) {
                    Direction = "down";
                } else if (i <= 75) {
                    Direction = "left";
                } else {
                    Direction = "right";
                }
                actionLockCounter = 0;
            }
        }
    }
    
    public void speak() {
        
        gp.ui.currentDialogue = "if you want to go to other islands,\n you need to find the key.";
        facePlayer();
    }
    
    public void spawnTablet() {
        int currentMap = gp.currentMap;
    
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
            if (gp.obj[currentMap][i] == null) {
                gp.obj[currentMap][i] = new OBJ_tablet(gp);
                gp.obj[currentMap][i].worldX = gp.TileSize * 11;
                gp.obj[currentMap][i].worldY = gp.TileSize * 24;
                break;
            }
        }
    }    
    
    public void facePlayer() {
        if (gp.player != null) {
            switch (gp.player.Direction) {
                case "up": Direction = "down"; break;
                case "down": Direction = "up"; break;
                case "left": Direction = "right"; break;
                case "right": Direction = "left"; break;
            }
        }
    }
}
