package tile_interactive;

public class IT_Trunk extends InteractiveTile {

    public IT_Trunk(main.GamePanel gp,int map, int col, int row) {
        super(gp,map, col, row);
        
        this.worldX = gp.TileSize * col;
        this.worldY = gp.TileSize * row;

        down1 = setup("/tiles_interactive/trunk", gp.TileSize, gp.TileSize);

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

}
