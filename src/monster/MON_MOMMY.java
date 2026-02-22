package monster;

import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Arrows;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;

public class MON_MOMMY extends Entity {

    GamePanel gp;
    private Random random = new Random();

    public MON_MOMMY(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Mummy";
        action = true;
        knockBackPower = 5;

        defaultSpeed = 1;
        speed = defaultSpeed;

        // Level scaling
        int playerLevel = 1;
        if (gp.player != null) {
            playerLevel = gp.player.level;
        }
        
        maxLife = 8 + playerLevel * 2;
        life = maxLife;
        strength = 3;
        attack = strength;
        defense = 3 + playerLevel / 3;
        exp = 3 + playerLevel;

        // Solid area for collision
        solidArea.x = 8;
        solidArea.y = 12;
        solidArea.width = 32;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Attack area for hit detection
        attackArea.width = 40;
        attackArea.height = 40;
        
        // Motion durations
        motion1_duration = 45;
        motion2_duration = 60;

        getImage();
        getAttackImage();
        
        // Set spawn point
        setSpawnPoint(worldX, worldY);
    }

    public void getImage() {
        try {
            up1 = setup("/monster/mummy_up_1", gp.TileSize, gp.TileSize);
            up2 = setup("/monster/mummy_up_2", gp.TileSize, gp.TileSize);
            down1 = setup("/monster/mummy_down_1", gp.TileSize, gp.TileSize);
            down2 = setup("/monster/mummy_down_2", gp.TileSize, gp.TileSize);
            left1 = setup("/monster/mummy_left_1", gp.TileSize, gp.TileSize);
            left2 = setup("/monster/mummy_left_2", gp.TileSize, gp.TileSize);
            right1 = setup("/monster/mummy_right_1", gp.TileSize, gp.TileSize);
            right2 = setup("/monster/mummy_right_2", gp.TileSize, gp.TileSize);
        } catch (Exception e) {
            System.err.println("Error loading mummy images: " + e.getMessage());
        }
    }

    public void getAttackImage() {
        try {
            // Up/Down attacks: width normal (TileSize), height double (TileSize * 2)
            attackUp1 = setup("/monster/mummy_hit_top", gp.TileSize, gp.TileSize * 2);
            attackUp2 = setup("/monster/mummy_hit_top_2", gp.TileSize, gp.TileSize * 2);
            attackDown1 = setup("/monster/mummy_amba_down", gp.TileSize, gp.TileSize * 2);
            attackDown2 = setup("/monster/mummy_hit_down", gp.TileSize, gp.TileSize * 2);
            
            // Left/Right attacks: width double (TileSize * 2), height normal (TileSize)
            attackLeft1 = setup("/monster/mummy_amba_left", gp.TileSize * 2, gp.TileSize);
            attackLeft2 = setup("/monster/mummy_hit_left", gp.TileSize * 2, gp.TileSize);
            attackRight1 = setup("/monster/mummy_amba_right", gp.TileSize * 2, gp.TileSize);
            attackRight2 = setup("/monster/mummy_hit_right", gp.TileSize * 2, gp.TileSize);
        } catch (Exception e) {
            System.err.println("Error loading mummy attack images: " + e.getMessage());
        }
    }

    // =============================
    // Move Toward Player Method
    // =============================
    public void moveTowardPlayer(int interval) {
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

    public void setAction() {
        if (gp.player == null) return;
    
        // ===== CHASE PLAYER =====
        if (gettileDistance(gp.player) < 10) {
            moveTowardPlayer(60);
        }

        if (attacking == false) {
            checkAttackOrNot(40, gp.TileSize*10, gp.TileSize*5);
        }

        // ===== RANDOM MOVEMENT =====
        getRandomDirection();

        // Check if should start chasing using parent method
        checkStartChasingOrNot(gp.player, 5, 100);
    }

    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
        
        invincible = true;
        invincibleCounter = 0;

        if (gp.player != null) {
            // Face the player (look at attacker)
            int dx = gp.player.worldX - worldX;
            int dy = gp.player.worldY - worldY;
            
            if (Math.abs(dx) > Math.abs(dy)) {
                Direction = (dx > 0) ? "right" : "left";
            } else {
                Direction = (dy > 0) ? "down" : "up";
            }
        }
    }

    // Using parent's draw method

    public void checkDrop() {
        int roll = random.nextInt(100) + 1;

        if (roll < 40) {
            // Nothing
        } else if (roll < 60) {
            dropItem(new OBJ_Coin_Bronze(gp));
        } else if (roll < 75) {
            dropItem(new OBJ_Arrows(gp));
        } else if (roll < 85) {
            dropItem(new OBJ_Heart(gp));
        } else if (roll < 93) {
            dropItem(new OBJ_ManaCrystal(gp));
        } else if (roll < 98) {
            dropItem(new OBJ_Potion_Blue(gp));
        } else {
            dropItem(new OBJ_Potion_Red(gp));
        }
    }
}