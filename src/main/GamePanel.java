package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.NPC_blueboy;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;
import main.CutsceneManager;

public class GamePanel extends JPanel implements Runnable {
    final int OriginalTileSize = 16; // 16x16 tile
    final int Scale = 3;

    public final int TileSize = OriginalTileSize * Scale; // 48x48 tile
    public final int MaxScreenCol = 18;
    public final int MaxScreenRow = 12;
    public final int ScreenWidth = TileSize * MaxScreenCol; // 864 pixels
    public final int ScreenHeight = TileSize * MaxScreenRow; // 576 pixels

    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;
    public int mapNum;
    
    //FPS
    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public Sound music = new Sound();
    public Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this); 
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public eventHandler eHandler = new eventHandler(this);
    public Config config = new Config(this);
    public PathFinder pFinder = new PathFinder(this);
    public SaveLoad saveLoad = new SaveLoad(this);
    public CutsceneManager csManager = new CutsceneManager(this);
    public LoadingManager loadingManager;
    Thread gameThread;

    // npc Direction
    public String npcDirection = "";

    // Entity and Object
    public Player player;
    public NPC_blueboy npc_blueboy;
    public Entity obj[][] = new Entity[maxMap][20];
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][100];
    public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
    public Entity projectile[][] = new Entity[maxMap][20];
    ArrayList<Entity> entityList = new ArrayList<>();
    public boolean questSlimesSpawned = false;
    public EndingManager endingManager;

    // GAME STATE
    public int gameState;
    public final static int titleState = 0;
    public final static int playState = 1;
    public final static int pauseState = 2;
    public final static int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int loadingState = 6;
    public final int gameOverState = 7;
    public final int transitionState = 8;
    public final int tradeState = 9;
    public final int questState = 10;
    public final int cutsceneState = 11;
    public final int sleepState = 12;
    public final int endingState = 13;

    // others 
    public boolean bossBattleOn = false;

    public int questProgress = 0; // Track quest progress

    public int monsterRespawnCounter = 0;
    private boolean loadingStarted = false;
    private boolean loadingComplete = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        loadingManager = new LoadingManager(this);
        endingManager = new EndingManager(this);
        gameState = loadingState;
    }


    public void setupGame() {
        
        if (aSetter != null) {
            aSetter.clearMapAssets(currentMap);
            aSetter.setObject(currentMap);
            aSetter.setNPC(currentMap);
            aSetter.setMonster(currentMap);
            aSetter.setInteractiveTile(currentMap);
        }
        
    }
    public void resetGame(boolean restart) {
        if (restart == true) {
            // Full game reset (when quitting to title)
            player.setDefaultValues();
            player.resetLifeAndMana();
            removeTempEntity(); // Remove temporary entities like boss doors
            bossBattleOn = false;
            questProgress = 0; // Reset quest progress
            
            // Reset all assets
            aSetter.resetAllPickedUpItems();
            aSetter.clearMapAssets(currentMap);
            aSetter.setObject(currentMap);
            aSetter.setNPC(currentMap);
            aSetter.setMonster(currentMap);
            aSetter.setInteractiveTile(currentMap);
            
            // Reset ending state if needed
            if (gameState == endingState) {
                gameState = titleState;
            }
            
        } else {
            // Just retry from game over - keep items and progress
            player.respawnAtMapEntrance(currentMap); // Respawn at safe location
            player.resetLifeAndMana(); // Just restore health/mana
        }
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
            gameState = loadingState; // Keep in loading state
            
            // Start loading in a separate thread
            Thread loadingStarter = new Thread(() -> {
                try {
                    Thread.sleep(100); // Give everything time to initialize
                    loadingManager.startLoading();
                    loadingComplete = true; // Mark loading as complete
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
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        // Don't update game logic while loading
        if (loadingManager != null && loadingManager.isLoading()) {
            return;
        }
    
        if (gameState == loadingState && loadingComplete && loadingManager != null && !loadingManager.isLoading()) {
            gameState = titleState; // Now show title screen
            return;
        }
    
        if (gameState == playState) {
            // Player - only update if player exists
            if (player != null) {
                player.update();
            }
            
            // NPC
            if (npc[currentMap] != null) {
                for(int i = 0; i < npc[1].length; i++) {
                    if(npc[currentMap][i] != null) {
                        npc[currentMap][i].update();
                    }
                }
            }
            
            // Monsters
            if (monster[currentMap] != null) {
                for (int i = 0; i < monster[1].length; i++) {
                    if(monster[currentMap][i] != null) {
                        if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
                            monster[currentMap][i].update();
                        } 
                        if(monster[currentMap][i].alive == false) {
                            monster[currentMap][i].checkDrop();
                            monster[currentMap][i] = null;
                        }
                    }
                }
            }
            
            // Projectiles
            if (projectile[currentMap] != null) {
                for (int i = 0; i < projectile[1].length; i++) {
                    if(projectile[currentMap][i] != null) {
                        if(projectile[currentMap][i].alive == true) {
                            projectile[currentMap][i].update();
                        } 
                        if(projectile[currentMap][i].alive == false) {
                            projectile[currentMap][i] = null;
                        }
                    }
                }
            }
            
            // Interactive Tiles
            if (iTile[currentMap] != null) {
                for (int i = 0; i < iTile[1].length; i++) {
                    if(iTile[currentMap][i] != null) {
                        iTile[currentMap][i].update();
                    }
                }
            }
        }
        
        else if (gameState == transitionState) {
            return;
        }
       
        if (gameState == pauseState) {}
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Enable anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
        // Always draw loading screen if loading
        if (loadingManager != null && loadingManager.isLoading()) {
            ui.drawLoadingScreen(g2);
        } 
        // If loading is complete AND we're in loading state, show loading screen until transition
        else if (gameState == loadingState) {
            ui.drawLoadingScreen(g2);
        }
        else if (gameState == titleState) {
            ui.draw(g2);
        }
        // ===== NEW: Handle ending state with EndingManager =====
        else if (gameState == endingState) {
            endingManager.draw(g2);
        }
        else if (gameState == playState || gameState == pauseState || 
                 gameState == dialogueState || gameState == characterState ||
                 gameState == optionsState || gameState == gameOverState ||
                 gameState == transitionState || gameState == tradeState ||
                 gameState == questState || gameState == cutsceneState) {
            
            ui.updateDialogueAnimation();
            if (player != null) {
                // Draw tile
                tileM.draw(g2);
    
                // Draw interactive tiles
                if (iTile[currentMap] != null) {
                    for (int i = 0; i < iTile[1].length; i++) {
                        if(iTile[currentMap][i] != null) {
                            iTile[currentMap][i].draw(g2);
                        }
                    }
                }
    
                // Add entities to the list
                entityList.add(player);
    
                // Add NPCs
                if (npc[currentMap] != null) {
                    for(int i = 0; i < npc[1].length; i++) {
                        if (npc[currentMap][i] != null) {
                            entityList.add(npc[currentMap][i]);
                        }
                    }
                }
                
                // Add objects
                if (obj[currentMap] != null) {
                    for(int i = 0; i < obj[1].length; i++) {
                        if (obj[currentMap][i] != null) {
                            entityList.add(obj[currentMap][i]);
                        }
                    }
                }
                
                // Add monsters
                if (monster[currentMap] != null) {
                    for(int i = 0; i < monster[1].length; i++) {
                        if (monster[currentMap][i] != null) {
                            entityList.add(monster[currentMap][i]);
                        }
                    }
                }
                
                // Add projectiles
                if (projectile[currentMap] != null) {
                    for(int i = 0; i < projectile[1].length; i++) {
                        if (projectile[currentMap][i] != null) {
                            entityList.add(projectile[currentMap][i]);
                        }
                    }
                }
    
                // Sort by Y position for proper drawing order
                if (entityList.size() > 0) {
                    Collections.sort(entityList, new Comparator<Entity>() {
                        @Override
                        public int compare(Entity e1, Entity e2) {
                            return Integer.compare(e1.worldY, e2.worldY);
                        }
                    });
                }
    
                // Draw entities
                for (int i = 0; i < entityList.size(); i++) {
                    entityList.get(i).draw(g2);
                }
                
                // Empty entities list
                entityList.clear();
    
                csManager.draw(g2);
    
                // Draw UI
                ui.draw(g2);
            } else {
                gameState = titleState;
                ui.draw(g2);
            }
        }
        
        // For transition state, still draw loading screen
        else if (gameState == transitionState) {
            ui.drawLoadingScreen(g2);
        }
    
        g2.dispose();  
    }

    public void playMusic(int i) {
        if (loadingManager != null && !loadingManager.isLoading()) {
            music.setFile(i);
            music.loop();
        }
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        if (loadingManager != null && !loadingManager.isLoading()) {
            se.setFile(i);
            se.play();
        }
    }
    public void removeTempEntity() {
        for(int i = 0; i < obj[1].length; i++) {
            if (obj[mapNum][i] != null && obj[mapNum][i].temp == true) {
                obj[mapNum][i] = null;
            }
        }
    }
}