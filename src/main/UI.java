package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import entity.Entity;

import object.OBJ_Heart;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    // String dialogues[] = new String[20];
    public int commandNum = 0;
    public int titleScreenState = 0; // 0 the first screen 1 second screen


    double playTime;
    DecimalFormat dFormat = new DecimalFormat("0.00");

    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Times New Roman", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        

        // Create HUD object
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text) {

        message = text;
        messageOn = true;
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
        }
        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
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

    }
    public void drawTitleScreen() {
        if (titleScreenState == 0) {

            // g2.setColor(new Color(50, 40, 90));
            g2.setColor(new Color(0,0,0));
            g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);

            // Title Name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,72F));
            String text = "Baddie's Adventure";
            int x = getXforCenteredText(text);
            int y = gp.TileSize*3;

            // shadow
            g2.setColor(Color.GRAY);
            g2.drawString(text, x+5, y+5);
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

    public void currentDialogue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'currentDialogue'");
    }
}