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
        setDialogue();

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

        int i = 0;
        // dialogues[i] = "kill 3 slime. "; i++;
        // dialogues[i] = "you killed 3 slime."; i++;
        dialogues[i] = "kill 3 slime. " + gp.player.killCount + "/3";i++;
        dialogues[i] = "you killed 3 slime."; i++;
        dialogues[i] = "take the clue";i++;
        dialogues[i] = "test";i++;
        
    }
    public void setAction(){

        if (onPath == true) {
            int goalCol = 7;
            int goalRow = 10;
            // int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
            // int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;

            searchPath(goalCol, goalRow);
        } 
        else {
            actionLockCounter ++;

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
                int i = random.nextInt(100)+1; //pick up numbner from 1 - 100
                
                if (i <=25) {
                    Direction = "up";
                }
                if (i >=25 && i <= 50) {
                    Direction = "down";
                } 
                if (i >=50  && i <= 75) {
                    Direction = "left";
                }
                if (i >= 75 && i <= 100) {
                    Direction = "right";
                }
                actionLockCounter = 0;
            }
        }
    }
    // public void speak(){
        
    //     super.speak(); 

    //     if (gp.player.killCount < 0) {
    //         gp.ui.showMessage("kill 3 slime" + gp.player.killCount);
    //     }
    //     else if (gp.player.killCount == 3) {
    //         talk = true; 
    //     talkcounter++;
    //     // talk to npc one time to spawn the clue
    //     if (talk == true && talkcounter == 1) {
    //         // Spawn tablet on current map
    //         int currentMap = gp.currentMap;
            
    //         // Find an empty slot in the object array
    //         for (int i = 0; i < gp.obj[1].length; i++) {
    //             if (gp.obj[currentMap][i] == null) {
    //                 gp.obj[currentMap][i] = new OBJ_tablet(gp);
    //                 gp.obj[currentMap][i].worldX = gp.TileSize * 10;
    //                 gp.obj[currentMap][i].worldY = gp.TileSize * 24;
    //                 break; // Exit after placing one tablet
    //             }
    //         }
    //         talk = false;
    //     } 
    //     else {
    //         gp.ui.showMessage("already talked" + talkcounter);
    //         talkcounter = talkcounter-1;
    //     }
    //     }
        
    //     switch (gp.player.Direction) {
    //         case "up" : Direction = "down"; speed+=defaultSpeed; idle(); break;
    //         case "down" : Direction = "up"; speed+=defaultSpeed; idle(); break;
    //         case "left" : Direction = "right"; speed-=defaultSpeed; idle(); break;
    //         case "right" : Direction = "left"; speed+=defaultSpeed; idle(); break;
    //     }
    //     // onPath = true;
    // }
    public void speak() {

        if (questState == 0) {
            gp.ui.currentDialogue = dialogues[0];
            gp.player.killCount = 0; // reset for quest
            questState = 1;
        }
    
        else if (questState == 1) {
    
            if (gp.player.killCount < 3) {
                gp.ui.currentDialogue =
                    "Kill 3 slimes: " + gp.player.killCount + "/3";
            }
            else {
                gp.ui.currentDialogue = dialogues[1];
                spawnTablet();
                questState = 2;
            }
        }
    
        else if (questState == 2) {
            gp.ui.currentDialogue = dialogues[3];
        }
    
        facePlayer();
    }
    public void spawnTablet() {

        int currentMap = gp.currentMap;
    
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
    
            if (gp.obj[currentMap][i] == null) {
    
                gp.obj[currentMap][i] = new OBJ_tablet(gp);
                gp.obj[currentMap][i].worldX = gp.TileSize * 10;
                gp.obj[currentMap][i].worldY = gp.TileSize * 24;
                break;
            }
        }
    }    
    public void facePlayer() {
        switch (gp.player.Direction) {
            case "up" : Direction = "down"; speed+=defaultSpeed; idle(); break;
            case "down" : Direction = "up"; speed+=defaultSpeed; idle(); break;
            case "left" : Direction = "right"; speed-=defaultSpeed; idle(); break;
            case "right" : Direction = "left"; speed+=defaultSpeed; idle(); break;
        }
    }
}
