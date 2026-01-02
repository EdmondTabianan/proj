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
        getPlayerAttackImage();
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
            up1 = setup("/player/boy_up_1", gp.TileSize, gp.TileSize);
            up2 = setup("/player/boy_up_2", gp.TileSize, gp.TileSize);
            // up1 = setup("/player/up_1");
            // up2 = setup("/player/up_2");
            down1 = setup("/player/boy_down_1", gp.TileSize, gp.TileSize);
            down2 = setup("/player/boy_down_2", gp.TileSize, gp.TileSize);
            // down1 = setup("/player/down_1");
            // down2 = setup("/player/down_2");
            left1 = setup("/player/boy_left_1", gp.TileSize, gp.TileSize);
            left2 = setup("/player/boy_left_2", gp.TileSize, gp.TileSize);
            // left1 = setup("/player/left_1");
            // left2 = setup("/player/left_2");
            right1 = setup("/player/boy_right_1", gp.TileSize, gp.TileSize);
            right2 = setup("/player/boy_right_2", gp.TileSize, gp.TileSize);
            // right1 = setup (null);
            // right2 = setup( null);
    }
    public void getPlayerAttackImage() {
        attackUp1 = setup("/player/boy_attack_up_1", gp.TileSize, gp.TileSize*2);
        attackUp2 = setup("/player/boy_attack_up_2", gp.TileSize, gp.TileSize*2);
        attackDown1 = setup("/player/boy_attack_down_1", gp.TileSize, gp.TileSize*2);
        attackDown2 = setup("/player/boy_attack_down_2", gp.TileSize, gp.TileSize*2);
        attackLeft1 = setup("/player/boy_attack_left_1", gp.TileSize*2, gp.TileSize);
        attackLeft2 = setup("/player/boy_attack_left_2", gp.TileSize*2, gp.TileSize);
        attackRight1 = setup("/player/boy_attack_right_1", gp.TileSize*2, gp.TileSize);
        attackRight2 = setup("/player/boy_attack_right_2", gp.TileSize*2, gp.TileSize);
    }
    public void update() {

        if (attacking == true){
            attacking();
        }
        else if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {

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


            // if collision is false, player can move
            if (collisionOn == false && keyH.enterPressed == false) {
                switch (Direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            gp.keyH.enterPressed =false;

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
    public void attacking() {
        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;
        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
    public void pickUpObject(int i) {

        if(i != 999) {  
            
        }
    }
    public void interactNPC(int i) {
        if(gp.keyH.enterPressed == true) {
            if (i != 999) {
                    gp.gameState = gp.dialogueState;
                    gp.npc[i].speak();
            }
            else {
                    attacking = true;
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
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (Direction) {
            case "up":
                if (attacking == false) {
                    if (spriteNum == 1) { image = up1; }
                    if (spriteNum == 2) { image = up2; }
                }
                if (attacking == true) { 
                    tempScreenY = screenY - gp.TileSize;
                    if (spriteNum == 1) { image = attackUp1; }
                    if (spriteNum == 2) { image = attackUp2; }
                }
                break;
            case "down":
                if (attacking == false) {
                    if (spriteNum == 1) { image = down1; }
                    if (spriteNum == 2) { image = down2; }
                }
                if (attacking == true) { 
                    if (spriteNum == 1) { image = attackDown1; }
                    if (spriteNum == 2) { image = attackDown2; }
                }
                break;
            case "left":
                if (attacking == false) {
                    if (spriteNum == 1) { image = left1; }
                    if (spriteNum == 2) { image = left2; }
                }
                if (attacking == true) { 
                    tempScreenX = screenX - gp.TileSize;
                    if (spriteNum == 1) { image = attackLeft1; }
                    if (spriteNum == 2) { image = attackLeft2; }
                }
               
                break;
            case "right":
                if (attacking == false) {
                    if (spriteNum == 1) { image = right1; }   
                    if (spriteNum == 2) { image = right2; }
                }
                if (attacking == true) { 
                    if (spriteNum == 1) { image = attackRight1; }
                    if (spriteNum == 2) { image = attackRight2; }
                }
                break;
        }

        if (Invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
            g2.drawImage(image, tempScreenX, tempScreenY,null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            // g2.setFont(new Font("arial", Font.PLAIN, 24));
            // g2.setColor(Color.white);
            // g2.drawString("Invible" + InvincibleCounter, 10, 400);
    }    
}