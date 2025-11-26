package object;

import main.GamePanel;

public class OBJ_Chest extends SuperObject {

    GamePanel gp;

    public OBJ_Chest(GamePanel gp) {

            this.gp = gp;
            
            name = "Chest";
        // image = setup("/objects/chest", gp.tileSize, gp.tileSize);
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/chest.png"));
            uTool.scaleImage(image, gp.TileSize, gp.TileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}