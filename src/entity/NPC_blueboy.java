package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_blueboy extends Entity {


    public NPC_blueboy(GamePanel gp) {
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
            up1 = setup("/player/boy_up_1", gp.TileSize, gp.TileSize);
            up2 = setup("/player/boy_up_2", gp.TileSize, gp.TileSize);
            down1 = setup("/player/boy_down_1", gp.TileSize, gp.TileSize);
            down2 = setup("/player/boy_down_2", gp.TileSize, gp.TileSize);
            left1 = setup("/player/boy_left_1", gp.TileSize, gp.TileSize);
            left2 = setup("/player/boy_left_2", gp.TileSize, gp.TileSize);
            right1 = setup("/player/boy_right_1", gp.TileSize, gp.TileSize);
            right2 = setup("/player/boy_right_2", gp.TileSize, gp.TileSize);
    }
    public void setDialogue() {

        int i = 0;

        dialogues[i] = "A vessel rests at dock,\n" + //
                        "locked from your journey—\n" + //
                        "the key decides who sails\n" + //
                        "";i++;
        dialogues[i] = "Goodluck!";i++;
        //gp.player.inventory.add(new OBJ_Potion_Red(gp));
    }
    public void setAction(){

        if (onPath == true) {
            // int goalCol = 7;
            // int goalRow = 10;
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;

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
    public void speak(){
        // Do this specfic stuff
        super.speak();

        onPath = true;
    }
}
