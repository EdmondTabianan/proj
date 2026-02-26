package entity;

import java.util.Random;

import main.GamePanel;
import object.OBJ_tablet;

public class NPC_sailor extends Entity {

    private boolean keySpawned = false;
    
    // questState: 0=not started, 1=waiting for key, 2=has key, 3=returned from sailing
    // questStatus: 0=no marker, 1=show quest marker (!) when ready to sail

    public NPC_sailor(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;
        type = type_npc;
        name = "Sailor";

        // Initialize inherited quest variables
        this.questState = 0;      // Not started
        this.questStatus = 0;      // No marker initially
        this.questProgress = 0;    // Reserved for future use

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
    
    /**
     * Get dialogue based on current game state
     * Called from transport() method
     */
    public String[] getShipDialogue() {
        updateQuestState();
        
        // Case 1: GREETINGS - First time meeting (questProgress 0)
        if (gp.questProgress == 0) {
            return new String[] {
                "Ahoy there, Hunter!",
                "Welcome to the island!",
                "You look like you're on an adventure.",
                "You'll need to prove yourself first.",
                "Talk to Vhong in the village.",
                "He'll tell you what needs to be done.",
                "The sailor points toward the village."
            };
        }
        // Case 2: HAS SNAKE KEY (hasKey == 1) - Can sail to pyramid area
        else if (gp.player != null && gp.player.hasKey == 1) {
            this.questState = 2; // Mark as ready to sail
            this.questStatus = 1; // Show marker
            
            if (gp.currentMap == 0) {
                return new String[] {
                    "You have the snake key! You've proven yourself!",
                    "The ancient pyramid lies across the sea.",
                    "But you'll need the PYRAMID KEY to enter its depths.",
                    "Find it first, then return to me.",
                    "The sailor points toward the northern ruins."
                };
            } else {
                return new String[] {
                    "Welcome back, brave traveler!",
                    "Did you find the pyramid key?",
                    "Ready to return to the main island?",
                    "The sailor unties the ropes.",
                    "Returning to first map..."
                };
            }
        }
        // Case 3: HAS PYRAMID KEY (hasKey == 2) - FINAL QUEST - Can enter pyramid
        else if (gp.player != null && gp.player.hasKey == 2) {
            this.questState = 2;
            this.questStatus = 1;
            
            if (gp.currentMap == 0) {
                return new String[] {
                    "You have the PYRAMID KEY! The lost tomb awaits!",
                    "Legends speak of an ancient guardian within.",
                    "Defeat the boss to claim the treasure of the ages!",
                    "The sailor unties the ropes with reverence.",
                    "The ship sets sail toward the sacred pyramid island...",
                    "Arriving at the PYRAMID OF THE ANCIENTS!",
                    "May the gods watch over you, hero!"
                };
            } else if (gp.currentMap == 2) {
                return new String[] {
                    "Welcome back to the pyramid, champion!",
                    "The boss awaits in the deepest chamber.",
                    "Defeat it to claim the lost tomb's treasure!",
                    "Ready to return to the main island?"
                };
            } else {
                return new String[] {
                    "The pyramid looms before you.",
                    "Somewhere inside lies the ancient guardian.",
                    "Defeat it and the lost tomb is yours!"
                };
            }
        }
        // Case 4: AFTER BOSS DEFEAT - Game complete (questProgress >= 5)
        else if (gp.questProgress >= 5) {
            return new String[] {
                "You did it! You defeated the ancient guardian!",
                "The lost tomb is yours to claim!",
                "Your legend will be told for generations!",
                "The ancient spirits bow to you, hero!"
            };
        }
        // Case 5: TALK TO VHONG - On slime quest (questProgress 1, no key)
        else if (gp.player != null && gp.player.hasKey == 0 && gp.questProgress == 1) {
            return new String[] {
                "The sailor leans on his ship.",
                "Have you spoken to Vhong yet?",
                "He's the elder in the desert village.",
                "He mentioned something about snakes",
                "blocking the path to an ancient key.",
                "Defeat the snakes and bring back the key.",
                "Then we can talk about sailing to the pyramid."
            };
        }
        // Case 6: TALK TO BEVERLY - After slime quest, before snake quest (questProgress 2)
        else if (gp.questProgress == 2) {
            return new String[] {
                "The sailor scratches his chin.",
                "You've helped Vhong, I see.",
                "But there's someone else you should meet.",
                "Talk to Beverly in the village.",
                "She has a task involving snakes.",
                "Complete her quest to get the snake key.",
                "Then seek the pyramid key from Ding!"
            };
        }
        // Case 7: SNAKE QUEST ACTIVE - On snake quest (questProgress 3)
        else if (gp.questProgress == 3) {
            return new String[] {
                "I see you're hunting snakes.",
                "That's good - you'll need that snake key.",
                "But the real challenge lies beyond.",
                "Defeat all three snakes first,",
                "then seek the pyramid key from Ding.",
                "The pyramid's guardian awaits the worthy!"
            };
        }
        // Case 8: READY FOR PYRAMID KEY - Has snake key, needs pyramid key (questProgress 4, hasKey=1)
        else if (gp.questProgress == 4 && gp.player != null && gp.player.hasKey == 1) {
            return new String[] {
                "You have the snake key, but the pyramid requires more.",
                "Deep in the northern ruins, Ding hides the pyramid key.",
                "Find it, and the pyramid's entrance will open for you.",
                "Then you must face the ancient guardian within!",
                "Return to me when you hold the PYRAMID KEY."
            };
        }
        // Case 9: DEFAULT
        else {
            return new String[] {
                "The sailor shakes his head.",
                "Not yet, adventurer.",
                "Complete the tasks given to you.",
                "Seek the snake key, then the pyramid key.",
                "The ancient guardian awaits the worthy."
            };
        }
    }
    
    /**
     * Get regular interaction dialogue (when talking to sailor directly)
     */
    @Override
    public void prepareDialoguePages() {
        // Regular NPC interaction dialogue (when talking to sailor directly)
        if (gp.questProgress == 0) {
            dialoguePages = new String[] {
                "Ahoy there, adventurer!",
                "Welcome to the desert port!",
                "If you seek the pyramid,",
                "you'll need to prove yourself first.",
                "Talk to Vhong in the village."
            };
        }
        else if (gp.player != null && gp.player.hasKey == 1) {
            dialoguePages = new String[] {
                "You have the snake key!",
                "Now find the pyramid key from Ding.",
                "Then return to sail to your destiny!"
            };
        }
        else if (gp.player != null && gp.player.hasKey == 2) {
            dialoguePages = new String[] {
                "You have the PYRAMID KEY!",
                "The ancient guardian awaits within.",
                "Step aboard when you're ready to face your fate!"
            };
        }
        else if (gp.questProgress >= 5) {
            dialoguePages = new String[] {
                "You defeated the guardian!",
                "You are a true legend!",
                "The lost tomb's treasure is yours!"
            };
        }
        else {
            dialoguePages = new String[] {
                "Come back when you've proven yourself.",
                "Find the keys to unlock the pyramid's secrets.",
                "The ancient guardian awaits the worthy."
            };
        }
        
        // Safety check
        if (dialoguePages == null) {
            dialoguePages = new String[] {"..."};
        }
    }
    
    /**
     * Update quest state based on game progress
     */
    private void updateQuestState() {
        // If player has pyramid key, mark as ready for final quest
        if (gp.player != null && gp.player.hasKey == 2) {
            this.questState = 2;
            this.questStatus = 1;
        }
        // If player has snake key but not pyramid key
        else if (gp.player != null && gp.player.hasKey == 1) {
            this.questState = 1;
            this.questStatus = 1;
        }
        
        // If player hasn't started, set to waiting
        if (this.questState == 0 && gp.questProgress > 0) {
            this.questState = 1;
        }
        
        // If player has defeated boss (progress 5)
        if (gp.questProgress >= 5) {
            this.questStatus = 0; // Remove marker - quest complete
        }
    }
    
    /**
     * Called when player actually sails
     */
    public void onSail() {
        if (this.questState == 2) {
            this.questState = 3; // Mark as sailed
        }
    }
    
    /**
     * Get current quest state
     */
    public int getQuestState() {
        return this.questState;
    }
    
    /**
     * Check if player can sail to pyramid (has pyramid key)
     */
    public boolean canSailToPyramid() {
        return (gp.player != null && gp.player.hasKey == 2);
    }
    
    /**
     * Check if player can sail to intermediate area (has snake key)
     */
    public boolean canSailToArea() {
        return (gp.player != null && gp.player.hasKey == 1);
    }
    
    @Override
    public void speak() {
        facePlayer();
        gp.ui.npcIndex = getIndex();
        prepareDialoguePages();
        
        if (dialoguePages != null && dialoguePages.length > 0) {
            gp.ui.setDialogue(dialoguePages);
        }
        
        gp.gameState = gp.dialogueState;
    }
    
    @Override
    public void afterDialogue() {
        // Update quest marker based on keys
        if (gp.player != null && gp.player.hasKey == 2) {
            this.questStatus = 1; // Show marker for pyramid sailing
        } else if (gp.player != null && gp.player.hasKey == 1) {
            this.questStatus = 1; // Show marker for area sailing
        } else {
            this.questStatus = 0;
        }
    }
    
    /**
     * Helper method to find this NPC's index
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
    
    @Override
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