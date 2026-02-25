package entity;

import main.GamePanel;
import object.OBJ_Key;

public class NPC_Ding extends Entity {

    private int questState = 0;
    private int requiredTotalKills = 6; // Need to kill 6 total monsters (slimes + snakes)
    private boolean keySpawned = false;

    public NPC_Ding(GamePanel gp) {
        super(gp);
        
        name = "Ding";
        type = type_npc;
        Direction = "down";
        speed = 1;
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 38; 
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        down1 = setup("/npc/npc_4_front", gp.TileSize, gp.TileSize);
    }

    @Override
    public void prepareDialoguePages() {
        if (gp.questProgress == 3 && questState == 0) {
            // First meeting after previous quests
            dialoguePages = new String[] {
                "Hello there, I'm Ding!",
                "I've been watching your progress.",
                "You've proven yourself against slimes and snakes.",
                "Now I have a final challenge for you.",
                "Defeat 6 monsters in total to prove your worth.",
                "You've already killed some, keep going!"
            };
            questState = 1;
        }
        else if (questState == 1) {
            int totalKills = (gp.player != null) ? gp.player.killCount : 0;
            
            if (totalKills < requiredTotalKills) {
                int remaining = requiredTotalKills - totalKills;
                dialoguePages = new String[] {
                    "You've killed " + totalKills + " out of " + requiredTotalKills + " monsters.",
                    "You still need to defeat " + remaining + " more.",
                    "Keep hunting! You're almost there!"
                };
            } else {
                dialoguePages = new String[] {
                    "Incredible! You've defeated all " + requiredTotalKills + " monsters!",
                    "You have proven yourself a true hero!",
                    "I've hidden a special key for you.",
                    "It's somewhere to the northwest of this ruined place.",
                    "Go find it and unlock your destiny!",
                    "The ancient door awaits!"
                };
                questState = 2;
            }
        }
        else if (questState == 2) {
            dialoguePages = new String[] {
                "You are a legend among hunters!",
                "The ancient door awaits you.",
                "Remember: the key is northwest of here!",
                "Go forth and claim your reward!"
            };
        }
        
        // Safety check
        if (dialoguePages == null) {
            dialoguePages = new String[] {"..."};
        }
    }
    
    @Override
    public void speak() {
        facePlayer();
        findMyIndex();
        prepareDialoguePages();
        
        if (dialoguePages != null && dialoguePages.length > 0) {
            gp.ui.setDialogue(dialoguePages);
        }
        
        gp.gameState = gp.dialogueState;
    }
    
    @Override
    public void afterDialogue() {
        // Spawn key when 6 monsters killed (questProgress 3, killCount >= 6)
        if (questState == 2 && gp.questProgress == 3 && gp.player != null && gp.player.killCount >= 6) {
            if (!keySpawned) {
                spawnKey(1, 10, 8); // Spawn key at map 1, col=10, row=8
                keySpawned = true;
            }
            spawnKey(1, 10, 8);
            gp.questProgress = 5; // Final quest complete
            gp.playSE(1);
            gp.ui.showMessage("All quests completed! The ancient door is now open!");
        }
    }
    
    public void spawnKey(int map, int col, int row) {
        int currentMap = gp.currentMap;
        
        // Spawn key at specified location
        for (int i = 0; i < gp.obj[currentMap].length; i++) {
            if (gp.obj[currentMap][i] == null) {
                gp.obj[currentMap][i] = new OBJ_Key(gp);
                gp.obj[currentMap][i].worldX = gp.TileSize * col;
                gp.obj[currentMap][i].worldY = gp.TileSize * row;
                gp.ui.showMessage("A key has appeared!");
                break;
            }
        }
    }
    
    @Override
    public void facePlayer() {
        if (gp.player != null) {
            switch (gp.player.Direction) {
                case "up": Direction = "down"; break;
                case "down": Direction = "up"; break;
                case "left": Direction = "right"; break;
                case "right": Direction = "left"; break;
            }
        }
    }
}