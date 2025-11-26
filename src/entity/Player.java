package entity;

import main.KeyHandler;
import main.UtilityTool;

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
    public int hasKey = 0;
    int standCounter = 0;

    public Player (GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        screenX = gp.ScreenWidth/2 - (gp.TileSize/2);
        screenY = gp.ScreenHeight/2 - (gp.TileSize/2);

        solidArea = new java.awt.Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32; 

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
        //  try {
        //      up1    = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
        //      up2    = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
        //      down1  = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
        //      down2  = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
        //      left1  = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
        //      left2  = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
        //      right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
        //      right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));

        //  } catch (IOException e) {
        //      e.printStackTrace();
        //  }
            up1 = setup("boy_up_1");
            up2 = setup("boy_up_2");
            down1 = setup("boy_down_1");
            down2 = setup("boy_down_2");
            left1 = setup("boy_left_1");
            left2 = setup("boy_left_2");
            right1 = setup("boy_right_1");
            right2 = setup("boy_right_2");
    }
    public BufferedImage setup(String imageName) {
        
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.TileSize, gp.TileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    public void update() {
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {

            if (keyH.upPressed == true) {
                Direction = "up";
            } 
            if (keyH.downPressed == true) {
                Direction = "down";
            }
            if (keyH.leftPressed == true) {
                Direction = "left";              
            }
            if (keyH.rightPressed == true) {
                Direction = "right";                
            }

            //Check tile collision
            collision = false;
            gp.cChecker.checkTile(this);

            // check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            // if collision is false, player can move

            if (collision == false) {
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
        else {
            standCounter++;
            if(standCounter == 20) {
                spriteNum = 1;
                standCounter = 0;
            }
            spriteNum = 1;
        }
    }
    public void pickUpObject(int i) {

        if(i != 999) {  
            
            String objectNum = gp.obj[i].name;

            switch (objectNum) {
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a Key");
                    break;
                case "Door":
                    gp.playSE(3);
                    if(hasKey > 0) {
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("You opened the door!");
                    } 
                    else {
                        gp.ui.showMessage("You need a Key!");
                    }
                    break;
                case "Boots":
                    gp.playSE(2);
                    speed += 2;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed Up!");
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4); 
                    break;
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
        g2.drawImage(image, screenX, screenY,null);
    }
}