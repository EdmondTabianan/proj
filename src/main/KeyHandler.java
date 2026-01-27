package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed, arrowKeyPressed;
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
        // game overstate 
        else if (gp.gameState == gp.gameOverState) {
            gameOverState(code);
        }
    }   

    public void titleState(int code) {
        if(gp.ui.titleScreenState == 0) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0) {
                    gp.ui.titleScreenState = 1;
                }
                if(gp.ui.commandNum == 1) {
                    // later add 
                }
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }
        else if(gp.ui.titleScreenState == 1) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if(gp.ui.commandNum == 1) {
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 2) {
                    gp.ui.titleScreenState = 0;
                }
            }
        }
    }
    public void playState(int code) {
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
            
            gp.aSetter.setInteractiveTile();
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
        }
        if (code == KeyEvent.VK_W) {
            if (gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
                gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_S) {
            if (gp.ui.slotRow != 3) {
            gp.ui.slotRow++;
            gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_A) {
            if (gp.ui.slotCol != 0) {
            gp.ui.slotCol--;
            gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_D) {
            if (gp.ui.slotCol != 4) {
            gp.ui.slotCol++;
            gp.playSE(9);
            }
        } 
        if (code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
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
            if (gp.ui.commandNum > maxCommandNum) {  // FIXED: > instead of ==
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
        }
        if (gp.ui.commandNum > 1) {
            gp.ui.commandNum = 0;
        }
            gp.playSE(9);
            
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
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
    }
}
