package main;

import entity.Entity;
import java.awt.Rectangle;

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
        
        // BOUNDS CHECKING - Prevent array index out of bounds
        if (entityLeftCol < 0) entityLeftCol = 0;
        if (entityRightCol >= gp.maxWorldCol) entityRightCol = gp.maxWorldCol - 1;
        if (entityTopRow < 0) entityTopRow = 0;
        if (entityBottomRow >= gp.maxWorldRow) entityBottomRow = gp.maxWorldRow - 1;
        
        int tileNum1, tileNum2; 
    
        switch (entity.Direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.TileSize;
                // Bounds check
                if (entityTopRow < 0) entityTopRow = 0;
                
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.TileSize;
                // Bounds check
                if (entityBottomRow >= gp.maxWorldRow) entityBottomRow = gp.maxWorldRow - 1;
                
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.TileSize;
                // Bounds check
                if (entityLeftCol < 0) entityLeftCol = 0;
                
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.TileSize;
                // Bounds check
                if (entityRightCol >= gp.maxWorldCol) entityRightCol = gp.maxWorldCol - 1;
                
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }     
                break;
        }
    }
    
    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gp.obj[1].length; i++) {
            if(gp.obj[gp.currentMap][i] != null) {
                // Create temporary rectangles - DON'T modify the original solidArea
                Rectangle entitySolid = new Rectangle(
                    entity.worldX + entity.solidArea.x,
                    entity.worldY + entity.solidArea.y,
                    entity.solidArea.width,
                    entity.solidArea.height
                );
                
                Rectangle objectSolid = new Rectangle(
                    gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x,
                    gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y,
                    gp.obj[gp.currentMap][i].solidArea.width,
                    gp.obj[gp.currentMap][i].solidArea.height
                );

                switch (entity.Direction) {
                    case "up": entitySolid.y -= entity.speed; break;
                    case "down": entitySolid.y += entity.speed; break;
                    case "left": entitySolid.x -= entity.speed; break;
                    case "right": entitySolid.x += entity.speed; break;
                }
                
                if(entitySolid.intersects(objectSolid)) {
                    if(gp.obj[gp.currentMap][i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                        break;  // ← CRITICAL: Stop at first collision for player!
                    }
                }
            }
        }
        return index;
    }
    
    public int checkEntity(Entity entity, Entity[][] target) {
        int index = 999;
    
        for (int i = 0; i < target[1].length; i++) {
            if(target[gp.currentMap][i] != null && target[gp.currentMap][i] != entity) {
                // Create temporary rectangles
                Rectangle entitySolid = new Rectangle(
                    entity.worldX + entity.solidArea.x,
                    entity.worldY + entity.solidArea.y,
                    entity.solidArea.width,
                    entity.solidArea.height
                );
                
                Rectangle targetSolid = new Rectangle(
                    target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x,
                    target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y,
                    target[gp.currentMap][i].solidArea.width,
                    target[gp.currentMap][i].solidArea.height
                );
    
                switch (entity.Direction) {
                    case "up": entitySolid.y -= entity.speed; break;
                    case "down": entitySolid.y += entity.speed; break;
                    case "left": entitySolid.x -= entity.speed; break;
                    case "right": entitySolid.x += entity.speed; break;
                }
                
                if(entitySolid.intersects(targetSolid)) {
                    entity.collisionOn = true;
                    index = i;
                    break;  // ← CRITICAL: Stop at first collision!
                }
            }
        }
        return index;
    }
    
    public boolean checkPlayer(Entity entity) {
        // FIX: Add null check at the beginning
        if (gp.player == null) {
            return false; // No player to collide with
        }
        
        // Create temporary rectangles
        Rectangle entitySolid = new Rectangle(
            entity.worldX + entity.solidArea.x,
            entity.worldY + entity.solidArea.y,
            entity.solidArea.width,
            entity.solidArea.height
        );
        
        Rectangle playerSolid = new Rectangle(
            gp.player.worldX + gp.player.solidArea.x,
            gp.player.worldY + gp.player.solidArea.y,
            gp.player.solidArea.width,
            gp.player.solidArea.height
        );

        switch (entity.Direction) { 
            case "up": entitySolid.y -= entity.speed; break;
            case "down": entitySolid.y += entity.speed; break;
            case "left": entitySolid.x -= entity.speed; break;
            case "right": entitySolid.x += entity.speed; break;
        }

        if(entitySolid.intersects(playerSolid)) {
            entity.collisionOn = true;
            return true;
        }
        
        return false;
    }
}