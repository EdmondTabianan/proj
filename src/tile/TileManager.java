package tile;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
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

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {

        // try {

            setup(0, "grass01", false);
            setup(1, "wall", true);
            setup(2, "water01", true);
            setup(3, "earth", false);
            setup(4, "tree", true);
            setup(5, "road00", false);
            
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
    		InputStream is = getClass().getResourceAsStream("/map/world01.txt");
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
