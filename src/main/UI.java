package main;

import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import entity.Entity;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    public boolean gameFinished = false;
    public String currentDialogue = "";
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
    private long lastCharTime = 0;
    private final long CHAR_DELAY = 30; // milliseconds between characters

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("0.00");


    public UI(GamePanel gp) {
        this.gp = gp;
        loadLogoImage();
        arial_40 = new Font("Times New Roman", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        
        // Initialize with a tip - ADD THIS LINE
        currentTip = getRandomGameplayTip();
        lastTipChangeTime = System.currentTimeMillis(); // Initialize time
    
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
            lastCharTime = currentTime;
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
            lastCharTime = currentTime;
            lastTipUpdateTime = currentTime;
        }
        
        // Handle typing effect
        if (tipCharIndex < currentTip.length()) {
            if (currentTime - lastCharTime > CHAR_DELAY) {
                displayedTip += currentTip.charAt(tipCharIndex);
                tipCharIndex++;
                lastCharTime = currentTime;
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
            "Upgrade equipment at blacksmiths.",
            "Listen for audio cues of danger.",
            "Collect coins to buy better gear.",
            "The mini-map shows unexplored areas.",
            "Some secrets require solving puzzles.",
            
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
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
        }
        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        // DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
        // CHARACTER STATE
        if (gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory(gp.player,true);
        }
        // OPTIONS STATE
        if (gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }
        // Game over STATE
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        // OPTIONS STATE
        if (gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }
        // TRANSITION STATE
        if (gp.gameState == gp.transitionState) {
            drawTransitionScreen();
        }
        // Trade STATE
        if (gp.gameState == gp.tradeState) {
            TradeScreen();
        }
    }
    public void drawPlayerLife() {

        //gp.player.life = 5;

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
            g2.drawImage(heart_half,x, y, null);
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
        if (titleScreenState == 0) {

            // g2.setColor(new Color(50, 40, 90));
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

            // Logo 
            x = gp.ScreenWidth/2 - (gp.TileSize*2)/2;
            y += gp.TileSize*2;
            g2.drawImage(gp.player.down1, x, y, gp.TileSize*2, gp.TileSize*2, null);

            //Menu
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));

            text = "New Game";
            x = getXforCenteredText(text);
            y += gp.TileSize*3.5;
            g2.drawString(text, x, y);
            if (commandNum == 0){
                g2.drawString(">", x-gp.TileSize, y);
            }
            text = "Load Game";
            x = getXforCenteredText(text);
            y += gp.TileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1){
                g2.drawString(">", x-gp.TileSize, y);
            }
            text = "Quit";
            x = getXforCenteredText(text);
            y += gp.TileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2){
                g2.drawString(">", x-gp.TileSize, y);
            }
        }
        else if (titleScreenState == 1 ) {
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "select your hunter";
            int x = getXforCenteredText(text);
            int y = gp.TileSize*3;
            g2.drawString(text, x, y);

            text = "Xylo";
            x = getXforCenteredText(text);
            y += gp.TileSize*3;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x-gp.TileSize, y);
            }
            text = "Alendria";
            x = getXforCenteredText(text);
            y += gp.TileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x-gp.TileSize, y);
            }
            text = "Back";
            x = getXforCenteredText(text);
            y += gp.TileSize*2;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x-gp.TileSize, y);
            }
            
        }
        
    }
    public void drawDialogueScreen() {

        // Window
        int x = gp.TileSize*2;
        int y = gp.TileSize/2;
        int width = gp.ScreenWidth - (gp.TileSize*4);
        int height = gp.TileSize*4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        x += gp.TileSize;
        y += gp.TileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y+= 40;
        }
        
    }
    public void drawCharacterScreen() {

        // Create a Name
        final int frameX = gp.TileSize;
        final int frameY = gp.TileSize;
        final int framewidth = gp.TileSize*5;
        final int frameHeight = gp.TileSize*10;
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
        g2.drawString("Strengt", textX, textY); textY += lineHeight;
        g2.drawString("Dexterity", textX, textY); textY += lineHeight;
        g2.drawString("Attack", textX, textY); textY += lineHeight;
        g2.drawString("Defense", textX, textY); textY += lineHeight;
        g2.drawString("Exp", textX, textY); textY += lineHeight;
        g2.drawString("Next Level", textX, textY); textY += lineHeight;
        g2.drawString("coin", textX, textY); textY += lineHeight + 10;
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

        // value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        // textX = getXforAlignToRightText(value, tailX);
        // g2.drawString(value, textX, textY);
        // textY += lineHeight;
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

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);

        g2.drawImage(gp.player.currentweapon.down1, tailX - gp.TileSize, textY+14, null);
        textY += gp.TileSize;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.TileSize, textY+14, null);  
        
        if (gp.gameState == gp.characterState) {
            drawInventory(gp.player, true);
        }
    }
    public void drawInventory(Entity entity, boolean cursor) {

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
            if(entity.inventory.get(i) == entity.currentweapon ||
                entity.inventory.get(i) == entity.currentShield || 
                entity.inventory.get(i) == entity.currentRange) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.TileSize, gp.TileSize, 10, 10);
            }
            
            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);

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

            // description frame
            int dframeX = frameX;
            int dframeY = frameY + frameHeight;
            int dframeWidth = framewidth;
            int dframeHeight = gp.TileSize*3;
            // drawSubWindow(dframeX, dframeY, dframeWidth, dframeHeight);
            // //description text
            // int textX = dframeX + 20;
            // int textY = dframeY + gp.TileSize;
            // g2.setFont(g2.getFont().deriveFont(28f));

            // int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

            // if (itemIndex < entity.inventory.size()) {

            //     for(String line: entity.inventory.get(itemIndex).description.split("\n")){
            //         g2.drawString(line, textX, textY);
            //         textY += 32;
            //     } 
                
            // }
            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

            if (cursor && itemIndex < entity.inventory.size()) {

                // description frame
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
                gp.gameState = gp.playState; // Or gp.titleState if from title
                commandNum = 0;
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
                String status = ""; // Add your save status check here
                g2.setFont(g2.getFont().deriveFont(20f)); // Smaller font for status
                g2.drawString(status, textX, textY + 15);
                g2.setFont(g2.getFont().deriveFont(32f)); // Reset to normal font
            }
            
            g2.drawString(slotNames[i], textX, textY);
            
            if (commandNum == i) {
                g2.drawString(">", textX - 25, textY);
                if (gp.keyH.enterPressed == true) {
                    if (i < 3) {
                        // Save to slot i
                        //saveGame(i);
                        showMessage("SAVED!");
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
        
        // Calculate frameWidth here (same as in drawOptionsScreen)
        int frameWidth = (int)(gp.TileSize * 8.5);
        
        // Title - centered within the window
        String text = "Control";
        textX = frameX + (frameWidth / 2) - (int)(g2.getFontMetrics().getStringBounds(text, g2).getWidth() / 2);
        textY = frameY + gp.TileSize;
        g2.drawString(text, textX, textY);
        
        // Controls list - left column
        textX = frameX + gp.TileSize;
        textY = frameY + gp.TileSize * 2;
        
        // Left column actions
        g2.drawString("Move", textX, textY);
        textY += gp.TileSize;
        g2.drawString("Attack", textX, textY);
        textY += gp.TileSize;
        g2.drawString("Shoot", textX, textY);
        textY += gp.TileSize;
        g2.drawString("Cast", textX, textY);
        textY += gp.TileSize;
        g2.drawString("Character", textX, textY);
        textY += gp.TileSize;
        g2.drawString("Pause", textX, textY);
        textY += gp.TileSize;
        g2.drawString("Options", textX, textY);
        
        // Key bindings - right column
        textX = frameX + gp.TileSize * 5;
        textY = frameY + gp.TileSize * 2;
        
        // Right column keys
        g2.drawString("W/A/S/D", textX, textY);
        textY += gp.TileSize;
        g2.drawString("ENTER", textX, textY);
        textY += gp.TileSize;
        g2.drawString("Q", textX, textY);
        textY += gp.TileSize;
        g2.drawString("F", textX, textY);
        textY += gp.TileSize;
        g2.drawString("C", textX, textY);
        textY += gp.TileSize;
        g2.drawString("P", textX, textY);
        textY += gp.TileSize;
        g2.drawString("ESC", textX, textY);
        
        // Back option - centered within window
        textY = frameY + gp.TileSize * 9;
        text = "Back";
        textX = frameX + (frameWidth / 2) - (int)(g2.getFontMetrics().getStringBounds(text, g2).getWidth() / 2);
        g2.drawString("Back", textX, textY);
        
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 3; // Go back to Control option
                gp.keyH.enterPressed = false;
            }
        }
    }
    public void option_endGameConfirmation(int frameX, int frameY) {
        int textX;
        int textY;
        
        // Shorter question to fit better
        textX = getXforCenteredText("Quit to Title?");
        textY = frameY + gp.TileSize * 3;
        g2.drawString("Quit to Title?", textX, textY);
        
        // Yes/No options
        String[] options = {"YES", "NO"};
        textY += gp.TileSize * 2;
        
        for (int i = 0; i < options.length; i++) {
            textX = getXforCenteredText(options[i]);
            g2.drawString(options[i], textX, textY);
            
            if (commandNum == i) {
                g2.drawString(">", textX - 25, textY);
                if (gp.keyH.enterPressed == true) {
                    if (i == 0) {
                        // Yes - quit to title
                        gp.gameState = gp.titleState;
                        gp.restart();
                        gp.music.stop();
                    } else {
                        // No - back to options
                        subState = 0;
                        commandNum = 4; // Select Quit Game item
                    }
                    gp.keyH.enterPressed = false;
                }
            }
            textY += gp.TileSize;
        }
    }
    public void drawTransitionScreen() {
        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
    
        if (counter == 50) {
            counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            
            // FIX: Use tempColFloat instead of tempCol (which doesn't exist)
            gp.player.worldX = (int)(gp.eHandler.tempColFloat * gp.TileSize);
            gp.player.worldY = gp.eHandler.tempRow * gp.TileSize;
            
            gp.eHandler.previouseEventX = gp.player.worldX;
            gp.eHandler.previouseEventY = gp.player.worldY;
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
            
            int amount = npc.inventory.get(itemIndex).amount;
            String text = "" + amount;
            x = getXforAlignToRightText(text, gp.TileSize*8 - 30);
            g2.drawString(text, x, y+35);
            
            // Check for Enter press to buy
            if (gp.keyH.enterPressed == true) {
                if (amount > gp.player.coin) {
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
                        
                    gp.player.coin -= amount;
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
            
            int amount = gp.player.inventory.get(itemIndex).amount/2;
            String text = "" + amount;
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
                    gp.player.coin += amount;
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