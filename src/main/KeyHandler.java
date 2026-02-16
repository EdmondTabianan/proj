package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
                    gp.ui.titleScreenState = 1;
                    gp.ui.commandNum = 0;
                }
                if(gp.ui.commandNum == 1) {
                    // later add 
                    gp.ui.showMessage("Load Game - Coming soon!");
                }
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
                enterPressed = false;
            }
        }
        else if(gp.ui.titleScreenState == 1) {
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
                    // Xylo selected - Use LoadingManager
                    gp.loadingManager.startGameWithCharacter(0);
                    gp.gameState = gp.transitionState;
                }
                if(gp.ui.commandNum == 1) {
                    // Alexandria selected - Use LoadingManager
                    gp.loadingManager.startGameWithCharacter(1);
                    gp.gameState = gp.transitionState;
                }
                if (gp.ui.commandNum == 2) {
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0;
                }
                enterPressed = false;
            }
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
                case 1: gp.tileM.loadMap("/map/secndmap.txt", 1);break;
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
                gp.retry();
                gp.playMusic(0);
            }
            else if (gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.restart();
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