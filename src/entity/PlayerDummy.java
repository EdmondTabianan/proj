package entity;

import main.GamePanel;

public class PlayerDummy extends Entity {

    public static final String npcName = "PlayerDummy";

    public PlayerDummy(GamePanel gp, int characterChoice) {
        super(gp);
        this.gp = gp;
        
        // Set the character used
        this.characterused = characterChoice;

        name = npcName;
        getImage();
    }

    public void getImage() {
        if (characterused == 1) {
            up1 = setup("/player/up_1", gp.TileSize, gp.TileSize);
            up2 = setup("/player/up_2", gp.TileSize, gp.TileSize);
            down1 = setup("/player/down_1", gp.TileSize, gp.TileSize);
            down2 = setup("/player/down_2", gp.TileSize, gp.TileSize);
            left1 = setup("/player/left_1", gp.TileSize, gp.TileSize);
            left2 = setup("/player/left_2", gp.TileSize, gp.TileSize);
            right1 = setup ("/player/right_1", gp.TileSize, gp.TileSize);
            right2 = setup ("/player/right_2", gp.TileSize, gp.TileSize);
        }
        if (characterused == 0) {
            up1 = setup("/xylo/b_up_1", gp.TileSize, gp.TileSize);
            up2 = setup("/xylo/b_up_2", gp.TileSize, gp.TileSize);
            down1 = setup("/xylo/b_down_1", gp.TileSize, gp.TileSize);
            down2 = setup("/xylo/b_down_2", gp.TileSize, gp.TileSize);
            left1 = setup("/xylo/b_left_1", gp.TileSize, gp.TileSize);
            left2 = setup("/xylo/b_left_2", gp.TileSize, gp.TileSize);
            right1 = setup ("/xylo/b_right_1", gp.TileSize, gp.TileSize);
            right2 = setup ("/xylo/b_right_2", gp.TileSize, gp.TileSize);
        }   
    }
}