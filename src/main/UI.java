package main;

import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
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
    
    // Helper method to get a random gameplay tip
    private String getRandomGameplayTip() {
        String[] tips = {
            "Explore every corner for hidden treasures!",
            "Different enemies have different weaknesses.",
            "Use ranged attacks against tough melee enemies.",
            "Conserve magic for challenging encounters.",
            "Some doors require special keys to open.",
            "Watch your health and use potions wisely.",
            "Environmental objects can be used against enemies.",
            "Combine items for more powerful effects.",
            "Patience is key when facing powerful bosses.",
            "Read ancient tablets and notes for valuable clues.",
            // Control tips added here
            "Move: W/A/S/D keys",
            "Attack/Confirm: ENTER key",
            "F key: Cast spells with wand, Shoot with bow",
            "Open character screen: C key",
            "Pause game: P key",
            "Open options: ESC key",
            "Use W/A/S/D to move around",
            "Press ENTER to attack or confirm actions",
            "Press F to cast spells when wielding a wand",
            "Press F to shoot arrows when wielding a bow",
            "Press C to check your character stats",
            "Press P to pause the game",
            "Press ESC to open options menu",
            "F casts spells with wand, shoots with bow",
            "Equip a wand and press F to cast magic",
            "Equip a bow and press F to shoot arrows",
            "The F key adapts to your equipped weapon",
            "With wand equipped: F = Cast spell",
            "With bow equipped: F = Shoot arrow",
            "Switch between wand and bow for different F actions",
            "Wand + F = Magic, Bow + F = Arrows",
            "F key's function depends on your weapon choice",
            "Use wand for magic, bow for ranged - both use F key"
        };
        
        // Select random tip
        int randomIndex = (int)(Math.random() * tips.length);
        return "Tip: " + tips[randomIndex];
    }

    // Add a method to update loading progress
    public void setLoadingProgress(float progress) {
        this.loadingProgress = (int) progress;
    }
    
    // Add a method to get loading progress (optional)
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
            g2.setColor(Color.white);
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
    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));

        // Sub window
        int frameX = gp.TileSize*5;
        int frameY = gp.TileSize;
        int frameWidth = gp.TileSize*8;
        int frameHeight = gp.TileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0: options_top(frameX, frameY); break;
            case 1: break; //save????????
            case 2: option_control(frameX, frameY);break;
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

        textX = frameX + gp.TileSize;
        textY += gp.TileSize*2;
        g2.drawString("save", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
        }

        // music
        textY += gp.TileSize;
        g2.drawString("Music", textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX-25, textY);
        }

        // sound effect
        textY += gp.TileSize;
        g2.drawString("SE", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX-25, textY);
        }

        // Control 
        textY += gp.TileSize;
        g2.drawString("Control", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 2;
                commandNum = 0;
                gp.keyH.enterPressed = false;
            }
        }
        // quit game
        textY += gp.TileSize;
        g2.drawString("Quit game", textX, textY);
        if (commandNum == 4) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyH.enterPressed == true) {
                subState = 3;
                commandNum = 0;
            }
        }

        //back
        textY += gp.TileSize*8;
        g2.drawString("Back", textX, textY);
        if (commandNum == 5) {
            g2.drawString(">", textX-25, textY);
        }

        // save box???
        textX = frameX + gp.TileSize*5;
        textY = frameY + gp.TileSize*2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 23, 24);

        // music
        textY += gp.TileSize;
        g2.drawRect(textX, textY, 120, 24); //120/5
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        // Sound Effect
        textY += gp.TileSize;
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);
    }
    public void option_control(int frameX, int frameY) {

        int textX;
        int textY;

        //Title
        String text = "Control";
        textX = getXforCenteredText(text);
        textY = frameY + gp.TileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.TileSize;
        textY += gp.TileSize;
        g2.drawString("Move", textX, textY);textY+=gp.TileSize;
        g2.drawString("Confirm/Attack", textX, textY);textY+=gp.TileSize;
        g2.drawString("Shoot", textX, textY);textY+=gp.TileSize;
        g2.drawString("Cast", textX, textY);textY+=gp.TileSize;
        g2.drawString("Character SCreen", textX, textY);textY+=gp.TileSize;
        g2.drawString("Pause", textX, textY);textY+=gp.TileSize;
        g2.drawString("Option", textX, textY);textY+=gp.TileSize;

        textX = frameX + gp.TileSize*6;
        textY = frameY + gp.TileSize*2;
        g2.drawString("W/A/S/D", textX, textY);textY+=gp.TileSize;
        g2.drawString("ENTER", textX, textY);textY+=gp.TileSize;
        g2.drawString("Q", textX, textY);textY+=gp.TileSize;
        g2.drawString("F", textX, textY);textY+=gp.TileSize;
        g2.drawString("C", textX, textY);textY+=gp.TileSize;
        g2.drawString("P", textX, textY);textY+=gp.TileSize;
        g2.drawString("ESC", textX, textY);textY+=gp.TileSize;

        // Back
        textX = frameX + gp.TileSize;
        textY = frameY + gp.TileSize * 9;
        g2.drawString("Back", textX, textY);
        
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 0;
                gp.keyH.enterPressed = false;
            }
        }
    }
    public void option_endGameConfirmation(int frameX, int frameY) {

        int textX = frameX + gp.TileSize;
        int textY = frameY + gp.TileSize;
        
        currentDialogue = "Quit the game and \nreturn to the title screen?";

        for (String line: currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // YES
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gp.TileSize*3;
        if (commandNum==0) {
            g2.drawString(">", textX, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 0;
                gp.gameState = gp.titleState;
                gp.keyH.enterPressed = false;
            }
        }
        // NO
        text = "No";
        textX = getXforCenteredText(text);
        textY += gp.TileSize;
        if (commandNum==1) {
            g2.drawString(">", textX, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 4;
            }
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
    public void currentDialogue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'currentDialogue'");
    }
}