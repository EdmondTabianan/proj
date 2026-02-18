package entity;

import java.util.Random;

import main.GamePanel;
import object.OBJ_tablet;

public class NPC_vhong extends Entity {

    int questState = 0;
    private String[] dialoguePages; // For multi-page dialogue
    private int currentPage = 0;

    public NPC_vhong(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;

        getImage();
        
        // Initialize with default dialogue (safe version without player)
        dialogues = new String[10]; // Initialize array
        dialogues[0] = "Kill 3 slimes.";
        dialogues[1] = "You killed 3 slimes!";
        dialogues[2] = "Take the clue";
        dialogues[3] = "Test";

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32; 
    }
    
    public void getImage() {
        up1 = setup("/npc/npc_1_back", gp.TileSize, gp.TileSize);
        up2 = setup("/npc/npc_1_back", gp.TileSize, gp.TileSize);
        down1 = setup("/npc/npc_1_front", gp.TileSize, gp.TileSize);
        down2 = setup("/npc/npc_1_front", gp.TileSize, gp.TileSize);
        left1 = setup("/npc/npc_1_left", gp.TileSize, gp.TileSize);
        left2 = setup("/npc/npc_1_left", gp.TileSize, gp.TileSize);
        right1 = setup("/npc/npc_1_right", gp.TileSize, gp.TileSize);
        right2 = setup("/npc/npc_1_right", gp.TileSize, gp.TileSize);
    }
    
    public void setDialogue() {
        // Only access player if it exists
        if (gp.player != null) {
            dialogues[0] = "Kill 3 slimes. " + gp.player.killCount + "/3";
            dialogues[1] = "You killed 3 slimes!";
            dialogues[2] = "Take the clue";
            dialogues[3] = "Test";
        } else {
            // Default dialogue when player doesn't exist yet
            dialogues[0] = "Kill 3 slimes.";
            dialogues[1] = "You killed 3 slimes!";
            dialogues[2] = "Take the clue";
            dialogues[3] = "Test";
        }
    }
    
    public void setAction(){
        if (onPath == true) {
            int goalCol = 7;
            int goalRow = 10;
            searchPath(goalCol, goalRow);
        } 
        else {
            actionLockCounter++;

            if (collisionOn == true) {
                Random random = new Random();
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

            if(actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;
                
                if (i <= 25) {
                    Direction = "up";
                } else if (i <= 50) {
                    Direction = "down";
                } else if (i <= 75) {
                    Direction = "left";
                } else {
                    Direction = "right";
                }
                actionLockCounter = 0;
            }
        }
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
            // First meeting - multiple pages
            dialoguePages = new String[] {
                "Hello adventurer!",
                "I have an important quest for you.",
                "Kill 3 slimes in the forest.",
                "Come back when you're done!"
            };
            gp.player.killCount = 0; // Reset kill count for quest
            questState = 1;
        }
        else if (questState == 1) {
            if (gp.player.killCount < 3) {
                // Quest in progress - single page with dynamic count
                dialoguePages = new String[] {
                    "You still need to kill " + (3 - gp.player.killCount) + " slimes.\nCome back when you're done."
                };
            } else {
                // Quest complete - multiple pages
                dialoguePages = new String[] {
                    "You killed 3 slimes! Well done!",
                    "Here's your reward.",
                    "Take this clue tablet."
                };
                spawnTablet();
                questState = 2;
            }
        }
        else if (questState == 2) {
            // After receiving reward
            dialoguePages = new String[] {
                "The clue is at the entrance of the passage.",
                "It will guide you to the treasure.",
                "Good luck on your journey!"
            };
            questState = 3;
        }
        else if (questState == 3) {
            // Final repeated dialogue
            dialoguePages = new String[] {
                "Remember, the clue is at the passage entrance.",
                "Farewell, adventurer!"
            };
        }
    }
    
    /**
     * Call this method when player presses ENTER during dialogue
     * Handles advancing through pages and animation
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
                
                // Optional: Trigger any post-dialogue actions
                if (questState == 2 && currentPage >= dialoguePages.length) {
                    // Quest just completed - you could add special effects here
                }
            }
        }
    }
    
    public void spawnTablet() {
        int currentMap = gp.currentMap;
    
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
            if (gp.obj[currentMap][i] == null) {
                gp.obj[currentMap][i] = new OBJ_tablet(gp);
                gp.obj[currentMap][i].worldX = gp.TileSize * 11;
                gp.obj[currentMap][i].worldY = gp.TileSize * 24;
                break;
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