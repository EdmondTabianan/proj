package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class EndingManager {
    
    GamePanel gp;
    Graphics2D g2;
    
    // Typing animation variables
    private long typingStartTime = 0;
    private String fullText = "";
    private int targetChars = 0;
    private boolean typingComplete = false;
    private int typingSpeed = 30; // milliseconds per character
    
    // Ending phases
    private int endingPhase = 0;
    
    // Credits animation
    private long creditStartTime = 0;
    
    // Credits data
    private String[][] credits = {
        {"DIRECTOR", "Zaki Bjorn Cardenas"},
        {"ASSISTANT DIRECTOR", "Kim Mendoza"},
        {"", ""},
        {"HEAD ARTIST / UI", "Albir Damahan"},
        {"ARTISTS", "Kirk Iglesia, Arvin Fornal, Kim Mendoza, Edmond Tabianan"},
        {"", ""},
        {"HEAD PROGRAMMER", "Edmond Tabianan"},
        {"CO-PROGRAMMER", "Zaki Cardenas"},
        {"", ""},
        {"SPECIAL THANKS", "Family & Friends"},
        {"", ""},
        {"© 2024", "All Rights Reserved"}
    };
    
    // Post-story text
    private String[] storyText = {
        "The battle with Anubis shook the very foundations of the pyramid.",
        "As the great guardian fell, the curse of the Lost Tomb was finally broken.",
        "",
        "The sands of Egypt settled, and peace returned to the land.",
        "",
        "The village elder returned to train a new generation of hunters.",
        "A school of adventure was founded, training heroes for generations.",
        "An old warrior finally found peace and opened a tavern for travelers.",
        "",
        "And you...",
        "You emerged from the pyramid forever changed.",
        "The spirit of Anubis had chosen you as its successor.",
        "",
        "Now you stand guard over the Lost Tomb of Cleopatra,",
        "protecting its secrets for all eternity.",
        "",
        "But legends say that one day, when the world needs you most,",
        "you will rise again.",
        "",
        "THE HUNT CONTINUES...",
        ""
    };
    
    public EndingManager(GamePanel gp) {
        this.gp = gp;
    }
    
    public void startEnding() {
        endingPhase = 0;
        typingStartTime = 0;
        creditStartTime = 0;
        typingComplete = false;
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        
        switch (endingPhase) {
            case 0:
                drawEpilogue();
                break;
            case 1:
                drawCredits();
                break;
            default:
                returnToTitle();
                break;
        }
    }
    
    private void drawEpilogue() {
        // Black background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
        
        // Chapter title - positioned higher
        g2.setFont(new Font("Times New Roman", Font.BOLD, 36)); // Smaller font
        g2.setColor(new Color(255, 215, 0)); // Gold
        String title = "EPILOGUE";
        int titleX = gp.ScreenWidth/2 - g2.getFontMetrics().stringWidth(title)/2;
        g2.drawString(title, titleX, 50); // Moved up from 80 to 50
        
        // Typing animation
        long currentTime = System.currentTimeMillis();
        if (typingStartTime == 0) {
            typingStartTime = currentTime;
            StringBuilder sb = new StringBuilder();
            for (String line : storyText) {
                sb.append(line).append("\n");
            }
            fullText = sb.toString();
            targetChars = fullText.length();
        }
        
        long elapsedTime = currentTime - typingStartTime;
        int charsToShow = (int)(elapsedTime / typingSpeed);
        
        if (charsToShow > targetChars) {
            charsToShow = targetChars;
            typingComplete = true;
        }
        
        String displayText = fullText.substring(0, charsToShow);
        
        // Draw the text - smaller font and tighter spacing
        g2.setFont(new Font("Times New Roman", Font.PLAIN, 18)); // Smaller font
        g2.setColor(Color.WHITE);
        drawWrappedText(displayText, 80, 90, gp.ScreenWidth - 160, 22); // Adjusted Y and line height
        
        // Continue prompt - positioned at bottom
        if (typingComplete) {
            g2.setFont(new Font("Times New Roman", Font.ITALIC, 16));
            g2.setColor(new Color(200, 200, 200));
            String continueText = "Press ENTER to see credits...";
            int x = gp.ScreenWidth/2 - g2.getFontMetrics().stringWidth(continueText)/2;
            int y = gp.ScreenHeight - 30;
            g2.drawString(continueText, x, y);
            
            if (gp.keyH.enterPressed) {
                gp.keyH.enterPressed = false;
                endingPhase = 1;
                creditStartTime = System.currentTimeMillis();
            }
        }
    }
    
    private void drawCredits() {
        // Semi-transparent overlay
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);
        
        int y = 50; // Start higher (was 100)
        int spacing = 25; // Reduced spacing (was 35)
        
        // Title - smaller
        g2.setFont(new Font("Times New Roman", Font.BOLD, 40)); // Smaller (was 56)
        g2.setColor(new Color(255, 215, 0));
        String title = "THE HUNT";
        int x = gp.ScreenWidth/2 - g2.getFontMetrics().stringWidth(title)/2;
        g2.drawString(title, x, y);
        y += 30; // Reduced (was 50)
        
        g2.setFont(new Font("Times New Roman", Font.BOLD, 24)); // Smaller (was 32)
        g2.setColor(new Color(200, 150, 50));
        String subtitle = "Lost Tomb of Cleopatra";
        x = gp.ScreenWidth/2 - g2.getFontMetrics().stringWidth(subtitle)/2;
        g2.drawString(subtitle, x, y);
        y += 40; // Reduced (was 70)
        
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - creditStartTime;
        int linesToShow = (int)(elapsedTime / 500); // Faster (was 600)
        
        for (int i = 0; i < credits.length && i < linesToShow; i++) {
            String[] credit = credits[i];
            
            if (credit[0].isEmpty()) {
                y += spacing;
                continue;
            }
            
            // Role - smaller font
            g2.setFont(new Font("Times New Roman", Font.BOLD, 18)); // Smaller (was 24)
            g2.setColor(new Color(255, 200, 100));
            g2.drawString(credit[0], gp.ScreenWidth/2 - 200, y); // Adjusted position
            
            // Names - smaller font
            g2.setFont(new Font("Times New Roman", Font.PLAIN, 18)); // Smaller (was 24)
            g2.setColor(Color.WHITE);
            
            // Handle long artist lists
            String names = credit[1];
            if (g2.getFontMetrics().stringWidth(names) > 300) { // Reduced threshold
                String[] nameArray = names.split(", ");
                StringBuilder line1 = new StringBuilder();
                StringBuilder line2 = new StringBuilder();
                
                for (String name : nameArray) {
                    if (line1.length() + name.length() < 20) { // Reduced (was 25)
                        if (line1.length() > 0) line1.append(", ");
                        line1.append(name);
                    } else {
                        if (line2.length() > 0) line2.append(", ");
                        line2.append(name);
                    }
                }
                
                g2.drawString(line1.toString(), gp.ScreenWidth/2 + 40, y); // Adjusted
                if (line2.length() > 0) {
                    g2.drawString(line2.toString(), gp.ScreenWidth/2 + 40, y + 20); // Adjusted
                    y += 20;
                }
            } else {
                g2.drawString(names, gp.ScreenWidth/2 + 40, y); // Adjusted
            }
            
            y += spacing;
        }
        
        // Show all credits and exit prompt
        if (linesToShow >= credits.length) {
            y = gp.ScreenHeight - 60; // Adjusted (was -100)
            
            g2.setFont(new Font("Times New Roman", Font.ITALIC, 16)); // Smaller
            g2.setColor(new Color(150, 150, 150));
            String thanksText = "Thank you for playing!";
            x = gp.ScreenWidth/2 - g2.getFontMetrics().stringWidth(thanksText)/2;
            g2.drawString(thanksText, x, y);
            y += 20;
            
            String resetText = "Press ENTER to return to title screen";
            x = gp.ScreenWidth/2 - g2.getFontMetrics().stringWidth(resetText)/2;
            g2.drawString(resetText, x, y);
            
            if (gp.keyH.enterPressed) {
                gp.keyH.enterPressed = false;
                returnToTitle();
            }
        }
    }
    
    private void drawWrappedText(String text, int x, int y, int maxWidth, int lineHeight) {
        String[] lines = text.split("\n");
        int drawY = y;
        
        for (String line : lines) {
            if (line.isEmpty()) {
                drawY += lineHeight;
                continue;
            }
            
            String[] words = line.split(" ");
            StringBuilder currentLine = new StringBuilder();
            
            for (String word : words) {
                String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                int width = g2.getFontMetrics().stringWidth(testLine);
                
                if (width > maxWidth) {
                    g2.drawString(currentLine.toString(), x, drawY);
                    currentLine = new StringBuilder(word);
                    drawY += lineHeight;
                } else {
                    currentLine = currentLine.length() == 0 ? 
                        new StringBuilder(word) : currentLine.append(" ").append(word);
                }
            }
            
            if (currentLine.length() > 0) {
                g2.drawString(currentLine.toString(), x, drawY);
                drawY += lineHeight;
            }
        }
    }
    
    private void returnToTitle() {
        gp.resetGame(true);
        gp.gameState = gp.titleState;
        gp.stopMusic();
        gp.playMusic(0);
        
        // Reset ending variables
        endingPhase = 0;
        typingStartTime = 0;
        creditStartTime = 0;
        typingComplete = false;
    }
}