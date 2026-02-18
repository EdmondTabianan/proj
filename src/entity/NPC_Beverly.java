package entity;

import java.util.Random;

import main.GamePanel;
import object.OBJ_Key;

public class NPC_Beverly extends Entity {

    // Quest state tracking
    private int questState = 0; // 0: quest not started, 1: quest active, 2: quest complete
    private String[] dialoguePages;
    private int currentPage = 0;
    
    // Snake kill tracking
    private int requiredSnakeKills = 1; // Only need to kill 1 snake
    private boolean keySpawned = false;

    public NPC_Beverly(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;

        getImage();
        
        // Initialize dialogues array
        dialogues = new String[10];
        setDialogue();

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32; 
    }
    
    public void getImage() {
        up1 = setup("/npc/npc_2_back", gp.TileSize, gp.TileSize);
        up2 = setup("/npc/npc_2_back", gp.TileSize, gp.TileSize);
        down1 = setup("/npc/npc_2_front", gp.TileSize, gp.TileSize);
        down2 = setup("/npc/npc_2_front", gp.TileSize, gp.TileSize);
        left1 = setup("/npc/npc_2_left", gp.TileSize, gp.TileSize);
        left2 = setup("/npc/npc_2_left", gp.TileSize, gp.TileSize);
        right1 = setup("/npc/npc_2_right", gp.TileSize, gp.TileSize);
        right2 = setup("/npc/npc_2_right", gp.TileSize, gp.TileSize);
    }
    
    public void setDialogue() {
        int i = 0;
        dialogues[i] = "Kill that snake, please"; i++;
        dialogues[i] = "Thanks for killing the snake"; i++;
        dialogues[i] = "Take this key"; i++;
    }
    
    public void setAction() {
        // NPC stands still - no movement
        // You can add patrol patterns here if needed
    }
    
    public void idle() {
        //nothing
    }
    
    public void speak() {
        facePlayer();
        
        // Prepare dialogue pages based on quest state
        prepareDialoguePages();
        
        // Start with first page
        if (dialoguePages != null && dialoguePages.length > 0) {
            currentPage = 0;
            gp.ui.setDialogue(dialoguePages[currentPage]);
        }
        
        // Enter dialogue state
        gp.gameState = gp.dialogueState;
    }
    
    private void prepareDialoguePages() {
        if (questState == 0) {
            // First meeting - give quest
            dialoguePages = new String[] {
                "Hello there, adventurer!",
                "There's a dangerous snake in the area.",
                "Kill that snake for me, please.",
                "I'll reward you with a key when you're done."
            };
            questState = 1; // Quest active
        }
        else if (questState == 1) {
            // Check if snake is killed
            if (checkSnakeKilled()) {
                // Quest complete
                dialoguePages = new String[] {
                    "You killed the snake! Thank you!",
                    "Here's your reward - a special key.",
                    "Take this key, it opens the ancient door."
                };
                
                // Spawn the key if not already spawned
                if (!keySpawned) {
                    spawnKey(7, 10); // Spawn at col 7, row 10
                    keySpawned = true;
                }
                
                questState = 2; // Quest completed
            } else {
                // Quest still in progress
                dialoguePages = new String[] {
                    "Please kill that snake for me.",
                    "It's still lurking around somewhere.",
                    "Come back when it's dead."
                };
            }
        }
        else if (questState == 2) {
            // After receiving reward
            dialoguePages = new String[] {
                "Thank you again for your help!",
                "That key will open the ancient door.",
                "Good luck on your journey!"
            };
            questState = 3; // Final state
        }
        else if (questState == 3) {
            // Final repeated dialogue
            dialoguePages = new String[] {
                "Remember, the key opens the ancient door.",
                "Farewell, brave adventurer!"
            };
        }
    }
    
    /**
     * Check if the snake monster has been killed
     */
    private boolean checkSnakeKilled() {
        // You need to track snake deaths - here are a few approaches:
        
        // Approach 1: Track a specific snake by reference (if you have a reference to it)
        // if (targetSnake != null && targetSnake.isDead()) {
        //     return true;
        // }
        
        // Approach 2: Use a global kill counter for snakes
        if (gp.player != null) {
            // You would need a snakeKillCount variable in Player class
            // return gp.player.snakeKillCount >= requiredSnakeKills;
            
            // For now, let's check if there are any snakes alive on the map
            boolean snakeFound = false;
            for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
                if (gp.monster[gp.currentMap][i] != null) {
                    String monsterName = gp.monster[gp.currentMap][i].name;
                    if (monsterName != null && monsterName.contains("Snake")) {
                        snakeFound = true;
                        break;
                    }
                }
            }
            
            // If no snakes found on the map, consider the quest complete
            return !snakeFound;
        }
        
        return false;
    }
    
    /**
     * Spawn a key at the specified grid position
     * @param col Grid column
     * @param row Grid row
     */
    public void spawnKey(int col, int row) {
        int currentMap = gp.currentMap;
        int worldX = col * gp.TileSize;
        int worldY = row * gp.TileSize;
        
        // Find an empty slot in the objects array
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
            if (gp.obj[currentMap][i] == null) {
                gp.obj[currentMap][i] = new OBJ_Key(gp);
                gp.obj[currentMap][i].worldX = worldX;
                gp.obj[currentMap][i].worldY = worldY;
                
                System.out.println("Key spawned at col " + col + ", row " + row);
                break;
            }
        }
    }
    
    /**
     * Call this method when player presses ENTER during dialogue
     */
    public void nextDialogue() {
        if (!gp.ui.isDialogueFinished()) {
            // If animation isn't finished, skip to the end
            gp.ui.skipToEnd();
        } else {
            // Move to next page or close dialogue
            currentPage++;
            
            if (dialoguePages != null && currentPage < dialoguePages.length) {
                // Show next page
                gp.ui.setDialogue(dialoguePages[currentPage]);
            } else {
                // No more pages, close dialogue
                gp.gameState = gp.playState;
            }
        }
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
}