package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import entity.Entity;
import object.OBJ_Heart;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; // 0 the first screen 1 second screen
    
    // Store character preview images
    private BufferedImage xyloPreview;
    private BufferedImage alexandriaPreview;
    private boolean previewsLoaded = false;
    
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
        
        // Don't load previews here - they'll be loaded on demand
    }
    
    private void loadCharacterPreviews() {
        if (previewsLoaded || gp.player == null) {
            return;
        }
        
        // Load Xylo preview
        xyloPreview = gp.player.setup("/xylo/b_down_1", gp.TileSize * 2, gp.TileSize * 2);
        
        // Load Alexandria preview
        alexandriaPreview = gp.player.setup("/player/down_1", gp.TileSize * 2, gp.TileSize * 2);
        
        previewsLoaded = true;
    }
    
    private BufferedImage getXyloPreview() {
        if (xyloPreview == null && gp.player != null) {
            xyloPreview = gp.player.setup("/xylo/b_down_1", gp.TileSize * 2, gp.TileSize * 2);
        }
        return xyloPreview;
    }
    
    private BufferedImage getAlexandriaPreview() {
        if (alexandriaPreview == null && gp.player != null) {
            alexandriaPreview = gp.player.setup("/player/down_1", gp.TileSize * 2, gp.TileSize * 2);
        }
        return alexandriaPreview;
    }
    
    public void showMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        
        // Title state
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
        }
    }
    
    public void drawPlayerLife() {
        int x = gp.TileSize / 2;
        int y = gp.TileSize / 2;
        int i = 0;
        
        // Draw blank heart
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.TileSize;
        }
        
        x = gp.TileSize / 2;
        y = gp.TileSize / 2;
        i = 0;
        
        // Draw current life
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.TileSize;
        }
    }
    
    public void drawMessage() {
        int messageX = gp.TileSize;
        int messageY = gp.TileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        
        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);
                
                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;
                
                if (messageCounter.get(i) > 100) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
    
    public void drawTitleScreen() {
        // Ensure previews are loaded
        if (!previewsLoaded && gp.player != null) {
            loadCharacterPreviews();
        }
        
        if (titleScreenState == 0) {
            // Main menu screen
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
            
            // Title Name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 72F));
            String text = "Baddie's Adventure";
            int x = getXforCenteredText(text);
            int y = gp.TileSize * 3;
            
            // Shadow
            g2.setColor(Color.GRAY);
            g2.drawString(text, x + 5, y + 5);
            // Main Color
            g2.setColor(Color.white);
            g2.drawString(text, x, y);
            
            // Logo - Show default player image (Alexandria)
            x = gp.ScreenWidth / 2 - (gp.TileSize * 2) / 2;
            y += gp.TileSize * 2;
            
            // Draw Alexandria preview if available
            BufferedImage preview = getAlexandriaPreview();
            if (preview != null) {
                g2.drawImage(preview, x, y, gp.TileSize * 2, gp.TileSize * 2, null);
            } else {
                // Fallback: Draw a placeholder rectangle
                g2.setColor(Color.GRAY);
                g2.fillRect(x, y, gp.TileSize * 2, gp.TileSize * 2);
                g2.setColor(Color.WHITE);
                g2.drawRect(x, y, gp.TileSize * 2, gp.TileSize * 2);
            }
            
            // Menu
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
            
            text = "New Game";
            x = getXforCenteredText(text);
            y += gp.TileSize * 3.5;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.TileSize, y);
            }
            
            text = "Load Game";
            x = getXforCenteredText(text);
            y += gp.TileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x - gp.TileSize, y);
            }
            
            text = "Quit";
            x = getXforCenteredText(text);
            y += gp.TileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x - gp.TileSize, y);
            }
        } else if (titleScreenState == 1) {
            // Character selection screen
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
            
            g2.setColor(Color.white);
            
            // Title
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
            String text = "Select your hunter";
            int x = getXforCenteredText(text);
            int y = gp.TileSize * 2;
            g2.drawString(text, x, y);
            
            // Character preview area in the middle
            int previewX = gp.ScreenWidth / 2 - (gp.TileSize * 2) / 2;
            int previewY = y + gp.TileSize * 2;
            
            // Show preview ONLY for character options (0: Xylo, 1: Alexandria)
            if (commandNum == 0) {
                // Show Xylo preview
                BufferedImage xyloPreview = getXyloPreview();
                if (xyloPreview != null) {
                    g2.drawImage(xyloPreview, previewX, previewY, gp.TileSize * 2, gp.TileSize * 2, null);
                }
            } else if (commandNum == 1) {
                // Show Alexandria preview
                BufferedImage alexandriaPreview = getAlexandriaPreview();
                if (alexandriaPreview != null) {
                    g2.drawImage(alexandriaPreview, previewX, previewY, gp.TileSize * 2, gp.TileSize * 2, null);
                }
            }
            // For "Back" option (commandNum == 2), show nothing
            
            // Start menu options after preview area
            y = previewY + gp.TileSize * 2 + gp.TileSize;
            
            // Menu options
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
            
            // Xylo selection
            text = "Xylo";
            x = getXforCenteredText(text);
            y += gp.TileSize;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.TileSize, y);
            }
            
            // Alexandria selection
            text = "Alexandria";
            x = getXforCenteredText(text);
            y += gp.TileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x - gp.TileSize, y);
            }
            
            // Back option
            text = "Back";
            x = getXforCenteredText(text);
            y += gp.TileSize*2;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x - gp.TileSize, y);
            }
        }
    }
    
    public void drawDialogueScreen() {
        // Window
        int x = gp.TileSize * 2;
        int y = gp.TileSize / 2;
        int width = gp.ScreenWidth - (gp.TileSize * 4);
        int height = gp.TileSize * 4;
        
        drawSubWindow(x, y, width, height);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.TileSize;
        y += gp.TileSize;
        
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }
    
    public void drawCharacterScreen() {
        // Create a frame
        final int frameX = gp.TileSize;
        final int frameY = gp.TileSize;
        final int frameWidth = gp.TileSize * 5;
        final int frameHeight = gp.TileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        // Text 
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));
        
        int textX = frameX + 20;
        int textY = frameY + gp.TileSize;
        final int lineHeight = 35;
        
        // Name
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 20;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);
        textY += lineHeight;
        
        // Values
        int tailX = (frameX + frameWidth) - 30;
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
        
        g2.drawImage(gp.player.currentweapon.down1, tailX - gp.TileSize, textY + 20, null);
        textY += gp.TileSize;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.TileSize, textY + 20, null);
    }
    
    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }
    
    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.ScreenHeight / 2;
        
        g2.drawString(text, x, y);
    }
    
    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.ScreenWidth / 2 - length / 2;
        return x;
    }
    
    public int getXforAlignToRightText(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}