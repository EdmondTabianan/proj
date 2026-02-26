package entity;

import main.GamePanel;
import object.OBJ_Key;

public class NPC_Ding extends Entity {

    // REMOVED: private int questState = 0; - Now inherited from Entity!
    private int requiredTotalKills = 6; // Need to kill 6 total monsters (slimes + snakes)
    private boolean keySpawned = false;
    
    // NOTE: Using inherited variables:
    // this.questState - 0=inactive, 1=quest active, 2=quest complete, 3=post-quest
    // this.questStatus - 0=no marker, 1=show quest marker (!)
    // this.questProgress - (unused, but available)

    public NPC_Ding(GamePanel gp) {
        super(gp);
        
        name = "Ding";
        type = type_npc;
        Direction = "down";
        speed = 1;
        
        // Initialize inherited quest variables
        this.questState = 0;      // Start inactive
        this.questStatus = 0;      // No marker initially (only appears at progress 3)
        this.questProgress = 0;    // Reserved for future use
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 38; 
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        down1 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize);
        down2 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize);
        up1 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize);   // Add these for
        up2 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize);
        left1 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize); // better visuals
        left2 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize);
        right1 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize);
        right2 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize);
    }

    @Override
    public void prepareDialoguePages() {
        // Ding should only be available after snake quest (progress >= 3)
        
        // Check if quest is ready to complete (killCount >= 6)
        if (gp.questProgress == 3 && gp.player != null && gp.player.killCount >= requiredTotalKills) {
            // Quest complete - reward dialogue
            dialoguePages = new String[] {
                "Incredible! You've defeated all " + requiredTotalKills + " monsters!",
                "You have proven yourself a true hero!",
                "I've hidden a special key for you.",
                "It's somewhere to the northwest of this ruined place.",
                "Go find it and unlock your destiny!",
                "The ancient door awaits!"
            };
            this.questState = 2;      // Complete state
            this.questStatus = 0;      // Remove marker - quest done
            return;
        }
        
        // Regular dialogue based on GLOBAL questProgress and current state
        if (gp.questProgress < 3) {
            // Not available yet
            dialoguePages = new String[] {
                "...",
                "The warrior meditates in silence.",
                "He doesn't seem ready to talk yet."
            };
            this.questState = 0;
            this.questStatus = 0;
        }
        else if (gp.questProgress == 3) {
            if (this.questState == 0) {
                // First meeting after previous quests
                dialoguePages = new String[] {
                    "Ah, you must be the one Vhong and Beverly mentioned.",
                    "I am Ding, the final challenge.",
                    "You've proven yourself against slimes and snakes.",
                    "Now I have a final test for you.",
                    "Defeat 6 monsters in total to prove your worth.",
                    "You've already killed " + getPlayerKills() + ". Keep going!"
                };
                this.questState = 1;      // Quest active
                this.questStatus = 1;      // Show marker
            }
            else if (this.questState == 1) {
                int totalKills = getPlayerKills();
                int remaining = requiredTotalKills - totalKills;
                
                if (totalKills < requiredTotalKills) {
                    dialoguePages = new String[] {
                        "You've killed " + totalKills + " out of " + requiredTotalKills + " monsters.",
                        "You still need to defeat " + remaining + " more.",
                        "Keep hunting! You're almost there!"
                    };
                } else {
                    // This case should be caught by the top check, but just in case
                    dialoguePages = new String[] {
                        "You've done it! The key awaits you northwest!",
                        "Go find it and claim your reward!"
                    };
                }
                this.questStatus = 1;      // Keep marker while active
            }
            else if (this.questState == 2) {
                dialoguePages = new String[] {
                    "You are a legend among hunters!",
                    "The ancient door awaits you.",
                    "Remember: the key is northwest of here!",
                    "Go forth and claim your reward!"
                };
                this.questStatus = 0;      // No marker
            }
        }
        else if (gp.questProgress == 4) {
            // Between snake quest complete and final key find
            dialoguePages = new String[] {
                "Have you found the key yet?",
                "It's hidden northwest of here.",
                "Search carefully - it will unlock your destiny!"
            };
            this.questState = 2;
            this.questStatus = 1;      // Keep marker until key found
        }
        else if (gp.questProgress >= 5) {
            // Final state - game complete
            dialoguePages = new String[] {
                "You have become a true legend!",
                "The ancient door is open because of you!",
                "Your name will be remembered for generations!",
                "Farewell, champion!"
            };
            this.questState = 3;      // Post-quest
            this.questStatus = 0;      // No marker
        }
        
        // Safety check
        if (dialoguePages == null) {
            dialoguePages = new String[] {"..."};
        }
    }
    
    private int getPlayerKills() {
        return (gp.player != null) ? gp.player.killCount : 0;
    }
    
    @Override
    public void speak() {
        facePlayer();
        findMyIndex();
        prepareDialoguePages();
        
        if (dialoguePages != null && dialoguePages.length > 0) {
            gp.ui.setDialogue(dialoguePages);
        }
        
        gp.gameState = gp.dialogueState;
    }
    
    @Override
    public void afterDialogue() {
        // Update GLOBAL quest progress AFTER dialogue finishes
        
        // Case 1: First meeting with Ding (progress stays 3, but questState updates)
        if (gp.questProgress == 3 && this.questState == 0) {
            // First meeting happened, state updated in prepareDialoguePages
            System.out.println(" DING: First meeting - final quest active");
        }
        
        // Case 2: Quest completed (killCount >= 6) - spawn key and advance progress
        if (gp.questProgress == 3 && gp.player != null && gp.player.killCount >= requiredTotalKills) {
            if (!keySpawned) {
                spawnKey(10, 8); // Spawn key at map 1, col=10, row=8
                keySpawned = true;
                gp.playSE(1); // Play reward sound
                gp.ui.showMessage("Ding has hidden a key northwest of here!");
                System.out.println(" DING: Key spawned at map 1, (10,8)");
            }
            
            // Don't advance progress yet - player needs to FIND the key
            // Progress will advance to 5 when key is picked up
        }
        
        // Case 3: Key picked up - advance to final progress (4 → 5)
        if (gp.questProgress == 3 && keySpawned && gp.player != null && gp.player.hasKey >= 1) {
            gp.questProgress = 5; // Jump to final (skip 4 if needed)
            System.out.println(" QUEST PROGRESS: 3 → 5 (All quests complete!)");
            gp.ui.showMessage("ALL QUESTS COMPLETE! You are a legend!");
        }
        
        // Alternative path if you want a progress 4 state:
        if (gp.questProgress == 3 && keySpawned && gp.player != null && gp.player.hasKey == 0) {
            // Key spawned but not picked up yet - move to progress 4
            gp.questProgress = 4;
            System.out.println(" QUEST PROGRESS: 3 → 4 (Key spawned, waiting for pickup)");
        }
        else if (gp.questProgress == 4 && gp.player != null && gp.player.hasKey >= 1) {
            // Key picked up during progress 4 - move to 5
            gp.questProgress = 5;
            System.out.println(" QUEST PROGRESS: 4 → 5 (All quests complete!)");
        }
        
        // Update quest marker status
        if (gp.questProgress == 3 && this.questState == 1) {
            this.questStatus = 1; // Show marker when quest is active
        } else if (gp.questProgress == 4 && !keySpawned) {
            this.questStatus = 1; // Show marker when waiting for key spawn
        } else {
            this.questStatus = 0; // Hide marker otherwise
        }
    }
    
    public void spawnKey(int col, int row) {
        if (keySpawned) return; // Prevent double spawning
        
        int currentMap = gp.currentMap;
        
        
        // For now, spawn on current map
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
            if (gp.obj[currentMap][i] == null) {
                gp.obj[currentMap][i] = new OBJ_Key(gp);
                gp.obj[currentMap][i].worldX = gp.TileSize * col;
                gp.obj[currentMap][i].worldY = gp.TileSize * row;
                keySpawned = true;
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