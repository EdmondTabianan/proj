package main;

import java.awt.Graphics2D;
import entity.Player;

public class LoadingManager {

    GamePanel gp;
    private float loadingProgress = 0;
    public boolean isLoading = false;
    private Thread loadingThread;
    private boolean initialized = false;
    private boolean assetsLoaded = false;
    
    // Add these flags to track loading state
    private boolean titleScreenReady = false;
    private boolean gameStarted = false;

    public LoadingManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startLoading() {
        if (isLoading) return;
        
        isLoading = true;
        loadingProgress = 0;
        initialized = false;
        assetsLoaded = false;
        titleScreenReady = false;
        gameStarted = false;

        loadingThread = new Thread(() -> {
            try {
                
                // Step 1: Initialize game components
                loadingProgress = 5;
                Thread.sleep(50);
                
                // Step 2: Set up initial map objects
                if (gp.aSetter != null) {
                    gp.aSetter.setObject(0);
                }
                loadingProgress = 20;
                Thread.sleep(50);
                
                // Step 3: Load NPCs
                if (gp.aSetter != null) {
                    gp.aSetter.setNPC(0);
                }
                loadingProgress = 40;
                Thread.sleep(50);
                
                // Step 4: Load monsters
                if (gp.aSetter != null) {
                    gp.aSetter.setMonster(0);
                }
                loadingProgress = 60;
                Thread.sleep(50);
                
                // Step 5: Load interactive tiles
                if (gp.aSetter != null) {
                    gp.aSetter.setInteractiveTile(0);
                }
                loadingProgress = 80;
                Thread.sleep(50);
                
                // Mark assets as loaded
                assetsLoaded = true;
                
                // Step 6: Finalize
                loadingProgress = 100;
                Thread.sleep(200);
                
                // Loading complete, switch to title state
                javax.swing.SwingUtilities.invokeLater(() -> {
                    gp.gameState = gp.titleState;
                    isLoading = false;
                    initialized = true;
                    titleScreenReady = true;
                });

            } catch (Exception e) {
                e.printStackTrace();
                isLoading = false;
            }
        });

        loadingThread.setName("LoadingThread");
        loadingThread.start();
    }

    public void startGameWithCharacter(int characterChoice) {
        // Prevent multiple game starts
        if (gameStarted || gp.gameState == gp.playState) {
            return;
        }
        
        gameStarted = true;
        isLoading = true;
        loadingProgress = 0;
        
        // Use a thread for loading to keep UI responsive
        Thread gameStartThread = new Thread(() -> {
            try {
                // Quick loading progress updates
                for (int i = 0; i <= 20; i++) {
                    loadingProgress = i * 5;
                    Thread.sleep(20);
                }
                
                // Stop any existing music first
                if (gp.music != null) {
                    gp.stopMusic();
                }
                
                // Clear any existing player
                gp.player = null;
                
                loadingProgress = 30;
                Thread.sleep(50);
                
                // Create the player directly
                gp.player = new Player(gp, gp.keyH, characterChoice);
                gp.player.setDefaultValues();
                gp.player.setItems();
                
                loadingProgress = 50;
                Thread.sleep(50);
                
                // Set player position
                gp.player.worldX = gp.TileSize * 46;
                gp.player.worldY = gp.TileSize * 39;
                
                loadingProgress = 70;
                Thread.sleep(50);
                
                // Clear and reload all map assets for the current map
                if (gp.aSetter != null) {
                    // Clear existing assets first
                    gp.aSetter.clearMapAssets(gp.currentMap);
                    
                    // Load new assets
                    gp.aSetter.setObject(gp.currentMap);
                    gp.aSetter.setNPC(gp.currentMap);
                    gp.aSetter.setMonster(gp.currentMap);
                    gp.aSetter.setInteractiveTile(gp.currentMap);
                }
                
                loadingProgress = 90;
                Thread.sleep(50);
                
                loadingProgress = 100;
                Thread.sleep(100);
                
                // Switch to play state on EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    // Start music
                    gp.playMusic(0);
                    
                    // Change game state
                    gp.gameState = gp.playState;
                    
                    // Reset loading flags
                    isLoading = false;
                    gameStarted = false;
                    
                    gp.repaint(); // Force a repaint
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                isLoading = false;
                gameStarted = false;
                // Go back to title on error
                javax.swing.SwingUtilities.invokeLater(() -> {
                    gp.gameState = gp.titleState;
                });
            }
        });
        
        gameStartThread.setName("GameStartThread");
        gameStartThread.start();
        
        // Set game to transition state to show loading screen
        gp.gameState = gp.transitionState;
    }

    public void startTransition(int targetMap, float col, int row) {
        if (isLoading) return;
        
        isLoading = true;
        loadingProgress = 0;
        
        // Store transition data
        gp.eHandler.tempMap = targetMap;
        gp.eHandler.tempColFloat = col;
        gp.eHandler.tempRow = row;
        
        Thread transitionThread = new Thread(() -> {
            try {
                // Smooth transition progress
                for (int i = 0; i <= 10; i++) {
                    loadingProgress = i * 10;
                    Thread.sleep(30);
                }
                
                // Set new map
                gp.currentMap = targetMap;
                loadingProgress = 50;
                Thread.sleep(30);
                
                // Clear and load new map assets
                if (gp.aSetter != null) {
                    gp.aSetter.clearMapAssets(gp.currentMap);
                    gp.aSetter.setMonster(gp.currentMap);
                    gp.aSetter.setNPC(gp.currentMap);
                    gp.aSetter.setObject(gp.currentMap);
                    gp.aSetter.setInteractiveTile(gp.currentMap);
                }
                
                loadingProgress = 80;
                Thread.sleep(30);
                
                // Position player on EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    try {
                        if (gp.player != null) {
                            gp.player.worldX = gp.TileSize * (int)col;
                            gp.player.worldY = gp.TileSize * row;
                            
                            // Reset player solid area
                            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
                            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
                            
                            // Reset event cooldown
                            gp.eHandler.previouseEventX = gp.player.worldX;
                            gp.eHandler.previouseEventY = gp.player.worldY;
                            gp.eHandler.canTouchEvent = true;
                            
                            // Reset monster respawn counter
                            gp.monsterRespawnCounter = 0;
                        }
                        
                        loadingProgress = 100;
                        
                        // End transition
                        gp.gameState = gp.playState;
                        isLoading = false;
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        isLoading = false;
                        gp.gameState = gp.playState;
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                isLoading = false;
                gp.gameState = gp.playState;
            }
        });
        
        transitionThread.setName("TransitionThread");
        transitionThread.start();
        
        gp.gameState = gp.transitionState;
    }

    public void setLoadingProgress(float progress) {
        this.loadingProgress = progress;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void draw(Graphics2D g2) {
        // Only draw loading screen if we're actually loading
        if (isLoading || gp.gameState == gp.transitionState) {
            gp.ui.drawLoadingScreen(g2);
        }
    }

    public float getProgress() {
        return loadingProgress;
    }

    public boolean isLoading() {
        return isLoading || gp.gameState == gp.transitionState;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public boolean areAssetsLoaded() {
        return assetsLoaded;
    }
    
    public boolean isTitleScreenReady() {
        return titleScreenReady;
    }
}