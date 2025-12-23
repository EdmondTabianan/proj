package entity;

import main.KeyHandler;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Player extends Entity {

    KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;
    int standCounter = 0;

    public Player (GamePanel gp, KeyHandler keyH) {
        super(gp);
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

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues() {
        worldX = gp.TileSize * 23;
        worldY = gp.TileSize * 21;
        speed = 4;
        Direction = "down";

        // Player status
        maxLife = 20;
        life = maxLife;
    }
    public void getPlayerImage() {
            //sidle1 = setup("/player/boy_idle_1.png");
            // up1 = setup("/player/boy_up_1");
            // up2 = setup("/player/boy_up_2");
            up1 = setup("/player/up_1");
            up2 = setup("/player/up_2");
            // down1 = setup("/player/boy_down_1");
            // down2 = setup("/player/boy_down_2");
            down1 = setup("/player/down_1");
            down2 = setup("/player/down_2");
            // left1 = setup("/player/boy_left_1");
            // left2 = setup("/player/boy_left_2");
            left1 = setup("/player/left_1");
            left2 = setup("/player/left_2");
            right1 = setup("/player/boy_right_1");
            right2 = setup("/player/boy_right_2");
            // right1 = setup (null);
            // right2 = setup( null);
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
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check NPC Collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // check monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);            

            // Check Event
            gp.eHandler.checkEvent();

            gp.keyH.enterPressed =false;

            // if collision is false, player can move
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
        // Invincibility Logic
        if (Invincible == true) {
            InvincibleCounter++;
            if (InvincibleCounter > 60) { // 1 second at 60 FPS
                Invincible = false;
                InvincibleCounter = 0;
            }
        }

    }
    public void pickUpObject(int i) {

        if(i != 999) {  
            
        }
    }
    public void interactNPC(int i) {
        if (i != 999) {
           
            if(gp.keyH.enterPressed == true) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
            
        }
    }

    public void contactMonster(int i) {

        if(i != 999) {
            if (Invincible == false) {
                life -= 1;
                Invincible = true;
            }
        }
    }
    public void draw(Graphics2D g2) {
    //    g2.setColor(Color.white);
    //    g2.fillRect(x, y, gp.TileSize, gp.TileSize);

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
        }

        if (Invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
            g2.drawImage(image, screenX, screenY,null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            // g2.setFont(new Font("arial", Font.PLAIN, 24));
            // g2.setColor(Color.white);
            // g2.drawString("Invible" + InvincibleCounter, 10, 400);
    }    
}