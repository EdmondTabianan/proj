package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import data.SaveLoad;
import entity.NPC_vhong; // Add this import

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed, arrowKeyPressed, questkeyPressed, spacePressed;
    boolean showDebugText = false;
    //hold archer
    //boolean arrowpressed = false;
    Sound music = new Sound();
    Sound se = new Sound();


    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //Title State 
        if (gp.gameState == gp.titleState){
            titleState(code);
        }
        //Play State
        else if (gp.gameState == gp.playState) {
            playState(code);
        }
        // pause state
        else if (gp.gameState == gp.pauseState) {
            pauseState(code);
        }
        // Dialgue state
        else if (gp.gameState == gp.dialogueState || gp.gameState == gp.cutsceneState) {
            dialogueState(code);
        }
        // character State
        else if (gp.gameState == gp.characterState) {
            characterState(code);
        }
        //options state
        else if (gp.gameState == gp.optionsState) {
            optionsState(code);
        }
        // game over state 
        else if (gp.gameState == gp.gameOverState) {
            gameOverState(code);
        }
        // trade state 
        else if (gp.gameState == gp.tradeState) {
            tradeState(code);
        }
        // transition state - allow escape to cancel? (optional)
        else if (gp.gameState == gp.transitionState) {
            // Optionally allow cancel during transition
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.titleState;
                gp.loadingManager.isLoading = false;
            }
        }
    }   

    public void titleState(int code) {
        if(gp.ui.titleScreenState == 0) {
            // Main menu
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
                gp.playSE(9);
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
                gp.playSE(9);
            }
            if (code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0) {
                    // New Game
                    gp.ui.titleScreenState = 1;
                    gp.ui.commandNum = 0; // Reset commandNum for character selection
                    // gp.aSetter.resetAllPickedUpItems();
                }
                if(gp.ui.commandNum == 1) {
                    // Load Game - go to load selection screen
                    gp.ui.titleScreenState = 2;
                    gp.ui.commandNum = 0; // Reset commandNum for load screen
                    // gp.aSetter.resetAllPickedUpItems();
                }
                if (gp.ui.commandNum == 2) {
                    // Quit
                    System.exit(0);
                }
                enterPressed = false;
            }
        }
        else if(gp.ui.titleScreenState == 1) {
            // Character selection screen (New Game)
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
                gp.playSE(9);
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
                gp.playSE(9);
            }
            if (code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0) {
                    // Xylo selected
                    gp.loadingManager.startGameWithCharacter(0);
                    gp.gameState = gp.transitionState;
                    gp.playMusic(0);
                }
                if(gp.ui.commandNum == 1) {
                    // Alexandria selected
                    gp.loadingManager.startGameWithCharacter(1);
                    gp.gameState = gp.transitionState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 2) {
                    // Back to main menu
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0; // Reset commandNum for main menu
                }
                enterPressed = false;
            }
        }
        else if(gp.ui.titleScreenState == 2) {
            // Load game selection screen
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 3; // 0-3 (Load 1, Load 2, Load 3, Back)
                }
                gp.playSE(9);
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 3) {
                    gp.ui.commandNum = 0;
                }
                gp.playSE(9);
            }
            if (code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0) {
                    // Load Slot 1
                    loadGameFromSlot(0);
                }
                else if(gp.ui.commandNum == 1) {
                    // Load Slot 2
                    loadGameFromSlot(1);
                }
                else if(gp.ui.commandNum == 2) {
                    // Load Slot 3
                    loadGameFromSlot(2);
                }
                else if(gp.ui.commandNum == 3) {
                    // Back to main menu
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0; 
                }
                enterPressed = false;
            }
            
            // Allow ESC to go back
            if (code == KeyEvent.VK_ESCAPE) {
                gp.ui.titleScreenState = 0;
                gp.ui.commandNum = 1; // Set cursor to "Load Game" in main menu
                enterPressed = false;
            }
        }
    }

    private void loadGameFromSlot(int slot) {
        String filename = "save_slot_" + (slot + 1) + ".dat";
        File saveFile = new File(filename);
        
        if (!saveFile.exists()) {
            gp.ui.showMessage("No save file in Slot " + (slot + 1));
            return;
        }
        
        try {
            // STEP 1: CRITICAL - Initialize GamePanel components if needed
            if (gp.cChecker == null) {
                gp.cChecker = new CollisionChecker(gp);
            }
            
            if (gp.aSetter == null) {
                gp.aSetter = new AssetSetter(gp);
            }
            
            if (gp.ui == null) {
                gp.ui = new UI(gp);
            }
            
            if (gp.eHandler == null) {
                gp.eHandler = new eventHandler(gp);
            }
            
            // STEP 2: Create player if it doesn't exist
            if (gp.player == null) {
                // CRITICAL: Pass 'this' (KeyHandler) to the player constructor
                gp.player = new entity.Player(gp, this, 0);
                
                // Initialize equipment
                gp.player.currentweapon = new object.OBJ_Sword_Normal(gp);
                gp.player.currentShield = new object.OBJ_Shield_Wood(gp);
                gp.player.currentRange = new object.OBJ_ice_wand(gp);
                gp.player.projectiles = new object.OBJ_ice(gp);
                
                // Initialize inventory
                gp.player.inventory = new ArrayList<>();
                gp.player.inventory.add(gp.player.currentweapon);
                gp.player.inventory.add(gp.player.currentShield);
                gp.player.inventory.add(gp.player.currentRange);
                
                // Load images
                gp.player.getImage();
                gp.player.getAttackImage();
                gp.player.getGuardImage();
                
            } else {
                // Ensure existing player has KeyHandler reference
                gp.player.keyH = this;
            }
            
            // STEP 3: Load the save data
            SaveLoad saveLoad = new SaveLoad(gp);
            saveLoad.load(slot);
            
            // STEP 4: Set to start position if desired
            // gp.player.startPosition();
            
            // STEP 5: Recalculate stats
            gp.player.attack = gp.player.getAttack();
            gp.player.defense = gp.player.getDefense();
            
            // STEP 6: Ensure KeyHandler is set (again, just to be safe)
            gp.player.keyH = this;
            
            // STEP 7: CRITICAL - Reset ALL player states
            gp.player.collisionOn = false;
            gp.player.attacking = false;
            gp.player.guarding = false;
            gp.player.knockBack = false;
            gp.player.invincible = false;
            gp.player.transparent = false;
            gp.player.spriteCounter = 0;
            gp.player.spriteNum = 1;
            gp.player.standCounter = 0;
            gp.player.Direction = "down";
            gp.player.alive = true;
            
            // STEP 8: Reset movement flags in KeyHandler
            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;
            enterPressed = false;
            spacePressed = false;
            shotKeyPressed = false;
            
            // STEP 9: Reset assets for the loaded map
            gp.aSetter.clearMapAssets(gp.currentMap);
            gp.aSetter.setObject(gp.currentMap);
            gp.aSetter.setNPC(gp.currentMap);
            gp.aSetter.setMonster(gp.currentMap);
            gp.aSetter.setInteractiveTile(gp.currentMap);
            
            // STEP 10: Refresh monster images
            for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
                if (gp.monster[gp.currentMap][i] != null) {
                    gp.monster[gp.currentMap][i].getImage();
                }
            }
            
            // STEP 11: Force a collision check
            gp.cChecker.checkTile(gp.player);
            
            // STEP 12: Start the game
            gp.gameState = gp.playState;
            gp.ui.titleScreenState = 0;
            gp.ui.commandNum = 0;
                        
            // Play background music
            gp.playMusic(0);
            
        } catch (Exception e) {
            e.printStackTrace();
            gp.ui.showMessage("LOAD FAILED!");
        }
    }

    public void playState(int code) {
        if (code == KeyEvent.VK_Q) {
            gp.gameState = gp.questState;
            questkeyPressed = true;
        }
    
        if (code == KeyEvent.VK_W) {
            upPressed = true;
         }
         if (code == KeyEvent.VK_S) {
            downPressed = true;
         }
         if (code == KeyEvent.VK_A) {
             leftPressed = true;
         }
         if (code == KeyEvent.VK_D) {
             rightPressed = true;
         }
         if (code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
         }
         if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
         }
         if (code == KeyEvent.VK_C){
            gp.gameState = gp.characterState;
            gp.ui.commandNum = 0; // Reset commandNum when entering character screen
         }
         if (code == KeyEvent.VK_F) {
            shotKeyPressed = true;
         }
         if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionsState;
            gp.ui.subState = 0; // Reset to main options
            gp.ui.commandNum = 0; // Reset commandNum when opening options
         }
         if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
         }
    
         // Debug
         if (code == KeyEvent.VK_T) {
             if (showDebugText == false) {
                 showDebugText = true;
             } else if (showDebugText == true) {
                 showDebugText = false;
             }
         }
         if (code == KeyEvent.VK_R) {
            switch (gp.currentMap) {
                case 0: gp.tileM.loadMap("/map/main.txt", 0);break;
                case 1: gp.tileM.loadMap("/map/secondmap.txt", 1);break;
                case 2: gp.tileM.loadMap("/map/first_floor.txt", 2);break;
                case 3: gp.tileM.loadMap("/map/shop.txt", 3);break;
            }
            gp.aSetter.setInteractiveTile(gp.currentMap);
         } 
    }
    
    public void pauseState(int code) {
        if (code == KeyEvent.VK_P) {
            gp.stopMusic();
            gp.gameState = gp.playState;
        }
    }
    
    // public void dialogueState(int code) {

    //     if (code == KeyEvent.VK_ENTER) {
    
    //         // ================= SPECIAL NPC (VHONG) =================
    //         if (gp.npc[gp.currentMap][gp.ui.npcIndex] instanceof NPC_vhong) {
    
    //             NPC_vhong npc =
    //                 (NPC_vhong) gp.npc[gp.currentMap][gp.ui.npcIndex];
    
    //             npc.nextDialogue();
    //             return;
    //         }
    
    //         // ================= NORMAL DIALOGUE =================
    
    //         // If typewriter still animating → finish instantly
    //         if (!gp.ui.isDialogueFinished()) {
    //             gp.ui.skipToEnd();
    //             return;
    //         }
    
    //         // If more pages exist → go next
    //         if (gp.ui.hasNextPage()) {
    //             gp.ui.nextPage();
    //             return;
    //         }
    
    //         // ================= DIALOGUE FINISHED =================
    //         // 🔥 THIS IS THE IMPORTANT PART
    
    //         if (gp.gameState == gp.cutsceneState) {
    //             // Continue the cutscene instead of exiting to play
    //             gp.csManager.scenePhase++;
    //         } else {
    //             // Normal gameplay dialogue closes normally
    //             gp.gameState = gp.playState;
    //         }
    //     }
    // }
    
    public void dialogueState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            
            // ================= SPECIAL NPC (VHONG) =================
            if (gp.npc != null && gp.npc[gp.currentMap] != null && 
                gp.ui.npcIndex >= 0 && gp.ui.npcIndex < gp.npc[gp.currentMap].length &&
                gp.npc[gp.currentMap][gp.ui.npcIndex] instanceof NPC_vhong) {
                
                NPC_vhong npc = (NPC_vhong) gp.npc[gp.currentMap][gp.ui.npcIndex];
                npc.nextDialogue();
                return;
            }
            
            // ================= CUTSCENE MONSTER DIALOGUE =================
            if (gp.gameState == gp.cutsceneState) {
                // For cutscene dialogue, just use the normal page handling
                if (!gp.ui.isDialogueFinished()) {
                    gp.ui.skipToEnd();
                    return;
                }
                
                if (gp.ui.hasNextPage()) {
                    System.out.println("Moving to next page");
                    gp.ui.nextPage();
                    return;
                }
                
                // No more pages, advance cutscene
                System.out.println("All pages done, advancing cutscene");
                gp.csManager.scenePhase++;
                return;
            }
            
            // ================= NORMAL DIALOGUE =================
            
            // If typewriter still animating → finish instantly
            if (!gp.ui.isDialogueFinished()) {
                gp.ui.skipToEnd();
                return;
            }
            
            // If more pages exist → go next
            if (gp.ui.hasNextPage()) {
                System.out.println("Moving to next page");
                gp.ui.nextPage();
                return;
            }
            
            // ================= DIALOGUE FINISHED =================
            System.out.println("All pages done, closing dialogue");
            gp.gameState = gp.playState;
        }
    }

    public void characterState(int code) {
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
            playerInventory(code);
        }
        if (code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
        playerInventory(code);
    }
    
    public void optionsState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.ui.subState == 0) {
                gp.gameState = gp.playState;
                gp.ui.commandNum = 0; // Reset commandNum when leaving options
            } else {
                gp.ui.subState = 0; // Go back to main options
                gp.ui.commandNum = 0; // Reset commandNum when going back to main options
            }
            enterPressed = false;
        }
        
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        
        // Handle the Quit option in main options (subState 0, commandNum 5)
        if (gp.ui.subState == 0 && gp.ui.commandNum == 5 && code == KeyEvent.VK_ENTER) {
            gp.ui.subState = 3; // Go to quit confirmation
            gp.ui.commandNum = 0; // Reset commandNum for quit confirmation
            enterPressed = false;
            return;
        }
        
        // Handle quit confirmation (subState 3)
        if (gp.ui.subState == 3) {
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) { // Yes
                    gp.gameState = gp.titleState;
                    gp.ui.titleScreenState = 0; // Main title screen
                    gp.ui.commandNum = 0; // Set to "New Game" (0)
                    gp.ui.subState = 0; // Reset substate
                    gp.aSetter.resetAllPickedUpItems();
                    gp.stopMusic();
                    enterPressed = false;
                    return;
                } else if (gp.ui.commandNum == 1) { // No
                    gp.ui.subState = 0; // Back to main options
                    gp.ui.commandNum = 0; // Reset to first option
                    enterPressed = false;
                    return;
                }
            }
        }
        
        // Set max command number based on substate
        int maxCommandNum = 0;
        switch (gp.ui.subState) {
            case 0: // Main options - 6 items (0-5)
                maxCommandNum = 5; 
                break;
            case 1: // Save menu - 4 items (0-3)
                maxCommandNum = 3;
                break;
            case 2: // Controls - 1 item (0)
                maxCommandNum = 0;
                break;
            case 3: // Quit confirmation - 2 items (0-1)
                maxCommandNum = 1;
                break;
        }
        
        // Navigation with W/S keys
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            gp.playSE(9);
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        
        if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            gp.playSE(9);
            if (gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        
        // Volume adjustment with A/D keys (only in main options)
        if (gp.ui.subState == 0) {
            if (code == KeyEvent.VK_A) {
                if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if (gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
                    gp.se.volumeScale--;
                    gp.playSE(9);
                }
            }
            if (code == KeyEvent.VK_D) {
                if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if (gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
                    gp.se.volumeScale++;
                    gp.playSE(9);
                }
            }
        }
    }
    
    public void gameOverState(int code) {
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
            gp.playSE(9);
        }
    
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
            gp.playSE(9);
        }
            
        if (code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                // Retry - keep items, just respawn
                gp.gameState = gp.playState;
                gp.resetGame(false); 
                gp.player.invincible = true; 
                gp.ui.commandNum = 0;
                gp.playMusic(0);
            }
            else if (gp.ui.commandNum == 1) {
                // Quit to title - full reset
                gp.gameState = gp.titleState;
                gp.ui.titleScreenState = 0; // Main title screen
                gp.ui.commandNum = 0; // Set to "New Game" (0)
                gp.resetGame(true); // Pass true for full reset
                gp.stopMusic();
            }
            enterPressed = false;
        }
    }

    public void tradeState(int code) {
        // Handle Escape key
        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.ui.subState == 0) {
                gp.gameState = gp.playState;
            } else {
                gp.ui.subState = 0;
                gp.ui.commandNum = 0;
            }
            enterPressed = false;
        }
        
        // Only set enterPressed to true if Enter is pressed
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        
        // Handle navigation in main trade menu
        if (gp.ui.subState == 0) {
            if(code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                gp.playSE(9);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
                enterPressed = false;
            }
            if(code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                gp.playSE(9);
                if (gp.ui.commandNum > 2) { 
                    gp.ui.commandNum = 0;
                }
                enterPressed = false;
            }
        }   
        
        // Handle navigation in buy menu (npc inventory)
        if (gp.ui.subState == 1) {
            npcInventory(code);
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_S || 
                code == KeyEvent.VK_A || code == KeyEvent.VK_D) {
                enterPressed = false;
            }
        }
        if (gp.ui.subState == 2) {
            playerInventory(code);
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_S || 
                code == KeyEvent.VK_A || code == KeyEvent.VK_D) {
                enterPressed = false;
            }
        }        
    }   
    
    public void playerInventory(int code) {
        if (code == KeyEvent.VK_W) {
            if (gp.ui.playerSlotRow != 0) {
                gp.ui.playerSlotRow--;
                gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_S) {
            if (gp.ui.playerSlotRow != 3) {
            gp.ui.playerSlotRow++;
            gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_A) {
            if (gp.ui.playerSlotCol != 0) {
            gp.ui.playerSlotCol--;
            gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_D) {
            if (gp.ui.playerSlotCol != 4) {
            gp.ui.playerSlotCol++;
            gp.playSE(9);
            }
        } 
    }
    
    public void npcInventory(int code) {
        if (code == KeyEvent.VK_W) {
            if (gp.ui.npcSlotRow != 0) {
                gp.ui.npcSlotRow--;
                gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_S) {
            if (gp.ui.npcSlotRow != 3) {
            gp.ui.npcSlotRow++;
            gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_A) {
            if (gp.ui.npcSlotCol != 0) {
            gp.ui.npcSlotCol--;
            gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_D) {
            if (gp.ui.npcSlotCol != 4) {
            gp.ui.npcSlotCol++;
            gp.playSE(9);
            }
        } 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (code == KeyEvent.VK_Q) {
            questkeyPressed = false;
        }
        if (code == KeyEvent.VK_W) {
           upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
           downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
        if (code == KeyEvent.VK_Q) {
            gp.gameState = gp.playState;
            questkeyPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }
}