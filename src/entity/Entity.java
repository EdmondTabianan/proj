package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

    GamePanel gp;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public Rectangle solidArea = new Rectangle(0, 0, 45, 45);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    String dialogues[] = new String[20];
    public BufferedImage image, image2, image3;
    
    // State
    public int worldX, worldY;
    public String Direction = "down";
    public int spriteNum = 1;
    int dialoguesIndex = 0;
    public boolean collisionOn = false;
    public boolean Invincible = false;
    boolean attacking = false;

    // counter 
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int InvincibleCounter = 0;

    // characte attri
    public int type; // 0 = player 1 = npc 2 = mob
    public String name;
    public int speed;
    public int maxLife;
    public int life;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    public void setAction() {}
    public void speak() {

        if (dialogues[dialoguesIndex] == null) {
            dialoguesIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialoguesIndex];
        dialoguesIndex++;

        switch (gp.player.Direction) {
            case "up":
                Direction = "down";
                break;
            case "down":
                Direction = "up";
                break;
            case "left":
                Direction = "right";
                break;
            case "right":
                Direction = "left";
                break;
        }
    }
    public void update(){
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == 2 && contactPlayer == true) {
            if (gp.player.Invincible == false) {
                gp.player.life -= 1;
                gp.player.Invincible = true; 
            }
        }
        
        if (collisionOn == false) {
            switch (Direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
                default:
                    break;
            }
        }
        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {


                switch (Direction) {
                    case "up":
                        if (spriteNum == 1) {
                            image = up1;
                        }
                        if (spriteNum == 2) {
                            image = up2;
                        }
                        break;
                    case "down":
                        if (spriteNum == 1) {
                            image = down1;
                        }
                        if (spriteNum == 2) {
                            image = down2;
                        }
                        break;
                    case "left":
                        if (spriteNum == 1) {
                            image = left1;
                        }
                        if (spriteNum == 2) {
                            image = left2;
                        }
                        break;
                    case "right":
                        if (spriteNum == 1) {
                            image = right1;
                        }   
                        if (spriteNum == 2) {
                            image = right2;
                        }
                        break;
                    default:
                        break;
                }
            g2.drawImage(image, screenX, screenY, gp.TileSize, gp.TileSize, null);
        }
    }
    public BufferedImage setup(String imagePath, int width, int height) {
        
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}