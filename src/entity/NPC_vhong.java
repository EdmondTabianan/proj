    package entity;

    import java.util.Random;

    import main.GamePanel;
    import object.OBJ_tablet;
    import monster.MON_EarthSlime;

    public class NPC_vhong extends Entity {

        int questState = 0;
        private String[] dialoguePages; // For multi-page dialogue
        private int currentPage = 0;
        private boolean slimesSpawned = false; // Track if slimes have been spawned

        public NPC_vhong(GamePanel gp) {
            super(gp);

            Direction = "down";
            speed = 1;

            getImage();
            
            // Initialize the inherited dialogues array
            dialogues = new String[10][10];
            
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
            
            // Spawn slimes immediately when first talking to the NPC (questProgress == 0)
            if (gp.questProgress == 0 && !slimesSpawned) {
                spawnSlimes();
                slimesSpawned = true;
                gp.questProgress = 1; // Set quest progress to 1 after spawning
                System.out.println("Quest progress set to 1 - Slimes spawned!");
            }
            
            // Start with first page
            if (dialoguePages != null && dialoguePages.length > 0) {
                currentPage = 0;
                gp.ui.setDialogue(dialoguePages); // Pass the entire array
            } else {
                // Fallback dialogue if something went wrong
                dialoguePages = new String[] {"..."};
                gp.ui.setDialogue(dialoguePages);
            }
            
            // Enter dialogue state
            gp.gameState = gp.dialogueState;
        }
        
        private void prepareDialoguePages() {
            if (gp.questProgress == 0) {
                // First meeting - multiple pages
                dialoguePages = new String[] {
                    "Ah, a brave adventurer!",
                    "I have a task for you.",
                    "Three slimes have been causing trouble in the forest.",
                    "They appeared at (32,32), (30,30), and (34,28).",
                    "Defeat them and return to me!"
                };
                if (gp.player != null) {
                    gp.player.killCount = 0; // Reset kill count for quest
                }
                questState = 1;
            }
            else if (gp.questProgress == 1) {
                if (gp.player != null && gp.player.killCount < 3) {
                    // Quest in progress - show progress
                    int remaining = 3 - gp.player.killCount;
                    dialoguePages = new String[] {
                        "You've killed " + gp.player.killCount + " out of 3 slimes.",
                        "You still need to defeat " + remaining + " more.",
                        "They're lurking in the forest nearby.",
                        "Come back when you're done!"
                    };
                } else if (gp.player != null && gp.player.killCount >= 3) {
                    // Quest complete - reward and unlock next area
                    dialoguePages = new String[] {
                        "Excellent work! You've defeated all the slimes!",
                        "As promised, here's your reward.",
                        "Take this clue tablet - it will guide you.",
                        "Also, I've unlocked the passage to the east.",
                        "You'll find dangerous snakes there, be careful!"
                    };
                    spawnTablet();
                    gp.questProgress = 2; // Set to 2 to unlock passage snakes
                    if (gp.player != null) {
                        gp.player.killCount = 0; // RESET killCount to 0 for the next quest
                    }
                    questState = 2;
                    System.out.println("Quest progress set to 2 - Passage snakes unlocked! KillCount reset to 0");
                } else {
                    // Fallback dialogue
                    dialoguePages = new String[] {
                        "Come back when you've defeated the slimes!",
                        "They're in the forest nearby."
                    };
                }
            }
            else if (gp.questProgress == 2) {
                // After receiving reward - hint about passage
                dialoguePages = new String[] {
                    "The passage to the east is now open.",
                    "Defeat the snakes there to prove your worth.",
                    "A merchant in that area may have useful items.",
                    "Good luck on your journey!"
                };
                questState = 3;
            }
            else if (gp.questProgress == 3) {
                // After passage quest (could be updated by Beverly)
                dialoguePages = new String[] {
                    "You've returned!",
                    "I heard you cleared the passage of snakes.",
                    "Beverly mentioned she had a reward for you.",
                    "You're becoming quite the hero!"
                };
            }
            else {
                // Default dialogue for any other state
                dialoguePages = new String[] {
                    "Keep exploring, adventurer!",
                    "There's always more to discover.",
                    "Farewell for now!"
                };
            }
            
            // Safety check - ensure dialoguePages is never null
            if (dialoguePages == null) {
                dialoguePages = new String[] {"..."};
            }
        }
        
        /**
         * Spawn 3 Earth Slimes at the specified locations immediately
         */
        private void spawnSlimes() {
            int currentMap = gp.currentMap;
            
            // Slime spawn locations: {x, y}
            int[][] slimeLocations = {
                {32, 32},
                {30, 30},
                {34, 28}
            };
            
            int slimeCount = 0;
            
            System.out.println("Attempting to spawn slimes immediately on map " + currentMap);
            
            // Check if monster array exists
            if (gp.monster == null) {
                System.out.println("ERROR: gp.monster is null!");
                return;
            }
            
            if (gp.monster[currentMap] == null) {
                System.out.println("ERROR: gp.monster[" + currentMap + "] is null!");
                return;
            }
            
            for (int[] location : slimeLocations) {
                boolean slotFound = false;
                // Find an empty slot in the monster array
                for (int i = 0; i < gp.monster[currentMap].length; i++) {
                    if (gp.monster[currentMap][i] == null) {
                        // Create and place the slime
                        MON_EarthSlime slime = new MON_EarthSlime(gp);
                        slime.worldX = gp.TileSize * location[0];
                        slime.worldY = gp.TileSize * location[1];
                        
                        // Set spawn point for respawning
                        slime.setSpawnPoint(slime.worldX, slime.worldY);
                        
                        gp.monster[currentMap][i] = slime;
                        slimeCount++;
                        slotFound = true;
                        System.out.println("✓ Spawned slime at slot " + i + " location (" + location[0] + ", " + location[1] + ")");
                        break;
                    }
                }
                if (!slotFound) {
                    System.out.println("✗ No empty slot found for location (" + location[0] + ", " + location[1] + ")");
                }
            }
            
            System.out.println("Spawned " + slimeCount + " slimes immediately for the quest!");
            
            if (slimeCount == 0) {
                System.out.println("⚠ WARNING: No slimes were spawned! Monster array might be full.");
                // Print current monster slots to debug
                System.out.println("Current monster slots on map " + currentMap + ":");
                for (int i = 0; i < gp.monster[currentMap].length; i++) {
                    if (gp.monster[currentMap][i] != null) {
                        System.out.println("  Slot " + i + ": " + gp.monster[currentMap][i].name);
                    }
                }
            }
        }
        
        /**
         * Call this method when player presses ENTER during dialogue
         * Handles advancing through pages and animation
         */
        public void nextDialogue() {
            // Safety check - if dialoguePages is null, prepare dialogue again
            if (dialoguePages == null) {
                System.out.println("WARNING: dialoguePages is null in nextDialogue() - preparing dialogue again");
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
                // Move to next page
                currentPage++;
                
                if (currentPage < dialoguePages.length) {
                    // Show next page - still in dialogue state
                    gp.ui.setDialogue(dialoguePages[currentPage]);
                    // Keep the game state as dialogueState
                    gp.gameState = gp.dialogueState;
                } else {
                    // No more pages, close dialogue
                    gp.gameState = gp.playState;
                    
                    // Optional: Trigger any post-dialogue actions
                    if (gp.questProgress == 2 && currentPage >= dialoguePages.length) {
                        // Quest just completed - you could add special effects here
                        gp.playSE(1); // Play a sound effect
                    }
                    
                    // Reset page counter for next time
                    currentPage = 0;
                }
            }
        }
        
        /**
         * Spawn the tablet beside Vhong at his location
         */
        public void spawnTablet() {
            int currentMap = gp.currentMap;
            
            // Vhong's location from AssetSetter: {0, 39, 30, NPC_vhong.class}
            // Spawn tablet slightly to the right of Vhong
            int tabletX = 40; // One tile to the right (39 + 1)
            int tabletY = 30; // Same Y coordinate
            
            for (int i = 0; i < gp.obj[currentMap].length; i++) {
                if (gp.obj[currentMap][i] == null) {
                    gp.obj[currentMap][i] = new OBJ_tablet(gp);
                    gp.obj[currentMap][i].worldX = gp.TileSize * tabletX;
                    gp.obj[currentMap][i].worldY = gp.TileSize * tabletY;
                    System.out.println("Tablet spawned beside Vhong at (" + tabletX + ", " + tabletY + ")");
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