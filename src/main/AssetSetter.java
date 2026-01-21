package main;

import entity.NPC_blueboy;
import monster.MON_GreenSlime;
import monster.MON_snik;
import object.OBJ_Door;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        gp.obj[0] = new OBJ_Door(gp);
        gp.obj[0].worldX = gp.TileSize*20;
        gp.obj[0].worldY = gp.TileSize*24;
    }
    public void setNPC() {
        int i = 0;
        gp.npc[i] = new NPC_blueboy(gp);
        gp.npc[i].worldX = gp.TileSize*21;
        gp.npc[i].worldY = gp.TileSize*21;
        gp.npc[i] = new NPC_blueboy(gp);
        gp.npc[i].worldX = gp.TileSize*24;
        gp.npc[i].worldY = gp.TileSize*21;
    }
    public void setMonster() {
        int i = 0;
        gp.monster[i] = new MON_snik(gp);
        gp.monster[i].worldX = gp.TileSize*18;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_snik(gp);
        gp.monster[i].worldX = gp.TileSize*20;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_snik(gp);
        gp.monster[i].worldX = gp.TileSize*24;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_snik(gp);
        gp.monster[i].worldX = gp.TileSize*25;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_snik(gp);
        gp.monster[i].worldX = gp.TileSize*30;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_snik(gp);
        gp.monster[i].worldX = gp.TileSize*35;
        gp.monster[i].worldY = gp.TileSize*23;
        i++;
        gp.monster[i] = new MON_snik(gp);
        gp.monster[i].worldX = gp.TileSize*40;
        gp.monster[i].worldY = gp.TileSize*30;
    }
}