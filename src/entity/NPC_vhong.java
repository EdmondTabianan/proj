package entity;

import java.util.Random;
import main.GamePanel;
import object.OBJ_tablet;
import monster.MON_EarthSlime;

public class NPC_vhong extends Entity {

    private int questState = 0;
    private boolean slimesSpawned = false;
    private boolean tabletSpawned = false;

    public NPC_vhong(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;
        type = type_npc;
        name = "Vhong";

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
        if (gp.questProgress == 0) {
            // First meeting - give quest
            dialoguePages = new String[] {
                "Ah, a brave hunter!",
                "I have a task for you.",
                "Three slimes have been causing trouble in the desert.",
                "Defeat them and return to me!"
            };
            if (gp.player != null) {
                gp.player.killCount = 0;
            }
            questState = 1;
        }
        else if (gp.questProgress == 1) {
            if (gp.player != null && gp.player.killCount < 3) {
                // Quest in progress
                int remaining = 3 - gp.player.killCount;
                dialoguePages = new String[] {
                    "You've killed " + gp.player.killCount + " out of 3 slimes.",
                    "You still need to defeat " + remaining + " more.",
                    "They're lurking in the desert nearby.",
                    "Come back when you're done!"
                };
            } else if (gp.player != null && gp.player.killCount >= 3) {
                // Quest complete - reward dialogue
                dialoguePages = new String[] {
                    "Excellent work! You've defeated all the slimes!",
                    "As promised, here's your reward.",
                    "Take this ancient tablet - it will guide you.",
                    "Also, I've unlocked the passage to the east.",
                    "You'll find dangerous snakes there, be careful!"
                };
                questState = 2;
                killCount = 0;
            } else {
                // Fallback dialogue
                dialoguePages = new String[] {
                    "Come back when you've defeated the slimes!",
                    "They're in the desert nearby."
                };
            }
        }
        else if (gp.questProgress == 2) {
            // Tablet reward given - quest moving to completion
            dialoguePages = new String[] {
                "The passage to the east is now open.",
                "Defeat the snakes there to prove your worth.",
                "A merchant in that area may have useful items.",
                "Good luck on your journey!"
            };
            questState = 2;
        }
        else if (gp.questProgress == 3) {
            // Post-quest dialogue
            dialoguePages = new String[] {
                "Keep exploring, hunter!",
                "There's always more to discover.",
                "Farewell for now!"
            };
            questState = 3;
        }
        else if (gp.questProgress >= 4) {
            // Final state - "ding"
            dialoguePages = new String[] {
                "You have become a true legend!",
                "Your deeds shall be remembered.",
                "The world is safer because of you!"
            };
            questState = 3;
        }
        else {
            // Default dialogue fallback
            dialoguePages = new String[] {
                "Keep exploring, hunter!",
                "There's always more to discover.",
                "Farewell for now!"
            };
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
        // Update quest progress AFTER dialogue finishes
        
        // Case 1: First meeting completed - move to quest active (0 → 1)
        if (gp.questProgress == 0 && slimesSpawned) {
            gp.questProgress = 1;
        }
        
        // Case 2: Quest completed (killCount >= 3) - give tablet reward (1 → 2)
        if (questState == 2 && gp.questProgress == 1 && gp.player != null && gp.player.killCount >= 3) {
            spawnTablet();
            gp.questProgress = 2; // Move to reward state
            gp.playSE(1); // Play reward sound
            
            // Reset kill count for next quest
            if (gp.player != null) {
                gp.player.killCount = 0;
            }
        }
        
        // Case 3: Move to post-quest dialogue (2 → 3)
        if (gp.questProgress == 2 && questState == 2) {
            gp.questProgress = 3;
        }
        
        // Case 4: Final completion "ding" (3 → 4)
        if (gp.questProgress == 3 && questState == 3) {
            gp.questProgress = 4;
        }
    }
    
    private void spawnSlimes() {
        int currentMap = gp.currentMap;
        
        // Spawn 3 slimes around Vhong's location
        int[][] slimeLocations = {
            {32, 32},
            {30, 30},
            {34, 28}
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
            gp.ui.showMessage("Slimes have appeared in the desert!");
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
                gp.ui.showMessage("A tablet has appeared next to Vhong!");
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