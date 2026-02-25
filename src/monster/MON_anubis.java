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
import object.OBJ_IronDoor;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;

public class MON_anubis extends Entity {

    GamePanel gp;
    private Random random = new Random();
    public static final String MONSTER_NAME = "Anubis"; 

    public MON_anubis(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = MONSTER_NAME;
        action = true;
        knockBackPower = 5;
        sleep = true; // Start asleep, wakes when player is near

        defaultSpeed = 1;
        speed = defaultSpeed;

        // Level scaling
        int playerLevel = 1;
        if (gp.player != null) {
            playerLevel = gp.player.level;
        }
        
        maxLife = 50 + playerLevel / 5;
        life = maxLife;
        strength = 10;
        attack = strength;
        defense = 3 + playerLevel / 3;
        exp = 50 + playerLevel;

        // Solid area for collision
        int size = gp.TileSize*5;
        solidArea.x = 40;
        solidArea.y = 40;
        solidArea.width = size - 48*2;
        solidArea.height = size - 48*2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Attack area for hit detection
        attackArea.width = 170;
        attackArea.height = 170;
        
        // Motion durations
        motion1_duration = 25;
        motion2_duration = 50;

        getImage();
        getAttackImage();
        setDialogue();
        
        // Set spawn point
        setSpawnPoint(worldX, worldY);
    }

    public void getImage() {
        int i = 5;
        up1 = setup("/monster/anubis_up_1", gp.TileSize * i, gp.TileSize * i);
        up2 = setup("/monster/anubis_up_2", gp.TileSize * i, gp.TileSize * i);
        down1 = setup("/monster/anubis_down_1", gp.TileSize * i, gp.TileSize * i);
        down2 = setup("/monster/anubis_down_2", gp.TileSize * i, gp.TileSize * i);
        left1 = setup("/monster/anubis_left_1", gp.TileSize * i, gp.TileSize * i);
        left2 = setup("/monster/anubis_left_2", gp.TileSize * i, gp.TileSize * i);
        right1 = setup("/monster/anubis_right_1", gp.TileSize * i, gp.TileSize * i);
        right2 = setup("/monster/anubis_right_2", gp.TileSize * i, gp.TileSize * i);
        slowEffectImage = setup("/effects/slow_effect", gp.TileSize * i, gp.TileSize * i);
    }
    
    public void getAttackImage() {
        int i = 5;
        // Up/Down attacks: width normal (32*i), height double (64*i)
        attackUp1 = setup("/monster/skeletonlord_attack_up_1", gp.TileSize * i, gp.TileSize * i * 2);
        attackUp2 = setup("/monster/skeletonlord_attack_up_2", gp.TileSize * i, gp.TileSize * i * 2);
        attackDown1 = setup("/monster/skeletonlord_attack_down_1", gp.TileSize * i, gp.TileSize * i * 2);
        attackDown2 = setup("/monster/skeletonlord_attack_down_2", gp.TileSize * i, gp.TileSize * i * 2);
        
        // Left/Right attacks: width double (64*i), height normal (32*i)
        attackLeft1 = setup("/monster/skeletonlord_attack_left_1", gp.TileSize * i * 2, gp.TileSize * i);
        attackLeft2 = setup("/monster/skeletonlord_attack_left_2", gp.TileSize * i * 2, gp.TileSize * i);
        attackRight1 = setup("/monster/skeletonlord_attack_right_1", gp.TileSize * i * 2, gp.TileSize * i);
        attackRight2 = setup("/monster/skeletonlord_attack_right_2", gp.TileSize * i * 2, gp.TileSize * i);
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

    // public void setDialogue() {
    //     // dialogues = new String[10][10];
    //     dialogues[5][0] = "Anong gentle gentle?";
    //     dialogues[5][1] = "Ilalabas ko ang aking dragon.";
    //     dialogues[5][2] = "Dragon na maliit na may tatong sisiw.";
    //     dialogues[5][3] = "Gusto mo bang makita ang aking 100 prosyento lakas?";
    // } 
    public void setDialogue() {
        System.out.println("MON_anubis.setDialogue() CALLED!");
        
        dialogues[5][0] = "Anong gentle gentle?";
        dialogues[5][1] = "Ilalabas ko ang aking dragon.";
        dialogues[5][2] = "Dragon na maliit na may tatong sisiw.";
        dialogues[5][3] = "Gusto mo bang makita ang aking 100 prosyento lakas?";
        
        System.out.println("Dialogues set at index 5:");
        System.out.println("  [5][0]: " + dialogues[5][0]);
        System.out.println("  [5][1]: " + dialogues[5][1]);
        System.out.println("  [5][2]: " + dialogues[5][2]);
        System.out.println("  [5][3]: " + dialogues[5][3]);
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

    
    public void checkDrop() {

        gp.bossBattleOn = false; // End boss battle when Anubis is defeated

        // restore the prevois music
        gp.stopMusic();
        gp.playMusic(0);

        // remove the iron door
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].name.equals(OBJ_IronDoor.objName)) {
                gp.playSE(3);
                gp.obj[gp.currentMap][i] = null;
            }
        }

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