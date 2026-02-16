package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    public boolean collision = false;
    String dialogues[] = new String[20];
    public BufferedImage image, image2, image3;
    
    // State
    public int worldX, worldY;
    public String Direction = "down";
    public int spriteNum = 1;
    public int mapnum = 0;
    int dialoguesIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    public boolean hasTablet = false;
    public boolean onPath = false;
    public boolean knockBack = false;
    public boolean action = false;
    public boolean talk = false;
    public boolean slowed = false;

    public boolean guarding = false;
    public boolean transparent = false;


    // counter 
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
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

    public Entity(GamePanel gp) {
        this.gp = gp;
        inventory = new ArrayList<>();
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
    
    public void update(){
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
            
            // Calculate knockback force (decreases over time)
            float knockbackForce = knockBackPower * (1 - (knockBackCounter / 20f));
            if (knockbackForce < 1) knockbackForce = 1;
            
            int moveDistance = (int)(speed + knockbackForce);
            
            // Determine direction AWAY from player
            String knockbackDirection = "";
            
            // Calculate direction from monster to player
            int dx = gp.player.worldX - worldX;
            int dy = gp.player.worldY - worldY;
            
            // Set knockback direction opposite to where player is
            if (Math.abs(dx) > Math.abs(dy)) {
                // Player is more left/right than up/down
                if (dx > 0) {
                    // Player is to the RIGHT, so knockback LEFT
                    knockbackDirection = "left";
                } else {
                    // Player is to the LEFT, so knockback RIGHT
                    knockbackDirection = "right";
                }
            } else {
                // Player is more up/down than left/right
                if (dy > 0) {
                    // Player is BELOW, so knockback UP
                    knockbackDirection = "up";
                } else {
                    // Player is ABOVE, so knockback DOWN
                    knockbackDirection = "down";
                }
            }
            
            // Store the knockback direction
            Direction = knockbackDirection;
            
            // MOVE away from player
            switch (knockbackDirection) {
                case "up": worldY -= moveDistance; break;
                case "down": worldY += moveDistance; break;
                case "left": worldX -= moveDistance; break;
                case "right": worldX += moveDistance; break;
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
            // BUT keep knockback active so it can try to move in a different direction
            if (collisionOn == true) {
                worldX = originalX;
                worldY = originalY;
                
                // Try to slide along the wall instead of stopping completely
                // This prevents getting stuck
                if (knockbackDirection.equals("up") || knockbackDirection.equals("down")) {
                    // Try moving left/right instead
                    worldX += (knockbackDirection.equals("up") ? -moveDistance : moveDistance) / 2;
                    worldY = originalY;
                    
                    // Check if this new position is valid
                    collisionOn = false;
                    gp.cChecker.checkTile(this);
                    gp.cChecker.checkObject(this, false);
                    
                    if (collisionOn == true) {
                        // If still colliding, revert completely
                        worldX = originalX;
                    }
                } 
                else if (knockbackDirection.equals("left") || knockbackDirection.equals("right")) {
                    // Try moving up/down instead
                    worldY += (knockbackDirection.equals("left") ? -moveDistance : moveDistance) / 2;
                    worldX = originalX;
                    
                    // Check if this new position is valid
                    collisionOn = false;
                    gp.cChecker.checkTile(this);
                    gp.cChecker.checkObject(this, false);
                    
                    if (collisionOn == true) {
                        // If still colliding, revert completely
                        worldY = originalY;
                    }
                }
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
            if(knockBackCounter > 20) {
                knockBack = false;
                speed = defaultSpeed;
                knockBackCounter = 0;
            }
        }
        else {
            if (action == true) {
                setAction();
                checkCollision();
    
                if (collisionOn == false) {
                    switch (Direction) {
                        case "up": worldY -= speed; break;
                        case "down": worldY += speed; break;
                        case "left": worldX -= speed; break;
                        case "right": worldX += speed; break;
                    }
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
        spriteCounter++;
        if (spriteCounter > 24) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }

    public void idle () {
        //nothing
    }
    
    public void damageplayer(int attack) {
        // FIX: Add null check for player
        if (gp.player == null) {
            return;
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
    
        // Only draw if entity is on screen
        if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {
    
            // Determine which image to draw based on direction and sprite number
            switch (Direction) {
                case "up":
                    if (spriteNum == 1) { image = up1; }
                    if (spriteNum == 2) { image = up2; }
                    break;
                case "down":
                    if (spriteNum == 1) { image = down1; }
                    if (spriteNum == 2) { image = down2; }
                    break;
                case "left":
                    if (spriteNum == 1) { image = left1; }
                    if (spriteNum == 2) { image = left2; }
                    break;
                case "right":
                    if (spriteNum == 1) { image = right1; }   
                    if (spriteNum == 2) { image = right2; }
                    break;
                default:
                    break;
            }
    
            // Draw monster HP bar
            if (type == 2 && hpBarOn == true) {
                double oneScale = (double)gp.TileSize/maxLife;
                double hpBarValue = oneScale*life;
    
                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX-1, screenY - 15, gp.TileSize+2, 12);
    
                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
    
                hpBarCounter++;
    
                if(hpBarCounter > 600) {
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
            
            // Draw the entity
            if (image != null) {
                g2.drawImage(image, screenX, screenY, gp.TileSize, gp.TileSize, null);
            }
            
            // Reset alpha
            changeAlpha(g2, 1f);
    
            // Draw slowed effect
            if (slowed == true) {
                g2.setColor(new Color(0, 0, 255, 100));
                g2.fillRect(screenX, screenY, gp.TileSize, gp.TileSize);
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
    
    // public void searchPath(int goalCol, int goalRow) {
    //     int startCol = (worldX + solidArea.x) / gp.TileSize;
    //     int startRow = (worldY + solidArea.y) / gp.TileSize;

    //     gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);

    //     if (gp.pFinder.search() == true) {
    //         int nextX = gp.pFinder.pathList.get(0).col * gp.TileSize;
    //         int nextY = gp.pFinder.pathList.get(0).row * gp.TileSize;

    //         int enLeftX = worldX + solidArea.x;
    //         int enRightX = worldX + solidArea.x + solidArea.width;
    //         int enTopY = worldY + solidArea.y;
    //         int enBottomY = worldY + solidArea.y + solidArea.height;

    //         if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.TileSize) {
    //             Direction = "up";
    //         } else if (enBottomY < nextY + gp.TileSize && enLeftX >= nextX && enRightX < nextX + gp.TileSize) {
    //             Direction = "down";
    //         } else if (enTopY >= nextY && enBottomY < nextY + gp.TileSize) {
    //             if (enLeftX > nextX) {
    //                 Direction = "left";
    //             }
    //             if (enRightX < nextX + gp.TileSize) {
    //                 Direction = "right";
    //             }
    //         } 
    //         else if (enTopY > nextY && enLeftX > nextX) {
    //             Direction = "up";
    //             if (collisionOn == true) {
    //                 Direction = "left";
    //             }
    //         } else if (enTopY > nextY && enRightX < nextX + gp.TileSize) {
    //             Direction = "up";
    //             if (collisionOn == true) {
    //                 Direction = "right";
    //             }
    //         } else if (enBottomY < nextY + gp.TileSize && enLeftX > nextX) {
    //             Direction = "down";
    //             if (collisionOn == true) {
    //                 Direction = "left";
    //             }
    //         } else if (enBottomY < nextY + gp.TileSize && enRightX < nextX + gp.TileSize) {
    //             Direction = "down";
    //             if (collisionOn == true) {
    //                 Direction = "right";
    //             }
    //         }
    //     }
    // }
    public boolean searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / gp.TileSize;
        int startRow = (worldY + solidArea.y) / gp.TileSize;
    
        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);
    
        if (gp.pFinder.search() == true && gp.pFinder.pathList != null && !gp.pFinder.pathList.isEmpty()) {
            
            // Get the next node from the path
            Node nextNode = gp.pFinder.pathList.get(0);
            
            // Calculate the CENTER of the target tile
            int targetTileCenterX = nextNode.col * gp.TileSize + gp.TileSize/2;
            int targetTileCenterY = nextNode.row * gp.TileSize + gp.TileSize/2;
            
            // Get entity's center position
            int enCenterX = worldX + solidArea.x + solidArea.width/2;
            int enCenterY = worldY + solidArea.y + solidArea.height/2;
            
            // Calculate distances
            int dx = targetTileCenterX - enCenterX;
            int dy = targetTileCenterY - enCenterY;
            
            // Debug output (remove after testing)
            System.out.println("Target: (" + targetTileCenterX + "," + targetTileCenterY + 
                               ") Center: (" + enCenterX + "," + enCenterY + 
                               ") dx: " + dx + " dy: " + dy);
            
            // Use a small threshold to prevent jittering
            int threshold = 3;
            
            if (Math.abs(dx) > Math.abs(dy)) {
                // Moving horizontally is priority
                if (dx > threshold) {
                    Direction = "right";
                    System.out.println("Moving right");
                } else if (dx < -threshold) {
                    Direction = "left";
                    System.out.println("Moving left");
                } else {
                    // If horizontal is within threshold, check vertical
                    if (dy > threshold) {
                        Direction = "down";
                        System.out.println("Moving down");
                    } else if (dy < -threshold) {
                        Direction = "up";
                        System.out.println("Moving up");
                    }
                }
            } else {
                // Moving vertically is priority
                if (dy > threshold) {
                    Direction = "down";
                    System.out.println("Moving down");
                } else if (dy < -threshold) {
                    Direction = "up";
                    System.out.println("Moving up");
                } else {
                    // If vertical is within threshold, check horizontal
                    if (dx > threshold) {
                        Direction = "right";
                        System.out.println("Moving right");
                    } else if (dx < -threshold) {
                        Direction = "left";
                        System.out.println("Moving left");
                    }
                }
            }
            
            // Check if we've reached the current node
            int currentTileCol = (worldX + solidArea.x + solidArea.width/2) / gp.TileSize;
            int currentTileRow = (worldY + solidArea.y + solidArea.height/2) / gp.TileSize;
            
            if (currentTileCol == nextNode.col && currentTileRow == nextNode.row) {
                System.out.println("Reached node at (" + nextNode.col + "," + nextNode.row + ")");
                gp.pFinder.pathList.remove(0);
                
                if (gp.pFinder.pathList.isEmpty()) {
                    System.out.println("Path complete!");
                    onPath = false;
                }
            }
            
            return true;
        }
        
        // No path found
        if (onPath) {
            System.out.println("No path found to (" + goalCol + "," + goalRow + ")");
        }
        onPath = false;
        return false;
    }
}