package entity;

import main.KeyHandler;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Player extends Entity {
    
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;
        
        type = 0;
        characterused = 0; // Default character
        
        screenX = gp.ScreenWidth/2 - (gp.TileSize/2);
        screenY = gp.ScreenHeight/2 - (gp.TileSize/2);
        
        solidArea = new java.awt.Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        
        attackArea.width = 36;
        attackArea.height = 36;
        
        setDefaultValues();
        setCharacterImages(); // Call this to set initial images
        getPlayerAttackImage();
    }
    
    public void setCharacterImages() {
        if (characterused == 0) { // Alexandria
            getAlexandriaImages();
        } else if (characterused == 1) { // Xylo
            getXyloImages();
        }
    }
    
    public void getAlexandriaImages() {
        up1 = setup("/player/up_1", gp.TileSize, gp.TileSize);
        up2 = setup("/player/up_2", gp.TileSize, gp.TileSize);
        down1 = setup("/player/down_1", gp.TileSize, gp.TileSize);
        down2 = setup("/player/down_2", gp.TileSize, gp.TileSize);
        left1 = setup("/player/left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/player/left_2", gp.TileSize, gp.TileSize);
        right1 = setup("/player/right_1", gp.TileSize, gp.TileSize);
        right2 = setup("/player/right_2", gp.TileSize, gp.TileSize);
    }
    
    public void getXyloImages() {
        up1 = setup("/xylo/b_up_1", gp.TileSize, gp.TileSize);
        up2 = setup("/xylo/b_up_2", gp.TileSize, gp.TileSize);
        down1 = setup("/xylo/b_down_1", gp.TileSize, gp.TileSize);
        down2 = setup("/xylo/b_down_2", gp.TileSize, gp.TileSize);
        left1 = setup("/xylo/b_left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/xylo/b_left_2", gp.TileSize, gp.TileSize);
        right1 = setup("/xylo/b_right_1", gp.TileSize, gp.TileSize);
        right2 = setup("/xylo/b_right_2", gp.TileSize, gp.TileSize);
    }
    
    public void getPlayerAttackImage() {
        // Using the same attack images for both characters
        attackUp1 = setup("/player/boy_attack_up_1", gp.TileSize, gp.TileSize*2);
        attackUp2 = setup("/player/boy_attack_up_2", gp.TileSize, gp.TileSize*2);
        attackDown1 = setup("/player/boy_attack_down_1", gp.TileSize, gp.TileSize*2);
        attackDown2 = setup("/player/boy_attack_down_2", gp.TileSize, gp.TileSize*2);
        attackLeft1 = setup("/player/boy_attack_left_1", gp.TileSize*2, gp.TileSize);
        attackLeft2 = setup("/player/boy_attack_left_2", gp.TileSize*2, gp.TileSize);
        attackRight1 = setup("/player/boy_attack_right_1", gp.TileSize*2, gp.TileSize);
        attackRight2 = setup("/player/boy_attack_right_2", gp.TileSize*2, gp.TileSize);
    }
    
    public void setDefaultValues() {
        worldX = gp.TileSize * 23;
        worldY = gp.TileSize * 21;
        Direction = "down";
        
        // Player status - Set different values based on character
        level = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentweapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        
        if (characterused == 0) { // Alexandria - Tank/Defender
            speed = 3; // Slower but tankier
            maxLife = 8; // Higher health
            life = maxLife;
            strength = 2; // Good damage
            dexterity = 3; // High defense
        } else if (characterused == 1) { // Xylo - Agile/Attacker
            speed = 5; // Faster movement
            maxLife = 5; // Lower health
            life = maxLife;
            strength = 3; // Higher damage
            dexterity = 1; // Lower defense
        }
        
        attack = getAttack();
        defense = getDefense();
    }
    
    // Update stats when character is changed
    public void updateCharacterStats() {
        if (characterused == 0) { // Alexandria
            speed = 3;
            maxLife = 8;
            life = maxLife;
            strength = 2;
            dexterity = 3;
        } else if (characterused == 1) { // Xylo
            speed = 5;
            maxLife = 5;
            life = maxLife;
            strength = 3;
            dexterity = 1;
        }
        
        attack = getAttack();
        defense = getDefense();
    }
    
    public int getAttack() {
        return attack = strength * currentweapon.attackvalue;
    }
    
    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }
    
    public void update() {
        if (attacking == true) {
            attacking();
        } else if (keyH.upPressed == true || keyH.downPressed == true || 
                   keyH.leftPressed == true || keyH.rightPressed == true || 
                   keyH.enterPressed == true) {
            
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

            // Check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // Check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check NPC Collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // Check monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);            

            // Check Event
            gp.eHandler.checkEvent();

            // If collision is false, player can move
            if (collisionOn == false && keyH.enterPressed == false) {
                switch (Direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            if (keyH.enterPressed == true && attackCanceled == false) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            gp.keyH.enterPressed = false;

            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            standCounter++;
            if (standCounter == 20) {
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

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;
            
            // Adjust player's worldX for the attack area
            switch (Direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }
            
            // Attack area becomes solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            
            // Check monster collision with updated worldX, worldY, and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            // After checking collision restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
    
    public void pickUpObject(int i) {
        if (i != 999) {  
            // Pick up object logic here
        }
    }
    
    public void interactNPC(int i) {
        if (gp.keyH.enterPressed == true) {
            if (i != 999) {
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (Invincible == false) {
                gp.playSE(6);

                int damage = gp.monster[i].attack - defense;
                if (damage < 0) {
                    damage = 0;
                }
                life -= damage;
                Invincible = true;
            }
        }
    }
    
    public void damageMonster(int i) {
        if (i != 999) {
            if (gp.monster[i].Invincible == false) {
                gp.playSE(5);

                int damage = attack - gp.monster[i].defense;
                if (damage < 0) {
                    damage = 0;
                }
                gp.monster[i].life -= damage;
                gp.ui.showMessage(damage + " damage!");
                gp.monster[i].Invincible = true;
                gp.monster[i].damageReaction();

                if (gp.monster[i].life <= 0) {
                    gp.monster[i].dying = true;
                    gp.ui.showMessage("Killed the " + gp.monster[i].name + "!");
                    gp.ui.showMessage("exp + " + gp.monster[i].exp);
                    exp += gp.monster[i].exp;
                    checkLevelUp();
                }
            }
        }
    }
    
    public void checkLevelUp() {
        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
            
            // Different level up bonuses based on character
            if (characterused == 0) { // Alexandria
                maxLife += 3; // More health per level
                life = maxLife;
                strength += 1; // Moderate strength increase
                dexterity += 2; // Good defense increase
            } else if (characterused == 1) { // Xylo
                maxLife += 2; // Less health per level
                life = maxLife;
                strength += 2; // Higher strength increase
                dexterity += 1; // Moderate defense increase
            }
            
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n"
                + "You feel stronger!";
        }
    }
    
    public void draw(Graphics2D g2) {
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
        
        g2.drawImage(image, tempScreenX, tempScreenY, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}