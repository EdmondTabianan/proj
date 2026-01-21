package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("0.00");

    public UI(GamePanel gp) {
        this.gp = gp;

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
                    gp.player.inventory.get(i) == gp.player.currentShield) {
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