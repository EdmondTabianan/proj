package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[100];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {

        // try {

            setup(0, "grass00", false);
            setup(1, "grass00", false);
            setup(2, "grass01", false);
            setup(3, "road00", false);
            setup(4, "road01", false);
            setup(5, "road02", false);
            setup(6, "road03", false);
            setup(7, "road04", false);
            setup(8, "road05", false);
            setup(9, "road06", false);
            setup(10, "road07", false);
            setup(11, "road08", false);

            setup(12, "road09", false);
            setup(13, "road10", false);
            setup(14, "road11", false);
            setup(15, "road12", false);
            setup(16, "tree", true);
            setup(17, "earth", false);
            setup(18, "water00", true);
            setup(19, "water01", true);
            setup(20, "water02", true);
            setup(21, "water03", true);
            setup(22, "water04", true);
            setup(23, "water05", true);
            setup(24, "water06", true);
            setup(25, "water07", true);
            setup(26, "water08", true);
            setup(27, "water09", true);
            setup(28, "water10", true);
            setup(29, "water11", true);
            setup(30, "water12", true);
            setup(31, "water13", true);
            setup(32, "wall", true);
            setup(33, "hut", false);
            setup(34, "floor01", false);
            setup(35, "table01", true);
            setup(36, "apple", true);
            setup(37, "house", true);
            setup(38, "water016", false);
            setup(39, "water014", true);
            setup(40, "water018", true);
            setup(41, "water018", true);
            setup(42, "water019", true);
            setup(43, "sand01", false);
            setup(58, "door1", false);
            setup(60, "door2", true);

            // setup(38, "road12", false);
            // setup(39, "earth", false);
            // setup(40, "wall", true);
            // setup(41, "tree", true);
            
            // tile[0] = new Tile();
            // tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass01.png"));

            // tile[1] = new Tile();
            // tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            // tile[1].collision = true;

            // tile[2] = new Tile();
            // tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water01.png"));
            // tile[2].collision = true;

            // tile[3] = new Tile();
            // tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));

            // tile[4] = new Tile();
            // tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            // tile[4].collision = true;

            // tile[5] = new Tile();
            // tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/road00.png"));
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
    public void setup(int index, String imageName, boolean collision ) {
    	
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName   +".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.TileSize, gp.TileSize);
            tile[index].collision = collision;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadMap () {
    	try {
    		InputStream is = getClass().getResourceAsStream("/map/ano.txt");
    		BufferedReader br = new BufferedReader(new InputStreamReader(is));
    		
    		int col = 0;
    		int row = 0;
    		
    		while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
    			
    			String line = br.readLine();
    			
    			while (col < gp.maxWorldCol) {
    				String numbers[] = line.split(" ");
    				
    				int num = Integer.parseInt(numbers[col]);
    				
    				mapTileNum[col][row] = num;
    				col++;
    			}
    			if (col == gp.maxWorldCol) {
    				col = 0;
    				row++;
    			}
    		}
    		br.close();
    	} catch (Exception e) {
    		
    	}
    }
    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.TileSize;
            int worldY = worldRow * gp.TileSize;

            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {

                g2.drawImage(tile[tileNum].image, screenX, screenY,null);
            }
            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    } 
}
