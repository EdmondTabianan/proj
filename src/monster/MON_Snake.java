package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Arrows;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;

public class MON_Snake extends Entity {

    GamePanel gp;
    private Random random = new Random();

    public MON_Snake(GamePanel gp) {
        super(gp);
        this.gp = gp;

        action = true;
        type = type_monster;
        name = "Snake";
        defaultSpeed = 2;
        speed = defaultSpeed;
        
        maxLife = 10;
        life = maxLife;
        attack = 2;
        defense = 2;
        exp = 5;

        // Solid area for narrow paths
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

    public void setAction() {
    
        if (onPath == true) {

            //check if stop chasing
            checkStopChasingOrNot(gp.player, 15, 100);
        
            // search direction to go
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
        }
        else {
            //check if start chasing 
            checkStartChasingOrNot(gp.player, 5, 100);
    
            getRandomDirection();
        }
    }   
    
    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
        
        switch (gp.player.Direction) {
            case "up":    Direction = "down";  break;
            case "down":  Direction = "up";    break;
            case "left":  Direction = "right"; break;
            case "right": Direction = "left";  break;
        }
    }
    
    /**
     * Override the dying behavior to track snake kills for quests
     */
    @Override
    public void update() {
        super.update();
        
        // Check if this snake just died
        if (dying && alive == false) {
            handleDeath();
        }
    }
    
    /**
     * Handle death - track kills for quests
     */
    private void handleDeath() {
        // Only count kills on map 6 (passage map) when quest progress is 2
        if (gp.currentMap == 6 && gp.questProgress == 2) {
            // Increment player's kill count
            if (gp.player != null) {
                gp.player.killCount++;
                System.out.println("Snake killed! Total kills: " + gp.player.killCount);
                
                // If this was the 3rd snake, quest progress will update when talking to Beverly
                // Beverly's dialogue will check and update questProgress when all snakes are dead
            }
        }
    }
    
    // =============================
    // Drop table
    // =============================
    @Override
    public void checkDrop() {
        int roll = random.nextInt(100) + 1;
        if (roll < 40) return;
        else if (roll < 60) dropItem(new OBJ_Coin_Bronze(gp));
        else if (roll < 75) dropItem(new OBJ_Arrows(gp));
        else if (roll < 85) dropItem(new OBJ_Heart(gp));
        else if (roll < 93) dropItem(new OBJ_ManaCrystal(gp));
        else if (roll < 98) dropItem(new OBJ_Coin_Bronze(gp)); // Changed to coin instead of potion blue
        else dropItem(new OBJ_Coin_Bronze(gp)); // Changed to coin instead of potion red
    }
}