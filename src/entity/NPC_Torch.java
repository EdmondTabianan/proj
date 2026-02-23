package entity;

import main.GamePanel;

public class NPC_Torch extends Entity {

    GamePanel gp;
    private int animationState = 0; // 0=mid, 1=right, 2=mid, 3=left, 4=mid

    public NPC_Torch(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        name = "Torch";
        type = type_torch;
        action = true;
        speed = 0;
        
        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        collision = true;
        
        getImage();
    }

    public void getImage() {
        // Load torch animation frames
        down1 = setup("/npc/torch_down_1", gp.TileSize, gp.TileSize); // mid
        down2 = setup("/npc/torch_down_2", gp.TileSize, gp.TileSize); // right
        down3 = setup("/npc/torch_down_3", gp.TileSize, gp.TileSize); // left
        
        up1 = setup("/npc/torch_up_1", gp.TileSize, gp.TileSize);
        up2 = setup("/npc/torch_up_2", gp.TileSize, gp.TileSize);
        up3 = setup("/npc/torch_up_3", gp.TileSize, gp.TileSize);
        
        left1 = setup("/npc/torch_left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/npc/torch_left_2", gp.TileSize, gp.TileSize);
        left3 = setup("/npc/torch_left_3", gp.TileSize, gp.TileSize);
        
        right1 = setup("/npc/torch_right_1", gp.TileSize, gp.TileSize);
        right2 = setup("/npc/torch_right_2", gp.TileSize, gp.TileSize);
        right3 = setup("/npc/torch_right_3", gp.TileSize, gp.TileSize);
        
        // Set initial image
        image = down1;
    }
    
    @Override
    public void setAction() {
        actionLockCounter++;
        
        if (actionLockCounter > 8) { // Change every 8 frames for faster flicker
            // Animation sequence: mid -> right -> mid -> left -> mid
            switch(animationState) {
                case 0: // mid -> right
                    animationState = 1;
                    break;
                case 1: // right -> mid
                    animationState = 2;
                    break;
                case 2: // mid -> left
                    animationState = 3;
                    break;
                case 3: // left -> mid
                    animationState = 0;
                    break;
            }
            
            // Update image based on current direction and animation state
            if (Direction.equals("down")) {
                if (animationState == 0) image = down1; // mid
                else if (animationState == 1) image = down2; // right
                else if (animationState == 2) image = down1; // mid
                else if (animationState == 3) image = down3; // left
            } else if (Direction.equals("up")) {
                if (animationState == 0) image = up1;
                else if (animationState == 1) image = up2;
                else if (animationState == 2) image = up1;
                else if (animationState == 3) image = up3;
            } else if (Direction.equals("left")) {
                if (animationState == 0) image = left1;
                else if (animationState == 1) image = left2;
                else if (animationState == 2) image = left1;
                else if (animationState == 3) image = left3;
            } else if (Direction.equals("right")) {
                if (animationState == 0) image = right1;
                else if (animationState == 1) image = right2;
                else if (animationState == 2) image = right1;
                else if (animationState == 3) image = right3;
            }
            
            actionLockCounter = 0;
        }
    }
    
    @Override
    public void speak() {
        gp.ui.currentDialogue = "The torch flickers warmly.";
        gp.gameState = gp.dialogueState;
    }
}