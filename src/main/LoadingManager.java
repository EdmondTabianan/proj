package main;

import java.awt.Graphics2D;

public class LoadingManager {
    GamePanel gp;
    private float loadingProgress = 0;
    private boolean isLoading = false;
    private Thread loadingThread;
    
    public LoadingManager(GamePanel gp) {
        this.gp = gp;
    }
    
    public void startLoading() {
        isLoading = true;
        loadingProgress = 0;
        
        loadingThread = new Thread(() -> {
            try {
                // Simulate loading with smooth progress
                while (loadingProgress < 100) {
                    Thread.sleep(100); // Update every 50ms for smooth animation
                    loadingProgress += 0.5f; // Adjust speed as needed

                    if ((int)loadingProgress == 30 || (int)loadingProgress == 60) {
                        Thread.sleep(5000); // Pause for 5 second
                    }
                    
                    gp.repaint();   
                }
                
                // Perform actual loading
                gp.setupGame();
                
                // Switch to title state
                gp.gameState = gp.titleState;
                isLoading = false;
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        loadingThread.start();
    }
    
    public void draw(Graphics2D g2) {
        if (!isLoading) return;
        
        // Draw the loading screen using UI's method
        gp.ui.drawLoadingScreen(g2);
    }
    
    public float getProgress() {
        return loadingProgress;
    }
    
    public boolean isLoading() {
        return isLoading;
    }
}