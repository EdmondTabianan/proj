package object;

import main.GamePanel;

public class OBJ_Door extends SuperObject {

    GamePanel gp;

    public OBJ_Door(GamePanel gp) {

        this.gp = gp;
        
        name = "Door";
        // image = setup("/objects/key", gp.tileSize, gp.tileSize);
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
            uTool.scaleImage(image, gp.TileSize, gp.TileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
