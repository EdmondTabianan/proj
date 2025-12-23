package main;

import entity.NPC_blueboy;
import monster.MON_GreenSlime;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        // gp.obj[0] = new OBJ_Door(gp);
        // gp.obj[0].worldX = gp.TileSize*21;
        // gp.obj[0].worldY = gp.TileSize*22;
    }
    public void setNPC() {
        gp.npc[0] = new NPC_blueboy(gp);
        gp.npc[0].worldX = gp.TileSize*21;
        gp.npc[0].worldY = gp.TileSize*21;
        gp.npc[1] = new NPC_blueboy(gp);
        gp.npc[1].worldX = gp.TileSize*24;
        gp.npc[1].worldY = gp.TileSize*21;
    }
    public void setMonster() {
        gp.monster[0] = new MON_GreenSlime(gp);
        gp.monster[0].worldX = gp.TileSize*22;
        gp.monster[0].worldY = gp.TileSize*23;
        gp.monster[1] = new MON_GreenSlime(gp);
        gp.monster[1].worldX = gp.TileSize*20;
        gp.monster[1].worldY = gp.TileSize*23;
        gp.monster[2] = new MON_GreenSlime(gp);
        gp.monster[2].worldX = gp.TileSize*24;
        gp.monster[2].worldY = gp.TileSize*23;
        gp.monster[3] = new MON_GreenSlime(gp);
        gp.monster[3].worldX = gp.TileSize*25;
        gp.monster[3].worldY = gp.TileSize*23;
    }
}