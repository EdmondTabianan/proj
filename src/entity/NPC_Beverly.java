package entity;

import java.util.Random;
import main.GamePanel;
import object.OBJ_Key;
import monster.MON_Snake; // Make sure you have a Snake monster class

public class NPC_Beverly extends Entity {

    // REMOVED: private int questState = 0; - Now inherited from Entity!
    private boolean snakesSpawned = false;
    private boolean keySpawned = false;
    
    // Using inherited variables:
    // this.questState - 0=inactive, 1=quest active, 2=quest complete, 3=post-quest
    // this.questStatus - 0=no marker, 1=show quest marker (!)

    public NPC_Beverly(GamePanel gp) {
        super(gp);

        Direction = "down";
        speed = 1;
        type = type_npc;
        name = "Beverly";
        
        // Initialize inherited quest variables
        this.questState = 0;
        this.questStatus = 0;

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
        // Beverly should only be available after slime quest (progress >= 2)
        
        // Check if snake quest is ready to complete (killCount >= 3)
        if (gp.questProgress == 3 && gp.player != null && gp.player.killCount >= 3 && !keySpawned) {
            // Snake quest complete - reward dialogue
            dialoguePages = new String[] {
                "Outstanding! You've defeated all the snakes!",
                "You're truly a hero!",
                "I've hidden a special SNAKE KEY somewhere in the area.",
                "Find it - it will allow you to sail to new lands.",
                "But the real challenge lies beyond...",
                "Seek the pyramid key for the lost tomb!",
                "May it serve you well on your journey!"
            };
            this.questState = 3;      // Complete state
            this.questStatus = 0;      // Remove marker - quest done
            
            // Spawn key immediately when dialogue is prepared
            if (!keySpawned) {
                spawnKey(0, 7, 10); // Spawn snake key at map 0, col=7, row=10
                keySpawned = true;
            }
            return;
        }
        
        // Regular dialogue based on GLOBAL questProgress
        switch (gp.questProgress) {
            case 0:
            case 1:
                // Not available yet - waiting for player to finish slime quest
                dialoguePages = new String[] {
                    "I'm not ready to talk yet.",
                    "Go help Vhong with his slime problem first.",
                    "Come back after you've proven yourself!"
                };
                this.questState = 0;
                this.questStatus = 0;
                break;
                
            case 2:
                // First meeting - give snake quest (after slimes done)
                dialoguePages = new String[] {
                    "Ah, Vhong sent you! I'm Beverly.",
                    "Now that you've handled those slimes,",
                    "I have a more dangerous task for you.",
                    "Three venomous snakes are terrorizing this area.",
                    "Defeat them and I'll reward you with the SNAKE KEY!",
                    "This key will allow you to sail across the sea.",
                    "But there's more... the pyramid's lost tomb awaits!"
                };
                // RESET KILL COUNT HERE - Start of snake quest
                if (gp.player != null) {
                    gp.player.killCount = 0;
                    System.out.println(" Kill count reset to 0 for snake quest");
                }
                this.questState = 1;      // Quest active
                this.questStatus = 1;      // Show marker
                break;
                
            case 3:
                // Snake quest in progress
                if (gp.player != null) {
                    if (gp.player.killCount == 0) {
                        dialoguePages = new String[] {
                            "You haven't killed any snakes yet!",
                            "They're in the eastern desert areas.",
                            "Be careful - they're much stronger than slimes!",
                            "Defeat all three for the SNAKE KEY.",
                            "Then seek the pyramid key for the lost tomb!"
                        };
                    } else if (gp.player.killCount < 3) {
                        int remaining = 3 - gp.player.killCount;
                        dialoguePages = new String[] {
                            "You've defeated " + gp.player.killCount + " out of 3 snakes.",
                            "You still need to defeat " + remaining + " more.",
                            "Keep hunting! The KEY will be yours!",
                            "Remember - this is just the beginning.",
                            "The pyramid's lost tomb awaits the worthy!"
                        };
                    }
                } else {
                    dialoguePages = new String[] {
                        "Defeat the three snakes in the eastern desert!",
                        "The KEY is your reward."
                    };
                }
                this.questState = 1;      // Still active
                this.questStatus = 1;      // Keep marker
                break;
                
            case 4:
                // Snake quest complete - key should be found
                if (gp.player != null && gp.player.hasKey == 1) {
                    dialoguePages = new String[] {
                        "You found the KEY! That's excellent!",
                        "Now you can sail to new lands.",
                        "But the real treasure lies in the pyramid.",
                        "Find the PYRAMID KEY to unlock the lost tomb!",
                        "The sailor to the west will take you across the sea.",
                        "Good luck, hero!"
                    };
                } else {
                    dialoguePages = new String[] {
                        "The KEY is hidden somewhere in this area.",
                        "Find it - it will allow you to sail.",
                        "Then seek the pyramid key for the lost tomb!"
                    };
                }
                this.questState = 2;      // Complete
                this.questStatus = 0;      // No marker
                break;
                
            case 5:
                // Final state - game complete (found lost tomb)
                dialoguePages = new String[] {
                    "You found the lost tomb! I knew you could do it!",
                    "From slime hunter to tomb explorer...",
                    "You've become a true legend!",
                    "The ancient treasure is yours!",
                    "Farewell, hero!"
                };
                this.questState = 3;      // Post-quest
                this.questStatus = 0;      // No marker
                break;
                
            default:
                dialoguePages = new String[] {
                    "Keep exploring, adventurer!",
                    "The pyramid's lost tomb awaits!",
                    "Farewell!"
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
        
        // Only spawn snakes on first meeting at progress 2
        boolean firstSnakeMeeting = (gp.questProgress == 2 && !snakesSpawned);
        
        if (firstSnakeMeeting) {
            spawnSnakes();
            snakesSpawned = true;
            gp.ui.showMessage("Beverly: Defeat 3 snakes in the  passage!");
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
        
        // Case 1: First meeting with Beverly (progress 2 → 3)
        if (gp.questProgress == 2 && snakesSpawned) {
            gp.questProgress = 3;
            System.out.println(" QUEST PROGRESS: 2 → 3 (Snake quest active)");
            
            // Show hint about pyramid
            gp.ui.showMessage("Hint: The pyramid's lost tomb awaits after the snakes!");
            
            // RESET KILL COUNT HERE - Just to be sure it's clean
            if (gp.player != null) {
                gp.player.killCount = 0;
                System.out.println(" Kill count reset to 0 for snake quest (verified)");
            }
        }
        
        // Case 2: Snake quest completed (killCount >= 3) - key already spawned in prepareDialoguePages
        if (gp.questProgress == 3 && gp.player != null && gp.player.killCount >= 3 && keySpawned) {
            // Don't advance progress yet - player needs to FIND the key first
            
            // Note: killCount NOT reset here - player needs to find key first
        }
        
        // Case 3: Key picked up - advance to progress 4
        if (gp.questProgress == 3 && keySpawned && gp.player != null && gp.player.hasKey == 1) {
            gp.questProgress = 4;
            gp.playSE(1); // Reward sound
            System.out.println(" QUEST PROGRESS: 3 → 4 (Snake key obtained)");
            gp.ui.showMessage("You obtained the Key! Now seek the Pyramid Key!");
            
            // RESET KILL COUNT HERE - After snake quest complete, before pyramid quest
            if (gp.player != null) {
                gp.player.killCount = 0;
                System.out.println(" Kill count reset to 0 for pyramid key hunt");
            }
        }
        
        // Case 4: Progress 4 with key - ready for pyramid
        if (gp.questProgress == 4 && gp.player != null && gp.player.hasKey == 1) {
            // Just update marker status
            this.questStatus = 0;
        }
        
        // Update quest marker status
        if (gp.questProgress == 2 || gp.questProgress == 3) {
            this.questStatus = 1; // Show marker when quest is available/active
        } else {
            this.questStatus = 0; // Hide marker otherwise
        }
    }
    
    private void spawnSnakes() {
        int currentMap = gp.currentMap;
        
        // Spawn 3 snakes in the eastern desert
        int[][] snakeLocations = {
            {45, 20},  // East area 1
            {48, 25},  // East area 2
            {42, 30}   // East area 3
        };
        
        int snakesSpawnedCount = 0;
        for (int[] location : snakeLocations) {
            for (int i = 0; i < gp.monster[currentMap].length; i++) {
                if (gp.monster[currentMap][i] == null) {
                    MON_Snake snake = new MON_Snake(gp); // You'll need this class
                    snake.worldX = gp.TileSize * location[0];
                    snake.worldY = gp.TileSize * location[1];
                    snake.setSpawnPoint(snake.worldX, snake.worldY);
                    gp.monster[currentMap][i] = snake;
                    snakesSpawnedCount++;
                    break;
                }
            }
        }
        
        if (snakesSpawnedCount > 0) {
            System.out.println(" Spawned " + snakesSpawnedCount + " snakes in the eastern desert");
        }
    }
    
    public void spawnKey(int map, int col, int row) {
        if (keySpawned) return; // Prevent multiple spawns
        
        int currentMap = gp.currentMap;
        
        // Spawn key at specified location
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
            if (gp.obj[currentMap][i] == null) {
                gp.obj[currentMap][i] = new OBJ_Key(gp);
                gp.obj[currentMap][i].worldX = gp.TileSize * col;
                gp.obj[currentMap][i].worldY = gp.TileSize * row;
                keySpawned = true;
                gp.ui.showMessage("A Snake Key has appeared!");
                System.out.println(" Snake Key spawned at: " + col + ", " + row);
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