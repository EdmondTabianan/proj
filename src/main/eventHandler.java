package main;

import entity.Entity;

public class eventHandler {
    GamePanel gp;
    EventRect eventRect[][][];

    int previouseEventX, previouseEventY;
    boolean canTouchEvent = true;
    int tempMap, tempRow;
    float tempColFloat;

    public eventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;
        while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
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
            
            else if(hit(1, 7, 29, "up") == true) {teleport(3, 24.0f, 35);}
            else if(hit(3, 24, 36, "down") == true) {teleport(1, 7.0f, 29);}
            else if(hit(3, 20, 22, "up") == true) {speak(gp.npc[3][1]);}
            else if(hit(2, 4, 3, "any") == true) {teleport(4, 4.0f, 3);}
            else if(hit(4, 4, 3, "any") == true) {teleport(2, 4.0f, 3);}
            else if(hit(4, 10, 10, "up") == true) {teleportToFinalStage(0, 25 , 35,gp.dialogueState);}            
            else if(hit(0, 28, 17, "up") == true || hit(0, 29, 17, "up") == true) {
                entrance(2, 24, 48, gp.dialogueState);
            }
            else if(hit(2, 24, 48, "down") == true) {teleport(0, 28, 17);}
            else if(hit(2, 25, 48, "down") == true) {teleport(0, 29, 17);}
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
    
    public void teleport(int map, float col, int row) {
        gp.gameState = gp.transitionState;
        tempMap = map;
        tempColFloat = col;
        tempRow = row;
        canTouchEvent = false;
        gp.playSE(13);
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
    public void entrance(int map, int col, int row, int gameState) {
    
        // First check if player presses enter
        if(gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            
            if(gp.player.hasKey >= 2) {  // Changed from == to >= for flexibility
                // Consume 2 keys for pyramid entrance
                gp.player.hasKey -= 2;
                
                // Show message
                if (gp.currentMap == 0) {
                    gp.ui.currentDialogue = "You used 2 keys to enter the Pyramid!\nTransporting to the Pyramid...";
                    gp.ui.showMessage("You used 2 keys to enter the Pyramid!");
                }
                
                // Set transition parameters
                tempMap = map;
                tempColFloat = col;
                tempRow = row;
                canTouchEvent = false;
                
                // Play sounds
                gp.playSE(2);  // Key use sound
                gp.playSE(13); // Transport sound
                
                // IMPORTANT: Set game state to transition
                gp.gameState = gp.transitionState;
                
            } 
            else if (gp.player.hasKey == 1) {
                gp.ui.currentDialogue = "The pyramid entrance is sealed!\nYou need 2 ancient keys.\nYou have only 1 key.";
                gp.ui.showMessage("Need 2 keys! You have only 1.");
                gp.playSE(10); // Locked sound
                gp.gameState = gameState; // Show dialogue
            }
            else if (gp.player.hasKey == 0) {
                gp.ui.currentDialogue = "The pyramid entrance is sealed!\nFind 2 ancient keys to enter.";
                gp.ui.showMessage("The pyramid is locked! Need 2 keys.");
                gp.playSE(10); // Locked sound
                gp.gameState = gameState; // Show dialogue
            }
            else if (gp.player.hasKey > 2) {
                gp.ui.currentDialogue = "You have " + gp.player.hasKey + " keys!\nThe pyramid requires exactly 2 ancient keys.";
                gp.ui.showMessage("Pyramid needs exactly 2 keys!");
                gp.playSE(10); // Locked sound
                gp.gameState = gameState; // Show dialogue
            }
        }
        else {
            // If player is just standing on the entrance but hasn't pressed enter
            // You might want to show a hint
            if(gp.player.worldX/gp.TileSize == 28 && gp.player.worldY/gp.TileSize == 16) {
                // Optional: Show "Press ENTER to use keys" hint
                // gp.ui.showMessage("Press ENTER to use keys");
            }
        }
    }
    public void transport(int map, int col,int row, int gameState) {
        
        gp.player.attackCanceled = true;
        if(gp.keyH.enterPressed == true) {
            
            if(gp.player.hasKey > 0) {
                gp.gameState = gp.gameState;
                gp.playSE(2); 
                
                if (gp.currentMap == 0) {
                    gp.ui.currentDialogue = "arrived in second map...";
                    gp.ui.showMessage("Arrived in second map...");
                }
                if (gp.currentMap == 1) {
                    gp.ui.currentDialogue = "arrived in first map...";
                    gp.ui.showMessage("arrived in first map...");
                }
                
                // Load the second map
                tempMap = map;
                tempColFloat = col;
                tempRow = row;
                canTouchEvent = false;
                gp.gameState = gp.transitionState;

                // Play map change sound
                gp.playSE(13); 
                
            } 
            else if (gp.player.hasKey == 0) {
                gp.ui.currentDialogue = "The ship is locked!\nYou need a key to enter.";
                gp.ui.showMessage("The ship is locked!");
                // Play locked door sound
                gp.playSE(10);
            }
            
        }
    }

    public void teleportToFinalStage(int map, int col,int row, int gameState) {
        
        gp.player.attackCanceled = true;
        if(gp.keyH.enterPressed == true) {
            
            if(gp.player.hasKey > 2) {
                
                gp.playSE(2); 
                
                if (gp.currentMap == 0) {
                    gp.ui.currentDialogue = "You used a key!\nTransporting to the second map...";
                }
                if (gp.currentMap == 2) {
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
                gp.playSE(9);
                
            } else {
                gp.ui.currentDialogue = "the door is lock find the all keys.";
                // Play locked door sound
                gp.playSE(10);
            }
            gp.gameState = gameState;
        }
    }
    
    public void speak(Entity entity) {
        if(gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            entity.speak();
        }
    }
}