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
            else if(hit(0, 46, 42, "down") == true) {transport(1, 24 , 42, gp.dialogueState);}
            else if(hit(1, 24, 42, "down") == true) {transport(0, 46 , 42, gp.dialogueState);}
            
            else if(hit(1, 9, 29, "up") == true) {teleport(3, 24.0f, 35);}
            else if(hit(3, 24, 36, "down") == true) {teleport(1, 9, 29);}
            else if(hit(3, 20, 22, "up") == true) {speak(gp.npc[3][1]);}
            else if(hit(2, 4, 3, "any") == true) {teleport(4, 4.0f, 3);}
            else if(hit(4, 4, 3, "any") == true) {teleport(2, 4.0f, 3);}
            else if(hit(4, 10, 10, "up") == true) {teleportToFinalStage(0, 25 , 35, gp.dialogueState);}            
            else if(hit(0, 28, 17, "up") == true || hit(0, 29, 17, "up") == true) {
                entrance(2, 24, 48, gp.dialogueState);
            }
            else if(hit(2, 24, 48, "down") == true) {teleport(0, 28, 17);}
            else if(hit(2, 25, 48, "down") == true) {teleport(0, 29, 17);}
            else if(hit(4, 43, 44, "any") == true) {teleport(5, 43, 44);}
            else if(hit(5, 43, 44, "any") == true) {teleport(4, 43, 44);}
            else if(hit(0, 10, 24, "left") == true) {teleport(6, 48, 18);}
            else if(hit(6, 48, 18, "right") == true) {teleport(0, 11, 24);}
            else if(hit(6, 48, 19, "right" ) == true) {teleport(0, 11, 24);}
            else if(hit(0, 5, 19, "down") == true) {teleport(6, 48, 18);}
            else if(hit(6, 13, 1, "up") == true) {teleport(0, 5, 17);}
            else if(hit(6, 14, 1, "up" ) == true) {teleport(0, 5, 17);}
        }
    }

    public boolean hit (int map, int col, int row, String regDirection) {
        boolean hit = false;

        if (map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col * gp.TileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.TileSize + eventRect[map][col][row].y;

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
    
    // ============ FIXED TELEPORT METHOD ============
    public void teleport(int map, float col, int row) {
        // Only teleport if ENTER is pressed
        if(gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            
            // Despawn current map assets
            gp.aSetter.despawnMonsters(gp.currentMap);
            gp.aSetter.despawnNPCs(gp.currentMap);
            gp.aSetter.despawnObjects(gp.currentMap);
            gp.aSetter.despawnInteractiveTiles(gp.currentMap);
            
            // Use LoadingManager for transition
            gp.loadingManager.startTransition(map, col, row);
            gp.playSE(13);
        }
    }
    
    public void healingPool(int gameState) {
        if(gp.keyH.enterPressed == true) {
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.ui.currentDialogue = "You drink water.\nYour life has been recovered!";
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
        }
    }
    
    // ============ FIXED ENTRANCE METHOD ============
    public void entrance(int map, int col, int row, int gameState) {
        // First check if player presses enter
        if(gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            
            if(gp.player.hasKey >= 2) {
                
                // Show message
                if (gp.currentMap == 0) {
                    gp.ui.currentDialogue = "You used 2 keys to enter the Pyramid!\nTransporting to the Pyramid...";
                    gp.ui.showMessage("You used 2 keys to enter the Pyramid!");
                }
                
                // Despawn current map assets
                gp.aSetter.despawnMonsters(gp.currentMap);
                gp.aSetter.despawnNPCs(gp.currentMap);
                gp.aSetter.despawnObjects(gp.currentMap);
                gp.aSetter.despawnInteractiveTiles(gp.currentMap);
                
                // Use LoadingManager for transition
                gp.loadingManager.startTransition(map, col, row);
                
                // Play sounds
                gp.playSE(2);
                gp.playSE(13);
            } 
            else if (gp.player.hasKey == 1) {
                gp.ui.currentDialogue = "The pyramid entrance is sealed!\nYou need 2 ancient keys.\nYou have only 1 key.";
                gp.ui.showMessage("Need 2 keys! You have only 1.");
                gp.playSE(10);
                gp.gameState = gameState;
            }
            else if (gp.player.hasKey == 0) {
                gp.ui.currentDialogue = "The pyramid entrance is sealed!\nFind 2 ancient keys to enter.";
                gp.ui.showMessage("The pyramid is locked! Need 2 keys.");
                gp.playSE(10);
                gp.gameState = gameState;
            }
            else if (gp.player.hasKey > 2) {
                gp.ui.currentDialogue = "You have " + gp.player.hasKey + " keys!\nThe pyramid requires exactly 2 ancient keys.";
                gp.ui.showMessage("Pyramid needs exactly 2 keys!");
                gp.playSE(10);
                gp.gameState = gameState;
            }
        }
    }
    
    public void transport(int map, int col, int row, int gameState) {
        // Only transport if ENTER is pressed
        if(gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            
            if(gp.player.hasKey > 0) {
                if (gp.currentMap == 0) {
                    gp.ui.currentDialogue = "Arrived in second map...";
                    gp.ui.showMessage("Arrived in second map...");
                }
                if (gp.currentMap == 1) {
                    gp.ui.currentDialogue = "Arrived in first map...";
                    gp.ui.showMessage("Arrived in first map...");
                }
                
                // Despawn current map assets
                gp.aSetter.despawnMonsters(gp.currentMap);
                gp.aSetter.despawnNPCs(gp.currentMap);
                gp.aSetter.despawnObjects(gp.currentMap);
                gp.aSetter.despawnInteractiveTiles(gp.currentMap);
                
                // Use LoadingManager for transition
                gp.loadingManager.startTransition(map, col, row);
                gp.playSE(13);
            } 
            else if (gp.player.hasKey == 0) {
                gp.ui.currentDialogue = "The ship is locked!\nYou need a key to enter.";
                gp.ui.showMessage("The ship is locked!");
                gp.playSE(10);
                gp.gameState = gameState;
            }
        }
    }

    // ============ FIXED TELEPORT TO FINAL STAGE METHOD ============
    public void teleportToFinalStage(int map, int col, int row, int gameState) {
        if(gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            
            if(gp.player.hasKey > 2) {
                // Despawn current map assets
                gp.aSetter.despawnMonsters(gp.currentMap);
                gp.aSetter.despawnNPCs(gp.currentMap);
                gp.aSetter.despawnObjects(gp.currentMap);
                gp.aSetter.despawnInteractiveTiles(gp.currentMap);
                
                gp.playSE(2);
                
                if (gp.currentMap == 0) {
                    gp.ui.currentDialogue = "You used keys!\nTransporting to the final stage...";
                }
                if (gp.currentMap == 2) {
                    gp.ui.currentDialogue = "You used keys!\nReturning to the first map...";
                }
                
                // Use LoadingManager for transition
                gp.loadingManager.startTransition(map, col, row);
                gp.playSE(9);
            } else {
                gp.ui.currentDialogue = "The door is locked! Find all the keys.";
                gp.playSE(10);
                gp.gameState = gameState;
            }
        }
    }
    
    public void speak(Entity entity) {
        if(gp.keyH.enterPressed == true) {
            if (entity == null) {
                gp.ui.showMessage("Warning: Tried to speak with null NPC at map " + gp.currentMap);
                return;
            }
            gp.player.attackCanceled = true;
            entity.speak();
        }
    }
}