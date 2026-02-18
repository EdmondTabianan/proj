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

public class MON_MOMMY extends Entity {

    GamePanel gp;
    private Random random = new Random();

    // Spawn point tracking
    private int spawnWorldX;
    private int spawnWorldY;
    private int aggroRange = 8; // Tiles
    private boolean returningToSpawn = false;

    // Timers
    private int aggroCheckCounter = 0;
    private final int AGGRO_CHECK_DELAY = 30;
    private int shootCooldown = 0;
    private final int SHOOT_DELAY = 180; // 3 seconds

    public MON_MOMMY(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Mummy";
        action = true;

        defaultSpeed = 1;
        speed = defaultSpeed;

        // Level scaling
        maxLife = 8 + gp.player.level * 2;
        life = maxLife;
        attack = 3 + gp.player.level / 2;
        defense = 3 + gp.player.level / 3;
        exp = 3 + gp.player.level;

        // Projectile
        projectiles = new OBJ_Bato(gp);

        // Shrunk solid area for narrow paths
        solidArea.x = 8;
        solidArea.y = 12;
        solidArea.width = 32;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void setSpawnPoint(int worldX, int worldY) {
        this.spawnWorldX = worldX;
        this.spawnWorldY = worldY;
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

    @Override
    public void update() {
        checkAggro();
        super.update();

        // Decrement shoot cooldown
        if (shootCooldown > 0) shootCooldown--;
    }

    // =============================
    // AGGRO LOGIC (Slime-style)
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

        // ===== RETURN TO SPAWN =====
        if (returningToSpawn) {
            moveTowards(spawnWorldX, spawnWorldY);
            return;
        }

        // ===== CHASE PLAYER =====
        if (onPath && gp.player != null) {
            moveTowards(gp.player.worldX, gp.player.worldY);

            // Shooting projectile if line of sight and cooldown ready
            if (shootCooldown <= 0 && random.nextInt(100) > 60 && isPlayerInLineOfSight()) {
                OBJ_Bato newProjectile = new OBJ_Bato(gp);
                newProjectile.set(worldX, worldY, Direction, true, this);

                for (int i = 0; i < gp.projectile[1].length; i++) {
                    if (gp.projectile[gp.currentMap][i] == null) {
                        gp.projectile[gp.currentMap][i] = newProjectile;
                        break;
                    }
                }
                shootCooldown = SHOOT_DELAY;
            }
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

    private void moveTowards(int targetX, int targetY) {
        int dx = targetX - worldX;
        int dy = targetY - worldY;

        if (Math.abs(dx) > Math.abs(dy)) {
            Direction = (dx > 0) ? "right" : "left";
        } else {
            Direction = (dy > 0) ? "down" : "up";
        }
    }

    private boolean isPlayerInLineOfSight() {
        if (gp.player == null) return false;

        int playerCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
        int playerRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;
        int monsterCol = (worldX + solidArea.x) / gp.TileSize;
        int monsterRow = (worldY + solidArea.y) / gp.TileSize;

        int colDiff = Math.abs(playerCol - monsterCol);
        int rowDiff = Math.abs(playerRow - monsterRow);

        if (colDiff > 4 || rowDiff > 4) return false;

        switch (Direction) {
            case "up":    return playerRow < monsterRow && colDiff <= 2;
            case "down":  return playerRow > monsterRow && colDiff <= 2;
            case "left":  return playerCol < monsterCol && rowDiff <= 2;
            case "right": return playerCol > monsterCol && rowDiff <= 2;
            default: return false;
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

        switch (gp.player.Direction) {
            case "up":    Direction = "down"; break;
            case "down":  Direction = "up"; break;
            case "left":  Direction = "right"; break;
            case "right": Direction = "left"; break;
        }
    }

    @Override
    public void checkDrop() {
        int roll = random.nextInt(100) + 1;

        if (roll < 40) {}
        else if (roll < 60) dropItem(new OBJ_Coin_Bronze(gp));
        else if (roll < 75) dropItem(new OBJ_Arrows(gp));
        else if (roll < 85) dropItem(new OBJ_Heart(gp));
        else if (roll < 93) dropItem(new OBJ_ManaCrystal(gp));
        else if (roll < 98) dropItem(new OBJ_Potion_Blue(gp));
        else dropItem(new OBJ_Potion_Red(gp));
    }
}
