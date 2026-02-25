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
        solidArea.height = 38; 
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
        
        // Set the current NPC index in UI so KeyHandler knows which NPC we're talking to
        gp.ui.npcIndex = getIndex();
        
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
            // 1. GREETINGS - First meeting
            dialoguePages = new String[] {
                "Ahoy there, adventurer!",
                "Welcome to dessert!",
                "You look like you're on a quest.",
                "If you need passage on my ship,",
                "you'll need to prove yourself first.",
                "Talk to Vhong in the village.",
                "He'll tell you what needs to be done."
            };
            questState = 1;
        }
        else if (questState == 1) {
            // Check if player has talked to Vhong and completed snake quest
            if (gp.player != null && gp.player.hasKey == 1) {
                // 3. IF HAS KEY - Player has the key from snakes
                dialoguePages = new String[] {
                    "Ah, I see you have the key!",
                    "You must have defeated those dangerous snakes.",
                    "Vhong told me about your bravery.",
                    "My ship is ready for you.",
                    "Where would you like to sail?",
                    "We can reach the mainland or the ancient ruins.",
                    "Just step aboard when you're ready to depart!"
                };
                questState = 2;
            } else {
                // 2. TALK TO VHONG - Player hasn't completed quest yet
                dialoguePages = new String[] {
                    "Have you spoken to Vhong yet?",
                    "He's the elder in the village center.",
                    "He mentioned something about snakes",
                    "blocking the path to an ancient key.",
                    "Defeat the snakes and bring back the key.",
                    "Then we can talk about sailing."
                };
            }
        }
        else if (questState == 2) {
            // 4. IF HAS KEY (repeated) - Ready to sail with destination options
            dialoguePages = new String[] {
                "Ready to set sail, key bearer?",
                "The wind is good today.",
                "We can head to the mainland,",
                "or to the ancient ruins across the sea.",
                "Just let me know when you're ready!",
                "Step aboard whenever you want to depart."
            };
        }
        else if (questState == 3) {
            // After sailing somewhere - return dialogue
            dialoguePages = new String[] {
                "Welcome back, brave traveler!",
                "Did you find what you were looking for?",
                "Vhong will be happy to see you returned safely.",
                "If you need to sail again, just let me know."
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
                // Need to set just the current page, not the whole array
                String[] singlePage = new String[]{dialoguePages[currentPage]};
                gp.ui.setDialogue(singlePage);
                gp.gameState = gp.dialogueState;
            } else {
                // No more pages, close dialogue
                gp.gameState = gp.playState;
                currentPage = 0;
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