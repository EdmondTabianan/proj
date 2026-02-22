package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import data.SaveLoad;

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
        else if (gp.gameState == gp.dialogueState) {
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
                    gp.ui.commandNum = 0;
                }
                if(gp.ui.commandNum == 1) {
                    // Load Game - go to load selection screen
                    gp.ui.titleScreenState = 2;
                    gp.ui.commandNum = 0;
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
                }
                if(gp.ui.commandNum == 1) {
                    // Alexandria selected
                    gp.loadingManager.startGameWithCharacter(1);
                    gp.gameState = gp.transitionState;
                }
                if (gp.ui.commandNum == 2) {
                    // Back to main menu
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0;
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
                    System.out.println("User selected: Load Slot 1");
                    loadGameFromSlot(0);
                }
                else if(gp.ui.commandNum == 1) {
                    // Load Slot 2
                    System.out.println("User selected: Load Slot 2");
                    loadGameFromSlot(1);
                }
                else if(gp.ui.commandNum == 2) {
                    // Load Slot 3
                    System.out.println("User selected: Load Slot 3");
                    loadGameFromSlot(2);
                }
                else if(gp.ui.commandNum == 3) {
                    // Back to main menu
                    System.out.println("User selected: Back to Main Menu");
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 1; // Set cursor to "Load Game"
                }
                enterPressed = false;
            }
            
            // Allow ESC to go back
            if (code == KeyEvent.VK_ESCAPE) {
                System.out.println("User pressed ESC - returning to main menu");
                gp.ui.titleScreenState = 0;
                gp.ui.commandNum = 1; // Set cursor to "Load Game"
                enterPressed = false;
            }
        }
    }

    private void loadGameFromSlot(int slot) {
        String filename = "save_slot_" + (slot + 1) + ".dat";
        File saveFile = new File(filename);
        
        if (!saveFile.exists()) {
            gp.ui.showMessage("No save file in Slot " + (slot + 1));
            System.out.println("LOAD ERROR: Save file not found: " + filename);
            return;
        }
        
        try {
            // STEP 1: CRITICAL - Initialize GamePanel components if needed
            if (gp.cChecker == null) {
                System.out.println("Initializing collision checker...");
                gp.cChecker = new CollisionChecker(gp);
            }
            
            if (gp.aSetter == null) {
                System.out.println("Initializing asset setter...");
                gp.aSetter = new AssetSetter(gp);
            }
            
            if (gp.ui == null) {
                System.out.println("Initializing UI...");
                gp.ui = new UI(gp);
            }
            
            if (gp.eHandler == null) {
                System.out.println("Initializing event handler...");
                gp.eHandler = new eventHandler(gp);
            }
            
            // STEP 2: Create player if it doesn't exist
            if (gp.player == null) {
                System.out.println("Player is null - creating new player...");
                // CRITICAL: Pass 'this' (KeyHandler) to the player constructor
                gp.player = new entity.Player(gp, this, 0);
                
                // Initialize equipment
                System.out.println("Initializing player equipment...");
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
                
                System.out.println("Player created successfully");
            } else {
                // Ensure existing player has KeyHandler reference
                System.out.println("Player exists - ensuring KeyHandler is set...");
                gp.player.keyH = this;
            }
            
            // STEP 3: Load the save data
            System.out.println("Loading game from slot " + (slot + 1) + "...");
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
            System.out.println("Resetting assets for map " + gp.currentMap + "...");
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
            
            // Show success message
            String successMsg = "✓ Game Loaded from Slot " + (slot + 1);
            gp.ui.showMessage(successMsg);
            System.out.println(successMsg);
            
            // Play background music
            gp.playMusic(0);
            
            // Debug info
            System.out.println("=== LOAD COMPLETE ===");
            System.out.println("Player world: (" + gp.player.worldX + ", " + gp.player.worldY + ")");
            System.out.println("KeyHandler set: " + (gp.player.keyH != null));
            System.out.println("Current map: " + gp.currentMap);
            System.out.println("Game state: " + gp.gameState);
            
        } catch (Exception e) {
            System.out.println("!!! LOAD EXCEPTION !!!");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            gp.ui.showMessage("LOAD FAILED!");
        }
    }

    public void playState(int code) {
        if (code == KeyEvent.VK_Q) {
            // gp.gameState = gp.questState;
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
         }
         if (code == KeyEvent.VK_F) {
            shotKeyPressed = true;
         }
         if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionsState;
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
            // gp.tileM.loadMap("/map/main.txt", 0);
            gp.aSetter.setInteractiveTile(gp.currentMap);
         } 
    }
    
    public void pauseState(int code) {
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
    }
    
    public void dialogueState(int code) {
        if(code == KeyEvent.VK_ENTER) {
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
            } else {
                gp.ui.subState = 0; // Go back to main options
            }
            enterPressed = false;
        }
        
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
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
                gp.gameState = gp.playState;
                gp.resetGame(false);
                gp.playMusic(0);
            }
            else if (gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.resetGame(true);
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
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }
}