package entity;

import main.KeyHandler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOError;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;

    public Player (GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        screenX = gp.ScreenWidth/2 - (gp.TileSize/2);
        screenY = gp.ScreenHeight/2 - (gp.TileSize/2);

        setDefaultCloseOperation();
        getPlayerImage();
    }
    public void setDefaultCloseOperation() {
        worldX = gp.TileSize * 23;
        worldY = gp.TileSize * 21;
        speed = 4;
        Direction = "down";
    }
    public void getPlayerImage() {
         try {
             up1    = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
             up2    = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
             down1  = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
             down2  = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
             left1  = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
             left2  = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
             right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
             right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));

         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    public void update() {
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {

            if (keyH.upPressed == true) {
                Direction = "up";
                worldY -= speed;
            } 
            if (keyH.downPressed == true) {
                Direction = "down";
                worldY += speed;
            }
            if (keyH.leftPressed == true) {
                Direction = "left";
                worldX -= speed;
            }
            if (keyH.rightPressed == true) {
                Direction = "right";
                worldX += speed;
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
    }
    public void draw(Graphics g2) {
//        g2.setColor(Color.white);
//        g2.fillRect(x, y, gp.TileSize, gp.TileSize);

        BufferedImage image = null;

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