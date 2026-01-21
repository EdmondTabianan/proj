package main;

import entity.NPC_blueboy;
import monster.MON_GreenSlime;
import monster.MON_Snake;
import object.OBJ_Axe;
import object.OBJ_Coin_Bronze;
import object.OBJ_Door;
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
        gp.obj[i] = new OBJ_piramid(gp);
        gp.obj[i].worldX = gp.TileSize*15;
        gp.obj[i].worldY = gp.TileSize*24;
        i++;
        gp.obj[i] = new OBJ_Key(gp);
        gp.obj[i].worldX = gp.TileSize*7;
        gp.obj[i].worldY = gp.TileSize*10;
        i++;
        gp.obj[i] = new OBJ_Axe(gp);
        gp.obj[i].worldX = gp.TileSize*36;
        gp.obj[i].worldY = gp.TileSize*6;
        i++;
        gp.obj[i] = new OBJ_boat(gp);
        gp.obj[i].worldX = gp.TileSize*46;
        gp.obj[i].worldY = gp.TileSize*43;
    }
    public void setNPC() {
        // int i = 0;
        // gp.npc[i] = new NPC_blueboy(gp);
        // gp.npc[i].worldX = gp.TileSize*21;
        // gp.npc[i].worldY = gp.TileSize*21;
        // i++;
        // gp.npc[i] = new NPC_blueboy(gp);
        // gp.npc[i].worldX = gp.TileSize*24;
        // gp.npc[i].worldY = gp.TileSize*21;
    }
    public void setMonster() {
        int i = 0;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*18;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*20;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*24;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*25;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_Snake(gp);
        gp.monster[i].worldX = gp.TileSize*30;
        gp.monster[i].worldY = gp.TileSize*23;
        // i++;
        // gp.monster[i] = new MON_GreenSlime(gp);
        // gp.monster[i].worldX = gp.TileSize*35;
        // gp.monster[i].worldY = gp.TileSize*23;
        // i++;
        // gp.monster[i] = new MON_GreenSlime(gp);
        // gp.monster[i].worldX = gp.TileSize*40;
        // gp.monster[i].worldY = gp.TileSize*30;
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