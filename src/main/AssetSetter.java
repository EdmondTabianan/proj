package main;

import entity.NPC_blueboy;
import monster.MON_GreenSlime;
import monster.MON_Snake;
import object.OBJ_Axe;
import object.OBJ_Coin_Bronze;
import object.OBJ_Door;
import object.OBJ_Door2;
import object.OBJ_Door3;
import object.OBJ_Doors;
import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Blue;
import object.OBJ_boat;
import object.OBJ_piramid;
import tile_interactive.IT_Drytree;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        int i = 0;
        gp.obj[i] = new OBJ_Key(gp);
        gp.obj[i].worldX = gp.TileSize*7;
        gp.obj[i].worldY = gp.TileSize*10;
        i++;
        gp.obj[i] = new OBJ_Axe(gp);
        gp.obj[i].worldX = gp.TileSize*18;
        gp.obj[i].worldY = gp.TileSize*6;
        i++;
        gp.obj[i] = new OBJ_boat(gp);
        gp.obj[i].worldX = gp.TileSize*46;
        gp.obj[i].worldY = gp.TileSize*43;
        i++;
        gp.obj[i] = new OBJ_Doors(gp);
        gp.obj[i].worldX = gp.TileSize*28;
        gp.obj[i].worldY = gp.TileSize*16;  
         i++;
        gp.obj[i] = new OBJ_Door3(gp);
        gp.obj[i].worldX = gp.TileSize*29;
        gp.obj[i].worldY = gp.TileSize*16;  
        i++;
        gp.obj[i] = new OBJ_Door2(gp);
        gp.obj[i].worldX = gp.TileSize*28;
        gp.obj[i].worldY = gp.TileSize*15;   
        i++;
        gp.obj[i] = new OBJ_Door2(gp);
        gp.obj[i].worldX = gp.TileSize*29;
        gp.obj[i].worldY = gp.TileSize*15;    
    }
    public void setNPC() {
        int i = 0;
        gp.npc[i] = new NPC_blueboy(gp);
        gp.npc[i].worldX = gp.TileSize*38;
        gp.npc[i].worldY = gp.TileSize*30;
        i++;
        // gp.npc[i] = new NPC_blueboy(gp);
        // gp.npc[i].worldX = gp.TileSize*24;
        // gp.npc[i].worldY = gp.TileSize*21;
    }
    public void setMonster() {
        int i = 0;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*40;
        gp.monster[i].worldY = gp.TileSize*6; //ok
        i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*26;
        gp.monster[i].worldY = gp.TileSize*5; // ok
        i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*24;
        gp.monster[i].worldY = gp.TileSize*20;
        i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*25;
        gp.monster[i].worldY = gp.TileSize*25;
        i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*21;
        gp.monster[i].worldY = gp.TileSize*6;
         i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*4;
        gp.monster[i].worldY = gp.TileSize*14;
         i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*4;
        gp.monster[i].worldY = gp.TileSize*16;
        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.TileSize*33;
        gp.monster[i].worldY = gp.TileSize*25;
        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.TileSize*35;
        gp.monster[i].worldY = gp.TileSize*18;
        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.TileSize*17;
        gp.monster[i].worldY = gp.TileSize*9;
        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.TileSize*6;
        gp.monster[i].worldY = gp.TileSize*9;
    }
    public void setInteractiveTile() {
        
        int i = 0;
        gp.iTile[i] = new IT_Drytree(gp, 5, 22);i++;
        gp.iTile[i] = new IT_Drytree(gp, 5, 23);i++;
        gp.iTile[i] = new IT_Drytree(gp, 5, 24);i++;
        gp.iTile[i] = new IT_Drytree(gp, 6, 24);i++;
        gp.iTile[i] = new IT_Drytree(gp, 7, 24);i++;
        gp.iTile[i] = new IT_Drytree(gp, 8, 24);i++;
        gp.iTile[i] = new IT_Drytree(gp, 9, 24);i++;
    }
}