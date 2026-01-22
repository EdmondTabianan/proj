package main;

import entity.Entity;

public class eventHandler {
    GamePanel gp;
    EventRect eventRect[][];

    int previouseEventX, previouseEventY;
    boolean canTouchEvent = true;

    public eventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow ) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent() {
        // Check if the player char is more than 1 tile from event
        int xDistance = Math.abs(gp.player.worldX - previouseEventX);
        int yDistance = Math.abs(gp.player.worldY - previouseEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.TileSize) {
            canTouchEvent = true;
        }

        if(canTouchEvent == true) {
            if(hit(25,35, "down") == true) {healingPool(25,35,gp.dialogueState);}
            if(hit(21,33, "up") == true) {healingPool1(21,30,gp.dialogueState);}
            // if(hit(29, 24, "up") == true ) {teleport(gp.dialogueState);}
            if(hit(46, 42, "down") == true) {transport(gp.dialogueState);}
        }
    }
    
    public boolean hit (int col, int row, String regDirection) {
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col*gp.TileSize + eventRect[col][row].x;
        eventRect[col][row].y = row*gp.TileSize + eventRect[col][row].y;

        if (gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false) {
            if (gp.player.Direction.contentEquals(regDirection) || regDirection.contentEquals("any")){
                hit = true;
                previouseEventX = gp.player.worldX;
                previouseEventY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }
    
    public void teleport1(int gameState) {
        gp.gameState = gameState;
        gp.player.worldX = gp.TileSize*24;
        gp.player.worldY = gp.TileSize*14;
    } 
    
    public void teleport(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue ="Teleport";
        gp.player.worldX = gp.TileSize*29;
        gp.player.worldY = gp.TileSize*20;
    } 
    
    public void healingPool(int col, int row, int gameState) {
         if(gp.keyH.enterPressed == true) {
             gp.gameState = gameState;
             gp.player.attackCanceled = true;
             gp.ui.currentDialogue = "You drink water.\nyour life been recovered";
             gp.player.life = gp.player.maxLife;
             gp.player.mana = gp.player.maxMana;
             gp.aSetter.setMonster();
        }
    }
    
    public void healingPool1(int col, int row, int gameState) {
        if(gp.keyH.enterPressed == true) {
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.ui.currentDialogue = "You drink water.\nyour life been recovered";
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            gp.aSetter.setMonster();
       }
    }
    public void transport(int gameState) {
        if(gp.keyH.enterPressed == true) {
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            
            // Check if player has a key
            if(gp.player.hasKey > 0) {
                gp.player.hasKey--; // Use one key
                
                // Play sound effect for using key
                gp.playSE(2); // Assuming 2 is key use sound
                
                gp.ui.currentDialogue = "You used a key!\nTransporting to the second map...";
                
                // Small delay for effect
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                // Load the second map
                gp.tileM.loadMap("/map/secondmap.txt");
                
                // Set new player position for the second map
                gp.player.worldX = gp.TileSize * 24;
                gp.player.worldY = gp.TileSize * 42;
                
                // Reset event handler for new map
                gp.eHandler = new eventHandler(gp);
                
                // Reset monsters for new map
                gp.aSetter.setMonster();
                
                // Play map change sound
                gp.playSE(9); // Assuming 9 is transport sound
                
            } else {
                gp.ui.currentDialogue = "The ship is locked!\nYou need a key to enter.";
                // Play locked door sound
                gp.playSE(10); // Assuming 10 is locked sound
            }
        }
    }
}