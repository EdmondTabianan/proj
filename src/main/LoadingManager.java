package main;

import java.awt.Graphics2D;

public class LoadingManager {

    GamePanel gp;
    private float loadingProgress = 0;
    private boolean isLoading = false;
    private Thread loadingThread;
    private boolean initialized = false;

    public LoadingManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startLoading() {
        if (isLoading) return;
        
        isLoading = true;
        loadingProgress = 0;
        initialized = false;

        loadingThread = new Thread(() -> {
            try {
                // Initialize game components
                loadingProgress = 5;
                Thread.sleep(100);
                
                // Step 1: Load objects
                gp.aSetter.setObject();
                loadingProgress = 20;
                Thread.sleep(200);
                
                // Step 2: Load NPCs
                gp.aSetter.setNPC();
                loadingProgress = 40;
                Thread.sleep(200);
                
                // Step 3: Load monsters
                gp.aSetter.setMonster();
                loadingProgress = 60;
                Thread.sleep(200);
                
                // Step 4: Load interactive tiles
                gp.aSetter.setInteractiveTile();
                loadingProgress = 80;
                Thread.sleep(200);
                
                // Step 5: Final setup
                gp.player.setDefaultValues();
                gp.player.selectItem();
                loadingProgress = 95;
                Thread.sleep(100);
                
                // Mark as initialized
                initialized = true;
                loadingProgress = 100;
                Thread.sleep(300);
                
                // Loading complete, switch to title state
                gp.gameState = gp.titleState;
                isLoading = false;

            } catch (Exception e) {
                e.printStackTrace();
                isLoading = false;
            }
        });

        loadingThread.setName("LoadingThread");
        loadingThread.start();
    }

    public void draw(Graphics2D g2) {
        if (!isLoading && !initialized) return;
        gp.ui.drawLoadingScreen(g2);
    }

    public float getProgress() {
        return loadingProgress;
    }

    public boolean isLoading() {
        return isLoading || !initialized;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}