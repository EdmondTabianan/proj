package main;

import entity.NPC_blueboy;
import monster.MON_GreenSlime;
import object.OBJ_Axe;
import object.OBJ_Coin_Bronze;
import object.OBJ_Door;
import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Blue;
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
        // i++;
        // gp.obj[i] = new OBJ_Key(gp);
        // gp.obj[i].worldX = gp.TileSize*20;
        // gp.obj[i].worldY = gp.TileSize*20;
        i++;
        gp.obj[i] = new OBJ_Shield_Blue(gp);
        gp.obj[i].worldX = gp.TileSize*20;
        gp.obj[i].worldY = gp.TileSize*27;
        i++;
        gp.obj[i] = new OBJ_Axe(gp);
        gp.obj[i].worldX = gp.TileSize*20;
        gp.obj[i].worldY = gp.TileSize*22;
        i++;
        gp.obj[i] = new OBJ_Potion_Red(gp);
        gp.obj[i].worldX = gp.TileSize*22;
        gp.obj[i].worldY = gp.TileSize*22;
        i++;
        gp.obj[i] = new OBJ_Coin_Bronze(gp);
        gp.obj[i].worldX = gp.TileSize*20;
        gp.obj[i].worldY = gp.TileSize*23;
        i++;
        gp.obj[i] = new OBJ_Coin_Bronze(gp);
        gp.obj[i].worldX = gp.TileSize*21;
        gp.obj[i].worldY = gp.TileSize*23;
        i++;
        gp.obj[i] = new OBJ_Coin_Bronze(gp);
        gp.obj[i].worldX = gp.TileSize*22;
        gp.obj[i].worldY = gp.TileSize*23;
        i++;
        gp.obj[i] = new OBJ_Coin_Bronze(gp);
        gp.obj[i].worldX = gp.TileSize*23;
        gp.obj[i].worldY = gp.TileSize*23;
        i++;
        gp.obj[i] = new OBJ_Heart(gp);
        gp.obj[i].worldX = gp.TileSize*20;
        gp.obj[i].worldY = gp.TileSize*18;
        i++;
        gp.obj[i] = new OBJ_ManaCrystal(gp);
        gp.obj[i].worldX = gp.TileSize*21;
        gp.obj[i].worldY = gp.TileSize*19;
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
        // gp.monster[i] = new MON_GreenSlime(gp);
        // gp.monster[i].worldX = gp.TileSize*18;
        // gp.monster[i].worldY = gp.TileSize*23;
        // i++;
        // gp.monster[i] = new MON_GreenSlime(gp);
        // gp.monster[i].worldX = gp.TileSize*20;
        // gp.monster[i].worldY = gp.TileSize*23;
        // i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.TileSize*24;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.TileSize*25;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
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
        gp.iTile[i] = new IT_Drytree(gp, 27, 18);i++;
        gp.iTile[i] = new IT_Drytree(gp, 28, 18);i++;
        gp.iTile[i] = new IT_Drytree(gp, 29, 18);i++;
        gp.iTile[i] = new IT_Drytree(gp, 18, 27);
    }
}