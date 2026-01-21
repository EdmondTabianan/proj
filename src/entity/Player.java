package entity;

import main.KeyHandler;
import object.OBJ_Arrows;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;
import object.OBJ_ice;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;

public class Player extends Entity {

    KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    public Player (GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        type = 0;
        
        screenX = gp.ScreenWidth/2 - (gp.TileSize/2);
        screenY = gp.ScreenHeight/2 - (gp.TileSize/2);

        solidArea = new java.awt.Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32; 

        // attackArea.width = 36;
        // attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }
    public void setDefaultValues() {
        worldX = gp.TileSize * 23;
        worldY = gp.TileSize * 21;
        speed = 4;
        Direction = "down";

        // Player status
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        arrow = 10;
        strength = 1; // the higher the strength, damage is higher.
        dexterity = 1; // the higher the dexterity, less the damage.
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentweapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        arrows = new OBJ_Arrows(gp);
        projectiles = new OBJ_ice(gp);
        // projectiles = new OBJ_Arrows(gp);
        attack = getAttack(); // total damage of weapon
        defense = getDefense(); // total defense 
    }
    public void setItems() {

        inventory.add(currentweapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
    }
    public int getAttack(){
        attackArea = currentweapon.attackArea;
        return attack = strength * currentweapon.attackvalue;
    }
    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }
    public void getPlayerImage() {
            up1 = setup("/player/up_1", gp.TileSize, gp.TileSize);
            up2 = setup("/player/up_2", gp.TileSize, gp.TileSize);
            down1 = setup("/player/down_1", gp.TileSize, gp.TileSize);
            down2 = setup("/player/down_2", gp.TileSize, gp.TileSize);
            left1 = setup("/player/left_1", gp.TileSize, gp.TileSize);
            left2 = setup("/player/left_2", gp.TileSize, gp.TileSize);
            right1 = setup ("/player/right_1", gp.TileSize, gp.TileSize);
            right2 = setup ("/player/right_2", gp.TileSize, gp.TileSize);
    }
    public void getPlayerAttackImage() {
        if (currentweapon.type == type_sword) {
            attackUp1 = setup("/player/boy_attack_up_1", gp.TileSize, gp.TileSize*2);
            attackUp2 = setup("/player/boy_attack_up_2", gp.TileSize, gp.TileSize*2);
            attackDown1 = setup("/player/boy_attack_down_1", gp.TileSize, gp.TileSize*2);
            attackDown2 = setup("/player/boy_attack_down_2", gp.TileSize, gp.TileSize*2);
            attackLeft1 = setup("/player/boy_attack_left_1", gp.TileSize*2, gp.TileSize);
            attackLeft2 = setup("/player/boy_attack_left_2", gp.TileSize*2, gp.TileSize);
            attackRight1 = setup("/player/boy_attack_right_1", gp.TileSize*2, gp.TileSize);
            attackRight2 = setup("/player/boy_attack_right_2", gp.TileSize*2, gp.TileSize);
        }
        if (currentweapon.type == type_axe) {
            attackUp1 = setup("/player/boy_axe_up_1", gp.TileSize, gp.TileSize*2);
            attackUp2 = setup("/player/boy_axe_up_2", gp.TileSize, gp.TileSize*2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.TileSize, gp.TileSize*2);
            attackDown2 = setup("/player/boy_axe_down_2", gp.TileSize, gp.TileSize*2);
            attackLeft1 = setup("/player/boy_axe_left_1", gp.TileSize*2, gp.TileSize);
            attackLeft2 = setup("/player/boy_axe_left_2", gp.TileSize*2, gp.TileSize);
            attackRight1 = setup("/player/boy_axe_right_1", gp.TileSize*2, gp.TileSize);
            attackRight2 = setup("/player/boy_axe_right_2", gp.TileSize*2, gp.TileSize);
        }

        
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
            
            // check interactive tile collision
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            //gp.iTile[iTileIndex].interactve();

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

            if(keyH.enterPressed == true && attackCanceled == false) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
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

        // ice shoot
        if (gp.keyH.shotKeyPressed == true && projectiles.alive == false && 
            shotAvailableCounter == 30 && projectiles.haveResource(this) == true
        ) {

            // set default coordination, direction and user
            projectiles.set(worldX, worldY, Direction, true, this);

            // subtract the cost (mana, arrows, etc)
            projectiles.SubtractResource(this);

            // add it to the list
            gp.projectileList.add(projectiles);

            shotAvailableCounter = 0;
        }
        // arrow shoot
        if (gp.keyH.arrowKeyPressed == true && projectiles.alive == false && 
            shotAvailableCounter == 30 && projectiles.haveResource(this) == true
        ) {

            // set default coordination, direction and user
            arrows.set(worldX, worldY, Direction, true, this);

            // subtract the cost (mana, arrows, etc)
            arrows.SubtractResource(this);

            // add it to the list
            gp.projectileList.add(arrows);

            shotAvailableCounter = 0;

            gp.playSE(7);
        }

        // Invincibility Logic
        if (Invincible == true) {
            InvincibleCounter++;
            if (InvincibleCounter > 60) { // 1 second at 60 FPS
                Invincible = false;
                InvincibleCounter = 0;
            }
        }

        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
            //System.err.println(shotAvailableCounter);
        }
        if (life > maxLife) {
            life = maxLife;
        }
        if (mana > maxMana) {
            mana = maxMana;
        }
        
    }
    public void attacking() {
        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            // save the current worldx, worldy, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;
            // adjust players worldx for the attactarea
            switch (Direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }
            //attackarea become solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            // check monster collision with updated worldx, worldy, and solidarea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

            // after checking collision resotre the original data
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
            //pick up items
            if(gp.obj[i].type == type_pickupOnly) {
                gp.obj[i].use(this);
                gp.obj[i] = null;
            }
            else {  
                String text;
                if(inventory.size() != maxInventorySize) {

                    inventory.add(gp.obj[i]);
                    gp.playSE(1);
                    text = "Got a" + gp.obj[i].name + "!";
                }
                else {
                    text = "you cannot carry any more!";
                }
                gp.ui.showMessage(text);
                gp.obj[i] = null;
            }
        }
    }
    public void interactNPC(int i) {
        if(gp.keyH.enterPressed == true) {
            if (i != 999) {
                    attackCanceled = true;
                    gp.gameState = gp.dialogueState;
                    gp.npc[i].speak();
            }   
        }
    }

    public void contactMonster(int i) {

        if(i != 999) {
            if (Invincible == false && gp.monster[i].dying == false) {
                gp.playSE(6);

                int damage = attack - (gp.monster[i].attack - defense);
                if(damage <= 0) {
                    damage = 0;
                }
                life -= damage;
                Invincible = true;
            }
        }
    }
    public void damageMonster(int i, int attack) {
        if (i != 999) {
            if (gp.monster[i].Invincible == false) {
                gp.playSE(5);

                int damage = attack - gp.monster[i].defense;
                if(damage < 0) {
                    damage = 0;
                }
                gp.monster[i].life -= damage;
                gp.ui.showMessage(damage + "damage!");
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
    public void damageInteractiveTile(int i) {

        if (i != 999 && gp.iTile[i].destructible == true 
            && gp.iTile[i].isCorrectItem(this)== true && gp.iTile[i].Invincible == false) { 
            
            gp.iTile[i].playSE();
            gp.iTile[i].life--;
            gp.iTile[i].Invincible = true;

            if (gp.iTile[i].life <= 0) {
                gp.iTile[i] = gp.iTile[i].getDestroyedForm();
            }
        }
    }

    public void checkLevelUp() {

        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp*2;
            maxLife += 2;
            life += 2;
            maxMana += 1;
            mana += 1;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "you are level " + level + " now!\n"
                + "You fell stronger!";
        }
    }
    public void selectItem() {

        int itemIndex = gp.ui.getItemIndexOnSlot();

        if (itemIndex < inventory.size()) {

            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.type == type_sword || selectedItem.type == type_axe) {

                currentweapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if (selectedItem.type == type_shield) {

                currentShield = selectedItem;
                defense = getDefense();
            }
            if (selectedItem.type  == type_consumable) {
                
                selectedItem.use(this);
                inventory.remove(itemIndex);
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
            if (type == 0) {
                double oneScale = (double)gp.TileSize / maxLife;
                double hpBarValue = oneScale * life;
                double manaScale = (double)gp.TileSize / maxMana;
                double mpBarValue = manaScale * mana;
        
                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 15, gp.TileSize + 2, 12);
        
                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX, screenY - 5, gp.TileSize + 2, 4);
                g2.setColor(new Color(0, 0, 255));
                g2.fillRect(screenX, screenY - 5, (int) mpBarValue, 3);
            }
    }    
}