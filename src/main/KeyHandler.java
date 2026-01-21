package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    boolean showDebugText = false;
    Sound music = new Sound();
    Sound se = new Sound();

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Title State 
        if (gp.gameState == gp.titleState) {
            titleState(code);
        }
        // Play State
        else if (gp.gameState == gp.playState) {
            playState(code);
        }
        // Pause state
        else if (gp.gameState == gp.pauseState) {
            pauseState(code);
        }
        // Dialogue state
        else if (gp.gameState == gp.dialogueState) {
            dialogueState(code);
        }
        // Character State
        else if (gp.gameState == gp.characterState) {
            characterState(code);
        }
    }   

    public void titleState(int code) {
        if (gp.ui.titleScreenState == 0) {
            // First screen - Main menu
            if (code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_X) {
                if (gp.ui.commandNum == 0) {
                    // New Game selected - go to character selection
                    gp.ui.titleScreenState = 1;
                    gp.ui.commandNum = 0; // Reset to first option
                }
                if (gp.ui.commandNum == 1) {
                    // Load Game - to be implemented later
                    // gp.gameState = gp.playState;
                    // gp.playMusic(0);
                }
                if (gp.ui.commandNum == 2) {
                    // Quit game
                    System.exit(0);
                }
            }
        } else if (gp.ui.titleScreenState == 1) {
            // Second screen - Character selection
            if (code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_X) {
                if (gp.ui.commandNum == 0) { // Xylo selected
                    gp.player.characterused = 1; // Set to Xylo
                    gp.player.setCharacterImages(); // Update images
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1) { // Alexandria selected
                    gp.player.characterused = 0; // Set to Alexandria
                    gp.player.setCharacterImages(); // Update images
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 2) {
                    // Back to main menu
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum = 0; // Reset to "New Game"
                }
            }
        }
    }

    public void playState(int code) {
        if (code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
        }
        if (code == KeyEvent.VK_X) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
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
            gp.tileM.loadMap("/map/ano.txt");
        }
    }

    public void pauseState(int code) {
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
    }

    public void dialogueState(int code) {
        if (code == KeyEvent.VK_X) {
            gp.gameState = gp.playState;
        }
    }

    public void characterState(int code) {
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }
}