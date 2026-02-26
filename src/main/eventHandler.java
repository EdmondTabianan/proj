package main;

import data.Progress;
import entity.Entity;
import entity.NPC_sailor;

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
            else if(hit(3, 20, 22, "up") == true) { speak(gp.npc[3][0]);}
            else if(hit(2, 4, 3, "any") == true) {teleport(4, 5, 3);}
            else if(hit(4, 4, 3, "any") == true) {teleport(2, 5.0f, 3);}
            else if(hit(4, 10, 10, "up") == true) {teleportToFinalStage(0, 25 , 35, gp.dialogueState);}            

            else if(hit(0, 28, 17, "up") == true) {entrance(2, 24 , 48, gp.dialogueState);}
            else if(hit(0, 29, 17, "up") == true) {entrance(2, 25 , 48, gp.dialogueState);}
            else if(hit(2, 24, 48, "down") == true) {teleport(0, 28, 17);}
            else if(hit(2, 25, 48, "down") == true) {teleport(0, 29, 17);}
            else if(hit(4, 43, 44, "any") == true) {teleport(5, 42, 44);}
            else if(hit(5, 43, 44, "any") == true) {teleport(4, 42, 44);}
            else if(hit(0, 10, 24, "left") == true) {teleport(6, 48, 18);}
            else if(hit(6, 48, 18, "right") == true) {teleport(0, 11, 24);}
            else if(hit(6, 48, 19, "right" ) == true) {teleport(0, 11, 24);}
            else if(hit(0, 5, 19, "down") == true) {teleport(6, 13, 1);}
            else if(hit(6, 13, 1, "up") == true) {teleport(0, 5, 17);}
            else if(hit(6, 14, 1, "up" ) == true) {teleport(0, 5, 17);}
            
            // Damage Pit
            else if(hit(1, 41, 39, "any") == true) {damagePit(gp.dialogueState);}
            else if(hit(1, 42, 33, "any") == true) {damagePit(gp.dialogueState);}
            else if(hit(1, 38, 27, "any") == true) {damagePit(gp.dialogueState);}
            else if(hit(5, 25, 39, "any" ) == true) {AnubisBoss();}
            else if(hit(5, 25, 6, "up")== true) {Ending();}
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
    
    public void teleport(int map, float col, int row) {
     
            // Despawn current map assets
            gp.aSetter.despawnMonsters(gp.currentMap);
            gp.aSetter.despawnNPCs(gp.currentMap);
            gp.aSetter.despawnObjects(gp.currentMap);
            gp.aSetter.despawnInteractiveTiles(gp.currentMap);
            
            canTouchEvent = false;

            // Use LoadingManager for transition
            gp.loadingManager.startTransition(map, col, row);
            gp.playSE(13);
    }

    public void damagePit(int gameState) {
        gp.gameState = gameState;
        gp.player.attackCanceled = true;
        
        // Create proper dialogue array for multi-page support
        String[] dialoguePages = new String[] {
            "You fell into a pit!",
            "You lost some life."
        };
        
        // Use the UI's setDialogue method
        gp.ui.setDialogue(dialoguePages);
        
        gp.player.life -= 1;
        if (gp.player.life < 0) {
            gp.player.life = 0;
        }
        
        // Teleport player back based on their direction
        switch (gp.player.Direction) {
            case "up":
                gp.player.worldY += gp.TileSize;
                break;
            case "down":
                gp.player.worldY -= gp.TileSize;
                break;
            case "left":
                gp.player.worldX += gp.TileSize;
                break;
            case "right":
                gp.player.worldX -= gp.TileSize;
                break;
        }
        
        // Set invincibility briefly to prevent immediate re-triggering
        gp.player.invincible = true;
        gp.player.invincibleCounter = 0;
        
        // Play a sound effect
        gp.playSE(6); // Hurt sound
        
        // Update previous event position to prevent immediate re-trigger
        previouseEventX = gp.player.worldX;
        previouseEventY = gp.player.worldY;
    }
    
    public void healingPool(int gameState) {
        if(gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            
            // Create proper dialogue array
            String[] dialoguePages = new String[] {
                "You drink the healing water.",
                "Your life and mana have been fully recovered!"
            };
            
            // Use the UI's setDialogue method which properly sets dialoguePages
            gp.ui.setDialogue(dialoguePages);
            
            // Set game state to dialogue state
            gp.gameState = gp.dialogueState;
            
            // Heal the player immediately (current behavior)
            gp.player.life = gp.player.maxLife;
            gp.player.mana = gp.player.maxMana;
            
            // Play a healing sound effect
            gp.playSE(1); // Assuming index 1 is a healing sound
        }
    }
    
    public void entrance(int map, int col, int row, int gameState) {
        // First check if player presses enter
        if(gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            
            if(gp.player.hasKey >= 2) {
                
                // Create proper dialogue array for successful entry
                String[] dialoguePages = new String[] {
                    "You used 2 keys to enter the Pyramid!",
                    "Transporting to the Pyramid..."
                };
                gp.ui.setDialogue(dialoguePages);
                
                // Set game state to dialogue state
                gp.gameState = gp.dialogueState;
                
                // Play sounds
                gp.playSE(2);
                gp.playSE(13);
                
                // TELEPORT DIRECTLY - just like in transport()
                teleport(map, col, row);
                
            } 
            else if (gp.player.hasKey == 1) {
                // Create proper dialogue array for having 1 key
                String[] dialoguePages = new String[] {
                    "The pyramid entrance is sealed!",
                    "You need 2 ancient keys to enter.",
                    "You have only 1 key."
                };
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(10);
            }
            else if (gp.player.hasKey == 0) {
                // Create proper dialogue array for having 0 keys
                String[] dialoguePages = new String[] {
                    "The pyramid entrance is sealed!",
                    "Find 2 ancient keys to enter."
                };
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(10);
            }
            else if (gp.player.hasKey > 2) {
                // Create proper dialogue array for having too many keys
                String[] dialoguePages = new String[] {
                    "You have " + gp.player.hasKey + " keys!",
                    "The pyramid requires exactly 2 ancient keys."
                };
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(10);
            }
        }
    }
    
    public void transport(int map, int col, int row, int gameState) {
        // Only transport if ENTER is pressed
        if(gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            
            // Make player and sailor face each other
            facePlayerToSailor();
            
            // Set the NPC index to the sailor
            findAndSetSailorIndex();
            
            String[] dialoguePages;
            
            // GREETINGS - First time meeting sailor (questProgress 0)
            if (gp.questProgress == 0) {
                dialoguePages = new String[] {
                    "Ahoy there, Hunter!",
                    "Welcome to the island!",
                    "You look like you're on an adventure.",
                    "You'll need to finish the quests first.",
                    "Talk to Vhong in the village.",
                    "He'll tell you what needs to be done.",
                    "The sailor points toward the village."
                };
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(10); // Denied sound
            }
            // HAS PYRAMID KEY - Player has the pyramid key (hasKey == 2) - FINAL QUEST
            else if (gp.player.hasKey == 2) {
                // Teleport to the destination map specified in the event
                gp.player.Direction = "down"; // Face down to show readiness to board
                
                // Determine which map we're going to based on current map
                if (gp.currentMap == 0) {
                    // On first map, going to pyramid island (map 2)
                    dialoguePages = new String[] {
                        "You have the PYRAMID KEY! The ancient prophecy speaks of this!",
                        "The lost tomb deep within the pyramid awaits your discovery.",
                        "Legends say only the worthy can find its hidden chambers.",
                        "The sailor unties the ropes with reverence.",
                        "The ship sets sail toward the sacred pyramid island...",
                        "Arriving at the PYRAMID OF THE ANCIENTS!",
                        "Find the lost tomb and claim your destiny!"
                    };
                    
                    // Teleport to pyramid map (assuming map 2 is pyramid)
                    teleport(2, 10, 10); // Adjust coordinates as needed
                } 
                else if (gp.currentMap == 2) {
                    // On pyramid map, returning to first map
                    dialoguePages = new String[] {
                        "Welcome back from the pyramid, tomb explorer!",
                        "Did you find the lost tomb?",
                        "The ancient spirits watch over you.",
                        "Ready to return to the main island?",
                        "The sailor unties the ropes.",
                        "Returning to first map..."
                    };
                    
                    // Return to main island
                    teleport(0, map, row);
                }
                else {
                    // Default teleport
                    teleport(map, col, row);
                    dialoguePages = new String[] {
                        "Setting sail to new lands...",
                        "The pyramid key glows with ancient power!"
                    };
                }
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(13); // Ship sailing sound
            }
            // HAS SNAKE KEY - Player has the snake key (hasKey == 1)
            else if (gp.player.hasKey == 1) {
                // Teleport to the destination map specified in the event
                gp.player.Direction = "down"; // Face down to show readiness to board
                
                // Determine which map we're going to based on current map
                if (gp.currentMap == 0) {
                    // On first map, going to second map (intermediate area)
                    dialoguePages = new String[] {
                        "You have the snake key! You've proven yourself!",
                        "But the true challenge lies beyond...",
                        "Find the pyramid key to unlock the lost tomb.",
                        "The sailor unties the ropes.",
                        "The ship sets sail across the sea...",
                        "Arriving at the second map!"
                    };
                } else {
                    // On second map, returning to first map
                    dialoguePages = new String[] {
                        "Welcome back, brave traveler!",
                        "Ready to return to the main island?",
                        "The sailor unties the ropes.",
                        "Returning to first map..."
                    };
                }
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                
                // Teleport to the destination map using the parameters from the event
                teleport(map, col, row);
                
                gp.playSE(13); // Ship sailing sound
            }
            // TALK TO VHONG - Player hasn't completed quest yet (questProgress 1, no key)
            else if (gp.player.hasKey == 0 && gp.questProgress == 1) {
                dialoguePages = new String[] {
                    "The sailor leans on his ship.",
                    "Have you spoken to Vhong yet?",
                    "He's the elder in the desert village.",
                    "He mentioned something about snakes",
                    "blocking the path to an ancient key.",
                    "Defeat the snakes and bring back the key.",
                    "Then we can talk about sailing."
                };
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(10); // Denied sound
            }
            // TALK TO BEVERLY - Player has completed Vhong's quest but not Beverly's (questProgress 2)
            else if (gp.questProgress == 2) {
                dialoguePages = new String[] {
                    "The sailor scratches his chin.",
                    "You've helped Vhong, I see.",
                    "But there's someone else you should meet.",
                    "Talk to Beverly in the village.",
                    "She has a task involving snakes.",
                    "Complete her quest to get the first key.",
                    "Then seek the pyramid key for the lost tomb!"
                };
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(10); // Denied sound
            }
            // SNAKE QUEST ACTIVE - Player is on snake quest (questProgress 3)
            else if (gp.questProgress == 3) {
                dialoguePages = new String[] {
                    "I see you're hunting snakes.",
                    "That's good - you'll need that snake key.",
                    "But the real treasure lies in the pyramid.",
                    "Defeat all three snakes first,",
                    "then seek the pyramid key for the lost tomb!"
                };
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(10); // Denied sound
            }
            // READY FOR PYRAMID - Player has completed all but needs pyramid key (questProgress 4)
            else if (gp.questProgress == 4 && gp.player.hasKey == 1) {
                dialoguePages = new String[] {
                    "You have the snake key, but the pyramid requires more.",
                    "Deep in the second map, a pyramid key is hidden.",
                    "Find it, and the lost tomb will open for you.",
                    "The tomb holds treasures beyond imagination!",
                    "Return to me when you hold the PYRAMID KEY."
                };
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(10); // Denied sound - can't sail to pyramid yet
            }
            // READY TO SAIL TO PYRAMID - Player has pyramid key (questProgress 4, hasKey == 2)
            else if (gp.questProgress >= 4 && gp.player.hasKey == 2) {
                if (gp.currentMap == 0) {
                    dialoguePages = new String[] {
                        "You have the PYRAMID KEY! The lost tomb awaits!",
                        "Legends speak of three chambers within the pyramid.",
                        "Only the worthy can reach the innermost tomb.",
                        "Ready to set sail for the pyramid, hero?",
                        "The wind is good today.",
                        "Hold tight as we depart for the PYRAMID OF THE ANCIENTS!"
                    };
                } else if (gp.currentMap == 2) {
                    dialoguePages = new String[] {
                        "Welcome back, tomb explorer!",
                        "The pyramid's secrets are yours to discover.",
                        "Find the three chambers and reach the lost tomb!",
                        "Ready to return to the main island?"
                    };
                } else {
                    dialoguePages = new String[] {
                        "Ready to set sail, key bearer?",
                        "The pyramid awaits your exploration!",
                        "The lost tomb is your final destination."
                    };
                }
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                
                // Teleport based on destination
                if (gp.currentMap == 0) {
                    // Going to pyramid map
                    teleport(2, 10, 10); // Adjust coordinates
                } else {
                    // Returning from pyramid
                    teleport(0, col, row);
                }
                
                gp.playSE(13); // Ship sailing sound
            }
            // ALL QUESTS COMPLETE - Player has explored pyramid (questProgress 5)
            else if (gp.questProgress >= 5) {
                if (gp.currentMap == 0) {
                    dialoguePages = new String[] {
                        "You found the lost tomb! You are a LEGEND!",
                        "The ancient treasure is yours!",
                        "Would you like to return to the pyramid to explore more?",
                        "The sailor bows respectfully."
                    };
                } else {
                    dialoguePages = new String[] {
                        "The pyramid acknowledges you, tomb conqueror!",
                        "You have claimed the treasure of the lost tomb!",
                        "Ready to return home, legend?"
                    };
                }
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                
                // Allow teleportation even after completion
                if (gp.currentMap == 0) {
                    teleport(2, 10, 10);
                } else {
                    teleport(0, col, row);
                }
                
                gp.playSE(13); // Ship sailing sound
            }
            // Default fallback
            else {
                dialoguePages = new String[] {
                    "The sailor shakes his head.",
                    "Not yet, adventurer.",
                    "Complete the tasks given to you.",
                    "Seek the snake key, then the pyramid key.",
                    "The lost tomb awaits the worthy."
                };
                
                gp.ui.setDialogue(dialoguePages);
                gp.gameState = gp.dialogueState;
                gp.playSE(10); // Denied sound
            }
            
            // Reset enterPressed to prevent multiple triggers
            gp.keyH.enterPressed = false;
        }
    }

private void facePlayerToSailor() {
    if (gp.player != null) {
        // Make player face the direction of the sailor
        // Based on ship location
        if (gp.currentMap == 0) {
            // On first map, sailor is likely left of player
            gp.player.Direction = "left";
            
            // Find the sailor and make him face right (toward the player)
            if (gp.npc != null && gp.npc[gp.currentMap] != null) {
                for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
                    if (gp.npc[gp.currentMap][i] instanceof NPC_sailor) {
                        gp.npc[gp.currentMap][i].Direction = "right";
                        break;
                    }
                    
                }
            }
        } else {
            // On second map, adjust as needed
            gp.player.Direction = "down";
            
            // Find the sailor and make him face left (toward the player)
            if (gp.npc != null && gp.npc[gp.currentMap] != null) {
                for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
                    if (gp.npc[gp.currentMap][i] instanceof NPC_sailor) {
                        gp.npc[gp.currentMap][i].Direction = "left";
                        break;
                    }
                }
            }
        }
    }
}

    /**
     * Helper method to find and set the sailor's index in UI
     */
    private void findAndSetSailorIndex() {
        if (gp.npc == null || gp.npc[gp.currentMap] == null) return;
        
        for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
            if (gp.npc[gp.currentMap][i] instanceof NPC_sailor) {
                gp.ui.npcIndex = i;
                break;
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
            gp.player.attackCanceled = true;
            entity.speak();
        }
    }
    public void AnubisBoss() {
        if (!gp.bossBattleOn && Progress.anubisBossDeafeated == false) {
            gp.gameState = gp.cutsceneState;
            gp.csManager.sceneNum = gp.csManager.anubis;
            gp.csManager.scenePhase = 0; // Important: Reset the phase!
        }
    }
    public void Ending() {
        if (gp.keyH.enterPressed == true) {
            gp.player.attackCanceled = true;
            gp.keyH.enterPressed = false;
            
            // Start the ending sequence
            gp.endingManager.startEnding();
            gp.gameState = gp.endingState;
        }
    }
}