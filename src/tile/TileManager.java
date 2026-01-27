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
    public int mapTileNum[][][];

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[300];
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/map/main.txt", 0);
        loadMap("/map/secondmap.txt", 1);
    }

    public void getTileImage() {
        int i = 0;
        
        // Fill in all missing numbers from 000 to 176
        setup(i, "000", false); i++;
        setup(i, "001", false); i++;
        setup(i, "002", true); i++;
        setup(i, "003", false); i++;
        setup(i, "004", false); i++;
        setup(i, "005", false); i++;
        setup(i, "006", false); i++;
        setup(i, "007", false); i++;
        setup(i, "008", false); i++;
        
        // Missing numbers 009-061
        setup(i, "009", false); i++;
        setup(i, "010", false); i++;
        setup(i, "011", false); i++;
        setup(i, "012", false); i++;
        setup(i, "013", false); i++;
        setup(i, "014", false); i++;
        setup(i, "015", true); i++;
        setup(i, "016", false); i++;
        setup(i, "017", false); i++;
        setup(i, "018", true); i++;
        setup(i, "019", true); i++;
        setup(i, "020", false); i++;
        setup(i, "021", false); i++;
        setup(i, "022", false); i++;
        setup(i, "023", false); i++;
        setup(i, "024", false); i++;
        setup(i, "025", false); i++;
        setup(i, "026", false); i++;
        setup(i, "027", false); i++;
        setup(i, "028", false); i++;
        setup(i, "029", false); i++;
        setup(i, "030", false); i++;
        setup(i, "031", false); i++;
        setup(i, "032", true); i++;
        setup(i, "033", true); i++;
        setup(i, "034", false); i++;
        setup(i, "035", false); i++;
        setup(i, "036", false); i++;
        setup(i, "037", false); i++;
        setup(i, "038", false); i++;
        setup(i, "039", false); i++;
        setup(i, "040", false); i++;
        setup(i, "041", true); i++;
        setup(i, "042", false); i++;
        setup(i, "043", false); i++;
        setup(i, "044", false); i++;
        setup(i, "045", false); i++;
        setup(i, "046", false); i++;
        setup(i, "047", false); i++;
        setup(i, "048", false); i++;
        setup(i, "049", false); i++;
        setup(i, "050", false); i++;
        setup(i, "051", false); i++;
        setup(i, "052", false); i++;
        setup(i, "053", true); i++;
        setup(i, "054", false); i++;
        setup(i, "055", false); i++;
        setup(i, "056", false); i++;
        setup(i, "057", false); i++;
        setup(i, "058", false); i++;
        setup(i, "059", false); i++;
        setup(i, "060", false); i++;
        setup(i, "061", false); i++;
        
        // Continue with your existing numbers
        setup(i, "062", true); i++;
        setup(i, "063", true); i++;
        setup(i, "064", true); i++;
        setup(i, "065", true); i++;
        setup(i, "066", true); i++; 
        setup(i, "067", true); i++;
        setup(i, "068", true); i++;
        setup(i, "069", true); i++;
        setup(i, "070", true); i++;
        setup(i, "071", true); i++;
        setup(i, "072", true); i++;
        setup(i, "073", true); i++;
        setup(i, "074", true); i++;
        setup(i, "075", true); i++;
        setup(i, "076", true); i++;
        setup(i, "077", true); i++;
        setup(i, "078", true); i++;
        setup(i, "079", true); i++;
        setup(i, "080", true); i++;
        setup(i, "081", true); i++;
        setup(i, "082", true); i++;
        setup(i, "083", true); i++;
        setup(i, "084", true); i++;
        setup(i, "085", true); i++;
        setup(i, "086", true); i++;
        setup(i, "087", true); i++;
        setup(i, "088", true); i++;
        setup(i, "089", true); i++;
        setup(i, "090", true); i++;
        setup(i, "091", true); i++;
        setup(i, "092", true); i++;
        setup(i, "093", true); i++;
        setup(i, "094", true); i++;
        setup(i, "095", true); i++;
        setup(i, "096", true); i++;
        setup(i, "097", true); i++;
        setup(i, "098", true); i++;
        setup(i, "099", true); i++;
        setup(i, "100", true); i++;
        setup(i, "101", true); i++;
        setup(i, "102", true); i++;
        setup(i, "103", true); i++;
        setup(i, "104", true); i++;
        setup(i, "105", true); i++;
        setup(i, "106", true); i++;
        setup(i, "107", true); i++;
        setup(i, "108", true); i++;
        setup(i, "109", true); i++;
        setup(i, "110", true); i++;
        setup(i, "111", true); i++;
        setup(i, "112", true); i++;
        setup(i, "113", true); i++;
        setup(i, "114", true); i++;
        setup(i, "115", true); i++;
        setup(i, "116", true); i++;
        setup(i, "117", false); i++;
        setup(i, "118", true); i++;
        setup(i, "119", false); i++;
        setup(i, "120", true); i++;
        setup(i, "121", true); i++;
        setup(i, "122", true); i++;
        setup(i, "123", true); i++;
        setup(i, "124", true); i++;
        setup(i, "125", true); i++;
        setup(i, "126", true); i++;
        setup(i, "127", true); i++;
        setup(i, "128", true); i++;
        setup(i, "129", true); i++;
        setup(i, "130", true); i++;
        setup(i, "131", true); i++;
        setup(i, "132", true); i++;
        setup(i, "133", true); i++;
        setup(i, "134", true); i++;
        setup(i, "135", true); i++;
        setup(i, "136", true); i++;
        setup(i, "137", true); i++;
        setup(i, "138", true); i++;
        setup(i, "139", true); i++;
        setup(i, "140", true); i++;
        setup(i, "141", true); i++;
        setup(i, "142", true); i++;
        setup(i, "143", true); i++;
        setup(i, "144", true); i++;
        setup(i, "145", true); i++;
        setup(i, "146", true); i++;
        setup(i, "147", true); i++;
        setup(i, "148", true); i++;
        setup(i, "149", true); i++;
        setup(i, "150", true); i++;
        setup(i, "151", true); i++;
        setup(i, "152", true); i++;
        setup(i, "153", true); i++;
        setup(i, "154", true); i++;
        setup(i, "155", true); i++;
        setup(i, "156", true); i++;
        setup(i, "157", true); i++;
        setup(i, "158", true); i++;
        setup(i, "159", true); i++;
        setup(i, "160", true); i++;
        setup(i, "161", true); i++;
        setup(i, "162", true); i++;
        setup(i, "163", true); i++;
        setup(i, "164", true); i++;
        setup(i, "165", true); i++;
        setup(i, "166", true); i++;
        setup(i, "167", true); i++;
        setup(i, "168", true); i++;
        setup(i, "169", true); i++;
        setup(i, "170", true); i++;
        setup(i, "171", true); i++;
        setup(i, "172", true); i++;
        setup(i, "173", true); i++;
        setup(i, "174", true); i++;
        setup(i, "175", true); i++;
        setup(i, "176", true); i++;        
        setup(i, "177", true); i++;
        setup(i, "178", true); i++;
        setup(i, "179", true); i++;
        setup(i, "180", true); i++;
        setup(i, "181", true); i++;
        setup(i, "182", true); i++;
        setup(i, "183", true); i++;
        setup(i, "184", true); i++;
        setup(i, "185", true); i++;
        setup(i, "186", true); i++;
        setup(i, "187", true); i++;
        setup(i, "188", true); i++;
        setup(i, "189", true); i++;
        setup(i, "190", true); i++;
        setup(i, "191", true); i++;
        setup(i, "192", true); i++;
        setup(i, "193", true); i++;
        setup(i, "194", true); i++;
        setup(i, "195", true); i++;
        setup(i, "196", true); i++;
        setup(i, "197", true); i++;
        setup(i, "198", true); i++;
        setup(i, "199", true); i++;
        setup(i, "200", true); i++;
        setup(i, "201", true); i++;
        setup(i, "202", true); i++;
        setup(i, "203", true); i++;
        setup(i, "204", true); i++;
        setup(i, "205", true); i++;
        setup(i, "206", true); i++;
        setup(i, "207", true); i++;
        setup(i, "208", true); i++;
        setup(i, "209", true); i++;
        setup(i, "210", true); i++;
        setup(i, "211", true); i++;
        setup(i, "212", true); i++;
        setup(i, "213", true); i++;
        setup(i, "214", true); i++;
        setup(i, "215", true); i++;
        setup(i, "216", true); i++;
        setup(i, "217", true); i++;
        setup(i, "218", true); i++;
        setup(i, "219", true); i++;
        setup(i, "220", true); i++;
        setup(i, "221", true); i++;
        setup(i, "222", true); i++;
        setup(i, "223", true); i++;
        setup(i, "224", true); i++;
        setup(i, "225", true); i++;
        setup(i, "226", true); i++;
        setup(i, "227", true); i++;
        setup(i, "228", true); i++;
        setup(i, "229", true); i++;
        setup(i, "230", true); i++;
        setup(i, "231", true); i++;
        setup(i, "232", true); i++;
        setup(i, "233", true); i++;
        setup(i, "234", true); i++;
        setup(i, "235", true); i++;
    }
    
    public void setup(int index, String imageName, boolean collision ) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.TileSize, gp.TileSize);
            tile[index].collision = collision;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadMap(String filepath, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filepath);
            if (is == null) {
                System.err.println("Map file not found: " + filepath);
                return;
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int col = 0;
            int row = 0;
            
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                
                String numbers[] = line.split(" ");
                
                for (int i = 0; i < numbers.length && col < gp.maxWorldCol; i++) {
                    int num = Integer.parseInt(numbers[i]);
                    mapTileNum[map][col][row] = num;
                    col++;
                }
                
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Optional: Add a method to check map bounds
    public boolean isTileCollision(int worldCol, int worldRow) {
        if (worldCol < 0 || worldCol >= gp.maxWorldCol || 
            worldRow < 0 || worldRow >= gp.maxWorldRow) {
            return true;
        }
        int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
        if (tileNum < 0 || tileNum >= tile.length) {
            return true;
        }
        return tile[tileNum].collision;
    }
    
    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];

            int worldX = worldCol * gp.TileSize;
            int worldY = worldRow * gp.TileSize;

            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}