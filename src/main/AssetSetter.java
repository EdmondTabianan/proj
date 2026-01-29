package main;

import entity.NPC_blueboy;
import entity.NPC_merchant;
import monster.MON_GreenSlime;
import monster.MON_Snake;
import object.OBJ_Axe;
import object.OBJ_Door2;
import object.OBJ_Door3;
import object.OBJ_Doors;
import object.OBJ_Key;
import object.OBJ_boat;
import object.OBJ_bow_normal;
import object.OBJ_tablet;
import tile_interactive.IT_Drytree;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        int mapnum = 0;
        int i = 0;
        gp.obj[mapnum][i] = new OBJ_Key(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*7;
        gp.obj[mapnum][i].worldY = gp.TileSize*10;
        i++;
        // if (gp.player.hasTablet == true) {
        //     gp.obj[mapnum][i] = new OBJ_Axe(gp);
        //     gp.obj[mapnum][i].worldX = gp.TileSize*18;
        //     gp.obj[mapnum][i].worldY = gp.TileSize*6;
        //     i++; 
        // } 
        gp.obj[mapnum][i] = new OBJ_boat(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*46;
        gp.obj[mapnum][i].worldY = gp.TileSize*43;
        i++;
        gp.obj[mapnum][i] = new OBJ_Doors(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*28;
        gp.obj[mapnum][i].worldY = gp.TileSize*16;  
         i++;
        gp.obj[mapnum][i] = new OBJ_Door3(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*29;
        gp.obj[mapnum][i].worldY = gp.TileSize*16;  
        i++;
        gp.obj[mapnum][i] = new OBJ_Door2(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*28;
        gp.obj[mapnum][i].worldY = gp.TileSize*15;   
        i++;
        gp.obj[mapnum][i] = new OBJ_Door2(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*29;
        gp.obj[mapnum][i].worldY = gp.TileSize*15; 
        i++;
        gp.obj[mapnum][i] = new OBJ_tablet(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*10;
        gp.obj[mapnum][i].worldY = gp.TileSize*24;  
        i++;
        gp.obj[mapnum][i] = new OBJ_bow_normal(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*12;
        gp.obj[mapnum][i].worldY = gp.TileSize*17;     
        
        mapnum = 1;
        i++;
        gp.obj[mapnum][i] = new OBJ_boat(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*24;
        gp.obj[mapnum][i].worldY = gp.TileSize*43;
    }
    public void setNPC() {
        int mapnum = 0;
        int i = 0;
        gp.npc[mapnum][i] = new NPC_blueboy(gp);
        gp.npc[mapnum][i].worldX = gp.TileSize*38;
        gp.npc[mapnum][i].worldY = gp.TileSize*30;
        i++;
        gp.npc[mapnum][i] = new NPC_merchant(gp);
        gp.npc[mapnum][i].worldX = gp.TileSize*25;
        gp.npc[mapnum][i].worldY = gp.TileSize*32;
    }
    // public void setMonster() {
    //     int i = 0;
    //     gp.monster[mapnum][i] = new MON_Snake(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*40;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*6; //ok
    //     i++;
    //     gp.monster[mapnum][i] = new MON_Snake(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*26;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*5; // ok
    //     i++;
    //     gp.monster[mapnum][i] = new MON_Snake(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*24;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*20;
    //     i++;
    //     gp.monster[mapnum][i] = new MON_Snake(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*25;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*25;
    //     i++;
    //     gp.monster[mapnum][i] = new MON_Snake(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*21;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*6;
    //      i++;
    //     gp.monster[mapnum][i] = new MON_Snake(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*4;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*14;
    //      i++;
    //     gp.monster[mapnum][i] = new MON_Snake(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*4;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*16;
    //     i++;
    //     gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*33;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*25;
    //     i++;
    //     gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*35;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*18;
    //     i++;
    //     gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*17;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*9;
    //     i++;
    //     gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*6;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*9;
    //     i++;
    //     gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*30;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*31;
    //      i++;
    //     gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    //     gp.monster[mapnum][i].worldX = gp.TileSize*30;
    //     gp.monster[mapnum][i].worldY = gp.TileSize*34;
    // }
    public void setMonster() {

        int mapnum = 0;
        int i = 0;
    
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 40;
        gp.monster[mapnum][i].worldY = gp.TileSize * 6;
        i++;
    
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 26;
        gp.monster[mapnum][i].worldY = gp.TileSize * 5;
        i++;
    
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 24;
        gp.monster[mapnum][i].worldY = gp.TileSize * 20;
        i++;
    
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 25;
        gp.monster[mapnum][i].worldY = gp.TileSize * 25;
        i++;
    
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 21;
        gp.monster[mapnum][i].worldY = gp.TileSize * 6;
        i++;
    
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 4;
        gp.monster[mapnum][i].worldY = gp.TileSize * 14;
        i++;
    
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 4;
        gp.monster[mapnum][i].worldY = gp.TileSize * 16;
        i++;
    
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 33;
        gp.monster[mapnum][i].worldY = gp.TileSize * 25;
        i++;
    
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 35;
        gp.monster[mapnum][i].worldY = gp.TileSize * 18;
        i++;
    
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 17;
        gp.monster[mapnum][i].worldY = gp.TileSize * 9;
        i++;
    
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 6;
        gp.monster[mapnum][i].worldY = gp.TileSize * 9;
        i++;
    
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 30;
        gp.monster[mapnum][i].worldY = gp.TileSize * 31;
        i++;
    
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 30;
        gp.monster[mapnum][i].worldY = gp.TileSize * 34;

        mapnum = 1;
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize * 30;
        gp.monster[mapnum][i].worldY = gp.TileSize * 34;

    }    
    public void setInteractiveTile() {
        
        int mapnum = 0;
        int i = 0;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 5, 22);i++;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 5, 23);i++;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 5, 24);i++;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 6, 24);i++;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 7, 24);i++;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 8, 24);i++;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 9, 24);i++;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 17, 12);i++;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 17, 13);i++;
        gp.iTile[mapnum][i] = new IT_Drytree(gp,0, 18, 17);i++;
    }
}