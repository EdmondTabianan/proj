package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Arrows;
import object.OBJ_Bato;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;

public class MON_Snake extends Entity {

    GamePanel gp;
    private Random random = new Random();

    // Spawn tracking
    private int spawnWorldX;
    private int spawnWorldY;
    private boolean returningToSpawn = false;
    private int aggroRange = 8;

    private int aggroCheckCounter = 0;
    private final int AGGRO_CHECK_DELAY = 30;

    public MON_Snake(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Snake";
        action = true;
        defaultSpeed = 2;
        speed = defaultSpeed;

        // SAFETY CHECK: Make sure player exists before accessing
        int playerLevel = 1; // Default level 1 if player not available
        int playerAttack = 1;
        
        if (gp.player != null) {
            playerLevel = gp.player.level;
            playerAttack = gp.player.attack;
        } else {
            
        }

        // Scale with player level (with fallback values)
        maxLife = 12 + playerLevel * 3;
        life = maxLife;
        attack = 2 + playerLevel / 2; // Slightly scale with player
        defense = 1 + playerLevel / 3;
        exp = 5 + playerLevel * 2;

        // Projectile (optional)
        projectiles = new OBJ_Bato(gp);

        // Shrinked solid area for narrow paths
        solidArea.x = 8;
        solidArea.y = 12;
        solidArea.width = 32;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }
    
    // Alternative constructor with explicit spawn position
    public MON_Snake(GamePanel gp, int worldX, int worldY) {
        this(gp);
        this.worldX = worldX;
        this.worldY = worldY;
        setSpawnPoint(worldX, worldY);
    }

    public void setSpawnPoint(int worldX, int worldY) {
        this.spawnWorldX = worldX;
        this.spawnWorldY = worldY;
    }

    public void getImage() {
        up1 = setup("/monster/snik_left_1", gp.TileSize, gp.TileSize);
        up2 = setup("/monster/snik_left_2", gp.TileSize, gp.TileSize);
        down1 = setup("/monster/snik_righ_1", gp.TileSize, gp.TileSize);
        down2 = setup("/monster/snik_righ_2", gp.TileSize, gp.TileSize);
        left1 = setup("/monster/snik_left_1", gp.TileSize, gp.TileSize);
        left2 = setup("/monster/snik_left_2", gp.TileSize, gp.TileSize);
        right1 = setup("/monster/snik_righ_1", gp.TileSize, gp.TileSize);
        right2 = setup("/monster/snik_righ_2", gp.TileSize, gp.TileSize);
    }

    @Override
    public void update() {
        checkAggro();
        super.update();
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

        // Return to spawn if too far
        if (onPath && spawnDist > aggroRange) {
            onPath = false;
            returningToSpawn = true;
        }

        // Start chasing player
        if (!onPath && !returningToSpawn && playerDist <= 5) {
            onPath = true;
        }

        // Stop returning when at spawn
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

        // ===== RETURN TO SPAWN =====
        if (returningToSpawn) {
            moveTowards(spawnWorldX, spawnWorldY);
            return;
        }

        // ===== CHASE PLAYER =====
        if (onPath && gp.player != null) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;

            searchPath(goalCol, goalRow);

            // Projectile attack (like Green Slime)
            shotAvailableCounter++;
            if (shotAvailableCounter >= 120) {
                int chance = random.nextInt(100) + 1;
                if (chance > 70) {
                    if (projectiles != null) {
                        projectiles.set(worldX, worldY, Direction, true, this);
                        for (int i = 0; i < gp.projectile[1].length; i++) {
                            if (gp.projectile[gp.currentMap][i] == null) {
                                gp.projectile[gp.currentMap][i] = projectiles;
                                break;
                            }
                        }
                    }
                    shotAvailableCounter = 0;
                } else {
                    shotAvailableCounter = 90;
                }
            }
            return;
        }

        // ===== RANDOM MOVEMENT =====
        actionLockCounter++;

        if (collisionOn) {
            int i = random.nextInt(4);
            switch (i) {
                case 0: Direction = "up"; break;
                case 1: Direction = "down"; break;
                case 2: Direction = "left"; break;
                case 3: Direction = "right"; break;
            }
            collisionOn = false;
            actionLockCounter = 0;
            return;
        }

        if (actionLockCounter >= 120) {
            int i = random.nextInt(100) + 1;
            if (i <= 25) Direction = "up";
            else if (i <= 50) Direction = "down";
            else if (i <= 75) Direction = "left";
            else Direction = "right";
            actionLockCounter = 0;
        }

        // Increment projectile cooldown even when not chasing
        if (shotAvailableCounter < 90) shotAvailableCounter++;
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

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
        returningToSpawn = false;

        if (gp.player != null) {
            switch (gp.player.Direction) {
                case "up":    Direction = "down";  break;
                case "down":  Direction = "up";    break;
                case "left":  Direction = "right"; break;
                case "right": Direction = "left";  break;
            }
        }
    }

    // =============================
    // Drop table
    // =============================
    @Override
    public void checkDrop() {
        int roll = random.nextInt(100) + 1;
        if (roll < 40) return; // 40% nothing
        else if (roll < 60) dropItem(new OBJ_Coin_Bronze(gp));   // 20%
        else if (roll < 75) dropItem(new OBJ_Arrows(gp));        // 15%
        else if (roll < 85) dropItem(new OBJ_Heart(gp));         // 10%
        else if (roll < 93) dropItem(new OBJ_ManaCrystal(gp));   // 8%
        else if (roll < 98) dropItem(new OBJ_Potion_Blue(gp));   // 5%
        else dropItem(new OBJ_Potion_Red(gp));                  // 2%
    }
}