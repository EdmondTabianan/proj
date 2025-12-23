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

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    public void getImage() {
            up1 = setup("/player/boy_up_1");
            up2 = setup("/player/boy_up_2");
            down1 = setup("/player/boy_down_1");
            down2 = setup("/player/boy_down_2");
            left1 = setup("/player/boy_left_1");
            left2 = setup("/player/boy_left_2");
            right1 = setup("/player/boy_right_1");
            right2 = setup("/player/boy_right_2");
    }
    public void setDialogue() {

        dialogues[0] = "Hello, Vhong!";
        dialogues[1] = "Some systems split “packages” \n from “orders” — but in a courier \n ";
        dialogues[2] = "tracking context, shipment is \n the unit that needs tracking.";
        dialogues[3] = "..................................";
    }
    public void setAction(){

        actionLockCounter ++;

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
    public void speak(){

        // Do this specfic stuff
        super.speak();
    }
}
