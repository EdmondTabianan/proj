package main;

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
            // if(hit(27,14,"right") == true ) {damagePit(27,16,gp.dialogueState);}
            //if(hit(27,14,"right") == true ) {teleport1(gp.dialogueState);}
            // if(hit(23,19,"any") == true ) {damagePit(27,16,gp.dialogueState);}
            if(hit(23,12, "up") == true) {healingPool(23,12,gp.dialogueState);}
            if(hit(29, 24, "up") == true ) {teleport(gp.dialogueState);}


        }
    }
    public boolean hit (int col, int row, String regDirection) {
        
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col*gp.TileSize + eventRect[col][row].x;
        eventRect[col][row].y = row*gp.TileSize + eventRect[col][row].y;

        //&& eventRect[col][row].eventDone == false 
        // if eventDone is false damagePit is working other wise the damagePit will not work unless the player walk one tiles 
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
    // public void damagePit(int col, int row, int gameState) {

    //     if (eventRect[col][row].eventDone == false) {
    //         gp.gameState = gameState;
    //         gp.ui.currentDialogue = "you fall in a trap";
    //         gp.player.life -= 1;
    //         // if damagePit happen it change eventDone to true to make it work one time
    //         // but it not happen
    //         //eventRect[col][row].eventDone = true;
    //         canTouchEvent = false;
    //     }
    // }
    // public void damagePit(int col, int row, int gameState) {

    //     if (eventRect[col][row].eventDone == false) {
    
    //         gp.gameState = gameState;
    //         gp.ui.currentDialogue = "You fell in a trap!";
    //         gp.player.life -= 1;
    
    //         // Teleport player safely next to the pit
    //         gp.player.worldX = gp.TileSize * (col - 1);
    //         gp.player.worldY = gp.TileSize * 14; // One tile above the pit
    
    //         // Prevent immediate retrigger
    //         canTouchEvent = false;
    //         //eventRect[col][row].eventDone = true;
    //     }
    // }
    
    public void healingPool(int col, int row, int gameState) {
         if(gp.keyH.enterPressed == true) {
             gp.gameState = gameState;
             gp.ui.currentDialogue = "You drink water.\nyour life been recovered";
             gp.player.life = gp.player.maxLife;
        }
    }
}
