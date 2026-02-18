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

    // Spawn point tracking
    private int aggroRange = 8; // Tiles
    private boolean returningToSpawn = false;

    // Timers
    private int aggroCheckCounter = 0;
    private final int AGGRO_CHECK_DELAY = 30;
    
    // ========== MELEE ATTACK SYSTEM ==========
    // Attack state
    private boolean isAttacking = false;
    private int attackCounter = 0;
    private final int ATTACK_ANIMATION_DURATION = 25; // Frames
    private final int MELEE_ATTACK_DELAY = 40; // Cooldown between melee attacks
    private int meleeCooldown = 0;
    
    // Attack damage
    private int meleeDamage = 5;
    
    // Knockback power
    private int knockbackPower = 5;

    public MON_MOMMY(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Mummy";
        action = true; // Enable AI movement

        defaultSpeed = 1;
        speed = defaultSpeed;

        // Level scaling with null check
        int playerLevel = 1;
        if (gp.player != null) {
            playerLevel = gp.player.level;
        }
        
        maxLife = 8 + playerLevel * 2;
        life = maxLife;
        attack = 3 + playerLevel / 2;
        meleeDamage = attack;
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

        getImage();
        getAttackImage();
        
        // Set spawn point to initial position
        setSpawnPoint(worldX, worldY);
    }

    public void getImage() {
        up1 = setup("/monster/mummy_up_1", gp.TileSize, gp.TileSize);
        up2 = setup("/monster/mummy_up_2", gp.TileSize, gp.TileSize);
        down1 = setup("/monster/mummy_down_1", gp.TileSize, gp.TileSize);
        down2 = setup("/monster/mummy_down_2", gp.TileSize, gp.TileSize);
        left1 = setup("/monster/mummy_left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/monster/mummy_left_2", gp.TileSize, gp.TileSize);
        right1 = setup("/monster/mummy_right_1", gp.TileSize, gp.TileSize);
        right2 = setup("/monster/mummy_right_2", gp.TileSize, gp.TileSize);
    }

    public void getAttackImage() {
        attackUp1 = setup("/monster/mummy_hit_top", gp.TileSize, gp.TileSize * 2);
        attackUp2 = setup("/monster/mummy_hit_top_2", gp.TileSize, gp.TileSize * 2);
        attackDown1 = setup("/monster/mummy_amba_down", gp.TileSize, gp.TileSize * 2);
        attackDown2 = setup("/monster/mummy_hit_down", gp.TileSize, gp.TileSize * 2);
        attackLeft1 = setup("/monster/mummy_amba_left", gp.TileSize * 2, gp.TileSize);
        attackLeft2 = setup("/monster/mummy_hit_left", gp.TileSize * 2, gp.TileSize);
        attackRight1 = setup("/monster/mummy_amba_right", gp.TileSize * 2, gp.TileSize);
        attackRight2 = setup("/monster/mummy_hit_right", gp.TileSize * 2, gp.TileSize);
    }

    @Override
    public void update() {
        // Update attack animation
        updateAttackState();
        
        // Update melee cooldown
        if (meleeCooldown > 0) meleeCooldown--;
        
        // Only move and act if not attacking (or if attacking but not in first few frames)
        if (!isAttacking || attackCounter > 5) {
            checkAggro();
            super.update(); // Call parent update for movement
        } else {
            // Still update parent for invincibility and other timers
            super.update(); // This will handle knockback, invincibility, etc.
        }
    }

    private void applyKnockbackToPlayer() {
        // Don't do anything if player doesn't exist
        if (gp.player == null) return;
        
        // Save player's original position in case we need to revert
        int originalX = gp.player.worldX;
        int originalY = gp.player.worldY;
        
        // Move player based on which way the mummy is facing
        switch (Direction) {
            case "up":
                gp.player.worldY -= knockbackPower; // Push player up
                break;
            case "down":
                gp.player.worldY += knockbackPower; // Push player down
                break;
            case "left":
                gp.player.worldX -= knockbackPower; // Push player left
                break;
            case "right":
                gp.player.worldX += knockbackPower; // Push player right
                break;
        }
        
        // Check if knockback pushed player into a wall
        gp.cChecker.checkTile(gp.player);
        
        // If player would go through a wall, cancel the knockback
        if (gp.player.collisionOn) {
            gp.player.worldX = originalX;
            gp.player.worldY = originalY;
            gp.player.collisionOn = false; // Reset collision flag
        }
    }
    
    private void updateAttackState() {
        if (isAttacking) {
            attackCounter++;
            
            // Deal damage at specific frame
            if (attackCounter == 5) {
                performMeleeAttack();
            }
            
            // End attack after duration
            if (attackCounter > ATTACK_ANIMATION_DURATION) {
                isAttacking = false;
                attackCounter = 0;
            }
        }
    }
    
    private void startAttack() {
        if (!isAttacking && meleeCooldown <= 0) {
            isAttacking = true;
            attackCounter = 0;
            meleeCooldown = MELEE_ATTACK_DELAY;
        }
    }

    // =============================
    // AGGRO LOGIC
    // =============================
    private void checkAggro() {
        if (gp.player == null) return;

        aggroCheckCounter++;
        if (aggroCheckCounter < AGGRO_CHECK_DELAY) return;
        aggroCheckCounter = 0;

        int playerDist = (Math.abs(worldX - gp.player.worldX)
                        + Math.abs(worldY - gp.player.worldY)) / gp.TileSize;
        int spawnDist = (Math.abs(worldX - spawnWorldX)
                        + Math.abs(worldY - spawnWorldY)) / gp.TileSize;

        // Too far from spawn → return
        if (onPath && spawnDist > aggroRange) {
            returningToSpawn = true;
            onPath = false;
        }

        // Start chasing player
        if (!onPath && !returningToSpawn && playerDist <= 5) {
            onPath = true;
        }

        // Stop returning
        if (returningToSpawn && spawnDist <= 1) {
            returningToSpawn = false;
            onPath = false;
        }
    }

    // =============================
    // AI Behavior
    // =============================
    @Override
    public void setAction() {
        // Don't change direction while attacking
        if (isAttacking) return;

        // ===== RETURN TO SPAWN =====
        if (returningToSpawn) {
            moveTowards(spawnWorldX, spawnWorldY);
            return;
        }

        // ===== CHASE PLAYER =====
        if (onPath && gp.player != null) {
            
            // Check if in melee range (collision distance)
            boolean inMeleeRange = checkCollisionWithPlayer();
            
            // MELEE ATTACK - if close enough and cooldown ready
            if (inMeleeRange && meleeCooldown <= 0) {
                startAttack();
                return; // Don't move while starting attack
            }
            
            // Move towards player if not attacking
            moveTowards(gp.player.worldX, gp.player.worldY);
            return;
        }

        // ===== RANDOM MOVEMENT =====
        actionLockCounter++;

        if (collisionOn) {
            getRandomDirection();
            collisionOn = false;
            actionLockCounter = 0;
            return;
        }

        if (actionLockCounter >= 120) {
            getRandomDirection();
            actionLockCounter = 0;
        }
    }
    
    private boolean checkCollisionWithPlayer() {
        if (gp.player == null) return false;
        
        // Simple rectangle collision check
        return worldX < gp.player.worldX + gp.TileSize &&
               worldX + gp.TileSize > gp.player.worldX &&
               worldY < gp.player.worldY + gp.TileSize &&
               worldY + gp.TileSize > gp.player.worldY;
    }
    
    private void performMeleeAttack() {
        if (gp.player == null) return;
        
        // Check if player is still in range
        if (checkCollisionWithPlayer() && !gp.player.invincible) {
            // Calculate damage
            int damage = meleeDamage - gp.player.defense;
            if (damage < 1) damage = 1;
            
            // Apply damage using parent method
            damageplayer(attack);
            
            // ===== APPLY KNOCKBACK =====
            applyKnockbackToPlayer();
            // ===========================
        }
    }

    private void moveTowards(int targetX, int targetY) {
        int dx = targetX - worldX;
        int dy = targetY - worldY;

        if (Math.abs(dx) > Math.abs(dy)) {
            Direction = (dx > 0) ? "right" : "left";
        } else {
            Direction = (dy > 0) ? "down" : "up";
        }
    }

    private void getRandomDirection() {
        int i = random.nextInt(4);
        switch (i) {
            case 0: Direction = "up"; break;
            case 1: Direction = "down"; break;
            case 2: Direction = "left"; break;
            case 3: Direction = "right"; break;
        }
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
        returningToSpawn = false;
        
        // Set invincible to true - this will trigger the parent's invincibility effect
        invincible = true;
        invincibleCounter = 0;

        // Turn to face player (opposite of where hit came from)
        if (gp.player != null) {
            switch (gp.player.Direction) {
                case "up":    Direction = "down"; break;
                case "down":  Direction = "up"; break;
                case "left":  Direction = "right"; break;
                case "right": Direction = "left"; break;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        // Skip if player is null (safety check)
        if (gp.player == null) return;
        
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Only draw if on screen
        if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {

            BufferedImage image = null;
            int drawX = screenX;
            int drawY = screenY;
            int drawWidth = gp.TileSize;
            int drawHeight = gp.TileSize;
            
            // ===== MELEE ATTACK ANIMATION =====
            if (isAttacking) {
                // Use attack sprites during attack animation
                switch (Direction) {
                    case "up":
                        image = (spriteNum == 1) ? attackUp1 : attackUp2;
                        drawY = screenY - gp.TileSize; // Move up to show extended sprite
                        drawHeight = gp.TileSize * 2; // Double height
                        drawWidth = gp.TileSize; // Normal width
                        break;
                    case "down":
                        image = (spriteNum == 1) ? attackDown1 : attackDown2;
                        drawHeight = gp.TileSize * 2; // Double height
                        drawWidth = gp.TileSize; // Normal width
                        break;
                    case "left":
                        image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                        drawX = screenX - gp.TileSize; // Move left to show extended sprite
                        drawWidth = gp.TileSize * 2; // Double width
                        drawHeight = gp.TileSize; // Normal height
                        break;
                    case "right":
                        image = (spriteNum == 1) ? attackRight1 : attackRight2;
                        drawWidth = gp.TileSize * 2; // Double width
                        drawHeight = gp.TileSize; // Normal height
                        break;
                }
            } else {
                // Normal movement sprites
                switch (Direction) {
                    case "up":    image = (spriteNum == 1) ? up1 : up2; break;
                    case "down":  image = (spriteNum == 1) ? down1 : down2; break;
                    case "left":  image = (spriteNum == 1) ? left1 : left2; break;
                    case "right": image = (spriteNum == 1) ? right1 : right2; break;
                }
                drawWidth = gp.TileSize;
                drawHeight = gp.TileSize;
            }

            // Let the parent class handle invincibility effects
            if (invincible) {
                // This will use the parent's invincibility rendering (flashing)
                super.draw(g2);
            } else {
                if (image != null) {
                    g2.drawImage(image, drawX, drawY, drawWidth, drawHeight, null);
                }
            }
            
            // ===== HEALTH BAR =====
            if (hpBarOn) {
                double oneScale = (double)gp.TileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.TileSize + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
            }
        }
    }

    @Override
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