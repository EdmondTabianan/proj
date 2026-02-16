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

public class MON_GreenSlime extends Entity {

    GamePanel gp;
    
    // Spawn point tracking
    private int spawnWorldX;
    private int spawnWorldY;
    private int aggroRange = 8; // Tiles - increased for better gameplay
    private boolean returningToSpawn = false;
    
    // Pathfinding variables
    private int pathUpdateCounter = 0;
    private final int PATH_UPDATE_DELAY = 60; // Update path every 60 frames (1 second)
    private int aggroCheckCounter = 0;
    private final int AGGRO_CHECK_DELAY = 30; // Check aggro every 30 frames

    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        this.gp = gp;
        
        type = type_monster;
        name = "Green Slime";
        action = true;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 8;
        life = maxLife;
        attack = 1;
        defense = 0;
        exp = 3;

        projectiles = new OBJ_Bato(gp);

        solidArea.x = 3;
        solidArea.y = 10;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    // Call this after setting worldX/worldY to record spawn point
    public void setSpawnPoint(int worldX, int worldY) {
        this.spawnWorldX = worldX;
        this.spawnWorldY = worldY;
    }

    public void getImage() {
        up1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        up2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        down1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        down2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        left1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        left2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
        right1 = setup("/monster/greenslime_down_1", gp.TileSize, gp.TileSize);
        right2 = setup("/monster/greenslime_down_2", gp.TileSize, gp.TileSize);
    }
    
    public void update() {
        // Check aggro FIRST before movement
        checkAggro();
        
        // Then do normal update (which calls setAction() and moves)
        super.update();
    }
    
    private void checkAggro() {
        if (gp.player == null) return;
        
        aggroCheckCounter++;
        if (aggroCheckCounter < AGGRO_CHECK_DELAY) return;
        aggroCheckCounter = 0;
        
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistanceFromPlayer = (xDistance + yDistance) / gp.TileSize;
        
        // Calculate distance from spawn point
        if (spawnWorldX != 0 || spawnWorldY != 0) {
            int xSpawnDistance = Math.abs(worldX - spawnWorldX);
            int ySpawnDistance = Math.abs(worldY - spawnWorldY);
            int tileDistanceFromSpawn = (xSpawnDistance + ySpawnDistance) / gp.TileSize;
        
            // Check if too far from spawn while following
            if (onPath == true && tileDistanceFromSpawn > aggroRange) {
                returningToSpawn = true;
                onPath = false;
                System.out.println("Returning to spawn - too far!");
            }
            
            // Check if should start following player
            if (onPath == false && !returningToSpawn && tileDistanceFromPlayer < 3) {
                int i = new Random().nextInt(100) + 1;
                if (i > 50) {
                    onPath = true;
                    System.out.println("GreenSlime aggroed on player!");
                }
            }
            
            // If we're at spawn while returning, stop returning
            if (returningToSpawn && tileDistanceFromSpawn <= 1) {
                returningToSpawn = false;
                System.out.println("Returned to spawn");
            }
        }
    }

    public void setAction() {
        if (returningToSpawn && (spawnWorldX != 0 || spawnWorldY != 0)) {
            // Return to spawn point
            int spawnCol = spawnWorldX / gp.TileSize;
            int spawnRow = spawnWorldY / gp.TileSize;
            
            // Only update path periodically
            pathUpdateCounter++;
            if (pathUpdateCounter > PATH_UPDATE_DELAY) {
                boolean pathFound = searchPath(spawnCol, spawnRow);
                if (!pathFound) {
                    // If no path found, move directly towards spawn
                    moveTowards(spawnWorldX, spawnWorldY);
                }
                pathUpdateCounter = 0;
            }
            
            // If very close to spawn, stop returning
            if (Math.abs(worldX - spawnWorldX) < gp.TileSize && 
                Math.abs(worldY - spawnWorldY) < gp.TileSize) {
                returningToSpawn = false;
                onPath = false;
            }
        }
        else if (onPath == true && gp.player != null) {
            // FOLLOW PLAYER
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;
    
            // Only update path periodically
            pathUpdateCounter++;
            if (pathUpdateCounter > PATH_UPDATE_DELAY) {
                boolean pathFound = searchPath(goalCol, goalRow);
                if (!pathFound) {
                    // If no path found, move directly towards player
                    moveTowards(gp.player.worldX, gp.player.worldY);
                }
                pathUpdateCounter = 0;
            }
            
            // Shooting logic
            shotAvailableCounter++;
            
            if (shotAvailableCounter >= 120) {
                int shootChance = new Random().nextInt(100) + 1;
                if (shootChance > 70) {
                    // Check if player is in line of sight
                    if (isPlayerInLineOfSight()) {
                        // Create a NEW projectile each time
                        OBJ_Bato newProjectile = new OBJ_Bato(gp);
                        newProjectile.set(worldX, worldY, Direction, true, this);
                        for (int ii = 0; ii < gp.projectile[1].length; ii++) {
                            if (gp.projectile[gp.currentMap][ii] == null) {
                                gp.projectile[gp.currentMap][ii] = newProjectile;
                                break;
                            }
                        }
                        shotAvailableCounter = 0;
                    }
                } else {
                    shotAvailableCounter = 90;
                }
            }
        } 
        else {
            // RANDOM MOVEMENT
            actionLockCounter++;
    
            // Change direction every 2 seconds (120 frames)
            if (actionLockCounter >= 120) {
                getRandomDirection();
                actionLockCounter = 0;
            }
            
            // Increment cooldown when not on path
            if (shotAvailableCounter < 90) {
                shotAvailableCounter++;
            }
        }
    }
    
    // Helper method to move directly towards a target
    private void moveTowards(int targetX, int targetY) {
        int dx = targetX - worldX;
        int dy = targetY - worldY;
        
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                Direction = "right";
            } else {
                Direction = "left";
            }
        } else {
            if (dy > 0) {
                Direction = "down";
            } else {
                Direction = "up";
            }
        }
    }
    
    @Override
    public boolean searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / gp.TileSize;
        int startRow = (worldY + solidArea.y) / gp.TileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);

        if (gp.pFinder.search() == true) {
            // Get the next node from the path
            if (gp.pFinder.pathList != null && !gp.pFinder.pathList.isEmpty()) {
                int nextX = gp.pFinder.pathList.get(0).col * gp.TileSize;
                int nextY = gp.pFinder.pathList.get(0).row * gp.TileSize;
                
                // Get entity's center position
                int enCenterX = worldX + solidArea.x + solidArea.width/2;
                int enCenterY = worldY + solidArea.y + solidArea.height/2;
                
                // Determine direction based on next node position
                if (Math.abs(enCenterX - (nextX + gp.TileSize/2)) > 5) {
                    if (enCenterX < nextX + gp.TileSize/2) {
                        Direction = "right";
                    } else {
                        Direction = "left";
                    }
                } else if (Math.abs(enCenterY - (nextY + gp.TileSize/2)) > 5) {
                    if (enCenterY < nextY + gp.TileSize/2) {
                        Direction = "down";
                    } else {
                        Direction = "up";
                    }
                } else {
                    // Reached the node, remove it from path
                    gp.pFinder.pathList.remove(0);
                }
                return true;
            }
        }
        return false;
    }
    
    // Helper method to check if player is in line of sight
    private boolean isPlayerInLineOfSight() {
        if (gp.player == null) return false;
        
        int playerCol = (gp.player.worldX + gp.player.solidArea.x) / gp.TileSize;
        int playerRow = (gp.player.worldY + gp.player.solidArea.y) / gp.TileSize;
        int monsterCol = (worldX + solidArea.x) / gp.TileSize;
        int monsterRow = (worldY + solidArea.y) / gp.TileSize;
        
        // Simple check: is player within 3 tiles and roughly in the direction monster is facing?
        int colDiff = Math.abs(playerCol - monsterCol);
        int rowDiff = Math.abs(playerRow - monsterRow);
        
        if (colDiff > 3 || rowDiff > 3) return false;
        
        switch (Direction) {
            case "up":    return playerRow < monsterRow && colDiff <= 2;
            case "down":  return playerRow > monsterRow && colDiff <= 2;
            case "left":  return playerCol < monsterCol && rowDiff <= 2;
            case "right": return playerCol > monsterCol && rowDiff <= 2;
            default: return false;
        }
    }
    
    // Helper method for random direction with equal probability
    private void getRandomDirection() {
        Random random = new Random();
        int i = random.nextInt(4); // 0-3
        
        switch (i) {
            case 0: Direction = "up"; break;
            case 1: Direction = "down"; break;
            case 2: Direction = "left"; break;
            case 3: Direction = "right"; break;
        }
    }
    
    public void damageReaction() {
        if (gp.player != null) {
            actionLockCounter = 0;
            onPath = true;
            returningToSpawn = false; // Cancel return if damaged
            System.out.println("GreenSlime damaged - aggro!");
            
            // Face the player when damaged
            switch (gp.player.Direction) {
                case "up":    Direction = "down";  break;
                case "down":  Direction = "up";    break;
                case "left":  Direction = "right"; break;
                case "right": Direction = "left";  break;
            }
        }
    }
    
    public void checkDrop() {
        int roll = new Random().nextInt(100) + 1;

        if (roll < 40) {
            // no drop (40%)
        }
        else if (roll < 60) {
            dropItem(new OBJ_Coin_Bronze(gp));     // 20%
        }
        else if (roll < 75) {
            dropItem(new OBJ_Arrows(gp));          // 15%
        }
        else if (roll < 85) {
            dropItem(new OBJ_Heart(gp));           // 10%
        }
        else if (roll < 93) {
            dropItem(new OBJ_ManaCrystal(gp));     // 8%
        }
        else if (roll < 98) {
            dropItem(new OBJ_Potion_Blue(gp));     // 5%
        }
        else if (roll < 100) {
            dropItem(new OBJ_Potion_Red(gp));      // 2%
        }
    }
}