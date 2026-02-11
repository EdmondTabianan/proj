package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_Beverly extends Entity{

    public NPC_Beverly(GamePanel gp) {
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
            up1 = setup("/npc/npc_2_back", gp.TileSize, gp.TileSize);
            up2 = setup("/npc/npc_2_back", gp.TileSize, gp.TileSize);
            down1 = setup("/npc/npc_2_front", gp.TileSize, gp.TileSize);
            down2 = setup("/npc/npc_2_front", gp.TileSize, gp.TileSize);
            left1 = setup("/npc/npc_2_left", gp.TileSize, gp.TileSize);
            left2 = setup("/npc/npc_2_left", gp.TileSize, gp.TileSize);
            right1 = setup("/npc/npc_2_right", gp.TileSize, gp.TileSize);
            right2 = setup("/npc/npc_2_right", gp.TileSize, gp.TileSize);
    }
    public void setDialogue() {

        int i = 0;

        dialogues[i] = "Hello!";i++;
    }
    public void setAction(){
            
        // if (onPath == true) {
        //     int goalCol = 7;
        //     int goalRow = 10;
        //     // int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
        //     // int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;

        //     searchPath(goalCol, goalRow);
        // } 
        // else {

            // actionLockCounter ++;

            // if (collisionOn == true) {
            //     Random random = new Random();
            //     int i = random.nextInt(4);

            //     switch (i) {
            //         case 0: Direction = "up"; break;
            //         case 1: Direction = "down"; break;
            //         case 2: Direction = "left"; break;
            //         case 3: Direction = "right"; break;
            //     }
            //     collisionOn = false;
            //     actionLockCounter = 0;
            //     return;
            // }

    //         if(actionLockCounter == 120) {
    //             Random random = new Random();
    //             int i = random.nextInt(100)+1; //pick up numbner from 1 - 100
                
    //             if (i <=25) {
    //                 Direction = "up";
    //             }
    //             if (i >=25 && i <= 50) {
    //                 Direction = "down";
    //             } 
    //             if (i >=50  && i <= 75) {
    //                 Direction = "left";
    //             }
    //             if (i >= 75 && i <= 100) {
    //                 Direction = "right";
    //             }
    //             actionLockCounter = 0;
    //         }
        // }
    }
    public void idle () {

        //nothing
    }
    
    public void speak(){
        // Do this specific stuff
        super.speak(); // Call parent speak method if needed
        switch (gp.player.Direction) {
            case "up" : Direction = "down"; speed+=defaultSpeed; idle(); break;
            case "down" : Direction = "up"; speed+=defaultSpeed; idle(); break;
            case "left" : Direction = "right"; speed-=defaultSpeed; idle(); break;
            case "right" : Direction = "left"; speed+=defaultSpeed; idle(); break;
        }
        // onPath = true;
    }
}
