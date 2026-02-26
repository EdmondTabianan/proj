package main;

import entity.Entity;

public class CollisionChecker {
    
    GamePanel gp;
    
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }
    
    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
        
        int entityLeftCol = entityLeftWorldX / gp.TileSize;
        int entityRightCol = entityRightWorldX / gp.TileSize;
        int entityTopRow = entityTopWorldY / gp.TileSize;
        int entityBottomRow = entityBottomWorldY / gp.TileSize;
        
        int tileNum1, tileNum2;
        
        switch(entity.Direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.TileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.TileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.TileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.TileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
    
    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        
        for(int i = 0; i < gp.obj[1].length; i++) {
            if(gp.obj[gp.currentMap][i] != null) {
                // Get entity's solid area position
                int entityLeftWorldX = entity.worldX + entity.solidArea.x;
                int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
                int entityTopWorldY = entity.worldY + entity.solidArea.y;
                int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
                
                // Get object's solid area position
                int objLeftWorldX = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;
                int objRightWorldX = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x + gp.obj[gp.currentMap][i].solidArea.width;
                int objTopWorldY = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;
                int objBottomWorldY = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y + gp.obj[gp.currentMap][i].solidArea.height;
                
                switch(entity.Direction) {
                    case "up":
                        entityBottomWorldY = entityTopWorldY;
                        entityTopWorldY = entityTopWorldY - entity.speed;
                        break;
                    case "down":
                        entityTopWorldY = entityBottomWorldY;
                        entityBottomWorldY = entityBottomWorldY + entity.speed;
                        break;
                    case "left":
                        entityRightWorldX = entityLeftWorldX;
                        entityLeftWorldX = entityLeftWorldX - entity.speed;
                        break;
                    case "right":
                        entityLeftWorldX = entityRightWorldX;
                        entityRightWorldX = entityRightWorldX + entity.speed;
                        break;
                }
                
                // Check collision between entity and object
                if(entityRightWorldX > objLeftWorldX && 
                   entityLeftWorldX < objRightWorldX && 
                   entityBottomWorldY > objTopWorldY && 
                   entityTopWorldY < objBottomWorldY) {
                    
                    if(gp.obj[gp.currentMap][i].collision == true) {
                        entity.collisionOn = true;
                    }
                    if(player == true) {
                        index = i;
                    }
                }
            }
        }
        return index;
    }
    
    public int checkEntity(Entity entity, Entity[][] target) {
        int index = 999;
        
        for(int i = 0; i < target[1].length; i++) {
            if(target[gp.currentMap][i] != null) {
                // Get entity's solid area position
                int entityLeftWorldX = entity.worldX + entity.solidArea.x;
                int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
                int entityTopWorldY = entity.worldY + entity.solidArea.y;
                int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
                
                // Get target's solid area position
                int targetLeftWorldX = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                int targetRightWorldX = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x + target[gp.currentMap][i].solidArea.width;
                int targetTopWorldY = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;
                int targetBottomWorldY = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y + target[gp.currentMap][i].solidArea.height;
                
                switch(entity.Direction) {
                    case "up":
                        entityBottomWorldY = entityTopWorldY;
                        entityTopWorldY = entityTopWorldY - entity.speed;
                        break;
                    case "down":
                        entityTopWorldY = entityBottomWorldY;
                        entityBottomWorldY = entityBottomWorldY + entity.speed;
                        break;
                    case "left":
                        entityRightWorldX = entityLeftWorldX;
                        entityLeftWorldX = entityLeftWorldX - entity.speed;
                        break;
                    case "right":
                        entityLeftWorldX = entityRightWorldX;
                        entityRightWorldX = entityRightWorldX + entity.speed;
                        break;
                }
                
                // Check collision between entity and target
                if(entityRightWorldX > targetLeftWorldX && 
                   entityLeftWorldX < targetRightWorldX && 
                   entityBottomWorldY > targetTopWorldY && 
                   entityTopWorldY < targetBottomWorldY) {
                    
                    if(entity != target[gp.currentMap][i]) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }
            }
        }
        return index;
    }
    
    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;
        
        // Get entity's solid area position
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
        
        // Get player's solid area position
        int playerLeftWorldX = gp.player.worldX + gp.player.solidArea.x;
        int playerRightWorldX = gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width;
        int playerTopWorldY = gp.player.worldY + gp.player.solidArea.y;
        int playerBottomWorldY = gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height;
        
        switch(entity.Direction) {
            case "up":
                entityBottomWorldY = entityTopWorldY;
                entityTopWorldY = entityTopWorldY - entity.speed;
                break;
            case "down":
                entityTopWorldY = entityBottomWorldY;
                entityBottomWorldY = entityBottomWorldY + entity.speed;
                break;
            case "left":
                entityRightWorldX = entityLeftWorldX;
                entityLeftWorldX = entityLeftWorldX - entity.speed;
                break;
            case "right":
                entityLeftWorldX = entityRightWorldX;
                entityRightWorldX = entityRightWorldX + entity.speed;
                break;
        }
        
        // Check collision between entity and player
        if(entityRightWorldX > playerLeftWorldX && 
           entityLeftWorldX < playerRightWorldX && 
           entityBottomWorldY > playerTopWorldY && 
           entityTopWorldY < playerBottomWorldY) {
            
            contactPlayer = true;
        }
        
        return contactPlayer;
    }
}