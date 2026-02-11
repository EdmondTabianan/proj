package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

    GamePanel gp;
    public BufferedImage up1, up2, up3, down1, down2, down3 ,left1, left2, left3, right1, right2, right3;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
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
    public boolean Invincible = false;
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    public boolean hasTablet = false;
    public boolean onPath = false;
    public boolean knockBack = false;
    public boolean action = false;
    public boolean talk = false;

    // counter 
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int InvincibleCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;
    int knockBackCounter = 0;
    public int talkcounter = 0;
    public int killCount = 0;

    // characte attri
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

    // item attri
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    public int value;
    public int attackvalue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int hasKey;
    public int amount;
    public int knockBackPower = 0; 

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
        
        // Only check NPC collisions (monsters can pass through NPCs)
        gp.cChecker.checkEntity(this, gp.npc);
        
        // MONSTERS: Only check collision with player, NOT with other monsters
        if (this.type == type_monster) {
            // Check collision with player
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if (contactPlayer == true) {
                damagaplayer(attack);
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
            checkCollision();

            if (collisionOn == true) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }  
            else if (collisionOn == false) {
                switch (gp.player.Direction) {
                    case "up": worldY -= speed;  break;
                    case "down":  worldY += speed;  break;
                    case "left":  worldX -= speed;  break;
                    case "right":  worldX += speed;  break;
                }
            }

            knockBackCounter++;
            if(knockBackCounter > 10) { // Knockback lasts for 20 frames
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }
        }
            
        else {

            if (action == true) {
                setAction();
                checkCollision();

                // if collision is false, entity can move
                if (collisionOn == false) {
                    switch (Direction) {
                        case "up": worldY -= speed;  break;
                        case "down":  worldY += speed;  break;
                        case "left":  worldX -= speed;  break;
                        case "right":  worldX += speed;  break;
                    }
                }
            } else if (action == false) {
                idle();
            }
            
        }
        
        // Update invincibility timer FIRST
        if (Invincible == true) {
            InvincibleCounter++;
            if (InvincibleCounter > 60) { // 1 second invincibility
                Invincible = false;
                InvincibleCounter = 0;
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

        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }

    public void idle () {

        //nothing
    }
    
    public void damagaplayer(int attack) {
        // Only damage player if player is not invincible AND monster is not dying
        if (gp.player.Invincible == false && this.dying == false) {
            gp.playSE(6);
            int damage = attack - gp.player.defense;
            if(damage < 1) {
                damage = 1; // Minimum 1 damage
            }
            gp.player.life -= damage;
            gp.player.Invincible = true;
            gp.player.InvincibleCounter = 0; // Reset player's invincibility counter
            
            // Show damage message
            gp.ui.showMessage(damage + " damage!");
        }
    }
    
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {

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

                // monster hp bar
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

                if (Invincible == true) {
                    // Flash effect: only show entity every other frame
                    if (InvincibleCounter % 10 < 5) { // Blink every 5 frames
                        changeAlpha(g2, 0.5f);
                    } else {
                        changeAlpha(g2, 1f);
                    }
                }
                
                if (dying == true) {
                    dyingAnimation(g2);
                }
                
                g2.drawImage(image, screenX, screenY, gp.TileSize, gp.TileSize, null);
                changeAlpha(g2, 1f);
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
}