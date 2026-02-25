package entity;

import java.util.Random;
import main.GamePanel;
import object.OBJ_Key;
import monster.MON_EarthSlime;

public class NPC_Beverly extends Entity {

    private int questState = 0;
    private boolean slimesSpawned = false;
    private boolean snakeQuestCompleted = false;

    public NPC_Beverly(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;
        type = type_npc;
        name = "Beverly";

        getImage();
        
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
            // First meeting - give quest for slimes
            dialoguePages = new String[] {
                "Hello, I'm Beverly!",
                "I need your help with some creatures.",
                "Three slimes have been causing trouble nearby.",
                "Defeat them and I'll reward you!"
            };
            if (gp.player != null) {
                gp.player.killCount = 0;
            }
            questState = 1;
        }
        else if (gp.questProgress == 1) {
            if (gp.player != null && gp.player.killCount < 3) {
                // Quest in progress - kill slimes
                int remaining = 3 - gp.player.killCount;
                dialoguePages = new String[] {
                    "You've killed " + gp.player.killCount + " out of 3 slimes.",
                    "You still need to defeat " + remaining + " more.",
                    "They're nearby, keep looking!",
                    "Come back when you're done!"
                };
            } else if (gp.player != null && gp.player.killCount >= 3) {
                // Quest complete - reward dialogue
                dialoguePages = new String[] {
                    "Amazing! You've defeated all the slimes!",
                    "You've proven yourself to be quite capable.",
                    "Take this key as your reward.",
                    "It might unlock something important!",
                    "Good luck on your continued journey!"
                };
                questState = 2;
            } else {
                // Fallback dialogue
                dialoguePages = new String[] {
                    "Come back when you've defeated the slimes!",
                    "I'll be waiting here."
                };
            }
        }
        else if (gp.questProgress == 2) {
            // Key reward given - second quest starts (snakes)
            dialoguePages = new String[] {
                "That key will open many doors.",
                "But I have another task for you...",
                "Dangerous snakes have been terrorizing the area.",
                "Defeat three of them and I'll have another reward!"
            };
            // Don't reset killCount here - let afterDialogue handle it
            questState = 2;
        }
        else if (gp.questProgress == 3) {
            // Snake quest in progress
            if (gp.player != null && gp.player.killCount < 3) {
                int remaining = 3 - gp.player.killCount;
                dialoguePages = new String[] {
                    "You've defeated " + gp.player.killCount + " out of 3 snakes.",
                    "You still need to defeat " + remaining + " more.",
                    "They're lurking in the eastern areas.",
                    "Return when you've completed the task!"
                };
            } else if (gp.player != null && gp.player.killCount >= 3) {
                // Snake quest complete - spawn key
                dialoguePages = new String[] {
                    "Outstanding! You've defeated all the snakes!",
                    "You're truly a hero!",
                    "Here's your final reward.",
                    "May it serve you well on your journey!"
                };
                // Spawn key immediately when killCount >= 3
                if (!snakeQuestCompleted) {
                    spawnKey(0, 7, 10); // Spawn key at map 0, col=7, row=10
                    snakeQuestCompleted = true;
                }
                questState = 3;
            } else {
                dialoguePages = new String[] {
                    "Come back when you've defeated the snakes!",
                    "The area is safer now because of you."
                };
            }
        }
        else if (gp.questProgress == 4) {
            // Post-quest dialogue after snake reward
            dialoguePages = new String[] {
                "You have proven yourself, adventurer!",
                "There's always more to discover.",
                "Farewell!"
            };
            questState = 3;
        }
        else if (gp.questProgress >= 5) {
            // Final state - "ding"
            dialoguePages = new String[] {
                "You have become a true legend!",
                "Your legend grows with each victory.",
                "May your journey be glorious!"
            };
            questState = 3;
        }
        else {
            // Default dialogue fallback
            dialoguePages = new String[] {
                "Keep exploring, adventurer!",
                "There's always more to discover.",
                "Farewell!"
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
            // Reset kill count for slime quest
            if (gp.player != null) {
                gp.player.killCount = 0;
            }
        }
        
        // Case 2: Slime quest completed (killCount >= 3) - move to snake quest (1 → 2)
        if (questState == 2 && gp.questProgress == 1 && gp.player != null && gp.player.killCount >= 3) {
            gp.questProgress = 2; // Move to reward state
            gp.playSE(1); // Play reward sound
            
            // Reset kill count for snake quest
            if (gp.player != null) {
                gp.player.killCount = 0;
            }
        }
        
        // Case 3: Snake quest completed (killCount >= 3) - move to final state (3 → 4)
        if (questState == 3 && gp.questProgress == 3 && gp.player != null && gp.player.killCount >= 3) {
            // Key already spawned in prepareDialoguePages, just advance quest
            gp.questProgress = 4; // Move to final reward state
            gp.playSE(1); // Play reward sound
            
            // Reset kill count
            if (gp.player != null) {
                gp.player.killCount = 0;
            }
        }
        
        // Case 4: Final completion "ding" (4 → 5)
        if (gp.questProgress == 4 && questState == 3) {
            gp.questProgress = 5;
        }
    }
    
    private void spawnSlimes() {
        int currentMap = gp.currentMap;
        
        // Spawn 3 slimes around Beverly's location
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
            gp.ui.showMessage("Slimes have appeared nearby!");
        }
    }
    
    public void spawnKey(int map, int col, int row) {
        int currentMap = gp.currentMap;
        
        // Spawn key at specified location
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
            if (gp.obj[currentMap][i] == null) {
                gp.obj[currentMap][i] = new OBJ_Key(gp);
                gp.obj[currentMap][i].worldX = gp.TileSize * col;
                gp.obj[currentMap][i].worldY = gp.TileSize * row;
                gp.ui.showMessage("A key has appeared!");
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