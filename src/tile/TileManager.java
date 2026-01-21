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

        tile = new Tile[200];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/map/pyramid.txt");
    }

    public void getTileImage() {

        // try {

            setup(0, "000", false);
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
            setup(37, "trunk", false);
            setup(38, "water016", false);
            setup(39, "water014", true);
            setup(40, "water018", true);
            setup(41, "water018", true);
            setup(42, "water019", true);
            setup(43, "sand01", false);
            setup(58, "door1", false);
            setup(60, "door2", true);
            int i = 62;
            setup( i, "062", true);i++;
            setup( i, "063", true);i++;
            setup( i, "064", true);i++;
            setup( i, "065", true);i++;
            setup( i, "066", true);i++; 
            setup( i, "067", true);i++;
            setup( i, "068", true);i++;
            setup( i, "069", true);i++;
            setup( i, "070", true);i++;
            setup( i, "071", true);i++;
            setup( i, "072", true);i++;
            setup( i, "073", true);i++;
            setup( i, "074", true);i++;
            setup( i, "075", true);i++;
            setup( i, "076", true);i++;
            setup( i, "077", true);i++;
            setup( i, "078", true);i++;
            setup( i, "079", true);i++;
            setup( i, "080", true);i++;
            setup( i, "081", true);i++;
            setup( i, "082", true);i++;
            setup( i, "083", true);i++;
            setup( i, "084", true);i++;
            setup( i, "085", true);i++;
            setup( i, "086", true);i++;
            setup( i, "087", true);i++;
            setup( i, "088", true);i++;
            setup( i, "089", true);i++;
            setup( i, "090", true);i++;
            setup( i, "091", true);i++;
            setup( i, "092", true);i++;
            setup( i, "093", true);i++;
            setup( i, "094", true);i++;
            setup( i, "095", true);i++;
            setup( i, "096", true);i++;
            setup( i, "097", true);i++;
            setup( i, "098", true);i++;
            setup( i, "099", true);i++;
            setup( i, "100", true);i++;
            setup( i, "101", true);i++;
            setup( i, "102", true);i++;
            setup( i, "103", true);i++;
            setup( i, "104", true);i++;
            setup( i, "105", true);i++;
            setup( i, "106", true);i++;
            setup( i, "107", true);i++;
            setup( i, "108", true);i++;
            setup( i, "109", true);i++;
            setup( i, "110", true);i++;
            setup( i, "111", true);i++;
            setup( i, "112", true);i++;
            setup( i, "113", true);i++;
            setup( i, "114", true);i++;
            setup( i, "115", true);i++;
            setup( i, "116", true);i++;
            setup( i, "117", false);i++;
            setup( i, "118", true);i++;
            setup( i, "119", false);i++;
            setup( i, "120", true);i++;
            setup( i, "121", true);i++;
            setup( i, "122", true);i++;
            setup( i, "123", true);i++;
            setup( i, "124", true);i++;
            setup( i, "125", true);i++;
            setup( i, "126", true);i++;
            setup( i, "127", true);i++;
            setup( i, "128", true);i++;
            setup( i, "129", true);i++;
            setup( i, "130", true);i++;
            setup( i, "131", true);i++;
            setup( i, "132", true);i++;
            setup( i, "133", true);i++;
            setup( i, "134", true);i++;
            setup( i, "135", true);i++;
            setup( i, "136", true);i++;
            setup( i, "137", true);i++;
            setup( i, "138", true);i++;
            setup( i, "139", true);i++;
            setup( i, "140", true);i++;
            setup( i, "141", true);i++;
            setup( i, "142", true);i++;
            setup( i, "143", true);i++;
            setup( i, "144", true);i++;
            setup( i, "145", true);i++;
            setup( i, "146", true);i++;
            setup( i, "147", true);i++;
            setup( i, "148", true);i++;
            setup( i, "149", true);i++;
            setup( i, "150", true);i++;
            setup( i, "151", true);i++;
            setup( i, "152", true);i++;
            setup( i, "153", true);i++;
            setup( i, "154", true);i++;
            setup( i, "155", true);i++;
            setup( i, "156", true);i++;
            setup( i, "157", true);i++;
            setup( i, "158", true);i++;
            setup( i, "159", true);i++;
            setup( i, "160", true);i++;
            setup( i, "161", true);i++;
            setup( i, "162", true);i++;
            setup( i, "163", true);i++;
            setup( i, "164", true);i++;
            setup( i, "165", true);i++;
            setup( i, "166", true);i++;
            setup( i, "167", true);i++;
            setup( i, "168", true);i++;
            setup( i, "169", true);i++;
            setup( i, "170", true);i++;
            setup( i, "171", true);i++;
            setup( i, "172", true);i++;
            setup( i, "173", true);i++;
            setup( i, "174", true);i++;
            setup( i, "175", true);i++;
            setup( i, "176", true);


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
    public void loadMap(String filepath) {
    	try {
    		InputStream is = getClass().getResourceAsStream(filepath);
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
