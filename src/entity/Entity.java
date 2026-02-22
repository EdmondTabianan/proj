package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import ai.Node;
import main.GamePanel;
import main.UtilityTool;

public class Entity {

    GamePanel gp;
    public BufferedImage up1, up2, up3, down1, down2, down3 ,left1, left2, left3, right1, right2, right3;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2,
    guardUp,  guardDown, guardLeft, guardRight;
    public Rectangle solidArea = new Rectangle(0, 0, 45, 45);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);    
    public int solidAreaDefaultX, solidAreaDefaultY, solidAreaDefaultWidth, solidAreaHeight;
    public int motion1_duration, motion2_duration;
    public boolean collision = false;
    String dialogues[] = new String[20];
    public BufferedImage image, image2, image3;
    public Entity attacker;
    
    // State
    public int worldX, worldY;
    public String Direction = "down";
    public int spriteNum = 1;
    public int mapnum = 0;
    int dialoguesIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;
    public boolean hasTablet = false;
    public boolean onPath = false;
    public boolean knockBack = false;
    public boolean action = false;
    public boolean talk = false;
    public boolean slowed = false;

    public boolean guarding = false;
    public boolean transparent = false;
    public String knockbackDirection;

    // counter 
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    protected int hpBarCounter = 0;
    int knockBackCounter = 0;
    public int talkcounter = 0;
    public int killCount = 0;
    public int slowCounter = 0;

    // character attri
    public int characterused; // 0 = alexandria 1 = xylo
    public String name;
    public int defaultSpeed;
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int arrow;
    public int level;
    public int strength;
    public int attack;
    public int defense;
    public int dexterity;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentweapon;
    public Entity currentShield;
    public Entity currentRange;
    public Projectile arrows;
    public Projectile projectiles;
    public Color tintColor;

    // item attri
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public int value;
    public int attackvalue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int hasKey;
    public int price = 0;
    public int knockBackPower = 0; 
    public boolean stackable = false;
    public int amount = 1;
    public int slowDuration = 0;
    public int slowAmount = 0;

    // type
    public int type;
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
    public final int type_transport = 8;
    public final int type_key = 9;
    public final int type_tablet = 10;
    public final int type_bow = 11;
    public final int type_wand = 12;
    public final int type_door = 13;
    public final int type_obstacle = 14;
    public final int type_arrows = 15;

    public boolean isPickup;

    // ===== Spawn System =====
    public int spawnWorldX;
    public int spawnWorldY;
    public int pathIndex = 0;

    public Entity(GamePanel gp) {
        this.gp = gp;
        inventory = new ArrayList<>();
    }

    public void getImage() {}

    public int getTileX() {
        return worldX / gp.TileSize;
    }

    public int getTileY() {
        return worldY / gp.TileSize;
    }

    public int getCol(Entity target) {
        int col = (target.worldX + target.solidArea.x) / gp.TileSize;
        return col;
    }

    public int getRow(Entity target) {
        int row = (target.worldY + target.solidArea.y) / gp.TileSize;
        return row;
    }

    public int getCenterX() {
        int centerX = worldX + left1.getWidth()/2;
        return centerX;
    }

    public int getCenterY() {
        int centerY = worldY + up1.getHeight()/2;
        return centerY;
    }

    public int getHeight() {
        int height = solidArea.y + solidArea.height;
        return height;
    }

    public int getwidth() {
        int width = solidArea.x + solidArea.width;
        return width;
    }
    
    public int getXDistance(Entity target) {
        int xDistance = Math.abs(getCenterX() - target.getCenterX());
        return xDistance;

    }
    public int getYDistance(Entity target) {
        int yDistance = Math.abs(getCenterY() - target.getCenterY());
        return yDistance;

    }
    public int gettileDistance (Entity target) {
        int tileDistance = (getXDistance(target) + getYDistance(target)) / gp.TileSize;
        return tileDistance;
    }
    public int getGoalCol(Entity target){
        int goalCol = (target.worldX + target.solidArea.x)/gp.TileSize;
        return goalCol;
    }
    public int getGoalRow(Entity target){
        int goalRow = (target.worldY + target.solidArea.y)/gp.TileSize;
        return goalRow;
    }
    public void setAction() {}
    public void damageReaction() {}
    
    public void speak() {
        if (dialogues[dialoguesIndex] == null) {
            dialoguesIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialoguesIndex];
        dialoguesIndex++;
    }
    public void interact() {

    }
    public void use(Entity entity) {}
    public void checkDrop() {}
    
    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }
    }
    
    public void checkCollision() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        
        // Only check NPC collisions if player exists (for monster pathfinding)
        if (gp.player != null) {
            gp.cChecker.checkEntity(this, gp.npc);
        }
        
        // MONSTERS: Only check collision with player, NOT with other monsters
        if (this.type == type_monster) {
            // Check collision with player only if player exists
            if (gp.player != null) {
                boolean contactPlayer = gp.cChecker.checkPlayer(this);
                if (contactPlayer == true) {
                    damageplayer(attack);
                }
            }
            
            // Check collision with interactive tiles
            gp.cChecker.checkEntity(this, gp.iTile);
        } 
        // PLAYER: Check collision with everything
        else if (this.type == type_player) {
            gp.cChecker.checkEntity(this, gp.npc);
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkEntity(this, gp.iTile);
        }
        // NPCs: Check collision with everything except other monsters
        else if (this.type == type_npc) {
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkEntity(this, gp.iTile);
        }
    }
    
    public void update() {
        // FIX: Ensure Direction is never null at the VERY START
        if (Direction == null) {
            Direction = "down";
        }
        
        if (knockBack == true) {
            // FIX: Add null check for player
            if (gp.player == null) {
                knockBack = false;
                speed = defaultSpeed;
                knockBackCounter = 0;
                return;
            }
            
            // STORE original position BEFORE moving
            int originalX = worldX;
            int originalY = worldY;
    
            // Calculate direction from attacker to this entity
            int dx = 0;
            int dy = 0;
            
            if (attacker != null) {
                dx = worldX - attacker.worldX;
                dy = worldY - attacker.worldY;
            } else {
                // Fallback to player position if attacker is null
                dx = worldX - gp.player.worldX;
                dy = worldY - gp.player.worldY;
            }
    
            // Determine direction AWAY from attacker (for movement)
            String moveDirection = "";
            if (Math.abs(dx) > Math.abs(dy)) {
                // Attacker is more left/right than up/down
                moveDirection = (dx > 0) ? "right" : "left"; // Move AWAY from attacker
            } else {
                // Attacker is more up/down than left/right
                moveDirection = (dy > 0) ? "down" : "up"; // Move AWAY from attacker
            }
    
            // CRITICAL: Set knockbackDirection for CollisionChecker to use
            knockbackDirection = moveDirection;
    
            // Set facing direction TOWARD attacker (look at who hit them)
            if (attacker != null) {
                int facingDx = attacker.worldX - worldX;
                int facingDy = attacker.worldY - worldY;
                
                if (Math.abs(facingDx) > Math.abs(facingDy)) {
                    Direction = (facingDx > 0) ? "right" : "left";
                } else {
                    Direction = (facingDy > 0) ? "down" : "up";
                }
            } else {
                // If no attacker, face opposite of movement direction
                if (moveDirection.equals("up")) Direction = "down";
                else if (moveDirection.equals("down")) Direction = "up";
                else if (moveDirection.equals("left")) Direction = "right";
                else if (moveDirection.equals("right")) Direction = "left";
                else Direction = "down";
            }
    
            // MOVE away from attacker using PURE knockBackPower
            int moveDistance = knockBackPower;
            
            switch (moveDirection) {
                case "up":
                    worldY -= moveDistance;
                    break;
                case "down":
                    worldY += moveDistance;
                    break;
                case "left":
                    worldX -= moveDistance;
                    break;
                case "right":
                    worldX += moveDistance;
                    break;
            }
    
            // CHECK collision AFTER moving
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, false);
    
            // Check for interactive tiles (for monsters)
            if (this.type == type_monster) {
                gp.cChecker.checkEntity(this, gp.iTile);
            }
    
            // If collision detected with a solid tile or object, REVERT position
            if (collisionOn == true) {
                worldX = originalX;
                worldY = originalY;
            }
    
            // Check for collision with player during knockback (for monsters)
            if (this.type == type_monster && collisionOn == false && gp.player != null) {
                boolean contactPlayer = gp.cChecker.checkPlayer(this);
                if (contactPlayer == true && gp.player.invincible == false) {
                    damageplayer(attack);
                }
            }
    
            // Update knockback timer
            knockBackCounter++;
            if (knockBackCounter > 20) {
                knockBack = false;
                speed = defaultSpeed;
                knockBackCounter = 0;
                attacker = null;
                knockbackDirection = null; // Clear knockback direction
            }
        } 
        else if (attacking == true) {
            attacking();
        }
        
        else {
            // Normal movement when not in knockback
            if (action == true) {
                setAction();
                checkCollision();
    
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
                if (spriteCounter > 24) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
                }
            } else {
                idle();
            }
        }
    
        // Update invincibility timer
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    
        // SPRITE ANIMATION
        // spriteCounter++;
        // if (spriteCounter > 24) {
        //     if (spriteNum == 1) {
        //         spriteNum = 2;
        //     } else if (spriteNum == 2) {
        //         spriteNum = 1;
        //     }
        //     spriteCounter = 0;
        // }
    
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }
    
    // public void update(){
    //     if (knockBack == true) {    
    //         // FIX: Add null check for player
    //         if (gp.player == null) {
    //             knockBack = false;
    //             speed = defaultSpeed;
    //             knockBackCounter = 0;
    //             return;
    //         }
    //         else {
    //             if (action == true) {
    //                 // CRITICAL FIX: Only call setAction() if NOT on a path
    //                 // This prevents overwriting the pathfinding direction
    //                 if (!onPath) {
    //                     setAction();
    //                 }
                    
    //                 checkCollision();
        
    //                 if (collisionOn == false) {
    //                     switch (Direction) {
    //                         case "up": worldY -= speed; break;
    //                         case "down": worldY += speed; break;
    //                         case "left": worldX -= speed; break;
    //                         case "right": worldX += speed; break;
    //                         case "idle": break; // Don't move when idle
    //                     }
    //                 } else {
    //                     // If we hit something while pathfinding, recalculate path
    //                     if (onPath) {
                            
    //                     }
    //                 }
    //             } else {
    //                 idle();
    //             }
    //         }
            
    //         // STORE original position BEFORE moving
    //         int originalX = worldX;
    //         int originalY = worldY;
            
    //         // Calculate knockback force (decreases over time)
    //         float knockbackForce = knockBackPower * (1 - (knockBackCounter / 20f));
    //         if (knockbackForce < 1) knockbackForce = 1;
            
    //         int moveDistance = (int)(speed + knockbackForce);
            
    //         // Determine direction AWAY from player
    //         String knockbackDirection = "";
            
    //         // Calculate direction from monster to player
    //         int dx = gp.player.worldX - worldX;
    //         int dy = gp.player.worldY - worldY;
            
    //         // Set knockback direction opposite to where player is
    //         if (Math.abs(dx) > Math.abs(dy)) {
    //             // Player is more left/right than up/down
    //             if (dx > 0) {
    //                 // Player is to the RIGHT, so knockback LEFT
    //                 knockbackDirection = "left";
    //             } else {
    //                 // Player is to the LEFT, so knockback RIGHT
    //                 knockbackDirection = "right";
    //             }
    //         } else {
    //             // Player is more up/down than left/right
    //             if (dy > 0) {
    //                 // Player is BELOW, so knockback UP
    //                 knockbackDirection = "up";
    //             } else {
    //                 // Player is ABOVE, so knockback DOWN
    //                 knockbackDirection = "down";
    //             }
    //         }
            
    //         // Store the knockback direction
    //         Direction = knockbackDirection;
            
    //         // MOVE away from player
    //         switch (knockbackDirection) {
    //             case "up": worldY -= moveDistance; break;
    //             case "down": worldY += moveDistance; break;
    //             case "left": worldX -= moveDistance; break;
    //             case "right": worldX += moveDistance; break;
    //         }
            
    //         // CHECK collision AFTER moving
    //         collisionOn = false;
    //         gp.cChecker.checkTile(this);
    //         gp.cChecker.checkObject(this, false);
            
    //         // Check for interactive tiles (for monsters)
    //         if (this.type == type_monster) {
    //             gp.cChecker.checkEntity(this, gp.iTile);
    //         }
            
    //         // If collision detected with a solid tile or object, REVERT position
    //         // BUT keep knockback active so it can try to move in a different direction
    //         if (collisionOn == true) {
    //             worldX = originalX;
    //             worldY = originalY;
                
    //             // Try to slide along the wall instead of stopping completely
    //             // This prevents getting stuck
    //             if (knockbackDirection.equals("up") || knockbackDirection.equals("down")) {
    //                 // Try moving left/right instead
    //                 worldX += (knockbackDirection.equals("up") ? -moveDistance : moveDistance) / 2;
    //                 worldY = originalY;
                    
    //                 // Check if this new position is valid
    //                 collisionOn = false;
    //                 gp.cChecker.checkTile(this);
    //                 gp.cChecker.checkObject(this, false);
                    
    //                 if (collisionOn == true) {
    //                     // If still colliding, revert completely
    //                     worldX = originalX;
    //                 }
    //             } 
    //             else if (knockbackDirection.equals("left") || knockbackDirection.equals("right")) {
    //                 // Try moving up/down instead
    //                 worldY += (knockbackDirection.equals("left") ? -moveDistance : moveDistance) / 2;
    //                 worldX = originalX;
                    
    //                 // Check if this new position is valid
    //                 collisionOn = false;
    //                 gp.cChecker.checkTile(this);
    //                 gp.cChecker.checkObject(this, false);
                    
    //                 if (collisionOn == true) {
    //                     // If still colliding, revert completely
    //                     worldY = originalY;
    //                 }
    //             }
    //         }
            
    //         // Check for collision with player during knockback (for monsters)
    //         if (this.type == type_monster && collisionOn == false && gp.player != null) {
    //             boolean contactPlayer = gp.cChecker.checkPlayer(this);
    //             if (contactPlayer == true && gp.player.invincible == false) {
    //                 damageplayer(attack);
    //             }
    //         }
            
    //         // Update knockback timer
    //         knockBackCounter++;
    //         if(knockBackCounter > 20) {
    //             knockBack = false;
    //             speed = defaultSpeed;
    //             knockBackCounter = 0;
    //         }
    //     }
    //     else {
    //         if (action == true) {
    //             setAction();
    //             checkCollision();
    
    //             if (collisionOn == false) {
    //                 switch (Direction) {
    //                     case "up": worldY -= speed; break;
    //                     case "down": worldY += speed; break;
    //                     case "left": worldX -= speed; break;
    //                     case "right": worldX += speed; break;
    //                 }
    //             }
    //         } else {
    //             idle();
    //         }
    //     }
        
    //     // Update invincibility timer
    //     if (invincible == true) {
    //         invincibleCounter++;
    //         if (invincibleCounter > 60) {
    //             invincible = false;
    //             invincibleCounter = 0;
    //         }
    //     }
        
    //     // SPRITE ANIMATION
    //     spriteCounter++;
    //     if (spriteCounter > 24) {
    //         if (spriteNum == 1) {
    //             spriteNum = 2;
    //         } else if (spriteNum == 2) {
    //             spriteNum = 1;
    //         }
    //         spriteCounter = 0;
    //     }
    
    //     if (shotAvailableCounter < 30) {
    //         shotAvailableCounter++;
    //     }
    // }

    public void idle () {
        //nothing
    }
    public void checkAttackOrNot(int rate, int straight, int horizontal) {

        boolean targetInRange = false;
        int xDis = getXDistance(gp.player);
        int yDis = getYDistance(gp.player);
    
        switch (Direction) {
            case "up":
                if (gp.player.getCenterY() < getCenterY() && yDis < horizontal && xDis < straight) {
                    targetInRange = true;
                }
                break;
            case "down":
                if (gp.player.getCenterY() > getCenterY() && yDis < horizontal && xDis < straight) {
                    targetInRange = true;
                }
                break;
            case "left":
                if (gp.player.getCenterX() < getCenterX() && xDis < straight && yDis < horizontal) {
                    targetInRange = true; 
                }
                break;
            case "right":
                // FIX: Changed from getAttack() to getCenterX()
                if (gp.player.getCenterX() > getCenterX() && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
                break;
        }
    
        if (targetInRange == true) {
            int i = new Random().nextInt(rate);
            if (i == 0) {
                attacking = true;
                spriteCounter = 0;
                shotAvailableCounter = 0;
            }
        }
    }
    public void checkShootOrNot(int rate, int shotInterval) {
        int shootChance = new Random().nextInt(rate);
            
            // Check if cooldown is ready (2 seconds = 120 frames at 60 FPS)
            if (shootChance == 0 && projectiles.alive == false && shotAvailableCounter == shotInterval) {

                    projectiles.set(getCenterX(), getCenterY(), Direction, true, this);
                    // check vacancy before adding
                    for (int ii = 0; ii < gp.projectile[1].length; ii++) {
                        if (gp.projectile[gp.currentMap][ii] == null) {
                            gp.projectile[gp.currentMap][ii] = projectiles;
                            break;
                        }
                    }
                    shotAvailableCounter = 0; // Reset cooldown
            }
    }
    public void checkStartChasingOrNot(Entity target, int distance, int rate) {
        
        if (gettileDistance(target) < distance ) {
            int i = new Random().nextInt(rate);
            if (i == 0) {
                onPath = true;
            }
        }
    }
    public void checkStopChasingOrNot(Entity target, int distance, int rate) {
        
        if (gettileDistance(target) > distance ) {
            int i = new Random().nextInt(rate);
            if (i == 0) {
                onPath = false;
            }
        }
    }
    public void getRandomDirection() {
        actionLockCounter++;
        if(actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100)+1; //pick up numbner from 1 - 100
            
            if (i <=25) { Direction = "up"; }
            if (i >=25 && i <= 50) { Direction = "down"; } 
            if (i >=50  && i <= 75) { Direction = "left"; }
            if (i >= 75 && i <= 100) { Direction = "right"; }
            actionLockCounter = 0;
        }
    }
    public void attacking() {
        spriteCounter++;

        if (spriteCounter <= motion1_duration) {
            spriteNum = 1;
        }
        if (spriteCounter > motion1_duration && spriteCounter <= motion2_duration) {
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

            if (type == type_monster) {
                if (gp.cChecker.checkPlayer(this) == true) {
                    damageplayer(attack);
                }
            }
            else {
                // check monster collision with updated worldx, worldy, and solidarea
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                gp.player.damageMonster(monsterIndex, this, attack, knockBackPower);

                int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
                gp.player.damageInteractiveTile(iTileIndex);

                int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
                gp.player.damageProjectile(projectileIndex);
            }
            

            // after checking collision resotre the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > motion2_duration) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        } 
    }
    
    public void damageplayer(int attack) {
        int screenX = worldX;
        int screenY = worldY;

        if (gp.player != null) {
            screenX = worldX - gp.player.worldX + gp.player.screenX;
            screenY = worldY - gp.player.worldY + gp.player.screenY;
        }

        // Only damage player if player is not invincible
        if (gp.player.invincible == false) {
            
            int damage = attack - gp.player.defense;
            if (damage < 1) {
                damage = 1;
            }
            
            // Get the opposite direction for guarding
            String canGuardDirection = getOppositeDirection(Direction);
            
            // Check if player is guarding in the correct direction
            if (gp.player.guarding == true && gp.player.Direction.equals(canGuardDirection)) {
                // PERFECT GUARD - NO DAMAGE!
                gp.playSE(15); // Guard sound
                gp.ui.showMessage("Perfect Guard! No damage!");
                
                // NO DAMAGE - DO NOT set invincible or transparent
                return; // EXIT WITHOUT APPLYING DAMAGE OR EFFECTS
                
            } else {
                // Guard failed or not guarding - TAKE DAMAGE
                gp.playSE(6); // Hurt sound
                gp.ui.showMessage(damage + " damage!");
                
                // Apply damage and effects
                gp.player.life -= damage;
                gp.player.transparent = true;
                gp.player.invincible = true;
                gp.player.invincibleCounter = 0;
            }
            
            if (gp.player.life <= 0) {
                gp.player.dying = true;
            }
        }
    }
    public void setKnockBack(Entity target, Entity attacker, int knockBackPower) {
        this.attacker = attacker;
        target.knockBack = true;
        target.knockBackCounter = 0;
        target.knockBackPower = knockBackPower;
        target.knockbackDirection = getOppositeDirection(attacker.Direction);
    }

    // public void setKnockBack(Entity target,Entity attacker, int knockBackPower) {

    //     this.attacker = attacker;
    //     target.knockbackDirection = attacker.Direction;
    //     target.speed += knockBackPower; 
    //     target.knockBack = true;
    //     target.knockBackCounter = 0;
    // }

    public void moveTowardPlayer (int interval) {

        actionLockCounter++;

        if (actionLockCounter > interval) {
            if (getXDistance(gp.player) > getYDistance(gp.player)) {
                if (gp.player.getCenterX() < getCenterX()) {
                    Direction = "left";
                } else {
                    Direction = "right";
                }
            } 
            else if (getXDistance(gp.player) < getYDistance(gp.player)) {
                if (gp.player.getCenterY() < getCenterY()) {
                    Direction = "up";
                } else {
                    Direction = "down";
                }
            } 
            actionLockCounter = 0; 
        }
    }

    public String getOppositeDirection(String Direction) {
        String oppositeDirection = "";
        
        switch (Direction) {
            case "up": oppositeDirection = "down"; break;
            case "down": oppositeDirection = "up"; break;
            case "left": oppositeDirection = "right"; break;
            case "right": oppositeDirection = "left"; break;
        }
        return oppositeDirection;
    }
    
    public void draw(Graphics2D g2) {
        // FIX: Add null check at the beginning
        if (gp.player == null) {
            return; // Don't draw anything if player doesn't exist
        }
        
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        // Get the actual image dimensions to determine scale
        int imageWidth = gp.TileSize;
        int imageHeight = gp.TileSize;
        
        // Try to get actual image dimensions if available
        if (up1 != null) {
            imageWidth = up1.getWidth();
            imageHeight = up1.getHeight();
        }
        
        // Calculate scale factor based on actual image size vs tile size
        int scale = imageWidth / gp.TileSize;
        if (scale < 1) scale = 1;
        
        // Calculate draw dimensions based on actual image size
        int drawWidth = imageWidth;
        int drawHeight = imageHeight;
        int drawX = screenX;
        int drawY = screenY;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
    
        // Only draw if entity is on screen (using expanded bounds for larger sprites)
        int checkWidth = Math.max(gp.TileSize * 2, drawWidth);
        int checkHeight = Math.max(gp.TileSize * 2, drawHeight);
        
        if (worldX + checkWidth > gp.player.worldX - gp.player.screenX &&
            worldX - checkWidth < gp.player.worldX + gp.player.screenX &&
            worldY + checkHeight > gp.player.worldY - gp.player.screenY &&
            worldY - checkHeight < gp.player.worldY + gp.player.screenY) {
    
            // Determine which image to draw based on state
            switch (Direction) {
                case "up":
                    if (attacking == false && guarding == false) {
                        image = (spriteNum == 1) ? up1 : up2;
                    }
                    if (attacking == true) { 
                        tempScreenY = screenY - (attackUp1 != null ? attackUp1.getHeight() - imageHeight : gp.TileSize * scale);
                        image = (spriteNum == 1) ? attackUp1 : attackUp2;
                        drawHeight = attackUp1 != null ? attackUp1.getHeight() : gp.TileSize * scale * 2;
                        drawWidth = imageWidth; // Reset width to normal for up attacks
                    }
                    if (guarding == true) {
                        image = guardUp;
                    }
                    break;
                case "down":
                    if (attacking == false && guarding == false) {
                        image = (spriteNum == 1) ? down1 : down2;
                    }
                    if (attacking == true) { 
                        image = (spriteNum == 1) ? attackDown1 : attackDown2;
                        drawHeight = attackDown1 != null ? attackDown1.getHeight() : gp.TileSize * scale * 2;
                        drawWidth = imageWidth; // Reset width to normal for down attacks
                    }
                    if (guarding == true) {
                        image = guardDown;
                    }
                    break;
                case "left":
                    if (attacking == false && guarding == false) {
                        image = (spriteNum == 1) ? left1 : left2;
                    }
                    if (attacking == true) { 
                        tempScreenX = screenX - (attackLeft1 != null ? attackLeft1.getWidth() - imageWidth : gp.TileSize * scale);
                        image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                        drawWidth = attackLeft1 != null ? attackLeft1.getWidth() : gp.TileSize * scale * 2;
                        drawHeight = imageHeight; // Reset height to normal for left attacks
                    }
                    if (guarding == true) {
                        image = guardLeft;
                    }
                    break;
                case "right":
                    if (attacking == false && guarding == false) {
                        image = (spriteNum == 1) ? right1 : right2;
                    }
                    if (attacking == true) { 
                        // For right attack, we don't need to adjust tempScreenX 
                        // because the sprite extends to the right
                        image = (spriteNum == 1) ? attackRight1 : attackRight2;
                        drawWidth = attackRight1 != null ? attackRight1.getWidth() : gp.TileSize * scale * 2;
                        drawHeight = imageHeight; // Reset height to normal for right attacks
                    }
                    if (guarding == true) {
                        image = guardRight;
                    }
                    break;
                default:
                    image = down1;
                    break;
            }
    
            // Draw monster HP bar (position based on original screen position, not draw position)
            if (type == 2 && hpBarOn == true) {
                int barWidth = imageWidth; // Use actual image width for health bar
                double oneScale = (double)barWidth / maxLife;
                double hpBarValue = oneScale * life;
    
                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - (imageHeight/8) - 4, barWidth + 2, 12);
    
                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - (imageHeight/8) - 3, (int)hpBarValue, 10);
    
                hpBarCounter++;
    
                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }
    
            // Handle invincibility flash effect
            if (invincible == true) {
                if (invincibleCounter % 10 < 5) {
                    changeAlpha(g2, 0.5f);
                } else {
                    changeAlpha(g2, 1f);
                }
            }
            
            // Handle dying animation
            if (dying == true) {
                dyingAnimation(g2);
            }
            
            // Draw the entity with proper dimensions
            if (image != null) {
                g2.drawImage(image, tempScreenX, tempScreenY, drawWidth, drawHeight, null);
            }
            
            // Reset alpha
            changeAlpha(g2, 1f);
    
            // Draw slowed effect (use original position with scaled size)
            if (slowed == true) {
                g2.setColor(new Color(0, 0, 255, 100));
                g2.fillRect(screenX, screenY, imageWidth, imageHeight);
            }
        }
    }   
    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        int i = 3;

        if(dyingCounter <= i) { changeAlpha(g2, 0f); }
        if(dyingCounter > i   && dyingCounter <= i*2) { changeAlpha(g2, 1f); }
        if(dyingCounter > i*2 && dyingCounter <= i*3) { changeAlpha(g2, 0f); }
        if(dyingCounter > i*3 && dyingCounter <= i*4) { changeAlpha(g2, 1f); }
        if(dyingCounter > i*4 && dyingCounter <= i*5) { changeAlpha(g2, 0f); }
        if(dyingCounter > i*5 && dyingCounter <= i*6) { changeAlpha(g2, 1f); }
        if(dyingCounter > i*6 && dyingCounter <= i*7) { changeAlpha(g2, 0f); }
        if(dyingCounter > i*7 && dyingCounter <= i*8) { changeAlpha(g2, 1f); }
        if(dyingCounter > i*8) {
            alive=false;
        }
    }
    
    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
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
    
    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / gp.TileSize;
        int startRow = (worldY + solidArea.y) / gp.TileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);

        if (gp.pFinder.search() == true) {
            int nextX = gp.pFinder.pathList.get(0).col * gp.TileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.TileSize;

            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.TileSize) {
                Direction = "up";
            } else if (enBottomY < nextY + gp.TileSize && enLeftX >= nextX && enRightX < nextX + gp.TileSize) {
                Direction = "down";
            } else if (enTopY >= nextY && enBottomY < nextY + gp.TileSize) {
                if (enLeftX > nextX) {
                    Direction = "left";
                }
                if (enRightX < nextX + gp.TileSize) {
                    Direction = "right";
                }
            } 
            else if (enTopY > nextY && enLeftX > nextX) {
                Direction = "up";
                if (collisionOn == true) {
                    Direction = "left";
                }
            } else if (enTopY > nextY && enRightX < nextX + gp.TileSize) {
                Direction = "up";
                if (collisionOn == true) {
                    Direction = "right";
                }
            } else if (enBottomY < nextY + gp.TileSize && enLeftX > nextX) {
                Direction = "down";
                if (collisionOn == true) {
                    Direction = "left";
                }
            } else if (enBottomY < nextY + gp.TileSize && enRightX < nextX + gp.TileSize) {
                Direction = "down";
                if (collisionOn == true) {
                    Direction = "right";
                }
            }
        }
    }
    public void setSpawnPoint(int worldX, int worldY) {
        this.spawnWorldX = worldX;
        this.spawnWorldY = worldY;
    }
}