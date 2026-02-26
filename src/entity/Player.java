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

    public KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;
    public int standCounter = 0;
    public boolean attackCanceled = false;
    Random random = new Random();

    public Player (GamePanel gp, KeyHandler keyH, int characterChoice) {
        super(gp);
        this.keyH = keyH;
        
        this.characterused = characterChoice;

        type = 0;

        Direction = "down";
        
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

        // setDefaultValues();
        // getImage();
        // getAttackImage();
        // getGuardImage();
        // setItems();
    }
    public void startPosition() {
        gp.currentMap = 0;
        // worldX = gp.TileSize * 46;
        // worldY = gp.TileSize * 39;
        
        Direction = "down"; // Set direction
        collisionOn = false; // Reset collision
    }
    public void setDefaultValues() {
        startPosition();
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

        killCount = 0;

        getImage();
        getAttackImage();
        getGuardImage();
        setItems();
    }
    // In Player class, add this method:
    public void respawnAtMapEntrance(int mapNum) {
        switch(mapNum) {
            case 0: // Main map - original spawn
                worldX = gp.TileSize * 46;
                worldY = gp.TileSize * 39;
                break;
            case 1: // Second map - boat arrival point
                worldX = gp.TileSize * 24;
                worldY = gp.TileSize * 42;
                break;
            case 2: // Pyramid first floor - entrance
                worldX = gp.TileSize * 24;
                worldY = gp.TileSize * 48;
                break;
            case 3: // Shop - arrival point
                worldX = gp.TileSize * 24;
                worldY = gp.TileSize * 35;
                break;
            case 4: // Pyramid first basement - stairs down
                worldX = gp.TileSize * 4;
                worldY = gp.TileSize * 3;
                break;
            case 5: // Pyramid second basement - stairs down
                worldX = gp.TileSize * 42;
                worldY = gp.TileSize * 44;
                break;
            case 6:
                worldX = gp.TileSize * 48;
                worldY = gp.TileSize * 18;
            default:
                worldX = gp.TileSize * 48;
                worldY = gp.TileSize * 18;
        }
        
        Direction = "down";
        life = maxLife;
        mana = maxMana;
        invincible = true;
        invincibleCounter = 0;
        
        // Reset combat states
        attacking = false;
        knockBack = false;
        guarding = false;    
        spriteCounter = 0;
        spriteNum = 1;
        standCounter = 0;
    }
    public void resetLifeAndMana() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
        attacking = false;
        guarding = false;
        knockBack = false;
    }
    public void setItems() {
        inventory.clear();
        inventory.add(currentweapon);
        inventory.add(currentShield);
        inventory.add(currentRange);
    }
    public int getAttack() {
        attackArea = currentweapon.attackArea;
        motion1_duration = currentweapon.motion1_duration;
        motion2_duration = currentweapon.motion2_duration;
        int baseDamage = strength * currentweapon.attackvalue;
        attack = baseDamage;
        return attack;
    }
    
    // public int getAttack() {
    //     return attack = strength * currentweapon.attackvalue;
    // }
    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }
    public void getImage() {
        if (characterused == 1) {
            up1 = setup("/player/up_1", gp.TileSize, gp.TileSize);
            up2 = setup("/player/up_2", gp.TileSize, gp.TileSize);
            down1 = setup("/player/down_1", gp.TileSize, gp.TileSize);
            down2 = setup("/player/down_2", gp.TileSize, gp.TileSize);
            left1 = setup("/player/left_1", gp.TileSize, gp.TileSize);
            left2 = setup("/player/left_2", gp.TileSize, gp.TileSize);
            right1 = setup ("/player/right_1", gp.TileSize, gp.TileSize);
            right2 = setup ("/player/right_2", gp.TileSize, gp.TileSize);
        }
        if (characterused == 0) {
            up1 = setup("/xylo/b_up_1", gp.TileSize, gp.TileSize);
            up2 = setup("/xylo/b_up_2", gp.TileSize, gp.TileSize);
            down1 = setup("/xylo/b_down_1", gp.TileSize, gp.TileSize);
            down2 = setup("/xylo/b_down_2", gp.TileSize, gp.TileSize);
            left1 = setup("/xylo/b_left_1", gp.TileSize, gp.TileSize);
            left2 = setup("/xylo/b_left_2", gp.TileSize, gp.TileSize);
            right1 = setup ("/xylo/b_right_1", gp.TileSize, gp.TileSize);
            right2 = setup ("/xylo/b_right_2", gp.TileSize, gp.TileSize);

        }   
    }
    public void getAttackImage() {
        if (currentweapon.type == type_sword) {
            if (characterused == 1) {
                attackUp1 = setup("/player/boy_attack_up_1", gp.TileSize, gp.TileSize*2);
                attackUp2 = setup("/player/boy_attack_up_2", gp.TileSize, gp.TileSize*2);
                attackDown1 = setup("/player/boy_attack_down_1", gp.TileSize, gp.TileSize*2);
                attackDown2 = setup("/player/boy_attack_down_2", gp.TileSize, gp.TileSize*2);
                attackLeft1 = setup("/player/boy_attack_left_1", gp.TileSize*2, gp.TileSize);
                attackLeft2 = setup("/player/boy_attack_left_2", gp.TileSize*2, gp.TileSize);
                attackRight1 = setup("/player/boy_attack_right_1", gp.TileSize*2, gp.TileSize);
                attackRight2 = setup("/player/boy_attack_right_2", gp.TileSize*2, gp.TileSize);
            }
            if (characterused == 0) {
                attackUp1 = setup("/xylo/b_attack_up_1", gp.TileSize, gp.TileSize*2);
                attackUp2 = setup("/xylo/b_attack_up_2", gp.TileSize, gp.TileSize*2);
                attackDown1 = setup("/xylo/b_attack_down_1", gp.TileSize, gp.TileSize*2);
                attackDown2 = setup("/xylo/b_attack_down_2", gp.TileSize, gp.TileSize*2);
                attackLeft1 = setup("/xylo/b_attack_left_1", gp.TileSize*2, gp.TileSize);
                attackLeft2 = setup("/xylo/b_attack_left_2", gp.TileSize*2, gp.TileSize);
                attackRight1 = setup("/xylo/b_attack_right_1", gp.TileSize*2, gp.TileSize);
                attackRight2 = setup("/xylo/b_attack_right_2", gp.TileSize*2, gp.TileSize);
            }
        }
        if (currentweapon.type == type_axe) {
            if (characterused == 1) {
                attackUp1 = setup("/player/boy_axe_up_1", gp.TileSize, gp.TileSize*2);
                attackUp2 = setup("/player/boy_axe_up_2", gp.TileSize, gp.TileSize*2);
                attackDown1 = setup("/player/boy_axe_down_1", gp.TileSize, gp.TileSize*2);
                attackDown2 = setup("/player/boy_axe_down_2", gp.TileSize, gp.TileSize*2);
                attackLeft1 = setup("/player/boy_axe_left_1", gp.TileSize*2, gp.TileSize);
                attackLeft2 = setup("/player/boy_axe_left_2", gp.TileSize*2, gp.TileSize);
                attackRight1 = setup("/player/boy_axe_right_1", gp.TileSize*2, gp.TileSize);
                attackRight2 = setup("/player/boy_axe_right_2", gp.TileSize*2, gp.TileSize);
            }
            
            if (characterused == 0) {
                attackUp1 = setup("/xylo/boy_axe_up_1", gp.TileSize, gp.TileSize*2);
                attackUp2 = setup("/xylo/boy_axe_up_2", gp.TileSize, gp.TileSize*2);
                attackDown1 = setup("/xylo/boy_axe_down_1", gp.TileSize, gp.TileSize*2);
                attackDown2 = setup("/xylo/boy_axe_down_2", gp.TileSize, gp.TileSize*2);
                attackLeft1 = setup("/xylo/boy_axe_left_1", gp.TileSize*2, gp.TileSize);
                attackLeft2 = setup("/xylo/boy_axe_left_2", gp.TileSize*2, gp.TileSize);
                attackRight1 = setup("/xylo/boy_axe_right_1", gp.TileSize*2, gp.TileSize);
                attackRight2 = setup("/xylo/boy_axe_right_2", gp.TileSize*2, gp.TileSize);
            }
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
    public void getGuardImage() {
        if (characterused == 1) {
            guardUp = setup("/player/girl_guard_up", gp.TileSize, gp.TileSize);
            guardDown = setup("/player/girl_guard_down", gp.TileSize, gp.TileSize);
            guardLeft = setup("/player/girl_guard_left", gp.TileSize, gp.TileSize); 
            guardRight = setup("/player/girl_guard_right", gp.TileSize, gp.TileSize);
        }
        if (characterused == 0) {
            guardUp = setup("/xylo/b_guard_up_1", gp.TileSize, gp.TileSize);
            guardDown = setup("/xylo/b_guard_down_1", gp.TileSize, gp.TileSize);
            guardLeft = setup("/xylo/b_guard_left_1", gp.TileSize, gp.TileSize); 
            guardRight = setup("/xylo/b_guard_right_1", gp.TileSize, gp.TileSize);
        }
    }
    public void update() {
        // SAFETY CHECK: Ensure projectiles is not null
        if (projectiles == null) {
            System.out.println("WARNING: projectiles was null, reinitializing...");
            if (currentRange != null) {
                if (currentRange.type == type_wand) {
                    projectiles = new OBJ_ice(gp);
                } else if (currentRange.type == type_bow) {
                    projectiles = new OBJ_Arrows(gp);
                } else {
                    projectiles = new OBJ_ice(gp);
                }
            } else {
                projectiles = new OBJ_ice(gp);
            }
        }
        
        if (Direction == null) {
            Direction = "down";
        }
        if (attacking == true) {
            attacking();
            guarding = false; // Can't guard while attacking
        }
        else if (keyH.spacePressed == true) {
            guarding = true;
            // Don't move while guarding
            spriteCounter = 0;
            // Reset movement flags to prevent sliding
            keyH.upPressed = false;
            keyH.downPressed = false;
            keyH.leftPressed = false;
            keyH.rightPressed = false;
        }
        else if (gp.keyH.shotKeyPressed == true && projectiles.alive == false && shotAvailableCounter == 30) {
            // Handle ranged attack
            guarding = false;
            
            // Check if a ranged weapon is equipped
            if (currentRange == null) {
                gp.ui.showMessage("No ranged weapon equipped!");
            }
            else if (projectiles.haveResource(this)) {
                // Set projectile position, direction, and owner
                projectiles.set(worldX, worldY, Direction, true, this);
                
                // Subtract resource (arrow or mana)
                projectiles.SubtractResource(this);
                
                // Check vacancy
                for (int i = 0; i < gp.projectile[1].length; i++) {
                    if (gp.projectile[gp.currentMap][i] == null) {
                        gp.projectile[gp.currentMap][i] = projectiles;
                        break;
                    }
                }
                
                // Reset cooldown
                shotAvailableCounter = 0;
                getAttackImage();
                gp.playSE(7);
            }
        }
        else if (keyH.upPressed == true || keyH.downPressed == true || 
                 keyH.leftPressed == true || keyH.rightPressed == true || 
                 keyH.enterPressed == true) {
            
            guarding = false; // Cancel guard when moving
    
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
            // Only call contactMonster if there's actually a monster (index not 999)
            if (monsterIndex != 999) {
                contactMonster(monsterIndex, gp.monster[gp.currentMap][monsterIndex]);
            } 
            
            // Check interactive tile collision
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
    
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
        }
        else {
            // Idle state
            standCounter++;
            if (standCounter == 20) {
                spriteNum = 1;
                standCounter = 0;
            }
            guarding = false; // Cancel guard when idle
        }
    
        // Projectile cooldown
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    
        // Invincibility Logic
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) { // 1 second at 60 FPS
                invincible = false;
                transparent = false;
                invincibleCounter = 0;
            }
        }
    
        // Cap life and mana
        if (life > maxLife) {
            life = maxLife;
        }
        if (mana > maxMana) {
            mana = maxMana;
        }
    
        // Check for death
        if (life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            invincible = false;
            gp.stopMusic();
            gp.playSE(12);
        }
    }
    
    public void attacking() {
        if (Direction == null) {
            Direction = "down";
        }
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
            if (monsterIndex != 999) {
                damageMonster(monsterIndex, this, attack, knockBackPower);
            }
    
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            if (iTileIndex != 999) {
                damageInteractiveTile(iTileIndex);
            }
    
            int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
            if (projectileIndex != 999) {
                damageProjectile(projectileIndex);
            }
    
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
            Entity item = gp.obj[gp.currentMap][i];
            
            // Add null check for item
            if (item == null) return;
    
            // Mark as picked up in AssetSetter - with null check for aSetter
            if (item.isPickup && gp.aSetter != null) {
                gp.aSetter.markItemAsPickedUp(gp.currentMap, i);
            }
            
            // Check for key
            if (item.type == type_key) {
                gp.playSE(2);
                hasKey++;
                gp.ui.showMessage("You got a key!");
                gp.obj[gp.currentMap][i] = null;
                return;
            }
            // For other pickup-only items
            else if (item.type == type_pickupOnly) {
                item.use(this);
                gp.obj[gp.currentMap][i] = null;
                return;
            }
            // Handle ARROWS pickup (stackable)
            else if (item.type == type_arrows) {
                gp.playSE(2);
                
                if (item.stackable) {
                    int existingIndex = searchItemInInventory(item.name);
                    if (existingIndex != -1) {
                        // Add to existing stack
                        inventory.get(existingIndex).amount += item.amount;
                        gp.ui.showMessage("Got " + item.amount + " arrows! Total: " + inventory.get(existingIndex).amount);
                    } else {
                        // New stack
                        inventory.add(item);
                        gp.ui.showMessage("Got " + item.amount + " arrows!");
                    }
                }
                
                gp.obj[gp.currentMap][i] = null;
                return;
            }
            else if (item.type == type_door) {
                // Don't pick up door, just interact with it
                return;
            }
            // Check for tablet
            else if (item.type == type_tablet) {
                gp.playSE(2);
                
                if (inventory.size() < maxInventorySize) {
                    inventory.add(item);
                    hasTablet = true;
                    gp.ui.showMessage("You picked " + item.name);
                    gp.obj[gp.currentMap][i] = null;
    
                    if (hasTablet == true) {
                        // Spawn axe
                        for (int j = 0; j < gp.obj.length; j++) {
                            if (gp.obj[gp.currentMap][j] == null) {
                                gp.obj[gp.currentMap][j] = new OBJ_Axe(gp);
                                gp.obj[gp.currentMap][j].worldX = gp.TileSize * 18;
                                gp.obj[gp.currentMap][j].worldY = gp.TileSize * 6;
                                break;
                            }
                        }
                    }
                }
                return;
            }
            else {
                // For regular inventory items
                if (canObtainItem(item)) {
                    if (item.stackable) {
                        int existingIndex = searchItemInInventory(item.name);
                        if (existingIndex != -1) {
                            // Stack with existing
                            inventory.get(existingIndex).amount += item.amount;
                            gp.playSE(1);
                            gp.ui.showMessage("Got " + item.amount + " " + item.name + 
                                            "! Total: " + inventory.get(existingIndex).amount);
                        } else {
                            // New stack
                            inventory.add(item);
                            gp.playSE(1);
                            gp.ui.showMessage("Got a " + item.name + "!");
                        }
                    } else {
                        // Non-stackable item
                        inventory.add(item);
                        gp.playSE(1);
                        gp.ui.showMessage("Got a " + item.name + "!");
                    }
                } else {
                    gp.ui.showMessage("You cannot carry any more!");
                    return; // Don't remove item if inventory full
                }
                
                gp.obj[gp.currentMap][i] = null;
            }
        }
    }

    public void interactNPC(int i) {
        if(gp.keyH.enterPressed == true) {
            if (i != 999) {
                attackCanceled = true;
                gp.npc[gp.currentMap][i].speak();
                gp.keyH.enterPressed = false; 
            }   
        }
    }

    public void contactMonster(int i, Entity monster) {
        if(i != 999 && monster != null) {
            
            // SAFETY CHECK: Ensure monster's Direction is not null
            if (monster.Direction == null) {
                monster.Direction = "down";
            }
            
            // Check if player is NOT invincible AND monster is NOT dying
            if (invincible == false && monster.dying == false) {
                
                // Calculate base damage
                int damage = monster.attack - defense;
                if(damage < 1) {
                    damage = 1;
                }
                
                // CHECK GUARD HERE!
                if (guarding == true) {
                    // Check if monster is attacking from the EXACT direction player is facing
                    // Get the direction the attack is coming from (monster's direction)
                    String attackDirection = monster.Direction;
                    
                    // Check if player is facing the exact same direction as monster (blocking)
                    // Player guards in their Direction, so if monster is in that direction, guard works
                    if (Direction != null && Direction.equals(attackDirection)) {
                        // PERFECT GUARD - NO DAMAGE! + KNOCKBACK MONSTER
                        gp.playSE(15); // Guard sound
                        gp.ui.showMessage("Perfect Guard! No damage!");
                        
                        // KNOCKBACK THE MONSTER!
                        int knockbackPower = 3; // Base knockback power
                        
                        // If player has a shield, use its knockback power
                        if (currentShield != null) {
                            knockbackPower = currentShield.knockBackPower;
                        }
                        
                        // Apply knockback to monster
                        setKnockBack(monster, this, knockbackPower);
                        
                        // DO NOT set invincible or transparent since no damage taken
                        return; // Exit without applying damage or invincibility
                        
                    } else {
                        // Guard failed - monster attacking from wrong direction
                        gp.playSE(6); // Hurt sound
                        gp.ui.showMessage(damage + " damage!");
                        
                        // Apply knockback to PLAYER when guard fails
                        int knockbackPower = 3;
                        if (monster.knockBackPower > 0) {
                            knockbackPower = monster.knockBackPower;
                        }
                        setKnockBack(this, monster, knockbackPower);
                        
                        // Apply damage
                        life -= damage;
                        
                        // Set invincibility and transparency only when taking damage
                        invincible = true;
                        transparent = true;
                    }
                } else {
                    // Not guarding
                    gp.playSE(6); // Hurt sound
                    gp.ui.showMessage(damage + " damage!");
                    
                    // Apply knockback to PLAYER when not guarding
                    int knockbackPower = 3;
                    if (monster.knockBackPower > 0) {
                        knockbackPower = monster.knockBackPower;
                    }
                    setKnockBack(this, monster, knockbackPower);
                        
                    // Apply damage
                    life -= damage;
                        
                    // Set invincibility and transparency only when taking damage
                    invincible = true;
                    transparent = true;
                }
            }
        }
    }

    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {
        if (i != 999) {
            if (gp.monster[gp.currentMap][i].invincible == false) {
                gp.playSE(5);
    
                // Get knockBackPower from the current weapon
                knockBackPower = 0;
                if (currentweapon != null) {
                    knockBackPower = currentweapon.knockBackPower;
                }
    
                // Apply knockback if power > 0
                if (knockBackPower > 0) {
                    setKnockBack(gp.monster[gp.currentMap][i],attacker, knockBackPower);
                }
            
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if(damage < 0) {
                    damage = 1; // Minimum 1 damage
                }
                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.showMessage(damage + " damage!");
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].invincibleCounter = 0;
                gp.monster[gp.currentMap][i].hpBarOn = true;
                gp.monster[gp.currentMap][i].hpBarCounter = 0;
                gp.monster[gp.currentMap][i].damageReaction();
    
                if (gp.monster[gp.currentMap][i].life <= 0) {
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.showMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.showMessage("exp + " + gp.monster[gp.currentMap][i].exp);
                    exp += gp.monster[gp.currentMap][i].exp;
                    killCount++;
                    checkLevelUp();
                }
            }
        }
    }
    // public void knockBack(Entity entity, int knockBackPower) {

    //     entity.Direction = Direction;
    //     entity.speed += knockBackPower; 
    //     entity.knockBack = true;
    //     entity.knockBackCounter = 0;
    // }
    public void damageInteractiveTile(int i) {
        if (i != 999 && gp.iTile[gp.currentMap][i].destructible == true 
            && gp.iTile[gp.currentMap][i].isCorrectItem(this)== true && gp.iTile[gp.currentMap][i].invincible == false) { 
            
            gp.iTile[gp.currentMap][i].playSE();
            gp.iTile[gp.currentMap][i].life--;
            gp.iTile[gp.currentMap][i].invincible = true;
            gp.iTile[gp.currentMap][i].invincibleCounter = 0;

            if (gp.iTile[gp.currentMap][i].life <= 0) {
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }
        }
    }
    
    public void damageProjectile(int i) {
        if (i != 999) {
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false;
            gp.playSE(14);
        }
    }
    
    public void checkLevelUp() {
        if (exp >= nextLevelExp) {
            level++;
            gp.ui.showMessage("You leveled up! You are\nlevel " + level + " now!");
            gp.playSE(8);
            exp -= nextLevelExp;
            nextLevelExp += level * 2;
    
            maxLife += 2;
            life += 2;
    
            if (level % 3 == 0) {
                maxMana += 1;
                mana += 1;
            }
             
            if (level % 2 == 0) strength+=1;
            if (level % 5 == 0) dexterity+=2;
    
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
                getAttackImage();
            }
            else if (selectedItem.type == type_bow) {
                currentRange = selectedItem;
                projectiles = new OBJ_Arrows(gp);  // Re-initialize projectiles for bow
                getAttackImage();
            }
            else if (selectedItem.type == type_wand) {
                currentRange = selectedItem;
                projectiles = new OBJ_ice(gp);     // Re-initialize projectiles for wand
                getAttackImage();
            }
            else if (selectedItem.type == type_shield) {
                currentShield = selectedItem;
                defense = getDefense();
            }
            else if (selectedItem.type == type_consumable) {
                selectedItem.use(this);
                
                // Decrement amount for stackable items
                if (selectedItem.stackable) {
                    selectedItem.amount--;
                    if (selectedItem.amount <= 0) {
                        inventory.remove(itemIndex);
                    }
                } else {
                    inventory.remove(itemIndex);
                }
            }
            else if (selectedItem.type == type_pickupOnly) {
                // Handle pickup-only items (keys, etc.)
            }
        }
    }
    public int searchItemInInventory(String itemName) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).name.equals(itemName)) {
                return i;  // Return the actual index when found
            }
        }
        return -1; 
        // int itemIndex = 999;
        // for (int i = 0; i < inventory.size(); i++) {
        //     if (inventory.get(i).name.equals(itemName)) {
        //         itemIndex = i;
        //         break;
        //     }
        // }
        // return -1; // Item not found
    }

    public boolean canObtainItem(Entity item) {
        boolean canObtain = false;
    
        if (item.stackable == true) {
            // For stackable items, check if we already have one to stack with
            int index = searchItemInInventory(item.name);
            if (index != -1) {
                canObtain = true;
            } else {
                // No existing stack, check if inventory has space
                if (inventory.size() < maxInventorySize) {
                    canObtain = true;
                }
            }
        } else {
            // For non-stackable items, just check inventory space
            if (inventory.size() < maxInventorySize) {
                canObtain = true;
            }
        }
        
        return canObtain; // Missing return statement!
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
                if (guarding == true) {
                    image = guardUp;
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
                if (guarding == true) {
                    image = guardDown;
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
                if (guarding == true) {
                    image = guardLeft;
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
                if (guarding == true) {
                    image = guardRight;
                }
                break;
        }

        if (transparent == true) {
            // Flash effect: only show player every other frame for blinking effect
            if (invincibleCounter % 10 < 5) { // Blink every 5 frames
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            } else {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        } 

        if (drawing == true) {
            g2.drawImage(image, tempScreenX, tempScreenY, null);  
            
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
        
        // Reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
        
    }    
}