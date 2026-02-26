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
    public String dialogues[][] = new String[30][30];
    public BufferedImage image, image2, image3;
    public Entity attacker;
    public boolean temp = false;

    // Dialogue tracking - SINGLE variable (removed duplicate)
    public int dialogueIndex = 0;
    
    // Multi-page dialogue support
    public String[] dialoguePages;
    public int currentPage = 0;
    
    // State
    public int worldX, worldY;
    public String Direction = "down";
    public int spriteNum = 1;
    public int mapnum = 0;
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

    public boolean sleep = false;
    public boolean drawing = true;

    // slow effect
    public BufferedImage slowEffectImage;
    public int slowCounter = 0;
    public final int SLOW_DURATION = 120;

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

    public int questProgress = 0;
    public int questStatus; // 0 - inactive 1 - active
    public int questState = 0;

    // npc direction
    public String npcDirection = "";

    // dialogue
    public boolean phase3DialogueStarted = false;

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
    public final int type_torch = 16;

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
    
    // ============ UNIFIED DIALOGUE SYSTEM ============
    
    /**
     * Find this entity's index in the npc or monster array
     */
    public void findMyIndex() {
        // Check in NPC array first
        if (gp.npc != null && gp.npc[gp.currentMap] != null) {
            for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
                if (gp.npc[gp.currentMap][i] == this) {
                    gp.ui.npcIndex = i;
                    return;
                }
            }
        }
        
        // Check in monster array
        if (gp.monster != null && gp.monster[gp.currentMap] != null) {
            for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
                if (gp.monster[gp.currentMap][i] == this) {
                    gp.ui.npcIndex = i;
                    return;
                }
            }
        }
    }
    
    /**
     * Prepare dialogue pages - can be overridden by subclasses
     */
    public void prepareDialoguePages() {
        // Default implementation - load from dialogues array at dialogueIndex
        if (dialogues != null && dialogues[dialogueIndex] != null) {
            // Count non-null dialogues
            int count = 0;
            while (count < dialogues[dialogueIndex].length && dialogues[dialogueIndex][count] != null) {
                count++;
            }
            
            if (count > 0) {
                dialoguePages = new String[count];
                for (int i = 0; i < count; i++) {
                    dialoguePages[i] = dialogues[dialogueIndex][i];
                }
            }
        }
        
        // Fallback if no dialogue
        if (dialoguePages == null) {
            dialoguePages = new String[] {"..."};
        }
    }
    
    /**
     * Main speak method - handles dialogue for all entities
     */
    public void speak() {
        facePlayer();
        
        // Find this entity's index in the npc/monster array
        findMyIndex();
        
        // Prepare dialogue pages (can be overridden)
        prepareDialoguePages();
        
        // Start with first page
        if (dialoguePages != null && dialoguePages.length > 0) {
            currentPage = 0;
            gp.ui.setDialogue(dialoguePages);
        }
        
        // Enter dialogue state
        gp.gameState = gp.dialogueState;
    }
    
    /**
     * Go to next dialogue page - called by KeyHandler
     */
    public void nextDialogue() {
        if (dialoguePages == null) {
            gp.gameState = gp.playState;
            return;
        }
        
        if (!gp.ui.isDialogueFinished()) {
            gp.ui.skipToEnd();
        } else {
            currentPage++;
            
            if (currentPage < dialoguePages.length) {
                // Show next page
                gp.ui.setDialogue(new String[]{dialoguePages[currentPage]});
                gp.gameState = gp.dialogueState;
            } else {
                // No more pages
                gp.gameState = gp.playState;
                currentPage = 0;
                
                // Optional: Trigger any post-dialogue actions
                afterDialogue();
            }
        }
    }
    
    /**
     * Hook for subclasses to add post-dialogue actions
     */
    public void afterDialogue() {
        // Override in subclasses if needed
    }
    
    public void facePlayer() {
        if (gp.player != null) {
            switch (gp.player.Direction) {
                case "up": Direction = "down"; break;
                case "down": Direction = "up"; break;
                case "left": Direction = "right"; break;
                case "right": Direction = "left"; break;
            }
        }
    }
    
    public void interact() {}
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
                // If player is guarding, don't let monster collide - apply knockback instead
                if (gp.player.guarding == true) {
                    // Check if in contact and push monster back
                    boolean contactPlayer = gp.cChecker.checkPlayer(this);
                    if (contactPlayer == true) {
                        // Knockback the monster away from player
                        int knockbackPower = 2;
                        setKnockBack(this, gp.player, knockbackPower);
                    }
                } else {
                    // Normal collision when player is not guarding
                    boolean contactPlayer = gp.cChecker.checkPlayer(this);
                    if (contactPlayer == true) {
                        damageplayer(attack);
                    }
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

    public void torch_animation() {
        actionLockCounter++;
        
        if (actionLockCounter > 8) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            actionLockCounter = 0;
        }
    }
    
    public void update() {
        if (Direction == null) {
            Direction = "down";
        }
        if (sleep == false) {
            if (knockBack == true) {
                if (gp.player == null) {
                    knockBack = false;
                    speed = defaultSpeed;
                    knockBackCounter = 0;
                    return;
                }
                
                int originalX = worldX;
                int originalY = worldY;
        
                int dx = 0;
                int dy = 0;
                
                if (attacker != null) {
                    dx = worldX - attacker.worldX;
                    dy = worldY - attacker.worldY;
                } else {
                    dx = worldX - gp.player.worldX;
                    dy = worldY - gp.player.worldY;
                }
        
                String moveDirection = "";
                if (Math.abs(dx) > Math.abs(dy)) {
                    moveDirection = (dx > 0) ? "right" : "left";
                } else {
                    moveDirection = (dy > 0) ? "down" : "up";
                }
        
                knockbackDirection = moveDirection;
        
                if (attacker != null) {
                    int facingDx = attacker.worldX - worldX;
                    int facingDy = attacker.worldY - worldY;
                    
                    if (Math.abs(facingDx) > Math.abs(facingDy)) {
                        Direction = (facingDx > 0) ? "right" : "left";
                    } else {
                        Direction = (facingDy > 0) ? "down" : "up";
                    }
                } else {
                    if (moveDirection.equals("up")) Direction = "down";
                    else if (moveDirection.equals("down")) Direction = "up";
                    else if (moveDirection.equals("left")) Direction = "right";
                    else if (moveDirection.equals("right")) Direction = "left";
                    else Direction = "down";
                }
        
                int moveDistance = knockBackPower;
                
                switch (moveDirection) {
                    case "up": worldY -= moveDistance; break;
                    case "down": worldY += moveDistance; break;
                    case "left": worldX -= moveDistance; break;
                    case "right": worldX += moveDistance; break;
                }
        
                collisionOn = false;
                gp.cChecker.checkTile(this);
                gp.cChecker.checkObject(this, false);
        
                if (this.type == type_monster) {
                    gp.cChecker.checkEntity(this, gp.iTile);
                }
        
                if (collisionOn == true) {
                    worldX = originalX;
                    worldY = originalY;
                }
        
                if (this.type == type_monster && collisionOn == false && gp.player != null) {
                    boolean contactPlayer = gp.cChecker.checkPlayer(this);
                    if (contactPlayer == true && gp.player.invincible == false) {
                        damageplayer(attack);
                    }
                }
        
                knockBackCounter++;
                if (knockBackCounter > 20) {
                    knockBack = false;
                    speed = defaultSpeed;
                    knockBackCounter = 0;
                    attacker = null;
                    knockbackDirection = null;
                }
            } 
            else if (attacking == true) {
                attacking();
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
        
            if (invincible == true) {
                invincibleCounter++;
                if (invincibleCounter > 60) {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
        
            if (shotAvailableCounter < 30) {
                shotAvailableCounter++;
            }
        }
    }

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
            
        if (shootChance == 0 && projectiles.alive == false && shotAvailableCounter == shotInterval) {
            projectiles.set(getCenterX(), getCenterY(), Direction, true, this);
            for (int ii = 0; ii < gp.projectile[1].length; ii++) {
                if (gp.projectile[gp.currentMap][ii] == null) {
                    gp.projectile[gp.currentMap][ii] = projectiles;
                    break;
                }
            }
            shotAvailableCounter = 0;
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
            int i = random.nextInt(100)+1;
            
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

            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;
            
            switch (Direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }
            
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            if (type == type_monster) {
                if (gp.cChecker.checkPlayer(this) == true) {
                    damageplayer(attack);
                }
            }
            else {
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                gp.player.damageMonster(monsterIndex, this, attack, knockBackPower);

                int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
                gp.player.damageInteractiveTile(iTileIndex);

                int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
                gp.player.damageProjectile(projectileIndex);
            }
            
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

        if (gp.player.invincible == false) {
            
            int damage = attack - gp.player.defense;
            if (damage < 1) {
                damage = 1;
            }
            
            String canGuardDirection = getOppositeDirection(Direction);
            
            if (gp.player.guarding == true && gp.player.Direction.equals(canGuardDirection)) {
                gp.playSE(15);
                gp.ui.showMessage("Perfect Guard! No damage!");
                return;
            } else {
                gp.playSE(6);
                gp.ui.showMessage(damage + " damage!");
                
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
        if (gp.player == null) {
            return;
        }
        
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        int imageWidth = gp.TileSize;
        int imageHeight = gp.TileSize;
        
        if (up1 != null) {
            imageWidth = up1.getWidth();
            imageHeight = up1.getHeight();
        }
        
        int scale = imageWidth / gp.TileSize;
        if (scale < 1) scale = 1;
        
        int drawWidth = imageWidth;
        int drawHeight = imageHeight;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
    
        int checkWidth = Math.max(gp.TileSize * 2, drawWidth);
        int checkHeight = Math.max(gp.TileSize * 2, drawHeight);
        
        if (worldX + checkWidth > gp.player.worldX - gp.player.screenX &&
            worldX - checkWidth < gp.player.worldX + gp.player.screenX &&
            worldY + checkHeight > gp.player.worldY - gp.player.screenY &&
            worldY - checkHeight < gp.player.worldY + gp.player.screenY) {
    
            switch (Direction) {
                case "up":
                    if (attacking == false && guarding == false) {
                        image = (spriteNum == 1) ? up1 : up2;
                    }
                    if (attacking == true) { 
                        tempScreenY = screenY - (attackUp1 != null ? attackUp1.getHeight() - imageHeight : gp.TileSize * scale);
                        image = (spriteNum == 1) ? attackUp1 : attackUp2;
                        drawHeight = attackUp1 != null ? attackUp1.getHeight() : gp.TileSize * scale * 2;
                        drawWidth = imageWidth;
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
                        drawWidth = imageWidth;
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
                        drawHeight = imageHeight;
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
                        image = (spriteNum == 1) ? attackRight1 : attackRight2;
                        drawWidth = attackRight1 != null ? attackRight1.getWidth() : gp.TileSize * scale * 2;
                        drawHeight = imageHeight;
                    }
                    if (guarding == true) {
                        image = guardRight;
                    }
                    break;
                default:
                    image = down1;
                    break;
            }
    
            if (type == 2 && hpBarOn == true) {
                int barWidth = imageWidth;
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
    
            if (invincible == true) {
                if (invincibleCounter % 10 < 5) {
                    changeAlpha(g2, 0.5f);
                } else {
                    changeAlpha(g2, 1f);
                }
            }
            
            if (dying == true) {
                dyingAnimation(g2);
            }
            
            if (image != null) {
                g2.drawImage(image, tempScreenX, tempScreenY, drawWidth, drawHeight, null);
            }
            
            changeAlpha(g2, 1f);
    
            if (slowed == true) {
                slowCounter++;
                
                float alpha = 0.5f;
                
                if (slowCounter > SLOW_DURATION - 30) {
                    alpha = 0.5f * (float)(SLOW_DURATION - slowCounter) / 30;
                    if (alpha < 0) alpha = 0;
                }
                
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                
                if (slowEffectImage != null) {
                    g2.drawImage(slowEffectImage, screenX, screenY, imageWidth, imageHeight, null);
                }
                
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                
                if (slowCounter >= SLOW_DURATION) {
                    slowed = false;
                    slowCounter = 0;
                    speed = defaultSpeed;
                }
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