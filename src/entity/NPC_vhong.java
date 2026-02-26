package entity;

import java.util.Random;
import main.GamePanel;
import object.OBJ_tablet;
import monster.MON_EarthSlime;

public class NPC_vhong extends Entity {

    private boolean slimesSpawned = false;
    private boolean tabletSpawned = false;
    
    // NOTE: questState, questStatus, and questProgress are INHERITED from Entity class
    // this.questState - 0=inactive, 1=quest active, 2=quest complete, 3=post-quest
    // this.questStatus - 0=no marker, 1=show quest marker (!) above NPC
    // this.questProgress - (unused in Vhong, but available for NPC-specific tracking)

    public NPC_vhong(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;
        type = type_npc;
        name = "Vhong";

        // Initialize inherited quest variables
        this.questState = 0;      // Start with first dialogue
        this.questStatus = 1;      // Show quest marker at start (!)
        this.questProgress = 0;    // Personal progress (reserved for future use)

        getImage();
        
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
    
    public void setAction() {
        if (onPath) {
            int goalCol = 7;
            int goalRow = 10;
            searchPath(goalCol, goalRow);
        } else {
            actionLockCounter++;

            if (collisionOn) {
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

            if (actionLockCounter == 120) {
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
    
    @Override
    public void prepareDialoguePages() {
        // Check if quest is ready to complete (killCount >= 3 during active quest)
        if (gp.questProgress == 1 && gp.player != null && gp.player.killCount >= 3) {
            // Quest complete - reward dialogue
            dialoguePages = new String[] {
                "Excellent work! You've defeated all the slimes!",
                "As promised, here's your reward.",
                "Take this ancient tablet - it will guide you.",
                "Also, I've unlocked the passage to the east.",
                "You'll find dangerous snakes there, be careful!"
            };
            this.questState = 2;      // Mark as complete
            this.questStatus = 1;      // Keep marker (!) until tablet picked up
            return;
        }
        
        // Regular dialogue based on GLOBAL questProgress
        switch (gp.questProgress) {
            case 0:
                // First meeting - give quest
                dialoguePages = new String[] {
                    "Ah, a brave hunter!",
                    "I have a task for you.",
                    "Three slimes have been causing trouble in the desert.",
                    "Defeat them and return to me!"
                };
                if (gp.player != null) {
                    gp.player.killCount = 0;  // RESET: Start of slime quest
                }
                this.questState = 1;      // Quest active
                this.questStatus = 1;      // Show marker (!) - quest available
                break;
                
            case 1:
                // Quest in progress (killCount < 3)
                if (gp.player != null) {
                    int remaining = 3 - gp.player.killCount;
                    if (gp.player.killCount == 0) {
                        dialoguePages = new String[] {
                            "You haven't killed any slimes yet!",
                            "They're in the desert to the south.",
                            "Come back when you've defeated all three!"
                        };
                    } else {
                        dialoguePages = new String[] {
                            "You've killed " + gp.player.killCount + " out of 3 slimes.",
                            "You still need to defeat " + remaining + " more.",
                            "They're lurking in the desert nearby.",
                            "Come back when you're done!"
                        };
                    }
                } else {
                    dialoguePages = new String[] {
                        "Come back when you've defeated the slimes!",
                        "They're in the desert nearby."
                    };
                }
                this.questState = 1;      // Still active
                this.questStatus = 1;      // Keep marker (!) - quest in progress
                break;
                
            case 2:
                // Tablet reward given - quest moving to completion
                dialoguePages = new String[] {
                    "The passage to the east is now open.",
                    "Defeat the snakes there to prove your worth.",
                    "A merchant named Beverly in that area may have useful items.",
                    "Good luck on your journey!"
                };
                this.questState = 2;      // Complete
                this.questStatus = 0;      // Remove marker - quest done
                break;
                
            case 3:
                // Snake quest active - Vhong gives encouragement
                dialoguePages = new String[] {
                    "How's the snake hunting going?",
                    "Beverly is waiting for you in the eastern desert.",
                    "She'll guide you through the next challenge.",
                    "Stay safe out there!"
                };
                this.questState = 3;      // Post-quest
                this.questStatus = 0;      // No marker
                break;
                
            case 4:
                // Snake quest complete - Vhong acknowledges progress
                dialoguePages = new String[] {
                    "I heard you defeated the snakes!",
                    "The snake key you found will allow you to sail.",
                    "But the real challenge lies in the pyramid.",
                    "Find the pyramid key and discover the lost tomb!",
                    "Now you should find Ding in the northern ruins.",
                    "He has one final challenge for you."
                };
                this.questState = 3;      // Post-quest
                this.questStatus = 0;      // No marker
                break;
                
            case 5:
                // Final state - game complete (found lost tomb)
                dialoguePages = new String[] {
                    "You've done it all!",
                    "The slimes, the snakes, the pyramid...",
                    "You found the lost tomb!",
                    "You're a true legend among hunters!",
                    "The desert is safe because of you!"
                };
                this.questState = 3;      // Post-quest
                this.questStatus = 0;      // No marker
                break;
                
            default:
                // Default dialogue fallback
                dialoguePages = new String[] {
                    "Keep exploring, hunter!",
                    "There's always more to discover.",
                    "Farewell for now!"
                };
                this.questState = 3;
                this.questStatus = 0;
                break;
        }
        
        // Safety check
        if (dialoguePages == null) {
            dialoguePages = new String[] {"..."};
        }
    }
    
    @Override
    public void speak() {
        facePlayer();
        
        // Store whether this is the first meeting
        boolean firstMeeting = (gp.questProgress == 0 && !slimesSpawned);
        
        // Spawn slimes if first meeting
        if (firstMeeting) {
            spawnSlimes();
            slimesSpawned = true;
            gp.ui.showMessage("Vhong: Defeat 3 slimes in the desert!");
        }
        
        findMyIndex();
        prepareDialoguePages();
        
        if (dialoguePages != null && dialoguePages.length > 0) {
            gp.ui.setDialogue(dialoguePages);
        } else {
            gp.ui.setDialogue(new String[]{"..."});
        }
        
        gp.gameState = gp.dialogueState;
    }
    
    @Override
    public void afterDialogue() {
        // Update GLOBAL quest progress AFTER dialogue finishes
        
        // Case 1: First meeting completed - move to quest active (0 → 1)
        if (gp.questProgress == 0 && slimesSpawned) {
            gp.questProgress = 1;
            System.out.println(" QUEST PROGRESS: 0 → 1 (Slime quest active)");
        }
        
        // Case 2: Quest completed (killCount >= 3) - give tablet reward (1 → 2)
        if (this.questState == 2 && gp.questProgress == 1 && gp.player != null && gp.player.killCount >= 3) {
            spawnTablet();
            gp.questProgress = 2;
            gp.playSE(1); // Play reward sound
            
            // RESET KILL COUNT HERE - After slime quest complete, before snake quest
            if (gp.player != null) {
                gp.player.killCount = 0;
                System.out.println(" Kill count reset to 0 for snake quest");
            }
            System.out.println(" QUEST PROGRESS: 1 → 2 (Slime quest complete, tablet spawned)");
        }
        
        // Case 3: Move to post-quest dialogue (2 → 3)
        if (gp.questProgress == 2 && this.questState == 2) {
            gp.questProgress = 3;
            System.out.println(" QUEST PROGRESS: 2 → 3 (Snake quest unlocked)");
            
            // RESET KILL COUNT HERE (backup) - Just to be safe
            if (gp.player != null) {
                gp.player.killCount = 0;
                System.out.println(" Kill count reset to 0 (backup)");
            }
        }
        
        // Case 4: Final completion "ding" (3 → 4) - before Ding's quest
        if (gp.questProgress == 3 && this.questState == 3) {
            gp.questProgress = 4;
            System.out.println(" QUEST PROGRESS: 3 → 4 (Final quest stage - Ding)");
            
            // RESET KILL COUNT HERE - Before Ding's final quest (total monster count)
            if (gp.player != null) {
                gp.player.killCount = 0;
                System.out.println(" Kill count reset to 0 for Ding's final quest");
            }
        }
        
        // Update quest marker status based on tablet
        if (tabletSpawned) {
            this.questStatus = 1; // Keep marker until tablet picked up
        }
        
        // Remove marker if tablet was picked up
        if (tabletSpawned && gp.player != null && checkIfTabletPickedUp()) {
            this.questStatus = 0;
        }
    }
    
    private boolean checkIfTabletPickedUp() {
        int currentMap = gp.currentMap;
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
            if (gp.obj[currentMap][i] instanceof OBJ_tablet) {
                return false; // Tablet still exists
            }
        }
        return true; // Tablet picked up
    }
    
    private void spawnSlimes() {
        int currentMap = gp.currentMap;
        
        // Spawn 3 slimes around Vhong's location in the desert
        int[][] slimeLocations = {
            {32, 32},  // South of Vhong
            {30, 30},  // Southwest
            {34, 28}   // East
        };
        
        int slimesSpawnedCount = 0;
        for (int[] location : slimeLocations) {
            for (int i = 0; i < gp.monster[currentMap].length; i++) {
                if (gp.monster[currentMap][i] == null) {
                    MON_EarthSlime slime = new MON_EarthSlime(gp);
                    slime.worldX = gp.TileSize * location[0];
                    slime.worldY = gp.TileSize * location[1];
                    slime.setSpawnPoint(slime.worldX, slime.worldY);
                    gp.monster[currentMap][i] = slime;
                    slimesSpawnedCount++;
                    break;
                }
            }
        }
        
        if (slimesSpawnedCount > 0) {
            System.out.println(" Spawned " + slimesSpawnedCount + " slimes in the desert");
        }
    }
    
    public void spawnTablet() {
        if (tabletSpawned) return;
        
        int currentMap = gp.currentMap;
        
        // Spawn tablet next to Vhong (right side)
        int tabletX = 40; // Vhong is at 39, so 40 is right next to him
        int tabletY = 30;
        
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
            if (gp.obj[currentMap][i] == null) {
                gp.obj[currentMap][i] = new OBJ_tablet(gp);
                gp.obj[currentMap][i].worldX = gp.TileSize * tabletX;
                gp.obj[currentMap][i].worldY = gp.TileSize * tabletY;
                tabletSpawned = true;
                gp.ui.showMessage("An ancient tablet appears next to Vhong!");
                System.out.println(" Tablet spawned at: " + tabletX + ", " + tabletY);
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