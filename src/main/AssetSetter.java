package main;

import entity.NPC_blueboy;
import entity.NPC_merchant;
import monster.MON_GreenSlime;
import monster.MON_Snake;
import object.OBJ_Axe;
//import object.OBJ_Door_Left;
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
        gp.obj[mapnum][i].worldY = gp.TileSize*15;  
        i++;
        // gp.obj[mapnum][i] = new OBJ_Door_Left(gp);
        // gp.obj[mapnum][i].worldX = gp.TileSize*29;
        // gp.obj[mapnum][i].worldY = gp.TileSize*15; 
        // i++;
        gp.obj[mapnum][i] = new OBJ_bow_normal(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*12;
        gp.obj[mapnum][i].worldY = gp.TileSize*17;     
        
        mapnum = 1;
        i++;
        gp.obj[mapnum][i] = new OBJ_boat(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*24;
        gp.obj[mapnum][i].worldY = gp.TileSize*43;
        i++;
        gp.obj[mapnum][i] = new OBJ_Key(gp);
        gp.obj[mapnum][i].worldX = gp.TileSize*10;
        gp.obj[mapnum][i].worldY = gp.TileSize*8;
    }
    public void setNPC() {
        int mapnum = 0;
        int i = 0;
        gp.npc[mapnum][i] = new NPC_blueboy(gp);
        gp.npc[mapnum][i].worldX = gp.TileSize*38;
        gp.npc[mapnum][i].worldY = gp.TileSize*30;

        mapnum = 3;
        i++;
        gp.npc[mapnum][i] = new NPC_merchant(gp);
        gp.npc[mapnum][i].worldX = gp.TileSize*20;
        gp.npc[mapnum][i].worldY = gp.TileSize*20;
    }
    public void setMonster() {
        int mapnum = 0;
        int i = 0;
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*40;
        gp.monster[mapnum][i].worldY = gp.TileSize*6; //ok
        i++;
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*26;
        gp.monster[mapnum][i].worldY = gp.TileSize*5; // ok
        i++;
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*24;
        gp.monster[mapnum][i].worldY = gp.TileSize*20;
        i++;
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*25;
        gp.monster[mapnum][i].worldY = gp.TileSize*25;
        i++;
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*21;
        gp.monster[mapnum][i].worldY = gp.TileSize*6;
         i++;
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*4;
        gp.monster[mapnum][i].worldY = gp.TileSize*14;
         i++;
        gp.monster[mapnum][i] = new MON_Snake(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*4;
        gp.monster[mapnum][i].worldY = gp.TileSize*16;
        i++;
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*33;
        gp.monster[mapnum][i].worldY = gp.TileSize*25;
        i++;
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*35;
        gp.monster[mapnum][i].worldY = gp.TileSize*18;
        i++;
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*17;
        gp.monster[mapnum][i].worldY = gp.TileSize*9;
        i++;
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*6;
        gp.monster[mapnum][i].worldY = gp.TileSize*9;
        i++;
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*30;
        gp.monster[mapnum][i].worldY = gp.TileSize*31;
         i++;
        gp.monster[mapnum][i] = new MON_GreenSlime(gp);
        gp.monster[mapnum][i].worldX = gp.TileSize*30;
        gp.monster[mapnum][i].worldY = gp.TileSize*34;
        // ========== MAP 1 MONSTERS ==========
    // Map 1 is mostly water/lava with islands. Place monsters on walkable tiles (18, 19, 44)
    mapnum = 1;
    i = 0;
    
    // Top-left island area (around col 10-20, row 10-20)
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*12;  // Walkable area
    gp.monster[mapnum][i].worldY = gp.TileSize*12;
    i++;
    
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*15;
    gp.monster[mapnum][i].worldY = gp.TileSize*15;
    i++;
    
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*18;
    gp.monster[mapnum][i].worldY = gp.TileSize*18;
    i++;
    
    // Bottom-right island area
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*40;
    gp.monster[mapnum][i].worldY = gp.TileSize*35;
    i++;
    
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*42;
    gp.monster[mapnum][i].worldY = gp.TileSize*37;
    i++;
    
    // Middle area
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*30;
    gp.monster[mapnum][i].worldY = gp.TileSize*25;
    
    // ========== MAP 2 MONSTERS ==========
    // Map 2 has more open walkable space (tile 44 = walkable, 32 = solid)
    mapnum = 2;
    i = 0;
    
    // Top area monsters
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*10;
    gp.monster[mapnum][i].worldY = gp.TileSize*5;
    i++;
    
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*15;
    gp.monster[mapnum][i].worldY = gp.TileSize*8;
    i++;
    
    // Left corridor area
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*5;
    gp.monster[mapnum][i].worldY = gp.TileSize*15;
    i++;
    
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*8;
    gp.monster[mapnum][i].worldY = gp.TileSize*18;
    i++;
    
    // Middle maze area
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*20;
    gp.monster[mapnum][i].worldY = gp.TileSize*20;
    i++;
    
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*25;
    gp.monster[mapnum][i].worldY = gp.TileSize*25;
    i++;
    
    // Right area
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*35;
    gp.monster[mapnum][i].worldY = gp.TileSize*15;
    i++;
    
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*38;
    gp.monster[mapnum][i].worldY = gp.TileSize*18;
    i++;
    
    // Bottom area
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*30;
    gp.monster[mapnum][i].worldY = gp.TileSize*35;
    i++;
    
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*33;
    gp.monster[mapnum][i].worldY = gp.TileSize*38;
    
    // ========== MAP 4 MONSTERS ==========
    // Map 4 is similar to map 2 - maze-like structure
    mapnum = 4;
    i = 0;
    
    // Entrance area
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*10;
    gp.monster[mapnum][i].worldY = gp.TileSize*10;
    i++;
    
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*12;
    gp.monster[mapnum][i].worldY = gp.TileSize*12;
    i++;
    
    // Middle corridors
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*20;
    gp.monster[mapnum][i].worldY = gp.TileSize*20;
    i++;
    
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*25;
    gp.monster[mapnum][i].worldY = gp.TileSize*25;
    i++;
    
    // Right side
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*35;
    gp.monster[mapnum][i].worldY = gp.TileSize*15;
    i++;
    
    // Bottom area
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*30;
    gp.monster[mapnum][i].worldY = gp.TileSize*35;
    i++;
    
    gp.monster[mapnum][i] = new MON_GreenSlime(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*32;
    gp.monster[mapnum][i].worldY = gp.TileSize*38;
    i++;
    
    // Secret/hidden area
    gp.monster[mapnum][i] = new MON_Snake(gp);
    gp.monster[mapnum][i].worldX = gp.TileSize*42;
    gp.monster[mapnum][i].worldY = gp.TileSize*42;

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