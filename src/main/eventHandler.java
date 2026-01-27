package main;

public class eventHandler {
    GamePanel gp;
    EventRect eventRect[][][];

    int previouseEventX, previouseEventY;
    boolean canTouchEvent = true;

    public eventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;
        while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow ) {
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;

                if (row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
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
            if(hit(0,25,35, "down") == true) {healingPool(gp.dialogueState);}
            else if(hit(0,21,33, "up") == true) {healingPool(gp.dialogueState);}
            else if(hit(0, 46, 42, "down") == true) {transport(1, 24 , 42,gp.dialogueState);}
            else if(hit(1, 24, 42, "down") == true) {transport(0, 46 , 42,gp.dialogueState);}
        }
    }
    
    public boolean hit (int map, int col, int row, String regDirection) {
        boolean hit = false;

        if (map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col*gp.TileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row*gp.TileSize + eventRect[map][col][row].y;

            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) {
                if (gp.player.Direction.contentEquals(regDirection) || regDirection.contentEquals("any")){
                    hit = true;
                    previouseEventX = gp.player.worldX;
                    previouseEventY = gp.player.worldY;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;

            }
        return hit;
    }
    
    public void teleport(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue ="Teleport";
        gp.player.worldX = gp.TileSize*29;
        gp.player.worldY = gp.TileSize*20;
    } 
    
    public void healingPool(int gameState) {
         if(gp.keyH.enterPressed == true) {
             gp.gameState = gameState;
             gp.player.attackCanceled = true;
             gp.ui.currentDialogue = "You drink water.\nyour life been recovered";
             gp.player.life = gp.player.maxLife;
             gp.player.mana = gp.player.maxMana;
             gp.aSetter.setMonster();
        }
    }
    
    public void transport(int map, int col,int row, int gameState) {
        
        gp.player.attackCanceled = true;
        if(gp.keyH.enterPressed == true) {
            
            if(gp.player.hasKey > 0) {
                
                gp.playSE(2); 
                
                if (gp.currentMap == 0) {
                    gp.ui.currentDialogue = "You used a key!\nTransporting to the second map...";
                }
                if (gp.currentMap == 1) {
                    gp.ui.currentDialogue = "You used a key!\nTransporting to the first map...";
                }
                
                // Small delay for effect
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                // Load the second map
                gp.currentMap = map;
                gp.player.worldX = gp.TileSize * col;
                gp.player.worldY = gp.TileSize * row;
                previouseEventX = gp.player.worldX;
                previouseEventY = gp.player.worldY;
                canTouchEvent = false;
                
                // Play map change sound
                gp.playSE(9); // Assuming 9 is transport sound
                
            } else {
                gp.ui.currentDialogue = "The ship is locked!\nYou need a key to enter.";
                // Play locked door sound
                gp.playSE(10); // Assuming 10 is locked sound
            }
            gp.gameState = gameState;
        }
    }
}