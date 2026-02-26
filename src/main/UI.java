package main;

import java.awt.Image;
import javax.imageio.ImageIO;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import object.OBJ_Sword_Normal;
import object.OBJ_Axe;
import object.OBJ_Shield_Wood;
import object.OBJ_ice_wand;
import object.OBJ_bow_normal;
import object.OBJ_Key;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;
import object.OBJ_boat;
import object.OBJ_tablet;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Arrows;
import object.OBJ_Doors;
import object.OBJ_ice;

import entity.Entity;
import entity.Player;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    public boolean gameFinished = false;
    
    // NPC tracking for dialogue
    public int npcIndex = 0; // Track which NPC we're talking to
    
    // Dialogue animation variables
    public String currentDialogue = "";
    private String targetDialogue = ""; // Full dialogue to display
    private int displayedChars = 0; // Number of characters displayed so far
    private boolean dialogueFinished = false;
    private long lastCharTime = 0;
    private final long CHAR_DELAY = 40; 

    // Multi-page dialogue variables
    public String[] dialoguePages; // Array of dialogue pages
    private int currentPageIndex = 0; // Current page being displayed
    private boolean dialogueActive = false;
    
    public int commandNum = 0;
    public int titleScreenState = 0; // 0 the first screen 1 second screen
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int subState = 0;
    int counter = 0;
    public Entity npc;
    private Image logoImage;
    public int loadingProgress = 0;
    private int loadingDirection = 1;

    // Quest screen page tracking
    public int questPage = 0;
    private int maxQuestPages = 1; // 0=Main Quests, 1=Pyramid Details

    // Tip display variables
    private String currentTip = "";
    private long lastTipChangeTime = 0;
    private final long TIP_DISPLAY_TIME = 8000; // 8 seconds per tip
    private int tipAlpha = 0; // For fade in/out effect
    private boolean tipFadingIn = true;
    private long lastTipUpdateTime = 0;
    private final long TIP_FADE_SPEED = 3000; // 3 seconds to fade in/out
    
    // For typing effect
    private String displayedTip = "";
    private int tipCharIndex = 0;
    private long lastTipCharTime = 0;
    private final long TIP_CHAR_DELAY = 30; // milliseconds between characters

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("0.00");


    public UI(GamePanel gp) {
        this.gp = gp;
        loadLogoImage();
        arial_40 = new Font("Times New Roman", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        
        // Initialize with a tip
        currentTip = getRandomGameplayTip();
        lastTipChangeTime = System.currentTimeMillis();
    
        // Create HUD object
        Entity heart = new OBJ_Heart(gp);
        Entity crystal = new OBJ_ManaCrystal(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
        Entity bronze_coin = new OBJ_Coin_Bronze(gp);
        coin = bronze_coin.down1;

    }

    private void loadLogoImage() {
        try {
            logoImage = ImageIO.read(getClass().getResourceAsStream("/loading/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawLoadingScreen(Graphics2D g2) {
        // Set background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
        
        // Draw logo in the center
        if (logoImage != null) {
            int logoWidth = 200;
            int logoHeight = 200;
            
            int logoX = gp.ScreenWidth / 2 - logoWidth / 2;
            int logoY = gp.ScreenHeight / 2 - logoHeight - 60;
            
            g2.drawImage(logoImage, logoX, logoY, logoWidth, logoHeight, null);
        }
        
        // Draw game title
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.setColor(new Color(255, 215, 0));
        
        String title = "The Hunt: Lost Tomb of Cleopatra";
        int titleX = gp.ScreenWidth / 2 - g2.getFontMetrics().stringWidth(title) / 2;
        int titleY = gp.ScreenHeight / 2;
        
        g2.drawString(title, titleX, titleY);
        
        // Update tip display BEFORE drawing
        updateTipDisplay();
        
        // Draw the gameplay tip
        g2.setFont(new Font("Arial", Font.ITALIC, 20));
        g2.setColor(new Color(180, 220, 255, tipAlpha)); // Use tipAlpha for fade effect
        
        // Use displayedTip which has the typewriter effect
        int tipTextX = gp.ScreenWidth / 2 - g2.getFontMetrics().stringWidth(displayedTip) / 2;
        int tipTextY = gp.ScreenHeight - 150;
        
        g2.drawString(displayedTip, tipTextX, tipTextY);
        
        // Progress bar
        int barWidth = gp.ScreenWidth - 200;
        int barHeight = 20;
        int barX = (gp.ScreenWidth - barWidth) / 2;
        int barY = gp.ScreenHeight - 100;
        
        // Border
        g2.setColor(Color.white);
        g2.drawRect(barX, barY, barWidth, barHeight);
        
        // Get actual loading progress from LoadingManager
        int actualProgress = (int)gp.loadingManager.getProgress();
        
        int fillWidth = (int)(barWidth * actualProgress / 100.0);
        
        // Gradient fill effect
        if (fillWidth > 0) {
            for (int i = 0; i < fillWidth; i += 3) {
                int segmentWidth = Math.min(3, fillWidth - i);
                float brightness = 0.6f + 0.4f * ((float)i / fillWidth);
                g2.setColor(new Color(
                    (int)(212 * brightness),
                    (int)(175 * brightness),
                    (int)(55 * brightness)
                ));
                g2.fillRect(barX + 1 + i, barY + 1, segmentWidth, barHeight - 2);
            }
        }
        
        // Percentage Text
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(Color.white);
        String percentText = actualProgress + "%";
        int percentX = gp.ScreenWidth / 2 - g2.getFontMetrics().stringWidth(percentText) / 2;
        int percentY = barY + 45;
        
        g2.drawString(percentText, percentX, percentY);
        
        // Draw "Loading..." text
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        String loadingText = "Loading...";
        int loadingX = gp.ScreenWidth / 2 - g2.getFontMetrics().stringWidth(loadingText) / 2;
        int loadingY = barY - 20;
        
        // Animate the dots
        int dotCount = (actualProgress / 20) % 4;
        String dots = "";
        for (int i = 0; i < dotCount; i++) {
            dots += ".";
        }
        
        g2.drawString(loadingText + dots, loadingX, loadingY);
    }
    
    private void updateTipDisplay() {
        long currentTime = System.currentTimeMillis();
        
        // Initialize if first time
        if (currentTip.isEmpty()) {
            currentTip = getRandomGameplayTip();
            displayedTip = "";
            tipCharIndex = 0;
            tipAlpha = 0;
            tipFadingIn = true;
            lastTipChangeTime = currentTime;
            lastTipCharTime = currentTime;
            lastTipUpdateTime = currentTime;
        }
        
        // Check if it's time to change the tip
        if (currentTime - lastTipChangeTime > TIP_DISPLAY_TIME) {
            currentTip = getRandomGameplayTip();
            displayedTip = "";
            tipCharIndex = 0;
            tipAlpha = 0;
            tipFadingIn = true;
            lastTipChangeTime = currentTime;
            lastTipCharTime = currentTime;
            lastTipUpdateTime = currentTime;
        }
        
        // Handle typing effect
        if (tipCharIndex < currentTip.length()) {
            if (currentTime - lastTipCharTime > TIP_CHAR_DELAY) {
                displayedTip += currentTip.charAt(tipCharIndex);
                tipCharIndex++;
                lastTipCharTime = currentTime;
            }
        }
        
        // Handle fade in/out effect (only after typing is complete)
        if (tipCharIndex == currentTip.length()) {
            if (currentTime - lastTipUpdateTime > 10) { // Update every 10ms for smooth fade
                if (tipFadingIn) {
                    tipAlpha += 5; // Faster fade in
                    if (tipAlpha >= 255) {
                        tipAlpha = 255;
                        tipFadingIn = false;
                        lastTipUpdateTime = currentTime;
                    }
                } else {
                    // Hold at full opacity for most of the time
                    long timeSinceFullOpacity = currentTime - lastTipUpdateTime;
                    long fadeOutStart = TIP_DISPLAY_TIME - TIP_FADE_SPEED;
                    
                    if (timeSinceFullOpacity > fadeOutStart) {
                        tipAlpha -= 2; // Faster fade out
                        if (tipAlpha <= 0) {
                            tipAlpha = 0;
                            // Start new tip
                            currentTip = getRandomGameplayTip();
                            displayedTip = "";
                            tipCharIndex = 0;
                            tipFadingIn = true;
                            lastTipChangeTime = currentTime;
                        }
                    }
                }
                lastTipUpdateTime = currentTime;
            }
        }
    }
    
    private String getRandomGameplayTip() {
        String[] tips = {
            // Shorter, easier-to-read tips
            "Explore every corner for hidden treasures!",
            "Different enemies have different weaknesses.",
            "Use ranged attacks against tough enemies.",
            "Save your game frequently at statues.",
            "Watch your health and use potions wisely.",
            
            // Control tips
            "Move: W, A, S, D",
            "Attack: ENTER",
            "Cast/Shoot: F",
            "Character Screen: C",
            "Pause: P",
            
            // Gameplay advice
            "Collect coins to buy better gear.",
            
            // Combat tips
            "Environmental objects can help in combat.",
            "Patience is key when facing bosses.",
            "Read ancient tombs for valuable clues.",
            "Conserve magic for challenging encounters.",
            "Some doors require special keys to open."
        };
        
        // Select random tip
        int randomIndex = (int)(Math.random() * tips.length);
        return tips[randomIndex];
    }
    
    public void setLoadingProgress(float progress) {
        this.loadingProgress = (int) progress;
    }
    public float getLoadingProgress() {
        return loadingProgress;
    }

    public void showMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }
    
    public void draw(Graphics2D g2) {
        
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);
        
        //title state
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        // PLAY STATE
        else if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
        }
        // PAUSE STATE
        else if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        // DIALOGUE STATE
        else if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
        // CHARACTER STATE
        else if (gp.gameState == gp.characterState) {
            drawPlayerLife();
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        // OPTIONS STATE
        else if (gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }
        // Game over STATE
        else if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        // TRANSITION STATE
        else if (gp.gameState == gp.transitionState) {
            drawTransitionScreen();
        }
        // Trade STATE
        else if (gp.gameState == gp.tradeState) {
            TradeScreen();
        }
        // Quest STATE
        else if (gp.gameState == gp.questState) {
            drawQuestScreen();
        }
        
        // In your draw method, add:
        else if (gp.gameState == gp.endingState) {
            drawEndigScreen();
        }
        
    }

    public void drawEndigScreen() {}
    
    public void drawPlayerLife() {
        // Add null check at the beginning
        if (gp.player == null) return;

        int x = gp.TileSize/2;
        int y = gp.TileSize/2;
        int i = 0;

        // Draw blank heart
        while (i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.TileSize;
        }

        x = gp.TileSize/2;
        y = gp.TileSize/2;
        i = 0;

        // Draw current life
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life){
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.TileSize;
        }

        // draw max mana
        x = (gp.TileSize/2)-5;
        y = (int) (gp.TileSize*1.5);
        i = 0;
        while (i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }
        //draw mana
        x = (gp.TileSize/2)-5;
        y = (int) (gp.TileSize*1.5);
        i = 0;
        while(i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }
    }
    
    public void drawMessage() {
        // Add null check
        if (gp.player == null) return;
        
        int messageX = gp.TileSize;
        int messageY = gp.TileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for(int i = 0; i < message.size(); i++) {

            if(message.get(i) != null) {
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2, messageY);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; //messagecounter++
                messageCounter.set(i, counter); // set the counter to the array
                messageY += 50;

                if (messageCounter.get(i) > 100) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public void drawTitleScreen() {
        // ============ DRAWING SECTION ============
        if (titleScreenState == 0) {
            // Draw main menu
            g2.setColor(new Color(0,0,0));
            g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
    
            // Title Name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));
            String text = "The Hunt: Lost Tomb of Cleopatra";
            int x = getXforCenteredText(text);
            int y = gp.TileSize*3;
    
            // shadow
            g2.setColor(Color.GRAY);
            g2.drawString(text, x+3, y+3);
            // Main Color
            g2.setColor(new Color(255, 215, 0));
            g2.drawString(text, x, y);
    
            // Logo / Character Preview - Simplified
            x = gp.ScreenWidth/2 - (gp.TileSize*2)/2;
            y += gp.TileSize*2;
                         
            // Menu options
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
    
            text = "New Game";
            x = getXforCenteredText(text);
            y += gp.TileSize;
            g2.drawString(text, x, y);
            if (commandNum == 0){
                g2.drawString(">", x-gp.TileSize, y);
            }
            
            text = "Load Game";
            x = getXforCenteredText(text);
            y += gp.TileSize*2;
            g2.drawString(text, x, y);
            if (commandNum == 1){
                g2.drawString(">", x-gp.TileSize, y);
                
                // Show available save slots when hovering
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN,20F));
                g2.setColor(Color.LIGHT_GRAY);
                
                int saveY = y + 40;
                boolean hasAnySave = false;
                
                for (int i = 0; i < 3; i++) {
                    String status = checkSaveSlotStatus(i);
                    if (!status.equals("EMPTY")) {
                        String saveInfo = "Slot " + (i+1) + ": " + status;
                        int saveX = getXforCenteredText(saveInfo);
                        g2.drawString(saveInfo, saveX, saveY);
                        saveY += 25;
                        hasAnySave = true;
                    }
                }
                
                if (!hasAnySave) {
                    String noSaveMsg = "No saved games found";
                    int msgX = getXforCenteredText(noSaveMsg);
                    g2.drawString(noSaveMsg, msgX, saveY);
                }
            }
            
            text = "Quit";
            x = getXforCenteredText(text);
            y += gp.TileSize*2;
            g2.drawString(text, x, y);
            if (commandNum == 2){
                g2.drawString(">", x-gp.TileSize, y);
            }
        }
        else if (titleScreenState == 1) {
            // Draw character selection (ONLY for New Game)
            drawCharacterSelectionScreen();
        }
        else if (titleScreenState == 2) {
            // Draw load game selection screen
            drawLoadGameScreen();
        }
    
        // ============ INPUT HANDLING SECTION ============
        
        // Handle main menu selection
        if (gp.keyH.enterPressed == true && titleScreenState == 0) {
            if (commandNum == 0) {
                // New Game - go to character selection
                titleScreenState = 1;
                commandNum = 0;
            }
            if (commandNum == 1) {
                // Load Game - go to load game selection screen
                titleScreenState = 2;
                commandNum = 0;
            }
            if (commandNum == 2) {
                // Quit
                System.exit(0);
            }
            gp.keyH.enterPressed = false;
        }
        
        // Handle character selection (for New Game only)
        if (gp.keyH.enterPressed == true && titleScreenState == 1) {
            handleCharacterSelection();
            gp.keyH.enterPressed = false;
        }
        
        // Handle load game selection
        if (gp.keyH.enterPressed == true && titleScreenState == 2) {
            handleLoadGameSelection();
            gp.keyH.enterPressed = false;
        }
    }
    
    public void drawCharacterSelectionScreen() {
        g2.setColor(new Color(0,0,0));
        g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
        
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
    
        String text = "Select Your Hunter";
        int x = getXforCenteredText(text);
        int y = gp.TileSize*3;
        g2.drawString(text, x, y);
    
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
        
        // Xylo option
        text = "Xylo";
        x = getXforCenteredText(text);
        y += gp.TileSize*3;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x-gp.TileSize, y);
        }
        
        // Alexandria option
        text = "Alexandria";
        x = getXforCenteredText(text);
        y += gp.TileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x-gp.TileSize, y);
        }
        
        // Back option
        text = "Back";
        x = getXforCenteredText(text);
        y += gp.TileSize*2;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x-gp.TileSize, y);
        }
    }

    private void handleCharacterSelection() {
        if (gp.gameState == gp.titleState && !gp.loadingManager.isLoading()) {
            if (commandNum == 0) {
                // Start new game with Xylo
                gp.loadingManager.startGameWithCharacter(0);
                gp.gameState = gp.transitionState;
                commandNum = 0;
            }
            else if (commandNum == 1) {
                // Start new game with Alexandria
                gp.loadingManager.startGameWithCharacter(1);
                gp.gameState = gp.transitionState;
                commandNum = 0;
            }
            else if (commandNum == 2) {
                // Back to main menu
                titleScreenState = 0;
                commandNum = 0;
            }
        }
    }

    public void drawLoadGameScreen() {
        g2.setColor(new Color(0,0,0));
        g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
        
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
        
        String text = "LOAD GAME";
        int x = getXforCenteredText(text);
        int y = gp.TileSize*2;
        g2.drawString(text, x, y);
        
        // Save slots
        y = gp.TileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
        
        String[] slotNames = {"LOAD 1", "LOAD 2", "LOAD 3", "BACK"};
        
        for (int i = 0; i < slotNames.length; i++) {
            text = slotNames[i];
            x = getXforCenteredText(text);
            
            // Set color based on save existence
            if (i < 3) {
                String status = checkSaveSlotStatus(i);
                if (status.equals("EMPTY")) {
                    g2.setColor(Color.DARK_GRAY);
                } else {
                    g2.setColor(Color.GREEN);
                }
            } else {
                g2.setColor(Color.WHITE);
            }
            
            g2.drawString(text, x, y);
            
            // Show save status below slot name
            if (i < 3) {
                String status = checkSaveSlotStatus(i);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
                g2.setColor(Color.LIGHT_GRAY);
                int statusX = getXforCenteredText(status);
                g2.drawString(status, statusX, y + 30);
                g2.setColor(Color.WHITE);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
            }
            
            // Draw cursor for selected slot
            if (commandNum == i) {
                g2.setColor(Color.WHITE);
                g2.drawString(">", x - 50, y);
            }
            
            y += gp.TileSize * 2;
        }
        
        // Draw instructions
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.LIGHT_GRAY);
        String instructions = "Use ↑/↓ to select, ENTER to load";
        int instX = getXforCenteredText(instructions);
        int instY = gp.ScreenHeight - 50;
        g2.drawString(instructions, instX, instY);
        
        // Reset color
        g2.setColor(Color.WHITE);
    }
    
    private void handleLoadGameSelection() {
        if (commandNum == 0) {
            // Load slot 1
            gp.saveLoad.load(0);
            afterLoad();
            showMessage("Game Loaded from Slot 1");
            gp.playMusic(0);
        }
        else if (commandNum == 1) {
            // Load slot 2
            gp.saveLoad.load(1);
            afterLoad();
            showMessage("Game Loaded from Slot 2");
            gp.playMusic(0);
        }
        else if (commandNum == 2) {
            // Load slot 3
            gp.saveLoad.load(2);
            afterLoad();
            showMessage("Game Loaded from Slot 3");
            gp.playMusic(0);
        }
        else if (commandNum == 3) {
            // Back to main menu
            titleScreenState = 0;
            commandNum = 1; // Select Load Game option
        }
    }

    private void afterLoad() {
        gp.gameState = gp.playState;
        titleScreenState = 0;
        commandNum = 0;
        
        // Reset assets for the loaded map
        if (gp.aSetter != null) {
            gp.aSetter.clearMapAssets(gp.currentMap);
            gp.aSetter.setObject(gp.currentMap);
            gp.aSetter.setNPC(gp.currentMap);
            gp.aSetter.setMonster(gp.currentMap);
            gp.aSetter.setInteractiveTile(gp.currentMap);
        }
        
        // Reset player state to ensure smooth gameplay
        if (gp.player != null) {
            gp.player.collisionOn = false;
            gp.player.attacking = false;
            gp.player.guarding = false;
            gp.player.knockBack = false;
            gp.player.invincible = false;
            gp.player.transparent = false;
            gp.player.Direction = "down";
            
            // Recalculate stats
            gp.player.attack = gp.player.getAttack();
            gp.player.defense = gp.player.getDefense();
            
            // Refresh images
            gp.player.getImage();
            gp.player.getAttackImage();
            gp.player.getGuardImage();
        }
    }

    private void saveGame(int slot) {
        gp.saveLoad.save(slot);
        showMessage("GAME SAVED TO SLOT " + (slot + 1) + "!");
    }

    private String checkSaveSlotStatus(int slot) {
        return gp.saveLoad.getSaveSlotStatus(slot);
    }
        

    // ============ DIALOGUE METHODS ============
    
    public void setDialogue(String[] pages) {
        
        this.dialoguePages = pages;
        this.currentPageIndex = 0;
        this.dialogueActive = true;
        
        if (pages != null && pages.length > 0) {
            setCurrentPage(pages[0]);
        }
    }
    
    public void setDialogue(String text) {
        setDialogue(new String[]{text});
    }
    
    private void setCurrentPage(String text) {
        
        this.targetDialogue = text;
        this.displayedChars = 0; // ← CHANGE THIS TO 0
        this.dialogueFinished = false;
        this.currentDialogue = ""; // Start with empty string
        this.lastCharTime = System.currentTimeMillis();
    }


    /**
     * Go to the next page of dialogue
     * @return true if there are more pages, false if dialogue ended
     */
    public boolean nextPage() {
        if (dialoguePages != null && currentPageIndex < dialoguePages.length - 1) {
            // Move to next page
            currentPageIndex++;
            setCurrentPage(dialoguePages[currentPageIndex]);
            return true; // There are more pages
        } else {
            // No more pages, end dialogue
            dialogueActive = false;
            return false; // Dialogue ended
        }
    }

    /**
     * Check if there's a next page
     */
    public boolean hasNextPage() {
        return dialoguePages != null && currentPageIndex < dialoguePages.length - 1;
    }

    public String getPageIndicator() {
        if (dialoguePages != null && dialoguePages.length > 1) {
            return (currentPageIndex + 1) + "/" + dialoguePages.length;
        }
        return "";
    }

    /**
     * Skip to the end of the current dialogue animation
     */
    public void skipToEnd() {
        if (targetDialogue != null) {
            displayedChars = targetDialogue.length();
            currentDialogue = targetDialogue;
            dialogueFinished = true;
        }
    }

    public boolean isDialogueFinished() {
        return dialogueFinished;
    }

    public void updateDialogueAnimation() {
        if (!dialogueFinished && targetDialogue != null && !targetDialogue.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            
            if (displayedChars < targetDialogue.length()) {
                if (currentTime - lastCharTime > CHAR_DELAY) {
                    displayedChars++;
                    currentDialogue = targetDialogue.substring(0, displayedChars);
                    lastCharTime = currentTime;
                    
                    if (displayedChars == targetDialogue.length()) {
                        dialogueFinished = true;
                    }
                }
            }
        }
    }

    /**
     * Draw the dialogue screen with multi-page support
     */
    // public void drawDialogueScreen() {
    //     // Add null check
    //     if (gp.player == null) return;
        
    //     // Update animation
    //     updateDialogueAnimation();
        
    //     // Window
    //     int x = gp.TileSize*2;
    //     int y = gp.TileSize/2;
    //     int width = gp.ScreenWidth - (gp.TileSize*4);
    //     int height = gp.TileSize*4;

    //     drawSubWindow(x, y, width, height);

    //     g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
    //     x += gp.TileSize;
    //     y += gp.TileSize;

    //     // Draw the animated dialogue
    //     if (currentDialogue != null && !currentDialogue.isEmpty()) {
    //         for (String line : currentDialogue.split("\n")) {
    //             g2.drawString(line, x, y);
    //             y += 40;
    //         }
    //     }
        
    //     // Draw page indicator if there are multiple pages
    //     if (dialoguePages != null && dialoguePages.length > 1) {
    //         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
    //         g2.setColor(new Color(255, 255, 255, 150));
    //         String pageInfo = getPageIndicator();
    //         int infoX = x + width - 100;
    //         int infoY = y + 10;
    //         g2.drawString(pageInfo, infoX, infoY);
    //         g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F)); // Reset font
    //     }
        
    //     // Draw a "next" indicator when dialogue is finished
    //     if (dialogueFinished) {
    //         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
    //         g2.setColor(new Color(255, 255, 255, 200));
    //         String nextIndicator;
    //         if (hasNextPage()) {
    //             nextIndicator = "▼ Press ENTER for next page";
    //         } else {
    //             nextIndicator = "▼ Press ENTER to continue";
    //         }
    //         int indicatorX = x + width - 250;
    //         int indicatorY = y + 10;
    //         g2.drawString(nextIndicator, indicatorX, indicatorY);
    //     }
    // }
    
    public void drawDialogueScreen() {

        // Safety check
        if (gp.player == null) return;
    
        // Update typewriter animation
        updateDialogueAnimation();
    
        // ================= WINDOW =================
        int x = gp.TileSize * 2;
        int y = gp.TileSize / 2;
        int width = gp.ScreenWidth - (gp.TileSize * 4);
        int height = gp.TileSize * 4;
    
        drawSubWindow(x, y, width, height);
    
        // ================= TEXT =================
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        int textX = x + gp.TileSize;
        int textY = y + gp.TileSize;
    
        // Draw animated dialogue text
        if (currentDialogue != null && !currentDialogue.isEmpty()) {
            for (String line : currentDialogue.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 40;
            }
        }
    
        // ================= PAGE INDICATOR =================
        if (dialoguePages != null && dialoguePages.length > 1) {
    
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            g2.setColor(new Color(255, 255, 255, 150));
    
            String pageInfo = getPageIndicator();
            int infoX = x + width - 120;
            int infoY = y + height - 20;
    
            g2.drawString(pageInfo, infoX, infoY);
    
            // reset font
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
            g2.setColor(Color.white);
        }
    
        // ================= NEXT INDICATOR =================
        if (dialogueFinished) {
    
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
            g2.setColor(new Color(255, 255, 255, 200));
    
            String nextIndicator;
    
            if (hasNextPage()) {
                nextIndicator = "▼ Press ENTER for next page";
            } else {
                nextIndicator = "▼ Press ENTER to continue";
            }
    
            int indicatorX = x + width - 260;
            int indicatorY = y + height - 45;
    
            g2.drawString(nextIndicator, indicatorX, indicatorY);
    
            // reset font
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
            g2.setColor(Color.white);
        }
    
        // =====================================================
        // ⚠️ IMPORTANT DESIGN RULE
        // UI DOES NOT CONTROL GAME FLOW
        // CutsceneManager and KeyHandler handle progression
        // =====================================================
    }

    // public void drawDialogueScreen() {

    //     // Safety check
    //     if (gp.player == null) return
    
    //     // Update typewriter animation
    //     updateDialogueAnimation();
    
    //     // ================= WINDOW =================
    //     int x = gp.TileSize * 2;
    //     int y = gp.TileSize / 2;
    //     int width = gp.ScreenWidth - (gp.TileSize * 4);
    //     int height = gp.TileSize * 4;
    
    //     drawSubWindow(x, y, width, height);
    
    //     // ================= TEXT =================
    //     g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
    //     g2.setColor(Color.white);
    //     int textX = x + gp.TileSize;
    //     int textY = y + gp.TileSize;
    
    //     // Draw animated dialogue text
    //     if (currentDialogue != null && !currentDialogue.isEmpty()) {
    //         // Split by newline to handle multi-line text
    //         String[] lines = currentDialogue.split("\n");
    //         for (String line : lines) {
    //             g2.drawString(line, textX, textY);
    //             textY += 40;
    //         }
            
    //         // ================= BLINKING CURSOR =================
    //         // Show blinking cursor at the end of text when animation is finished
    //         if (dialogueFinished) {
    //             long time = System.currentTimeMillis();
    //             if (time % 1000 < 500) { // Blink every 500ms
    //                 g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
    //                 g2.setColor(Color.YELLOW);
    //                 String cursor = "_";
    //                 // Get the last line's width for cursor positioning
    //                 String lastLine = lines[lines.length - 1];
    //                 int cursorX = textX + g2.getFontMetrics().stringWidth(lastLine);
    //                 int cursorY = textY - 40; // Position after last line
    //                 g2.drawString(cursor, cursorX, cursorY);
    //             }
    //         }
    //     } else {
    //         // Debug: Draw placeholder if no dialogue
    //         g2.drawString("...", textX, textY);
    //     }
    
    //     // ================= PAGE INDICATOR =================
    //     if (dialoguePages != null && dialoguePages.length > 1) {
    
    //         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
    //         g2.setColor(new Color(255, 255, 255, 150));
    
    //         String pageInfo = (currentPageIndex + 1) + "/" + dialoguePages.length;
    //         int infoX = x + width - 80;
    //         int infoY = y + height - 20;
    
    //         g2.drawString(pageInfo, infoX, infoY);
    
    //         // reset font
    //         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
    //         g2.setColor(Color.white);
    //     }
    
    //     // ================= NEXT INDICATOR =================
    //     if (dialogueFinished) {
    
    //         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
            
    //         // Pulsing effect for the indicator
    //         long time = System.currentTimeMillis();
    //         int alpha = (int)(Math.sin(time * 0.003) * 100 + 155); // Pulsing between 55-255
    //         alpha = Math.min(255, Math.max(55, alpha));
    //         g2.setColor(new Color(255, 255, 255, alpha));
    
    //         String nextIndicator;
    
    //         if (hasNextPage()) {
    //             nextIndicator = "▼ Press ENTER for next page";
    //         } else {
    //             nextIndicator = "▼ Press ENTER to continue";
    //         }
    
    //         int indicatorX = x + width - 260;
    //         int indicatorY = y + height - 45;
    
    //         g2.drawString(nextIndicator, indicatorX, indicatorY);
    
    //         // Draw a second smaller arrow for emphasis
    //         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16F));
    //         g2.setColor(new Color(255, 255, 100, alpha - 50));
    //         g2.drawString("▼", indicatorX - 25, indicatorY - 2);
    
    //         // reset font
    //         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
    //         g2.setColor(Color.white);
    //     }
    
    //     // ================= DEBUG INFO =================
    //     // Debug info (remove after fixing)
    //     g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16F));
    //     g2.setColor(Color.YELLOW);
    //     g2.drawString("Page: " + (currentPageIndex + 1) + "/" + 
    //                   (dialoguePages != null ? dialoguePages.length : 0), 
    //                   x + 20, y + height - 60);
        
    //     // Draw a small "PRESS ENTER" reminder at the bottom
    //     if (!dialogueFinished) {
    //         g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
    //         g2.setColor(new Color(200, 200, 200, 150));
    //         g2.drawString("(ENTER to skip)", x + width - 150, y + height - 10);
    //     }
    // }
    
    public void drawCharacterScreen() {
        // Add null check
        if (gp.player == null) return;

        // Create a Name
        final int frameX = gp.TileSize;
        final int frameY = gp.TileSize-10;
        final int framewidth = gp.TileSize*5;
        final int frameHeight = gp.TileSize*11;
        drawSubWindow(frameX, frameY, framewidth, frameHeight);

        //text 
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));

        int textX = frameX + 20;
        int textY = frameY  + gp.TileSize;
        final int lineHeight = 35;

        // Name
        g2.drawString("Level", textX, textY); textY += lineHeight;
        g2.drawString("Life", textX, textY); textY += lineHeight;
        // g2.drawString("Mana", textX, textY); textY += lineHeight;
        g2.drawString("Arrows", textX, textY); textY += lineHeight;
        g2.drawString("Strength", textX, textY); textY += lineHeight; // Fixed typo
        g2.drawString("Dexterity", textX, textY); textY += lineHeight;
        g2.drawString("Attack", textX, textY); textY += lineHeight;
        g2.drawString("Defense", textX, textY); textY += lineHeight;
        g2.drawString("Exp", textX, textY); textY += lineHeight;
        g2.drawString("Next Level", textX, textY); textY += lineHeight;
        g2.drawString("Key", textX, textY); textY += lineHeight;
        g2.drawString("Coin", textX, textY); textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY); textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY); textY += lineHeight;

        // Values
        int tailX = (frameX + framewidth) - 30;
        // Reset textY
        textY = frameY + gp.TileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.arrow);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.hasKey);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);

        if (gp.player.currentweapon != null) {
            g2.drawImage(gp.player.currentweapon.down1, tailX - gp.TileSize, textY+14, null);
        }
        textY += gp.TileSize;
        if (gp.player.currentShield != null) {
            g2.drawImage(gp.player.currentShield.down1, tailX - gp.TileSize, textY+14, null);
        }
    }
    
    public void drawInventory(Entity entity, boolean cursor) {
        // Add null check
        if (entity == null) return;

        //Frame
        int frameX = 0;
        int frameY = 0;
        int framewidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;

        if (entity == gp.player) {
            frameX = gp.TileSize*9;
            frameY = gp.TileSize;
            framewidth = gp.TileSize*6;
            frameHeight = gp.TileSize*5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        }
        else {
            frameX = gp.TileSize*2;
            frameY = gp.TileSize;
            framewidth = gp.TileSize*6;
            frameHeight = gp.TileSize*5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }

        // Draw frame
        drawSubWindow(frameX, frameY, framewidth, frameHeight);

        // sloth
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.TileSize+3;

        // draw entity item
        for(int i = 0; i < entity.inventory.size(); i++){

            // equip cursor
            if(entity.inventory.get(i) == entity.currentweapon  ||
                entity.inventory.get(i) == entity.currentShield || 
                entity.inventory.get(i) == entity.currentRange) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.TileSize, gp.TileSize, 10, 10);
            }
            
            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);

            // display ammounts 
            if (entity.inventory.get(i).amount > 1 ) {

                g2.setFont(g2.getFont().deriveFont(28f));
                int amountX = slotX + gp.TileSize - 20;
                int amountY = slotY + gp.TileSize - 10;

                String s = "" + entity.inventory.get(i).amount;
                amountX = getXforAlignToRightText(s, slotX + 44);
                amountY = slotY + gp.TileSize;

                // shadow
                g2.setColor(new Color(60, 60, 60));
                g2.drawString(s, amountX, amountY);

                //numbers
                g2.setColor(Color.white);  
                g2.drawString(s, amountX-3, amountY-3);
            }

            slotX += slotSize;

            if(i == 4 || i == 9 || i == 14) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        //Cursor
        if (cursor == true)  {
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            int cursorWidth = gp.TileSize;
            int cursorHeight = gp.TileSize; 
            // draw cursor
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

            if (cursor && itemIndex < entity.inventory.size()) {

                // description frame
                int dframeX = frameX;
                int dframeY = frameY + frameHeight;
                int dframeWidth = framewidth;
                int dframeHeight = gp.TileSize*3;
                drawSubWindow(dframeX, dframeY, dframeWidth, dframeHeight);

                int textX = dframeX + 20;
                int textY = dframeY + gp.TileSize;
                g2.setFont(g2.getFont().deriveFont(28f));

                Entity item = entity.inventory.get(itemIndex);

                // extra safety
                if (item.description != null && !item.description.isEmpty()) {
                    for (String line : item.description.split("\n")) {
                        g2.drawString(line, textX, textY);
                        textY += 32;
                    }
                }
            }
        }
    }
    
    public void drawGameOverScreen() {
        
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);

        int x;
        int y;
        String text;g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110));

        text = "Game Over";
        // Shadow
        g2.setColor(Color.BLACK);
        x = getXforCenteredText(text);
        y = gp.TileSize*4;
        // Main
        g2.setColor(Color.WHITE);
        g2.drawString(text, x-4, y-4);

        // Retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.TileSize*4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x-40, y);
        }

        // Back to title screen
        text = "Quit";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x-40, y);
        }
    }
    
    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));
        
        // Just half a tile wider than original
        int frameWidth = (int)(gp.TileSize * 8.5); // 8.5 tiles wide
        int frameHeight = gp.TileSize * 10;
        int frameX = gp.TileSize * 4; // Adjusted slightly for centering
        int frameY = gp.TileSize;
        
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        switch (subState) {
            case 0: options_top(frameX, frameY); break;
            case 1: option_save(frameX, frameY); break;
            case 2: option_control(frameX, frameY); break;
            case 3: option_endGameConfirmation(frameX, frameY); break;
        }
    }
    
    public void options_top(int frameX, int frameY) {
        int textX;
        int textY;
        
        // Title
        String text = "Options";    
        textX = getXforCenteredText(text);
        textY = frameY + gp.TileSize;
        g2.drawString(text, textX, textY);
        
        // Menu items
        textX = frameX + gp.TileSize;
        textY += gp.TileSize * 2;
        
        // Save
        g2.drawString("Save", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                subState = 1;
                commandNum = 0;
                gp.keyH.enterPressed = false;
            }
        }
        
        // Music
        textY += gp.TileSize;
        g2.drawString("Music", textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            // Volume control with arrow keys
            if (gp.keyH.leftPressed) {
                gp.music.volumeScale--;
                gp.music.volumeScale = Math.max(gp.music.volumeScale, 0);
                gp.music.checkVolume();
                gp.keyH.leftPressed = false;
            }
            if (gp.keyH.rightPressed) {
                gp.music.volumeScale++;
                gp.music.volumeScale = Math.min(gp.music.volumeScale, 5);
                gp.music.checkVolume();
                gp.keyH.rightPressed = false;
            }
        }
        
        // Sound Effect
        textY += gp.TileSize;
        g2.drawString("SE", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX - 25, textY);
            // Volume control with arrow keys
            if (gp.keyH.leftPressed) {
                gp.se.volumeScale--;
                gp.se.volumeScale = Math.max(gp.se.volumeScale, 0);
                gp.keyH.leftPressed = false;
            }
            if (gp.keyH.rightPressed) {
                gp.se.volumeScale++;
                gp.se.volumeScale = Math.min(gp.se.volumeScale, 5);
                gp.keyH.rightPressed = false;
            }
        }
        
        // Control 
        textY += gp.TileSize;
        g2.drawString("Control", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                subState = 2;
                commandNum = 0;
                gp.keyH.enterPressed = false;
            }
        }
        
        // Quit Game
        textY += gp.TileSize;
        g2.drawString("Quit Game", textX, textY);
        if (commandNum == 4) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                subState = 3;
                commandNum = 0;
                gp.keyH.enterPressed = false;
            }
        }
        
        // Back
        textY += gp.TileSize * 2;
        g2.drawString("Back", textX, textY);
        if (commandNum == 5) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                // Return to play state
                gp.gameState = gp.playState;
                commandNum = 0;
                subState = 0;
                gp.keyH.enterPressed = false;
            }
        }
        
        // Draw volume bars
        int volumeBarX = frameX + gp.TileSize * 5;
        int volumeBarY = frameY + gp.TileSize * 3 + 24;
        
        // Music volume bar
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(volumeBarX, volumeBarY, 100, 20);
        if (gp.music != null) {
            int musicFillWidth = 20 * gp.music.volumeScale; // 0-5 scale * 20px
            g2.fillRect(volumeBarX, volumeBarY, musicFillWidth, 20);
        }
        
        // SE volume bar
        volumeBarY += gp.TileSize;
        g2.drawRect(volumeBarX, volumeBarY, 100, 20);
        if (gp.se != null) {
            int seFillWidth = 20 * gp.se.volumeScale; // 0-5 scale * 20px
            g2.fillRect(volumeBarX, volumeBarY, seFillWidth, 20);
        }
        
        // Save config
        if (gp.config != null) {
            gp.config.saveConfig();
        }
    }
    
    public void option_save(int frameX, int frameY) {
        int textX;
        int textY;
        
        // Title
        String text = "SAVE";
        textX = getXforCenteredText(text);
        textY = frameY + gp.TileSize + 20;
        g2.drawString(text, textX, textY);
        
        // Save slots
        textY = frameY + gp.TileSize * 3;
        
        String[] slotNames = {"SAVE 1", "SAVE 2", "SAVE 3", "BACK"};
        
        for (int i = 0; i < slotNames.length; i++) {
            textX = getXforCenteredText(slotNames[i]);
            
            // Add save status indicator
            if (i < 3) {
                String status = checkSaveSlotStatus(i);
                g2.setFont(g2.getFont().deriveFont(20f)); // Smaller font for status
                g2.setColor(Color.LIGHT_GRAY);
                
                // Draw status text
                int statusX = textX - 150; // Position status to the left of slot name
                g2.drawString(status, statusX, textY + 15);
                
                g2.setFont(g2.getFont().deriveFont(32f)); // Reset to normal font
                g2.setColor(Color.WHITE);
            }
            
            g2.drawString(slotNames[i], textX, textY);
            
            if (commandNum == i) {
                g2.drawString(">", textX - 25, textY);
                if (gp.keyH.enterPressed == true) {
                    if (i < 3) {
                        // Save to slot i
                        saveGame(i);
                        showMessage("GAME SAVED TO SLOT " + (i + 1) + "!");
                    } else {
                        // Back
                        subState = 0;
                        commandNum = 0;
                    }
                    gp.keyH.enterPressed = false;
                }
            }
            textY += gp.TileSize * 1.5;
        }
    }
    
    public void option_control(int frameX, int frameY) {
        int textX;
        int textY;
        
        int frameWidth = (int)(gp.TileSize * 8.5);
        
        // Title
        String text = "CONTROLS";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36f));
        textX = frameX + (frameWidth / 2) - (int)(g2.getFontMetrics().getStringBounds(text, g2).getWidth() / 2);
        textY = frameY + gp.TileSize;
        g2.drawString(text, textX, textY);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24f));
        
        // Control pairs
        String[][] controls = {
            {"Move", "W/A/S/D"},
            {"Attack", "ENTER"},
            {"Ranged", "F"},
            {"Character", "C"},
            {"Quest Log", "Q"},
            {"Pause", "P"},
            {"Options", "ESC"},
            {"Interact", "ENTER (near)"},
            {"Guard", "SPACE"}
        };
        
        textX = frameX + gp.TileSize;
        textY = frameY + gp.TileSize * 2;
        
        for (String[] pair : controls) {
            // Action (left)
            g2.drawString(pair[0], textX, textY);
            // Key (right)
            g2.drawString(pair[1], frameX + gp.TileSize * 5, textY);
            textY += 35;
        }
        
        // Back button
        textY = frameY + gp.TileSize * 9;
        text = "Back";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
        g2.setColor(new Color(255, 215, 0));
        textX = frameX + (frameWidth / 2) - (int)(g2.getFontMetrics().getStringBounds(text, g2).getWidth() / 2);
        g2.drawString(text, textX, textY);
        
        if (commandNum == 0) {
            g2.setColor(Color.WHITE);
            g2.drawString(">", textX - 30, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 3;
                gp.keyH.enterPressed = false;
                gp.playSE(9);
            }
        }
    }

    public void option_endGameConfirmation(int frameX, int frameY) {
        int textX;
        int textY;
        
        int frameWidth = (int)(gp.TileSize * 8.5);
        int frameHeight = gp.TileSize * 10; // Get from options screen
        
        // Question - centered within the window
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
        String question = "Quit to Title?";
        textX = frameX + (frameWidth / 2) - (g2.getFontMetrics().stringWidth(question) / 2);
        textY = frameY + gp.TileSize * 3;
        g2.setColor(Color.WHITE);
        g2.drawString(question, textX, textY);
        
        // YES option
        textY += gp.TileSize * 2;
        String yesText = "YES";
        textX = frameX + (frameWidth / 2) - (g2.getFontMetrics().stringWidth(yesText) / 2);
        
        if (commandNum == 0) {
            g2.setColor(new Color(255, 215, 0));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
            g2.drawString(">", textX - 30, textY);
        } else {
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28f));
        }
        g2.drawString(yesText, textX, textY);
        
        // NO option
        textY += 50;
        String noText = "NO";
        textX = frameX + (frameWidth / 2) - (g2.getFontMetrics().stringWidth(noText) / 2);
        
        if (commandNum == 1) {
            g2.setColor(new Color(255, 215, 0));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
            g2.drawString(">", textX - 30, textY);
        } else {
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28f));
        }
        g2.drawString(noText, textX, textY);
        
        // Handle input
        if (commandNum == 0 || commandNum == 1) {
            if (gp.keyH.enterPressed == true) {
                if (commandNum == 0) {
                    // Yes - quit to title
                    gp.gameState = gp.titleState;
                    gp.resetGame(true);
                    gp.music.stop();
                } else {
                    // No - back to options
                    subState = 0;
                    commandNum = 4;
                }
                gp.keyH.enterPressed = false;
                gp.playSE(9);
            }
        }
        
        // Draw navigation hint
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 16f));
        g2.setColor(new Color(150, 150, 150));
        String hint = "Use ↑/↓ to select, ENTER to confirm";
        int hintX = frameX + (frameWidth / 2) - (g2.getFontMetrics().stringWidth(hint) / 2);
        int hintY = frameY + frameHeight - 40;
        g2.drawString(hint, hintX, hintY);
    }

    // public void option_endGameConfirmation(int frameX, int frameY) {
    //     int textX;
    //     int textY;
        
    //     // Shorter question to fit better
    //     textX = getXforCenteredText("Quit to Title?");
    //     textY = frameY + gp.TileSize * 3;
    //     g2.drawString("Quit to Title?", textX, textY);
        
    //     // Yes/No options
    //     String[] options = {"YES", "NO"};
    //     textY += gp.TileSize * 2;
        
    //     for (int i = 0; i < options.length; i++) {
    //         textX = getXforCenteredText(options[i]);
    //         g2.drawString(options[i], textX, textY);
            
    //         if (commandNum == i) {
    //             g2.drawString(">", textX - 25, textY);
    //             if (gp.keyH.enterPressed == true) {
    //                 if (i == 0) {
    //                     // Yes - quit to title
    //                     gp.gameState = gp.titleState;
    //                     gp.resetGame(true);
    //                     gp.music.stop();
    //                 } else {
    //                     // No - back to options
    //                     subState = 0;
    //                     commandNum = 4; // Select Quit Game item
    //                 }
    //                 gp.keyH.enterPressed = false;
    //             }
    //         }
    //         textY += gp.TileSize;
    //     }
    // }
    
    public void drawTransitionScreen() {
        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
    
        if (counter == 50) {
            counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            
            // FIX: Use tempColFloat instead of tempCol (which doesn't exist)
            if (gp.player != null) {
                gp.player.worldX = (int)(gp.eHandler.tempColFloat * gp.TileSize);
                gp.player.worldY = gp.eHandler.tempRow * gp.TileSize;
                
                gp.eHandler.previouseEventX = gp.player.worldX;
                gp.eHandler.previouseEventY = gp.player.worldY;
            }
        }
    }   
    
    public void TradeScreen() {
        switch (subState) {
           
            case 0: trade_select(); break;
            case 1: trade_buy(); break;
            case 2: trade_sell(); break;
        }
        // gp.keyH.enterPressed = false;
    }
    
    public void trade_select() {
        drawDialogueScreen();
        // draw window
        int x = gp.TileSize*10;
        int y = gp.TileSize*5;
        int width = gp.TileSize*3;
        int height = (int) (gp.TileSize*3.5);
        drawSubWindow(x, y, width, height);
        
        // draw text
        x += gp.TileSize;
        y += gp.TileSize;
        g2.drawString("Buy", x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - (gp.TileSize-17), y);
            if (gp.keyH.enterPressed == true) {
                subState = 1;
                gp.keyH.enterPressed = false;  // Reset only after changing state
            }
        }
        y += gp.TileSize;
        g2.drawString("sell", x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - (gp.TileSize-17), y);
            if (gp.keyH.enterPressed == true) {
                subState = 2;
                gp.keyH.enterPressed = false;  // Reset only after changing state
            }
        }
        y += gp.TileSize;
        
        g2.drawString("leave", x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - (gp.TileSize-17), y);
            if (gp.keyH.enterPressed == true) {
                commandNum = 0;
                gp.gameState = gp.dialogueState;
                currentDialogue = "Thank you for your visit!";
                gp.keyH.enterPressed = false;  // Reset only after changing state
            }
        }
    }

    public void trade_buy() {
        // Add null check
        if (gp.player == null || npc == null) return;
        
        // draw players inventory
        drawInventory(gp.player, false);
        // draw npc inventory
        drawInventory(npc, true);
        
        // draw hint window
        int x = gp.TileSize*2;
        int y = gp.TileSize*9;
        int width = gp.TileSize*6;
        int height = gp.TileSize*2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[Esc] Back", x+20, y+40);
        
        // draw player coin window
        x = gp.TileSize*9;
        y = gp.TileSize*9;
        width = gp.TileSize*6;
        height = gp.TileSize*2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your coin: " + gp.player.coin, x+20, y+40);
        
        // draw price
        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if (itemIndex < npc.inventory.size()) {
            
            x = (int) (gp.TileSize*5.5);
            y = (int) (gp.TileSize*5.5);
            width = (int) (gp.TileSize*2.5);
            height = gp.TileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x+10, y+10, 32, 32, null);
            
            int price = npc.inventory.get(itemIndex).price;
            String text = "" + price;
            x = getXforAlignToRightText(text, gp.TileSize*8 - 30);
            g2.drawString(text, x, y+35);
            
            // Check for Enter press to buy
            if (gp.keyH.enterPressed == true) {
                if (price > gp.player.coin) {
                    subState = 0;  // Go back to main trade menu
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You don't have enough coin!";
                    gp.keyH.enterPressed = false;  // Reset only after processing
                }
                else if (gp.player.inventory.size() == gp.player.maxInventorySize) {
                    subState = 0; // Go back to main trade menu
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "Your inventory is full!";
                    gp.keyH.enterPressed = false;  // Reset only after processing
                }
                else {
                        
                    gp.player.coin -= price;
                    gp.player.inventory.add(npc.inventory.get(itemIndex));
                    showMessage("Purchased: " + npc.inventory.get(itemIndex).name);
                    subState = 0;  // Go back to main trade menu
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You bought " + npc.inventory.get(itemIndex).name + "!";
                    gp.playSE(1); // Play purchase sound
                    gp.keyH.enterPressed = false;  // Reset only after processing
                }
            }
        }
    }

    
    public void trade_sell() {
        // Add null check
        if (gp.player == null) return;
        
        // draw players inventory
        drawInventory(gp.player, true);
        // draw hint window
        int x;
        int y;
        int width;
        int height;

        // draw hint window
        x = gp.TileSize*2;
        y = gp.TileSize*9;
        width = gp.TileSize*6;
        height = gp.TileSize*2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[Esc] Back", x+20, y+40);
        
        // draw player coin window
        x = gp.TileSize*9;
        y = gp.TileSize*9;
        width = gp.TileSize*6;
        height = gp.TileSize*2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your coin: " + gp.player.coin, x+20, y+40);
        
        // draw price
        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if (itemIndex < gp.player.inventory.size()) {
            
            x = (int) (gp.TileSize*12.5);
            y = (int) (gp.TileSize*5.5);
            width = (int) (gp.TileSize*2.5);
            height = gp.TileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x+10, y+10, 32, 32, null);
            
            int price = gp.player.inventory.get(itemIndex).price/2;
            String text = "" + price;
            x = getXforAlignToRightText(text, gp.TileSize*15 - 30);
            g2.drawString(text, x, y+35);
            
            // Check for Enter press to buy
            if (gp.keyH.enterPressed == true) {
                
                if (gp.player.inventory.get(itemIndex) == gp.player.currentweapon ||
                    gp.player.inventory.get(itemIndex) == gp.player.currentShield || 
                    gp.player.inventory.get(itemIndex) == gp.player.currentRange) {
                    
                    subState = 0;  // Go back to main trade menu
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You cannot sell an equipped item!";
                    gp.keyH.enterPressed = false;  // Reset only after processing
                }
                else {
                    gp.player.coin += price;
                    String itemName = gp.player.inventory.get(itemIndex).name;
                    gp.player.inventory.remove(itemIndex);
                    showMessage("Sold: " + itemName);
                    subState = 0;  // Go back to main trade menu
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You sold " + itemName + "!";
                    gp.playSE(2); // Play sell sound
                    gp.keyH.enterPressed = false;  // Reset only after processing
                }
            }
        }
    } 

    public void drawQuestScreen() {
        // Add null check
        if (gp.player == null) return;
        
        // Page tracking variables
        // private int questPage = 0;
        // private int maxQuestPages = 2; // 0=Main Quests, 1=Pyramid Details, 2=Boss/Lost Tomb
        
        // Smaller frame - center of screen
        int frameX = gp.TileSize * 3;
        int frameY = gp.TileSize;
        int frameWidth = gp.ScreenWidth - (gp.TileSize * 6);
        int frameHeight = gp.ScreenHeight - (gp.TileSize * 2);
        
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(28f));
    
        int textX = frameX + gp.TileSize;
        int textY = frameY + gp.TileSize + 10;
    
        // Draw title with page indicator
        if (maxQuestPages > 0) {
            g2.drawString("QUEST LOG  " + (questPage + 1) + "/" + (maxQuestPages + 1), textX, textY);
        } else {
            g2.drawString("QUEST LOG", textX, textY);
        }
        textY += 45;
    
        // ===== PAGE 0: MAIN QUESTS (Vhong, Beverly, Ding) =====
        if (questPage == 0) {
            // ===== SLIME QUEST (VHONG) =====
            if (gp.questProgress >= 0) {
                // Quest title
                g2.setFont(g2.getFont().deriveFont(24f));
                if (gp.questProgress >= 2) {
                    g2.setColor(new Color(100, 255, 100));
                    g2.drawString("✓ 1. Slime Hunt (Vhong)", textX, textY);
                } else {
                    g2.setColor(Color.white);
                    g2.drawString("1. Slime Hunt (Vhong)", textX, textY);
                }
                textY += 30;
                
                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20f));
                
                if (gp.questProgress == 0) {
                    g2.drawString("   ▶ Talk to Vhong in the village", textX + 15, textY);
                    textY += 25;
                    g2.setColor(Color.yellow);
                    g2.drawString("   ! Vhong is waiting", textX + 15, textY);
                    textY += 25;
                }
                else if (gp.questProgress == 1) {
                    if (gp.player.killCount < 3) {
                        g2.drawString("   ▶ Kill Slimes: " + gp.player.killCount + "/3", textX + 15, textY);
                        textY += 25;
                        drawProgressBar(textX + 15, textY - 12, 180, 15, (gp.player.killCount * 100) / 3);
                    } else {
                        g2.setColor(new Color(255, 255, 100));
                        g2.drawString("   ✓ Slimes: COMPLETED", textX + 15, textY);
                        textY += 25;
                        g2.setColor(Color.yellow);
                        g2.drawString("   ! Return to Vhong for tablet", textX + 15, textY);
                    }
                    textY += 25;
                }
                else if (gp.questProgress >= 2) {
                    g2.setColor(new Color(150, 255, 150));
                    g2.drawString("   ✓ Slimes: COMPLETED", textX + 15, textY);
                    textY += 25;
                    if (gp.questProgress == 2) {
                        g2.setColor(Color.cyan);
                        g2.drawString("   [TABLET] Received - East passage open", textX + 15, textY);
                    }
                    textY += 25;
                }
            
                textY += 10;
                g2.setColor(Color.white);
            }
    
            // ===== SNAKE QUEST (BEVERLY) =====
            if (gp.questProgress >= 2) {
                g2.setFont(g2.getFont().deriveFont(24f));
                if (gp.questProgress >= 4) {
                    g2.setColor(new Color(100, 255, 100));
                    g2.drawString("✓ 2. Snake Hunt (Beverly)", textX, textY);
                } else {
                    g2.setColor(Color.white);
                    g2.drawString("2. Snake Hunt (Beverly)", textX, textY);
                }
                textY += 30;
                
                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20f));
                
                if (gp.questProgress == 2) {
                    g2.drawString("   ▶ Talk to Beverly in eastern desert", textX + 15, textY);
                    textY += 25;
                    g2.setColor(Color.yellow);
                    g2.drawString("   ! Beverly is waiting", textX + 15, textY);
                    textY += 25;
                }
                else if (gp.questProgress == 3) {
                    if (gp.player.killCount < 3) {
                        g2.drawString("   ▶ Kill Snakes: " + gp.player.killCount + "/3", textX + 15, textY);
                        textY += 25;
                        drawProgressBar(textX + 15, textY - 12, 180, 15, (gp.player.killCount * 100) / 3);
                    } else {
                        g2.setColor(new Color(255, 255, 100));
                        g2.drawString("   ✓ Snakes: COMPLETED", textX + 15, textY);
                        textY += 25;
                        g2.setColor(Color.yellow);
                        g2.drawString("   ! Return to Beverly for SNAKE KEY", textX + 15, textY);
                    }
                    textY += 25;
                }
                else if (gp.questProgress >= 4) {
                    g2.setColor(new Color(150, 255, 150));
                    g2.drawString("   ✓ Snakes: COMPLETED", textX + 15, textY);
                    textY += 25;
                    
                    if (gp.questProgress == 4 && gp.player.hasKey == 0) {
                        g2.setColor(Color.cyan);
                        g2.drawString("   🔑 Find SNAKE KEY in desert (7,10)", textX + 15, textY);
                    } else if (gp.player.hasKey == 1) {
                        g2.setColor(Color.green);
                        g2.drawString("   🔑 SNAKE KEY OBTAINED", textX + 15, textY);
                    }
                    textY += 25;
                }
            
                textY += 10;
                g2.setColor(Color.white);
            }
    
            // ===== FINAL CHALLENGE (DING) =====
            if (gp.questProgress >= 3) {
                g2.setFont(g2.getFont().deriveFont(24f));
                if (gp.questProgress >= 5) {
                    g2.setColor(new Color(100, 255, 100));
                    g2.drawString("✓ 3. Final Challenge (Ding)", textX, textY);
                } else {
                    g2.setColor(Color.white);
                    g2.drawString("3. Final Challenge (Ding)", textX, textY);
                }
                textY += 30;
                
                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20f));
                
                if (gp.questProgress == 3) {
                    int totalKills = (gp.player != null) ? gp.player.killCount : 0;
                    if (totalKills < 6) {
                        g2.drawString("   ▶ Talk to Ding in northern ruins", textX + 15, textY);
                        textY += 25;
                        g2.drawString("   ▶ Monster Kills: " + totalKills + "/6", textX + 15, textY);
                        textY += 25;
                        drawProgressBar(textX + 15, textY - 12, 180, 15, (totalKills * 100) / 6);
                    } else {
                        g2.setColor(new Color(255, 255, 100));
                        g2.drawString("   ✓ Monster Kills: " + totalKills + "/6", textX + 15, textY);
                        textY += 25;
                        g2.drawString("   ! Return to Ding for PYRAMID KEY", textX + 15, textY);
                    }
                    textY += 25;
                }
                else if (gp.questProgress == 4) {
                    g2.setColor(new Color(150, 255, 150));
                    g2.drawString("   ✓ Trials: COMPLETED", textX + 15, textY);
                    textY += 25;
                    
                    if (gp.player.hasKey == 1) {
                        g2.setColor(Color.cyan);
                        g2.drawString("   🔑 Find PYRAMID KEY northwest (map1,10,8)", textX + 15, textY);
                    } else if (gp.player.hasKey == 2) {
                        g2.setColor(Color.green);
                        g2.drawString("   🔑 PYRAMID KEY OBTAINED", textX + 15, textY);
                        textY += 25;
                        g2.setColor(Color.orange);
                        g2.drawString("   ⬆ Sail to pyramid for final quest!", textX + 15, textY);
                    }
                    textY += 25;
                }
            }
        }
        
        // ===== PAGE 1: PYRAMID & SAILOR =====
        else if (questPage == 1) {
            g2.setFont(g2.getFont().deriveFont(24f));
            g2.setColor(new Color(255, 215, 0));
            g2.drawString("THE PYRAMID", textX, textY);
            textY += 35;
            
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(20f));
            
            // Show Sailor's role based on progress
            g2.drawString("Sailor's Ship:", textX + 10, textY);
            textY += 25;
            
            if (gp.questProgress < 4) {
                g2.drawString("   ⚓ Locked - Complete quests first", textX + 20, textY);
                textY += 25;
                g2.drawString("   Requirements:", textX + 20, textY);
                textY += 25;
                g2.drawString("   • Snake key from Beverly", textX + 30, textY);
                textY += 22;
                g2.drawString("   • Pyramid key from Ding", textX + 30, textY);
            }
            else if (gp.player.hasKey == 1) {
                g2.setColor(Color.cyan);
                g2.drawString("   ⚓ Has Snake Key", textX + 20, textY);
                textY += 30;
                g2.setColor(Color.white);
                g2.drawString("   Need Pyramid Key to enter pyramid!", textX + 20, textY);
                textY += 25;
                g2.drawString("   Find it in northern ruins (map 1)", textX + 20, textY);
            }
            else if (gp.player.hasKey == 2) {
                g2.setColor(Color.green);
                g2.drawString("   ⚓ HAS PYRAMID KEY!", textX + 20, textY);
                textY += 30;
                g2.setColor(Color.orange);
                g2.drawString("   ✓ Pyramid entrance unlocked!", textX + 20, textY);
                textY += 25;
                g2.drawString("   Talk to Sailor to sail to pyramid", textX + 20, textY);
            }
            
            textY += 30;
            
            // Pyramid status
            if (gp.currentMap == 2) {
                g2.setColor(new Color(255, 200, 0));
                g2.drawString("★ YOU ARE INSIDE THE PYRAMID ★", textX + 10, textY);
                textY += 30;
            } else if (gp.player.hasKey == 2 && gp.questProgress < 5) {
                g2.drawString("Pyramid Status: UNLOCKED - Ready to enter", textX + 10, textY);
            } else if (gp.questProgress >= 5) {
                g2.drawString("Pyramid Status: CLEARED", textX + 10, textY);
            }
        }
        
        // ===== PAGE 2: BOSS & LOST TOMB =====
        else if (questPage == 2) {
            g2.setFont(g2.getFont().deriveFont(24f));
            g2.setColor(new Color(255, 215, 0));
            g2.drawString("LOST TOMB", textX, textY);
            textY += 35;
            
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(20f));
            
            if (gp.questProgress < 5) {
                if (gp.player.hasKey == 2) {
                    g2.drawString("⚠ BOSS CHAMBER AWAITS!", textX + 10, textY);
                    textY += 35;
                    g2.drawString("Deep within the pyramid lies", textX + 10, textY);
                    textY += 25;
                    g2.drawString("the ANCIENT GUARDIAN.", textX + 10, textY);
                    textY += 35;
                    g2.setColor(Color.red);
                    g2.drawString("Defeat the boss to claim:", textX + 10, textY);
                    textY += 30;
                    g2.setColor(Color.orange);
                    g2.drawString("• The Lost Tomb's Treasure", textX + 25, textY);
                    textY += 25;
                    g2.drawString("• Eternal Glory", textX + 25, textY);
                    textY += 25;
                    g2.drawString("• Legend Status", textX + 25, textY);
                } else {
                    g2.drawString("???", textX + 10, textY);
                    textY += 30;
                    g2.drawString("Find the pyramid key to", textX + 10, textY);
                    textY += 25;
                    g2.drawString("learn more about the lost tomb.", textX + 10, textY);
                }
            } else {
                g2.setColor(new Color(255, 255, 100));
                g2.drawString("🏆 BOSS DEFEATED! 🏆", textX + 10, textY);
                textY += 40;
                g2.setColor(new Color(100, 255, 100));
                g2.drawString("You have claimed the", textX + 10, textY);
                textY += 30;
                g2.drawString("LOST TOMB'S TREASURE!", textX + 10, textY);
                textY += 40;
                g2.setColor(new Color(255, 215, 0));
                g2.drawString("YOU ARE A LEGEND!", textX + 10, textY);
                
                // Draw a small trophy
                textY += 40;
                g2.setFont(g2.getFont().deriveFont(24f));
                g2.drawString("   🏆", textX + 50, textY);
            }
        }
    
        // ===== NAVIGATION =====
        int navY = frameY + frameHeight - 60;
        g2.setColor(Color.lightGray);
        g2.setFont(g2.getFont().deriveFont(16f));
        
        if (questPage > 0) {
            g2.drawString("◀ PREV", textX, navY);
        }
        
        if (questPage < maxQuestPages) {
            String nextText = "NEXT ▶";
            int nextWidth = (int)g2.getFontMetrics().getStringBounds(nextText, g2).getWidth();
            g2.drawString(nextText, frameX + frameWidth - nextWidth - 30, navY);
        }
        
        // Legend
        g2.setFont(g2.getFont().deriveFont(14f));
        g2.drawString("[ACTIVE]  ✓ [DONE]  ! [NPC]  🔑 [KEY]  ⚓ [SHIP]", textX, navY - 20);
        
        // Close instruction
        g2.setFont(g2.getFont().deriveFont(16f));
        g2.setColor(Color.white);
        g2.drawString("Press Q to close", textX, navY + 25);
    }
    
    // Progress bar helper
    private void drawProgressBar(int x, int y, int width, int height, int percent) {
        // Draw background
        g2.setColor(new Color(60, 60, 60));
        g2.fillRect(x, y, width, height);
        
        // Draw progress
        if (percent > 0) {
            if (percent < 50) g2.setColor(new Color(255, 200, 100));
            else if (percent < 100) g2.setColor(new Color(100, 255, 100));
            else g2.setColor(new Color(0, 255, 255));
            
            int progressWidth = (width * percent) / 100;
            g2.fillRect(x, y, progressWidth, height);
        }
        
        // Draw border
        g2.setColor(Color.white);
        g2.drawRect(x, y, width, height);
    }
    
    public void nextQuestPage() {
        if (questPage < maxQuestPages) {
            questPage++;
            gp.playSE(5); // Page turn sound
        }
    }
    
    public void prevQuestPage() {
        if (questPage > 0) {
            questPage--;
            gp.playSE(5); // Page turn sound
        }
    }
    
    // Add these key handlers in your KeyHandler class:
    // if (code == KeyEvent.VK_RIGHT && gp.gameState == gp.questState) {
    //     ui.nextQuestPage();
    // }
    // if (code == KeyEvent.VK_LEFT && gp.gameState == gp.questState) {
    //     ui.prevQuestPage();
    // }
    
    // Helper method to draw progress bar
    // private void drawProgressBar(int x, int y, int width, int height, int percent) {
    //     // Draw background
    //     g2.setColor(new Color(60, 60, 60));
    //     g2.fillRect(x, y, width, height);
        
    //     // Draw progress
    //     if (percent > 0) {
    //         if (percent < 50) {
    //             g2.setColor(new Color(255, 200, 100)); // Orange-yellow
    //         } else if (percent < 100) {
    //             g2.setColor(new Color(100, 255, 100)); // Green
    //         } else {
    //             g2.setColor(new Color(0, 255, 255)); // Cyan for completed
    //         }
    //         int progressWidth = (width * percent) / 100;
    //         g2.fillRect(x, y, progressWidth, height);
    //     }
        
    //     // Draw border
    //     g2.setColor(Color.white);
    //     g2.drawRect(x, y, width, height);
        
    //     // Draw percent text
    //     g2.setFont(g2.getFont().deriveFont(14f));
    //     g2.setColor(Color.white);
    //     g2.drawString(percent + "%", x + width + 5, y + height - 2);
    // }

    public int getItemIndexOnSlot(int slotCol, int slotRow) {
        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
    }
    
    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    
    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        
        int y = gp.ScreenHeight/2;

        g2.drawString(text, x ,y);
    }
    
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.ScreenWidth/2 - length/2;
        return x;
    }
    
    public int getXforAlignToRightText(String text,int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
    
    public void currentDialogue() {}
}