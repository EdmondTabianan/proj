package main;

import java.awt.Graphics2D;

import entity.PlayerDummy;
import monster.MON_anubis;
import object.OBJ_IronDoor;

public class CutsceneManager {

    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;
    
    // Store original player position for camera return
    private int originalPlayerY;

    // scene number
    public final int NA = 0;
    public final int anubis = 1;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        switch (sceneNum) {
            case anubis: scene_AnubisBoss(); break;
        } 
    }
    
    public void scene_AnubisBoss() {
        // PHASE 0: Setup - spawn door and dummy player
        if (scenePhase == 0) {
            gp.bossBattleOn = true;
            
            // STORE ORIGINAL PLAYER POSITION
            originalPlayerY = gp.player.worldY;
    
            // Place the iron door at (25, 40) to block the exit
            for(int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                if (gp.obj[gp.currentMap][i] == null) {
                    gp.obj[gp.currentMap][i] = new OBJ_IronDoor(gp);
                    gp.obj[gp.currentMap][i].worldX = gp.TileSize * 25;
                    gp.obj[gp.currentMap][i].worldY = gp.TileSize * 40;
                    gp.obj[gp.currentMap][i].temp = true;
                    gp.playSE(2);
                    break;
                }
            }
            
            // Get player's character choice
            int playerCharacter = gp.player.characterused;
            
            // Search vacant spot for dummy player
            for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
                if (gp.npc[gp.currentMap][i] == null) {
                    gp.npc[gp.currentMap][i] = new entity.PlayerDummy(gp, playerCharacter);
                    gp.npc[gp.currentMap][i].worldX = gp.player.worldX;     
                    gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                    gp.npc[gp.currentMap][i].Direction = gp.player.Direction;
                    gp.npc[gp.currentMap][i].temp = true; // Mark as temporary
                    break;
                }
            }
    
            gp.player.drawing = false; // Hide real player
            
            scenePhase = 1;
        }
        
        // PHASE 1: Move camera up to Anubis
        else if (scenePhase == 1) {
            // Move camera up
            gp.player.worldY -= 2;

            if (gp.player.worldY <= gp.TileSize * 25) {
                scenePhase = 2;
            }
        }
        
        // PHASE 2: Wake up Anubis
        else if (scenePhase == 2) {
            for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
                if (gp.monster[gp.currentMap][i] != null && 
                        gp.monster[gp.currentMap][i].name == MON_anubis.MONSTER_NAME) {
                    
                    gp.ui.npc = gp.monster[gp.currentMap][i];
                    scenePhase = 3;
                    break;
                }
            }
        }
        
        // PHASE 3: Show Anubis dialogue
        else if (scenePhase == 3) {
    
            // Only set dialogue if not already in dialogue state
            if (gp.gameState != gp.dialogueState) {
                
                // Find Anubis in the current map
                for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
                    
                    if (gp.monster[gp.currentMap][i] instanceof MON_anubis) {
                        
                        MON_anubis anubis = (MON_anubis) gp.monster[gp.currentMap][i];
                        
                        // Create and fill the dialogue pages array
                        String[] dialoguePages = new String[4];
                        dialoguePages[0] = anubis.dialogues[5][0];
                        dialoguePages[1] = anubis.dialogues[5][1];
                        dialoguePages[2] = anubis.dialogues[5][2];
                        dialoguePages[3] = anubis.dialogues[5][3];
                        
                        // Set the dialogue
                        gp.ui.setDialogue(dialoguePages);
                        gp.gameState = gp.dialogueState;
                        
                        break;
                    }
                }
            }
            // If already in dialogue state, do nothing and wait
        }
        
        // PHASE 4: Move camera back down to player
        else if (scenePhase == 4) {
            
            // Move camera back down to original position
            if (gp.player.worldY < originalPlayerY) {
                gp.player.worldY += 4;
                if (gp.player.worldY > originalPlayerY) {
                    gp.player.worldY = originalPlayerY;
                }
            } else {
                // Camera is back at original position, do cleanup
                
                // Search for dummy player
                for (int i = 0; i < gp.npc[gp.currentMap].length; i++) {
                    if (gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)) {
                        // Restore player position (just in case)
                        gp.monster[gp.currentMap][i].sleep = false;
                        gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
                        gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
                        // Delete dummy
                        gp.npc[gp.currentMap][i] = null;
                        break;
                    }
                }
        
                // Start drawing the player again
                gp.player.drawing = true;
        
                // Reset cutscene
                sceneNum = NA;
                scenePhase = 0;
                gp.gameState = gp.playState;
        
                // Change music to boss battle music
                gp.stopMusic();
                gp.playMusic(16);
            }
        }
    }
}