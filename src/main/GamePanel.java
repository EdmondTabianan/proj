package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import ai.PathFinder;
import entity.Entity;
import entity.NPC_blueboy;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable {
    final int OriginalTileSize = 16; // 16x16 tile
    final int Scale = 3;

    public final int TileSize = OriginalTileSize * Scale; // 48x48 tile
    public final int MaxScreenCol = 18;
    public final int MaxScreenRow = 12;
    public final int ScreenWidth = TileSize * MaxScreenCol; // if maxscreen is 16 768 pixels if maxscreen is 18 864 pixels
    public final int ScreenHeight = TileSize * MaxScreenRow; // 576 pixels

    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;
    
    //FPS
    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this); 
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public eventHandler eHandler = new eventHandler(this);
    Config config = new Config(this);
    public PathFinder pFinder = new PathFinder(this);
    public LoadingManager loadingManager;
    Thread gameThread;

    // Entity and Object
    public Player player = new Player(this, keyH);
    public NPC_blueboy npc_blueboy = new NPC_blueboy(this);
    public Entity obj[][] = new Entity[maxMap][20];
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][20];
    public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
    public Entity projectile[][] = new Entity[maxMap][20];
    // public ArrayList<Entity> projectileList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int loadingState = 6;
    public final int gameOverState = 7;
    public final int transitionState = 8;
    public final int tradeState = 9;
    public final int questState = 10;

    public int monsterRespawnCounter = 0;
    private boolean loadingStarted = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // Initialize loading manager
        loadingManager = new LoadingManager(this);
        
        // Set initial game state to loading
        gameState = loadingState;
    }

    public void setupGame() {
        // This will be called by LoadingManager
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        
        // Set player defaults AFTER loading
        player.setDefaultValues();
        player.selectItem();
    }

    public void retry() {
        player.respawnAtMapEntrance(currentMap);
        player.resetLifeAndMana();
    }

    public void restart() {
        player.setDefaultValues();
        player.selectItem();
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;

        // Start loading ONLY ONCE
        if (!loadingStarted) {
            loadingStarted = true;
            gameState = loadingState;
            
            // Start loading in a separate thread
            Thread loadingStarter = new Thread(() -> {
                try {
                    Thread.sleep(500); // Give everything time to initialize
                    loadingManager.startLoading();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            loadingStarter.start();
        }

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
                // System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
            
            // Check if loading is complete and switch to title state
            if (gameState == loadingState && !loadingManager.isLoading()) {
                gameState = titleState;
            }
        }
    }

    public void update() {
        // Don't update game logic while loading
        if (loadingManager.isLoading()) {
            return;
        }

        if (gameState == playState) {
            // Player
            player.update();
            
            // NPC
            for(int i = 0; i < npc[1].length; i++){
                if(npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }
            
            // Monsters
            for (int i = 0; i < monster[1].length; i++) {
                if(monster[currentMap][i] != null) {
                    if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
                        monster[currentMap][i].update();
                    } 
                    if(monster[currentMap][i].alive == false) {
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                        monsterRespawnCounter++;
                        if (monsterRespawnCounter == 120) {
                            aSetter.setMonster();
                        }
                    }
                }
            }
            
            // Projectiles
            for (int i = 0; i < projectile[1].length; i++) {
                if(projectile[currentMap][i]  != null) {
                    if(projectile[currentMap][i].alive == true) {
                        projectile[currentMap][i].update();
                    } 
                    if(projectile[currentMap][i].alive == false) {
                        projectile[currentMap][i] = null;
                    }
                }
            }
            
            // Interactive Tiles
            for (int i = 0; i < iTile[1].length; i++) {
                if(iTile[currentMap][i] != null) {
                    iTile[currentMap][i].update();
                }
            }
        }
        
        if (gameState == pauseState) {
            // Nothing updates when paused
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Always draw loading screen if loading
        if (loadingManager.isLoading()) {
            ui.drawLoadingScreen(g2);
        } 
        else if (gameState == titleState) {
            ui.draw(g2);
        }
        else if (gameState == playState || gameState == pauseState || 
                 gameState == dialogueState || gameState == characterState ||
                 gameState == optionsState || gameState == gameOverState ||
                 gameState == transitionState || gameState == tradeState ||
                 gameState == questState) {
            
            // Debug timing
            long drawStart = 0;
            if (keyH.showDebugText == true){
                drawStart = System.nanoTime();
            }

            // Draw tile
            tileM.draw(g2);

            // Interactive Tile
            for (int i = 0; i < iTile[1].length; i++) {
                if(iTile[currentMap][i] != null) {
                    iTile[currentMap][i].draw(g2);
                }
            }

            // Add entities to the list
            entityList.add(player);

            for(int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }
            
            for(int i = 0; i < obj[1].length; i++) {
                if (obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }
            
            for(int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }
            
            for(int i = 0; i < projectile[1].length; i++) {
                if (projectile[currentMap][i] != null) {
                    entityList.add(projectile[currentMap][i]);
                }
            }

            // Sort by Y position for proper drawing order
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            // Draw entities
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            
            // Empty entities list
            entityList.clear();

            // Draw UI
            ui.draw(g2);

            // Debug info
            if (keyH.showDebugText == true){
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;
                g2.setFont(new Font("Arial", Font.PLAIN,20));
                g2.setColor(Color.white);
                int x = 10;
                int y = 400;
                int lineHeight = 20;
                g2.drawString("worldX" + player.worldX, x, y); y += lineHeight;
                g2.drawString("worldY" + player.worldY, x, y); y += lineHeight;
                g2.drawString("Col" + (player.worldX + player.solidArea.x)/TileSize, x, y); y += lineHeight;
                g2.drawString("Row" + (player.worldY + player.solidArea.y)/TileSize, x, y); y += lineHeight;
                g2.drawString("Draw Time: " + passed, x, y);
            }
        }

        g2.dispose();
    }

    public void playMusic(int i) {
        if (!loadingManager.isLoading()) { // Don't play music while loading
            music.setFile(i);
            music.play();
            music.loop();
        }
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        if (!loadingManager.isLoading()) { // Don't play sounds while loading
            se.setFile(i);
            se.play();
        }
    }
}