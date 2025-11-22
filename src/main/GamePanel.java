package main;

import java.awt.*;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    final int OriginalTileSize = 16; // 16x16 tile
    final int Scale = 3;

    public final int TileSize = OriginalTileSize * Scale; // 48x48 tile
    public final int MaxScreenCol = 16;
    public final int MaxScreenRow = 12;
    public final int ScreenWidth = TileSize * MaxScreenCol; // 768 pixels
    public final int ScreenHeight = TileSize * MaxScreenRow; // 576 pixels

    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = TileSize * maxWorldCol;
    public final int worldHeight = TileSize * maxWorldRow;
    
    //FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    // public void run() {
    //     double drawInterval = 1000000000 / FPS;
    //     double nextDrawTime = System.nanoTime() + drawInterval;
    //     long lastTime = System.nanoTime();
    //     long currentTime;
    //     long timer = 0;
    //     long drawCount = 0;

    //     while (gameThread != null) {
    //         currentTime = System.nanoTime();
    //         update();

    //         repaint();
    //         try {
    //             double remainingTime = nextDrawTime - System.nanoTime();
    //             remainingTime = remainingTime / 1000000;

    //             if (remainingTime < 0) {
    //                 remainingTime = 0;
    //             }

    //             Thread.sleep((long) remainingTime);

    //             nextDrawTime += drawInterval;

    //             if (timer >= 1000000000) {
    //                 System.out.println("FPS: " + drawCount);
    //                 drawCount = 0;
    //                 timer = 0;
    //             }
    //         } catch (InterruptedException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update() {
       player.update();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);
        player.draw(g2);

        g2.dispose();
    }
}