package object;

import main.GamePanel;

public class OBJ_Boots extends SuperObject{

    GamePanel gp;

    public OBJ_Boots(GamePanel gp) {
        
        this.gp = gp;

        name = "Boots";
        // image = setup("/objects/boots", gp.tileSize, gp.tileSize);
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/boots.png"));
            uTool.scaleImage(image, gp.TileSize, gp.TileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
