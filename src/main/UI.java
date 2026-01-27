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

import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
    public boolean messageOn = false;
    // public String message = "";
    // int messageCounter = 0;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    public boolean gameFinished = false;
    public String currentDialogue = "";
    // String dialogues[] = new String[20];
    public int commandNum = 0;
    public int titleScreenState = 0; // 0 the first screen 1 second screen
    public int slotCol = 0;
    public int slotRow = 0;
    int subState = 0;
    private Image logoImage;
    public int loadingProgress = 0;
    private int loadingDirection = 1;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("0.00");

    public UI(GamePanel gp) {
        this.gp = gp;
        loadLogoImage();
        arial_40 = new Font("Times New Roman", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        

        // Create HUD object
        Entity heart = new OBJ_Heart(gp);
        Entity crystal = new OBJ_ManaCrystal(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
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
            int logoWidth = 200; // Adjust as needed
            int logoHeight = 200; // Adjust as needed
            
            // finding the center position
            int logoX = gp.ScreenWidth / 2 - logoWidth / 2;
            int logoY = gp.ScreenHeight / 2 - logoHeight - 60; // 60 pixels above center
            
            // Draw the image
            g2.drawImage(logoImage, logoX, logoY, logoWidth, logoHeight, null);
        }
        
        // Draw game title
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.setColor(new Color(255, 215, 0));
        
        String title = "The Hunt: Lost Tomb of Cleopatra";
        int titleX = gp.ScreenWidth / 2 - g2.getFontMetrics().stringWidth(title) / 2;
        int titleY = gp.ScreenHeight / 2; // Adjusted position
        
        g2.drawString(title, titleX, titleY);
        
        // Draw a random gameplay tip above the progress bar
        String randomTip = getRandomGameplayTip();
        g2.setFont(new Font("Arial", Font.ITALIC, 20));
        g2.setColor(new Color(180, 220, 255)); // Light blue color
        
        int tipTextX = gp.ScreenWidth / 2 - g2.getFontMetrics().stringWidth(randomTip) / 2;
        int tipTextY = gp.ScreenHeight - 150; // Position above progress bar
        
        g2.drawString(randomTip, tipTextX, tipTextY);
        
        // Progress bar
        int barWidth = gp.ScreenWidth - 200;
        int barHeight = 20;
        int barX = (gp.ScreenWidth - barWidth) / 2;
        int barY = gp.ScreenHeight - 100;
        
        // Border
        g2.setColor(Color.white);
        g2.drawRect(barX, barY, barWidth, barHeight);
        
        // Animate loading progress
        loadingProgress += loadingDirection;
        
        if (loadingProgress >= 100) {
            loadingProgress = 100;
        }
        
        int fillWidth = (int)(barWidth * loadingProgress / 100.0);
        
        g2.setColor(new Color(212, 175, 55));
        g2.fillRect(barX + 1, barY + 1, fillWidth - 2, barHeight - 2);
        
        // Percentage Text
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        String percentText = (int)loadingProgress + "%";
        int percentX = gp.ScreenWidth / 2 - g2.getFontMetrics().stringWidth(percentText) / 2;
        int percentY = barY + 45;
        
        g2.drawString(percentText, percentX, percentY);
    }
    private String getRandomGameplayTip() {
        String[] tips = {
            // Original tips kept
            "Explore every corner for hidden treasures!",
            "Different enemies have different weaknesses.",
            "Use ranged attacks against tough melee enemies.",
            "Conserve magic for challenging encounters.",
            "Some doors require special keys to open.",
            "Watch your health and use potions wisely.",
            "Environmental objects can be used against enemies.",
            "Combine items for more powerful effects.",
            "Patience is key when facing powerful bosses.",
            "Read ancient tombs for valuable clues.",
            
            // Added longer, more detailed tips
            "TIP: Save your game frequently at statues to avoid losing progress.",
            "TIP: Different weapons work better against different enemy types.",
            "TIP: Upgrade your equipment at blacksmiths when you find them.",
            "TIP: Listen for audio cues - they can warn you of nearby danger.",
            "TIP: Some secrets require solving environmental puzzles.",
            "TIP: Your character's stamina affects how many attacks you can perform.",
            "TIP: Use stealth to avoid unnecessary combat when low on health.",
            "TIP: Collect all coins and gems to buy better equipment.",
            "TIP: The mini-map shows unexplored areas - try to fill it completely.",
            "TIP: Some enemies are immune to certain types of damage.",
            
            // Control tips made more descriptive
            "CONTROLS: Use W, A, S, D keys to move your character around.",
            "CONTROLS: Press ENTER to attack enemies or confirm menu selections.",
            "CONTROLS: Press F to cast spells with wand or shoot arrows with bow.",
            "CONTROLS: Press C to open character screen and check your stats.",
            "CONTROLS: Press P to pause the game at any time.",
            "CONTROLS: Press ESC to open options and adjust settings.",
            "CONTROLS: The F key adapts to your equipped weapon - wand or bow.",
            "CONTROLS: With wand equipped, F casts magical spells.",
            "CONTROLS: With bow equipped, F shoots ranged arrows.",
            "CONTROLS: Switch weapons using inventory to change F key function."
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
            drawInventory();
        }
        // OPTIONS STATE
        if (gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }
        // Game over STATE
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }

        if (gp.gameState == gp.optionsState) {
            drawOptionsScreen();
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
    }
    public void drawInventory() {

        //Frame
        int frameX = gp.TileSize*9;
        int frameY = gp.TileSize;
        int framewidth = gp.TileSize*6;
        int frameHeight = gp.TileSize*5;
        drawSubWindow(frameX, frameY, framewidth, frameHeight);

        // sloth
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slitsize = gp.TileSize+3;

        // draw players item
        for(int i = 0; i < gp.player.inventory.size(); i++){

            // equip cursor
            if(gp.player.inventory.get(i) == gp.player.currentweapon ||
                gp.player.inventory.get(i) == gp.player.currentShield || 
                gp.player.inventory.get(i) == gp.player.currentRange) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.TileSize, gp.TileSize, 10, 10);
            }
            
            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);

            slotX += slitsize;

            if(i == 4 || i == 9 || i == 14) {
                slotX = slotXstart;
                slotY += slitsize;
            }
        }

         //Cursor
         int cursorX = slotXstart + (slitsize * slotCol);
         int cursorY = slotYstart + (slitsize * slotRow);
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
        drawSubWindow(dframeX, dframeY, dframeWidth, dframeHeight);
        //description text
        int textX = dframeX + 20;
        int textY = dframeY + gp.TileSize;
        g2.setFont(g2.getFont().deriveFont(28f));

        int itemIndex = getItemIndexOnSlot();

        if (itemIndex < gp.player.inventory.size()) {

            for(String line: gp.player.inventory.get(itemIndex).description.split("\n")){
                //g2.drawString(gp.player.inventory.get(itemIndex).description, textX, textY);
                g2.drawString(line, textX, textY);
                textY += 32;
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
    public int getItemIndexOnSlot() {
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