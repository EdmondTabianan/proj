package object;

import entity.Entity;
import main.GamePanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;

public class OBJ_Doors extends Entity {
    
    GamePanel gp; // Store GamePanel reference
    
    public OBJ_Doors(GamePanel gp) {
        super(gp);
        this.gp = gp; // Store the reference
        name = "Door";
        type = type_door;
        
        // Create 96x96 door image (4 tiles)
        down1 = createDoorImage();
        
        collision = true;
        
        // Collision covers 4 tiles (96x96)
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 96;   // 2 tiles wide
        solidArea.height = 96;  // 2 tiles high
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    
    private BufferedImage createDoorImage() {
        BufferedImage door = new BufferedImage(96, 96, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = door.createGraphics();
        
        try {
            // Load door images - each should be 48x96 (1 tile wide, 2 tiles tall)
            BufferedImage leftDoor = setup("/objects/door_left", 48, 96);
            BufferedImage rightDoor = setup("/objects/door_right", 48, 96);
            
            if (leftDoor != null && rightDoor != null) {
                // Draw left door (covers left 2 tiles)
                g2.drawImage(leftDoor, 0, 0, null);
                // Draw right door (covers right 2 tiles)
                g2.drawImage(rightDoor, 48, 0, null);
            } else {
                throw new Exception("Door images not found");
            }
        } catch (Exception e) {
            // If images not found, create a simple placeholder door
            g2.setColor(new Color(101, 67, 33));
            g2.fillRect(0, 0, 48, 96);
            
            // Right half - lighter brown
            g2.setColor(new Color(139, 69, 19));
            g2.fillRect(48, 0, 48, 96);
            
            // Border
            g2.setColor(Color.BLACK);
            g2.drawRect(2, 2, 92, 92);
            
            // Draw grid lines to show 4 tiles
            g2.drawLine(48, 0, 48, 96);  // Vertical line between left/right
            g2.drawLine(0, 48, 96, 48);  // Horizontal line between top/bottom
            
            // Label
            g2.setColor(Color.WHITE);
            g2.drawString("DOOR", 30, 50);
        }
        
        g2.dispose();
        return door;
    }
    
    @Override
    public void draw(Graphics2D g2) {
        // Safety check
        if (gp == null || gp.player == null) {
            return;
        }
        
        // Calculate screen position
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        // Check if any part of the door is visible
        // Using 96x96 instead of TileSize (48)
        if (worldX + 96 > gp.player.worldX - gp.player.screenX &&
            worldX - 96 < gp.player.worldX + gp.player.screenX &&
            worldY + 96 > gp.player.worldY - gp.player.screenY &&
            worldY - 96 < gp.player.worldY + gp.player.screenY) {
            
            // Draw the door
            g2.drawImage(down1, screenX, screenY, null);
        }
    }
}