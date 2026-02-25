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
        if (scenePhase == 0) {
            gp.bossBattleOn = true;
    
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
            
            scenePhase++;
        }
        else if (scenePhase == 1) {
            // Move camera up
            gp.player.worldY -= 2;

            if (gp.player.worldY <= gp.TileSize * 25) {
                scenePhase++;
            }
        }
        else if (scenePhase == 2) {
            
            // search boss
            for (int i = 0; i < gp.monster[i].length; i++) {

                if (gp.monster[gp.currentMap][i] != null && 
                        gp.monster[gp.currentMap][i].name == MON_anubis.MONSTER_NAME) {

                    gp.monster[gp.currentMap][i].sleep = false;
                    gp.ui.npc = gp.monster[gp.currentMap][i];
                    scenePhase++;
                    break;
                }
            }
        }
        // else if (scenePhase == 3) {
    
        //     // Find Anubis in the current map
        //     for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
        //         if (gp.monster[gp.currentMap][i] instanceof MON_anubis) {
                    
        //             MON_anubis anubis = (MON_anubis) gp.monster[gp.currentMap][i];
                    
        //             // Set the dialogue pages directly
        //             String[] dialoguePages = new String[] {
        //                 anubis.dialogues[5][0],
        //                 anubis.dialogues[5][1], 
        //                 anubis.dialogues[5][2],
        //                 anubis.dialogues[5][3]
        //             };
                    
        //             // Use UI's setDialogue method for multi-page dialogue
        //             gp.ui.setDialogue(dialoguePages);
                    
        //             // Set game state to dialogue
        //             gp.gameState = gp.dialogueState;
                    
        //             // Advance to next phase
        //             scenePhase++;
        //             break;
        //         }
        //     }
        // }
        else if (scenePhase == 3) {
    
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
                    
                    scenePhase++;
                    break;
                }
            }
        }
        else if (scenePhase == 4) {
            // return of camera to player 

            // search for dummy 
            for (int i = 0; i < gp.npc[1].length; i++) {

                if (gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)) {
                    // restore player position
                    gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
                    gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
                    // delete dummy
                    gp.npc[gp.currentMap][i] = null;
                    break;
                }
            }

            // start drawing the player
            gp.player.drawing = true;

            // Reset
            sceneNum = NA;
            scenePhase = 0;
            gp.gameState = gp.playState;

            gp.stopMusic();
            gp.playMusic(16);
        }
    }
}