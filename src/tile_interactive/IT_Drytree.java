package tile_interactive;

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
    }
}