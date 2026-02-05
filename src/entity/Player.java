package entity;

import main.KeyHandler;
import object.OBJ_Arrows;
import object.OBJ_Axe;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;
import object.OBJ_ice;
import object.OBJ_ice_wand;
import object.OBJ_tablet;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import main.GamePanel;

public class Player extends Entity {

    KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    Random random = new Random();

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
        worldX = gp.TileSize * 46;
        worldY = gp.TileSize * 39;
        // worldX = gp.TileSize * 25;
        // worldY = gp.TileSize * 34;
        defaultSpeed = 4;
        speed = defaultSpeed;
        Direction = "down";

        // Player status
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        arrow = 10;
        strength = 1; // the higher the strength, higher the damage.
        dexterity = 1; // the higher the dexterity, less the damage.
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        hasKey = 0; 
        hasTablet = false;
        currentweapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        currentRange = new OBJ_ice_wand(gp);
        projectiles = new OBJ_ice(gp);
        attack = getAttack(); // total damage of weapon
        defense = getDefense(); // total defense 
    }
    public void setDeaultPosition() {
        worldX = gp.TileSize * 45;
        worldY = gp.TileSize * 40;
        Direction = "down";
    }
    public void resetLifeAndMana() {
        life = maxLife;
        mana = maxMana;
        Invincible = false;
    }
    public void setItems() {
        inventory.clear();
        inventory.add(currentweapon);
        inventory.add(currentShield);
        inventory.add(currentRange);
    }
    public int getAttack() {
        int roll = random.nextInt(100); // 0–99
        attackArea = currentweapon.attackArea;
        int baseDamage = strength * currentweapon.attackvalue;
        if (roll < 80) {
            // Normal hit (80%)
            attack = baseDamage;
        } 
        else if (roll < 95) {
            // Crit hit (15%)
            attack = baseDamage * 2;
        } 
        else {
            // Super crit (5%)
            attack = baseDamage * 4;
        }
        return attack;
    }
    
    // public int getAttack() {
    //     return attack = strength * currentweapon.attackvalue;
    // }
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
        if (currentweapon.type == type_wand) {
            attackUp1 = setup("/player/boy_axe_up_1", gp.TileSize, gp.TileSize*2);
            attackUp2 = setup("/player/boy_axe_up_2", gp.TileSize, gp.TileSize*2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.TileSize, gp.TileSize*2);
            attackDown2 = setup("/player/boy_axe_down_2", gp.TileSize, gp.TileSize*2);
            attackLeft1 = setup("/player/boy_axe_left_1", gp.TileSize*2, gp.TileSize);
            attackLeft2 = setup("/player/boy_axe_left_2", gp.TileSize*2, gp.TileSize);
            attackRight1 = setup("/player/boy_axe_right_1", gp.TileSize*2, gp.TileSize);
            attackRight2 = setup("/player/boy_axe_right_2", gp.TileSize*2, gp.TileSize);
        }
        if (currentweapon.type == type_bow) {
            attackUp1 = setup("/player/boy_attack_up_1", gp.TileSize, gp.TileSize*2);
            attackUp2 = setup("/player/boy_attack_up_2", gp.TileSize, gp.TileSize*2);
            attackDown1 = setup("/player/boy_attack_down_1", gp.TileSize, gp.TileSize*2);
            attackDown2 = setup("/player/boy_attack_down_2", gp.TileSize, gp.TileSize*2);
            attackLeft1 = setup("/player/boy_attack_left_1", gp.TileSize*2, gp.TileSize);
            attackLeft2 = setup("/player/boy_attack_left_2", gp.TileSize*2, gp.TileSize);
            attackRight1 = setup("/player/boy_attack_right_1", gp.TileSize*2, gp.TileSize);
            attackRight2 = setup("/player/boy_attack_right_2", gp.TileSize*2, gp.TileSize);
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
            //gp.iTile[iTileIndex].interactve(); why this error

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

        if (gp.keyH.shotKeyPressed && projectiles.alive == false && 
            shotAvailableCounter == 30 && projectiles.haveResource(this)) {
        
            // Set projectile position, direction, and owner
            projectiles.set(worldX, worldY, Direction, true, this);
        
            // Subtract resource (arrow or mana)
            projectiles.SubtractResource(this);
        
            // check vacancy
            for (int i = 0; i < gp.projectile[1].length; i++) {
                if (gp.projectile[gp.currentMap][i] == null) {
                    gp.projectile[gp.currentMap][i] = projectiles;
                    break;
                }
                if (i == gp.projectile[1].length - 1) {
                    gp.projectile[gp.currentMap][i] = projectiles;
                    break;
                }
            }   
        
            // Reset cooldown
            shotAvailableCounter = 0;
            getPlayerAttackImage();
            gp.playSE(7);  // sound effect
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
        if (life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            Invincible = false;
            gp.stopMusic();
            gp.playSE(12);
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
            damageMonster(monsterIndex, attack, knockBackPower);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

            int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
            damageProjectile(projectileIndex);

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
            // Check for key
            if (gp.obj[gp.currentMap][i].type == type_key) {
                gp.playSE(2);
                hasKey++;
                gp.ui.showMessage("You got a key!");
                gp.obj[gp.currentMap][i] = null;
                return;
            }
            if (gp.obj[gp.currentMap][i].type == type_tablet) {
                gp.playSE(2);
                
                if (inventory.size() < maxInventorySize ) {
                    inventory.add(gp.obj[gp.currentMap][i]);
                    hasTablet = true;
                    gp.ui.showMessage("you pick " + new OBJ_tablet(gp).name);
                    gp.obj[gp.currentMap][i] = null;

                    if (hasTablet == true) {
                        for (int j = 0; j < gp.obj.length; j++) {
                            if (gp.obj[gp.currentMap][j] == null) {
                                gp.obj[gp.currentMap][j] = new OBJ_Axe(gp);
                                gp.obj[gp.currentMap][j].worldX = gp.TileSize * 18;
                                gp.obj[gp.currentMap][j].worldY = gp.TileSize * 6;
                                break;
                            }
                        }
                        return;
                    }
                }
            }
            
            // For other pickup-only items
            if(gp.obj[gp.currentMap][i].type == type_pickupOnly) {
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
                return; // Exit after handling pickup-only
            }
            
            // For regular inventory items
            if(inventory.size() != maxInventorySize) {
                inventory.add(gp.obj[gp.currentMap][i]);
                gp.playSE(1);
                gp.ui.showMessage("Got a " + gp.obj[gp.currentMap][i].name + "!");
            }
            else {
                gp.ui.showMessage("You cannot carry any more!");
            }
            gp.obj[gp.currentMap][i] = null;
        }
    }
    public void interactNPC(int i) {
        if(gp.keyH.enterPressed == true) {
            if (i != 999) {
                    attackCanceled = true;
                    gp.gameState = gp.dialogueState;
                    gp.npc[gp.currentMap][i].speak();
            }   
        }
    }

    // public void contactMonster(int i) {

    //     if(i != 999) {
    //         if (Invincible == false && gp.monster[gp.currentMap][i].dying == false) {
    //             gp.playSE(6);

    //             int damage = attack - (gp.monster[gp.currentMap][i].attack - defense);
    //             if(damage <= 0) {
    //                 damage = 0;
    //             }
    //             life -= damage;
    //             Invincible = true;
    //         }
    //     }
    // }
    
    public void contactMonster(int i) {
        if(i != 999) {
            // Check if player is NOT invincible AND monster is NOT dying
            if (Invincible == false && gp.monster[gp.currentMap][i].dying == false) {
                gp.playSE(6);
                
                // Calculate damage (monster attack minus player defense)
                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if(damage < 0) {
                    damage = 1; // Minimum 1 damage
                }
                
                life -= damage;
                
                // Set invincibility
                Invincible = true;
                InvincibleCounter = 0; // Reset counter
                
                // Show damage message
                gp.ui.showMessage(damage + " damage!");
                
                // Make player immune to further damage for 1 second
                // The InvincibleCounter will handle the timing in the update() method
            }
        }
    }
    
    // public void damageMonster(int i, int attack, int knockBackPower) {
    //     if (i != 999) {
    //         if (gp.monster[gp.currentMap][i].Invincible == false) {
    //             gp.playSE(5);

    //             if (knockBackPower > 0) {
    //                 knockBack(gp.monster[gp.currentMap][i], knockBackPower);
    //             }
            
    //             int damage = attack - gp.monster[gp.currentMap][i].defense;
    //             if(damage < 0) {
    //                 damage = 1; // Minimum 1 damage
    //             }
    //             gp.monster[gp.currentMap][i].life -= damage;
    //             gp.ui.showMessage(damage + " damage!");
    //             gp.monster[gp.currentMap][i].Invincible = true;
    //             gp.monster[gp.currentMap][i].InvincibleCounter = 0; // Reset monster's invincibility counter
    //             gp.monster[gp.currentMap][i].hpBarOn = true;
    //             gp.monster[gp.currentMap][i].hpBarCounter = 0;
    //             gp.monster[gp.currentMap][i].damageReaction();

    //             if (gp.monster[gp.currentMap][i].life <= 0) {
    //                 gp.monster[gp.currentMap][i].dying = true;
    //                 gp.ui.showMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!");
    //                 gp.ui.showMessage("exp + " + gp.monster[gp.currentMap][i].exp);
    //                 exp += gp.monster[gp.currentMap][i].exp;
    //                 checkLevelUp();
    //             }
    //         }
    //     }
    // }
    public void damageMonster(int i, int attack, int knockBackPower) {
        if (i != 999) {
            if (gp.monster[gp.currentMap][i].Invincible == false) {
                gp.playSE(5);
    
                // Get knockBackPower from the current weapon
                knockBackPower = 0;
                if (currentweapon != null) {
                    knockBackPower = currentweapon.knockBackPower;
                    System.out.println("DEBUG: Using " + currentweapon.name + 
                                     " with knockBackPower: " + knockBackPower); // Debug
                }
    
                // Apply knockback if power > 0
                if (knockBackPower > 0) {
                    knockBack(gp.monster[gp.currentMap][i], knockBackPower);
                }
            
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if(damage < 0) {
                    damage = 1; // Minimum 1 damage
                }
                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.showMessage(damage + " damage!");
                gp.monster[gp.currentMap][i].Invincible = true;
                gp.monster[gp.currentMap][i].InvincibleCounter = 0;
                gp.monster[gp.currentMap][i].hpBarOn = true;
                gp.monster[gp.currentMap][i].hpBarCounter = 0;
                gp.monster[gp.currentMap][i].damageReaction();
    
                if (gp.monster[gp.currentMap][i].life <= 0) {
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.showMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.showMessage("exp + " + gp.monster[gp.currentMap][i].exp);
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }
    public void knockBack(Entity entity, int knockBackPower) {

        entity.Direction = Direction;
        entity.speed += knockBackPower; 
        entity.knockBack = true;

    }
    public void damageInteractiveTile(int i) {
        if (i != 999 && gp.iTile[gp.currentMap][i].destructible == true 
            && gp.iTile[gp.currentMap][i].isCorrectItem(this)== true && gp.iTile[gp.currentMap][i].Invincible == false) { 
            
            gp.iTile[gp.currentMap][i].playSE();
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].Invincible = true;
            gp.iTile[gp.currentMap][i].InvincibleCounter = 0;

            if (gp.iTile[gp.currentMap][i].life <= 0) {
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }
        }
    }
    
    public void damageProjectile(int i) {
        if (i != 999) {
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            gp.playSE(9);
        }
    }
    
    public void checkLevelUp() {
        if (exp >= nextLevelExp) {
            level++;
            gp.ui.showMessage("You leveled up! You are\nlevel " + level + " now!");
            gp.playSE(8);
            exp -= nextLevelExp;
            nextLevelExp += level * 5;
    
            maxLife += 2;
            life += 2;
    
            if (level % 3 == 0) {
                maxMana += 1;
                mana += 1;
            }
             
            if (level % 3 == 0) strength++;
            if (level % 5 == 0) dexterity++;
    
            attack = getAttack();
            defense = getDefense();
        }
    }    

    public void selectItem() {
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.type == type_sword || selectedItem.type == type_axe) {
                currentweapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if (selectedItem.type == type_bow) {
                currentRange = selectedItem;
                projectiles = new OBJ_Arrows(gp);  
                getPlayerAttackImage();
            }
            
            if (selectedItem.type == type_wand) {
                currentRange = selectedItem;
                projectiles = new OBJ_ice(gp);     
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
            // Flash effect: only show player every other frame for blinking effect
            if (InvincibleCounter % 10 < 5) { // Blink every 5 frames
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            } else {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
        
        g2.drawImage(image, tempScreenX, tempScreenY, null);
        
        // Reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
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