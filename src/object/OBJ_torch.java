package object;

import entity.Entity;
import main.GamePanel;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class OBJ_torch extends Entity {

    GamePanel gp;
    private int frameDelay = 12;
    private int animStep = 0;

    public OBJ_torch(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        this.type = type_obstacle;  // CHANGED: from type_torch to type_obstacle
        this.name = "Torch";
        this.speed = 0;
        this.collision = true;       // Player can't walk through it
        this.maxLife = 999;        // Indestructible
        this.life = maxLife;
        
        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        this.action = true;
        
        getImage();
    }

    public void getImage() {
        // Load torch animation frames
        up1 = setup("/objects/torch_1", gp.TileSize, gp.TileSize); // mid
        up2 = setup("/objects/torch_2", gp.TileSize, gp.TileSize); // right
        up3 = setup("/objects/torch_3", gp.TileSize, gp.TileSize); // left
        
        // Set all direction images to the same torch frames
        down1 = up1; down2 = up2; down3 = up3;
        left1 = up1; left2 = up2; left3 = up3;
        right1 = up1; right2 = up2; right3 = up3;
        
        spriteNum = 1;
    }
    
    @Override
    public void setAction() {
        spriteCounter++;
        
        if (spriteCounter > frameDelay) {
            // Pattern: 1 (mid) -> 2 (right) -> 1 (mid) -> 3 (left) -> repeat
            if (animStep == 0) {
                spriteNum = 1; // mid
                animStep = 1;
            }
            else if (animStep == 1) {
                spriteNum = 2; // right
                animStep = 2;
            }
            else if (animStep == 2) {
                spriteNum = 1; // mid
                animStep = 3;
            }
            else if (animStep == 3) {
                spriteNum = 3; // left
                animStep = 0;
            }
            
            spriteCounter = 0;
        }
    }
    
    @Override
    public void update() {
        if (!sleep) {
            if (action) {
                setAction();
            }
            
            if (invincible) {
                invincibleCounter++;
                if (invincibleCounter > 60) {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g2) {
        if (gp.player == null) {
            return;
        }
        
        // Calculate screen position
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        // Only draw if on screen
        if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {
            
            BufferedImage image = null;
            
            // Select image based on direction and spriteNum
            switch (Direction) {
                case "up":
                    if (spriteNum == 1) image = up1;
                    else if (spriteNum == 2) image = up2;
                    else if (spriteNum == 3) image = up3;
                    break;
                case "down":
                    if (spriteNum == 1) image = down1;
                    else if (spriteNum == 2) image = down2;
                    else if (spriteNum == 3) image = down3;
                    break;
                case "left":
                    if (spriteNum == 1) image = left1;
                    else if (spriteNum == 2) image = left2;
                    else if (spriteNum == 3) image = left3;
                    break;
                case "right":
                    if (spriteNum == 1) image = right1;
                    else if (spriteNum == 2) image = right2;
                    else if (spriteNum == 3) image = right3;
                    break;
                default:
                    if (spriteNum == 1) image = down1;
                    else if (spriteNum == 2) image = down2;
                    else if (spriteNum == 3) image = down3;
                    break;
            }
            
            // Handle invincibility flash effect
            if (invincible) {
                if (invincibleCounter % 10 < 5) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
            }
            
            // Draw the torch
            if (image != null) {
                g2.drawImage(image, screenX, screenY, null);
            }
            
            // Reset alpha
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
    
   
    public void damage(int damage) {
        // Do nothing - torch is indestructible
        // This prevents any accidental damage
    }
}