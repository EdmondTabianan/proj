package object;

import main.GamePanel;

public class OBJ_Key extends SuperObject {

    GamePanel gp;

    public OBJ_Key(GamePanel gp) {

        this.gp = gp;
        
        name = "Key";
        // image = setup("/objects/key", gp.tileSize, gp.tileSize);
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
            uTool.scaleImage(image, gp.TileSize, gp.TileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
