package tile_interactive;

import entity.Entity;
import main.GamePanel;

public class IT_Drytree extends InteractiveTile {

    GamePanel gp;

    public IT_Drytree(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.worldX = gp.TileSize * col;
        this.worldY = gp.TileSize * row;

        down1 = setup("/tiles_interactive/drytree", gp.TileSize, gp.TileSize);
        destructible = true;
        life = 3;
    }
    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;
        
        if (entity.currentweapon.type == type_axe) {
            isCorrectItem = true;
        }

        return isCorrectItem;
    }
    public void playSE() {
        gp.playSE(11);
    }
    public InteractiveTile getDestroyedForm() {
        InteractiveTile tile = new IT_Trunk(gp, worldX/gp.TileSize, worldY/gp.TileSize);
        return tile;
    }
}