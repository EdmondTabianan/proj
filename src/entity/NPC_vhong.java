package entity;

import java.util.Random;

import main.GamePanel;
import object.OBJ_tablet;

public class NPC_vhong extends Entity {

    int questState = 0;

    public NPC_vhong(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;

        getImage();
        // Don't call setDialogue() here - player doesn't exist yet!
        // setDialogue(); // REMOVE THIS LINE

        // Initialize with default dialogue
        dialogues[0] = "Kill 3 slimes.";
        dialogues[1] = "You killed 3 slimes!";
        dialogues[2] = "Take the clue";
        dialogues[3] = "Test";

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32; 
    }
    
    public void getImage() {
        up1 = setup("/npc/npc_1_back", gp.TileSize, gp.TileSize);
        up2 = setup("/npc/npc_1_back", gp.TileSize, gp.TileSize);
        down1 = setup("/npc/npc_1_front", gp.TileSize, gp.TileSize);
        down2 = setup("/npc/npc_1_front", gp.TileSize, gp.TileSize);
        left1 = setup("/npc/npc_1_left", gp.TileSize, gp.TileSize);
        left2 = setup("/npc/npc_1_left", gp.TileSize, gp.TileSize);
        right1 = setup("/npc/npc_1_right", gp.TileSize, gp.TileSize);
        right2 = setup("/npc/npc_1_right", gp.TileSize, gp.TileSize);
    }
    
    public void setDialogue() {
        // Only access player if it exists
        if (gp.player != null) {
            dialogues[0] = "Kill 3 slimes. " + gp.player.killCount + "/3";
            dialogues[1] = "You killed 3 slimes!";
            dialogues[2] = "Take the clue";
            dialogues[3] = "Test";
        } else {
            // Default dialogue when player doesn't exist yet
            dialogues[0] = "Kill 3 slimes.";
            dialogues[1] = "You killed 3 slimes!";
            dialogues[2] = "Take the clue";
            dialogues[3] = "Test";
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
        // Update dialogue with current kill count before speaking
        if (gp.player != null) {
            if (questState == 0) {
                gp.ui.currentDialogue = "Kill 3 slimes. 0/3";
                gp.player.killCount = 0; // reset for quest
                questState = 1;
            }
            else if (questState == 1) {
                if (gp.player.killCount < 3) {
                    gp.ui.currentDialogue = "Kill 3 slimes: " + gp.player.killCount + "/3";
                } else {
                    gp.ui.currentDialogue = "You killed 3 slimes!";
                    spawnTablet();
                    questState = 2;
                }
            }
            else if (questState == 2) {
                gp.ui.currentDialogue = "Test";
            }
        } else {
            // Fallback if player is somehow null
            gp.ui.currentDialogue = "Hello, adventurer!";
        }
        
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