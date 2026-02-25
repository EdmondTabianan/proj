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
    private boolean keySpawned = false;
    private int passageMap = 6; // Map ID for the passage map (where snakes are)
    private int keyMap = 0; // Map ID for where the key spawns (main map)
    private int requiredSnakeKills = 3; // Need to kill 3 snakes

    public NPC_Beverly(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;

        getImage();
        
        // Initialize dialogues array
        dialogues = new String[10][10];
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
        dialogues[i][0] = "Kill that snake, please"; i++;
        dialogues[i][1] = "Thanks for killing the snake"; i++;
        dialogues[i][1] = "Take this key"; i++;
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
        
        // Set the current NPC index in UI
        gp.ui.npcIndex = getIndex();
        
        // Prepare dialogue pages based on quest state
        prepareDialoguePages();
        
        // Start with first page
        if (dialoguePages != null && dialoguePages.length > 0) {
            currentPage = 0;
            gp.ui.setDialogue(dialoguePages);
        } else {
            // Fallback dialogue if something went wrong
            dialoguePages = new String[] {"..."};
            gp.ui.setDialogue(dialoguePages);
        }
        
        // Enter dialogue state
        gp.gameState = gp.dialogueState;
    }
    
    private void prepareDialoguePages() {
        if (questState == 0) {
            // First meeting - give quest
            dialoguePages = new String[] {
                "Hello there, hunter!",
                "There are dangerous snakes in the passage.",
                "Clear all 3 snakes in the passage for me.",
                "I'll reward you with a key when you're done."
            };
            questState = 1; // Quest active
        }
        else if (questState == 1) {
            // Check if all snakes are killed using player's killCount
            int killCount = (gp.player != null) ? gp.player.killCount : 0;
            
            if (killCount == 0) {
                // No snakes killed yet
                dialoguePages = new String[] {
                    "You haven't killed any snakes yet!",
                    "Please go to the passage and kill the 3 snakes.",
                    "They're lurking somewhere in there.",
                    "Come back when you're done!"
                };
            }
            else if (killCount < requiredSnakeKills) {
                // Some snakes killed, but not all
                int remaining = requiredSnakeKills - killCount;
                dialoguePages = new String[] {
                    "You've killed " + killCount + " out of " + requiredSnakeKills + " snakes.",
                    "Keep going! You still need to kill " + remaining + " more.",
                    "They're waiting for you in the passage.",
                    "Come back when they're all gone!"
                };
            }
            else if (killCount >= requiredSnakeKills) {
                // UPDATE: If killcount is 3, increment questprogress by 3
                // All snakes killed - quest complete
                dialoguePages = new String[] {
                    "You cleared all the snakes! Thank you so much!",
                    "Here's your reward - a special key.",
                    "This key will open the ancient door.",
                    "Good luck on your journey!"
                };
                
                // UPDATE: Increment questProgress by 3 when snakes are killed
                if (!keySpawned) {
                    // Spawn the key first
                    spawnKey(7, 10, 0); // Spawn at col 7, row 10 on map 0
                    keySpawned = true;
                    
                    // Increment questProgress by 3
                    gp.questProgress += 3;
                    
                    // Reset kill count for future quests
                    if (gp.player != null) {
                        gp.player.killCount = 0;
                    }
                }
                
                questState = 2; // Quest completed
            }
        }
        else if (questState == 2) {
            // After receiving reward
            dialoguePages = new String[] {
                "Thank you again for your help!",
                "That key will open the ancient door.",
                "I heard it leads to great treasure.",
                "Good luck on your journey!"
            };
            questState = 3; // Final state
        }
        else if (questState == 3) {
            // UPDATE: If questprogress is done, thank the player
            // Final repeated dialogue - thanking the player
            int killCount = (gp.player != null) ? gp.player.killCount : 0;
            
            if (gp.questProgress >= 3) {
                // Quest is completed, thank the player
                dialoguePages = new String[] {
                    "Thank you so much for your help!",
                    "The key I gave you is very special.",
                    "It will open the ancient door.",
                    "May the gods watch over your journey!"
                };
            } else {
                // Default dialogue
                dialoguePages = new String[] {
                    "Remember, the key opens the ancient door.",
                    "You've killed " + killCount + " snakes in total.",
                    "You're becoming quite the hunter!",
                    "Farewell, brave hunter!"
                };
            }
        }
        
        // Safety check - ensure dialoguePages is never null
        if (dialoguePages == null) {
            dialoguePages = new String[] {"..."};
        }
    }
    
    /**
     * Count how many snakes have been killed in the passage map
     * This is kept for backward compatibility but now uses player.killCount
     */
    private int countSnakesKilledInPassage() {
        if (gp.player != null) {
            return gp.player.killCount;
        }
        return 0;
    }
    
    /**
     * Spawn a key at the specified grid position on the specified map
     * @param col Grid column
     * @param row Grid row
     * @param map The map ID to spawn the key on
     */
    public void spawnKey(int col, int row, int map) {
        int worldX = col * gp.TileSize;
        int worldY = row * gp.TileSize;
        
        // Find an empty slot in the objects array for the specified map
        for (int i = 0; i < gp.obj[map].length; i++) {
            if (gp.obj[map][i] == null) {
                gp.obj[map][i] = new OBJ_Key(gp);
                gp.obj[map][i].worldX = worldX;
                gp.obj[map][i].worldY = worldY;
                gp.obj[map][i].isPickup = true; // Mark as pickup
                
                break;
            }
        }
    }
    
    /**
     * Call this method when player presses ENTER during dialogue
     */
    public void nextDialogue() {
        // Safety check - if dialoguePages is null, prepare dialogue again
        if (dialoguePages == null) {
            prepareDialoguePages();
            
            // If still null, close dialogue
            if (dialoguePages == null) {
                gp.gameState = gp.playState;
                currentPage = 0;
                return;
            }
        }
        
        if (!gp.ui.isDialogueFinished()) {
            // If animation isn't finished, skip to the end
            gp.ui.skipToEnd();
        } else {
            // Move to next page or close dialogue
            currentPage++;
            
            if (currentPage < dialoguePages.length) {
                // Show next page
                String[] remainingPages = new String[dialoguePages.length - currentPage];
                for (int i = 0; i < remainingPages.length; i++) {
                    remainingPages[i] = dialoguePages[currentPage + i];
                }
                gp.ui.setDialogue(remainingPages);
                gp.gameState = gp.dialogueState;
            } else {
                // No more pages, close dialogue
                gp.gameState = gp.playState;
                currentPage = 0; // Reset for next conversation
            }
        }
    }
    
    /**
     * Helper method to find this NPC's index in the npc array
     */
    private int getIndex() {
        if (gp.npc == null || gp.npc[gp.currentMap] == null) return 0;
        
        for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
            if (gp.npc[gp.currentMap][i] == this) {
                return i;
            }
        }
        return 0;
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