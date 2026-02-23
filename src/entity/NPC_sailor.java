package entity;

import java.util.Random;

import main.GamePanel;
import object.OBJ_tablet;

public class NPC_sailor extends Entity {

    private int questState = 0; // 0: not started, 1: quest active, 2: key found, 3: completed
    private String[] dialoguePages;
    private int currentPage = 0;
    private boolean keySpawned = false;
    private boolean hasKey = false;

    public NPC_sailor(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;

        getImage();
        
        // Initialize dialogues array
        dialogues = new String[10][10];

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32; 
    }
    
    public void getImage() {
        up1 = setup("/npc/npc_3_back", gp.TileSize, gp.TileSize);
        up2 = setup("/npc/npc_3_back", gp.TileSize, gp.TileSize);
        down1 = setup("/npc/npc_3_front", gp.TileSize, gp.TileSize);
        down2 = setup("/npc/npc_3_front", gp.TileSize, gp.TileSize);
        left1 = setup("/npc/npc_3_left", gp.TileSize, gp.TileSize);
        left2 = setup("/npc/npc_3_left", gp.TileSize, gp.TileSize);
        right1 = setup("/npc/npc_3_right", gp.TileSize, gp.TileSize);
        right2 = setup("/npc/npc_3_right", gp.TileSize, gp.TileSize);
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
            gp.ui.setDialogue(dialoguePages);
        }
        
        // Enter dialogue state
        gp.gameState = gp.dialogueState;
    }
    
    private void prepareDialoguePages() {
        if (questState == 0) {
            // First meeting - welcome
            dialoguePages = new String[] {
                "Ahoy there, adventurer!",
                "Welcome to the island!",
                "If you want to sail to other islands,",
                "you'll need to find the special key.",
                "It's hidden somewhere on this island."
            };
            questState = 1;
        }
        else if (questState == 1) {
            // Check if player has the key in inventory
            if (checkPlayerHasKey()) {
                // Player has found the key
                dialoguePages = new String[] {
                    "You found the key! Well done!",
                    "Now you can sail to other islands.",
                    "My boat is ready when you are.",
                    "Just step aboard whenever you're ready!"
                };
                questState = 2;
            } else {
                // Still looking for key
                dialoguePages = new String[] {
                    "Still looking for that key?",
                    "Keep searching! It's somewhere on this island.",
                    "Check behind rocks and in hidden areas."
                };
            }
        }
        else if (questState == 2) {
            // Player has key and can travel
            dialoguePages = new String[] {
                "Ready to set sail?",
                "My boat will take you to new lands.",
                "Just step aboard when you're ready!"
            };
        }
        else if (questState == 3) {
            // After traveling
            dialoguePages = new String[] {
                "Welcome back!",
                "Did you find what you were looking for?",
                "I can take you to other islands anytime."
            };
        }
    }
    
    /**
     * Check if player has the key in their inventory
     */
    private boolean checkPlayerHasKey() {
        if (gp.player == null) return false;
        
        // Check if player has key in inventory
        for (int i = 0; i < gp.player.inventory.size(); i++) {
            if (gp.player.inventory.get(i) != null) {
                String itemName = gp.player.inventory.get(i).name;
                if (itemName != null && itemName.contains("Key")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Call this method when player presses ENTER during dialogue
     */
    public void nextDialogue() {
        if (!gp.ui.isDialogueFinished()) {
            // If animation isn't finished, skip to the end
            gp.ui.skipToEnd();
        } else {
            // Move to next page
            currentPage++;
            
            if (dialoguePages != null && currentPage < dialoguePages.length) {
                // Show next page - still in dialogue state
                gp.ui.setDialogue(dialoguePages[currentPage]);
                gp.gameState = gp.dialogueState;
            } else {
                // No more pages, close dialogue
                gp.gameState = gp.playState;
                currentPage = 0;
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
                System.out.println("Tablet spawned at (11, 24)");
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