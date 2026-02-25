package object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

public class OBJ_Cleopatra_tomb extends Entity {

    GamePanel gp; // Add this line

    public OBJ_Cleopatra_tomb(GamePanel gp) {
        super(gp);
        this.gp = gp; // Initialize the gp field
         
        name = "Cleopatra's Tomb";
        
        // Tomb size - 3 tiles wide/tall (144x144 pixels with 48px tiles)
        int tombSize = gp.TileSize * 3; // 48 * 3 = 144px
        
        // Load the image at the correct size
        down1 = setup("/objects/cleopatratomb", tombSize, tombSize);
        
        type = type_door;
        collision = true;
        
        // Set solid area for collision (3 tiles wide, 1 tile tall)
        solidArea = new Rectangle();
        solidArea.x = (gp.TileSize * 3 - gp.TileSize * 3) / 2; // Center the 3-tile width
        solidArea.y = (gp.TileSize * 3 - 48) / 2; // Center the 1-tile height
        solidArea.width = gp.TileSize * 3; // 48 * 3 = 144px (3 tiles wide)
        solidArea.height = 48; // 48px (1 tile tall)
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    
    @Override
    public void draw(Graphics2D g2) {
        // Calculate screen position manually
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        // Don't draw if off-screen
        if (worldX + gp.TileSize * 10 > gp.player.worldX - gp.player.screenX &&
            worldX - gp.TileSize * 10 < gp.player.worldX + gp.player.screenX &&
            worldY + gp.TileSize * 10 > gp.player.worldY - gp.player.screenY &&
            worldY - gp.TileSize * 10 < gp.player.worldY + gp.player.screenY) {
            
            // Draw the tomb image at screen position
            if (down1 != null) {
                g2.drawImage(down1, screenX, screenY, null);
            }
            
            // Add a glow effect when player has enough keys
            if (gp.player != null && gp.player.hasKey >= 2) {
                g2.setColor(new Color(255, 215, 0, 50)); // Gold transparent
                g2.fillRect(screenX, screenY, gp.TileSize * 10, gp.TileSize * 10);
                
                // Draw pulsing border
                int alpha = (int)(Math.sin(System.currentTimeMillis() * 0.005) * 50 + 100);
                g2.setColor(new Color(255, 215, 0, alpha));
                g2.setStroke(new java.awt.BasicStroke(5));
                g2.drawRect(screenX + 5, screenY + 5, gp.TileSize * 10 - 10, gp.TileSize * 10 - 10);
            }
            
            // Debug: Draw collision area (uncomment for testing)
            // g2.setColor(new Color(255, 0, 0, 100));
            // g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, 
            //             solidArea.width, solidArea.height);
        }
    }
    
    /**
     * Update method called every frame
     */
    @Override
    public void update() {
        // No update needed
    }
}