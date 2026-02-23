package main;

import entity.Entity;
import entity.NPC_Beverly;
import entity.NPC_blueboy;
import entity.NPC_merchant;
import entity.NPC_sailor;
import entity.NPC_vhong;
import monster.MON_EarthSlime;
import monster.MON_MOMMY;
import monster.MON_Snake;
import monster.MON_anubis;
import object.OBJ_Axe;
import object.OBJ_Doors;
import object.OBJ_Key;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;
import object.OBJ_boat;
import object.OBJ_bow_normal;
import object.OBJ_torch;
import object.OBJ_display_axe;
import object.OBJ_ice_wand;
import object.OBJ_tablet;
import object.OBJ_display_bow;
import object.OBJ_display_shield;
import object.OBJ_display_sword;
import tile_interactive.IT_Drytree;
import tile_interactive.InteractiveTile;

public class AssetSetter {

    GamePanel gp;
    
    // Track which items have been picked up
    private boolean[][][] itemPickedUp; // [map][index][0] - boolean flag
    
    // Pre-allocate monster configs to avoid recreating lists every time
    private Object[][] baseMonsterConfigs;
    private Object[][] baseObjectConfigs;
    private Object[][] baseNPCConfigs;
    private Object[][] baseTileConfigs;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
        
        // Initialize the pickup tracking array
        itemPickedUp = new boolean[gp.maxMap][20][1]; // 20 is max objects per map
        
        // Initialize base configurations once
        initConfigs();
    }
    
    private void initConfigs() {
        // Base object configurations
        baseObjectConfigs = new Object[][] {
            {0, 46, 43, OBJ_boat.class, true},
            {0, 28, 15, OBJ_Doors.class, false},
            {0, 12, 17, OBJ_bow_normal.class, true},
            {1, 24, 43, OBJ_boat.class, true},
            {1, 10, 8, OBJ_Key.class, true},

            // shop
            {3, 28, 24, OBJ_display_bow.class, false},
            {3, 29, 24, OBJ_display_bow.class, false},
            {3, 30, 24, OBJ_display_bow.class, false},
            {3, 31, 24, OBJ_display_bow.class, false},
            {3, 32, 24, OBJ_display_bow.class, false},

            {3, 28, 26, OBJ_display_sword.class, false},
            {3, 29, 26, OBJ_display_sword.class, false},
            {3, 30, 26, OBJ_display_sword.class, false},
            {3, 31, 26, OBJ_display_sword.class, false},
            {3, 32, 26, OBJ_display_sword.class, false},

            {3, 28, 28, OBJ_display_shield.class, false},
            {3, 29, 28, OBJ_display_shield.class, false},
            {3, 30, 28, OBJ_display_shield.class, false},
            {3, 31, 28, OBJ_display_shield.class, false},
            {3, 32, 28, OBJ_display_shield.class, false},

            {3, 28, 30, OBJ_display_axe.class, false},
            {3, 29, 30, OBJ_display_axe.class, false},
            {3, 30, 30, OBJ_display_axe.class, false},
            {3, 31, 30, OBJ_display_axe.class, false},
            {3, 32, 30, OBJ_display_axe.class, false}

        };
        
        // Base NPC configurations
        baseNPCConfigs = new Object[][] {
            {0, 39, 30, NPC_vhong.class},
            {0, 8, 10, NPC_Beverly.class},
            {0, 45, 42, NPC_sailor.class},
            {3, 20, 20, NPC_merchant.class}
        };
        
        // Base interactive tile configurations
        baseTileConfigs = new Object[][] {
            {0, 10, 24, IT_Drytree.class},
            {0, 17, 12, IT_Drytree.class},
            {0, 17, 13, IT_Drytree.class},
            {0, 18, 17, IT_Drytree.class}
        };
        
        // Base monster configurations (without quest-dependent ones)
        java.util.ArrayList<Object[]> configList = new java.util.ArrayList<>();
        
        // Map 0 - Always add snake
        configList.add(new Object[]{0, 35, 6, MON_Snake.class});
        configList.add(new Object[]{0, 44, 38, OBJ_torch.class});

        // Map 1 monsters
        configList.add(new Object[]{1, 22, 31, MON_Snake.class});
        configList.add(new Object[]{1, 10, 29, MON_Snake.class});
        configList.add(new Object[]{1, 38, 27, MON_Snake.class});
        configList.add(new Object[]{1, 37, 26, MON_Snake.class});
        configList.add(new Object[]{1, 7, 42, MON_Snake.class});
        configList.add(new Object[]{1, 43, 43, MON_Snake.class});
        configList.add(new Object[]{1, 26, 32, MON_Snake.class});
        configList.add(new Object[]{1, 30, 20, MON_Snake.class});
        configList.add(new Object[]{1, 6, 35, MON_Snake.class});
        configList.add(new Object[]{1, 43, 43, MON_Snake.class});
        configList.add(new Object[]{1, 27, 28, MON_Snake.class});
        configList.add(new Object[]{1, 16, 22, MON_Snake.class});
        configList.add(new Object[]{1, 16, 33, MON_EarthSlime.class});
        configList.add(new Object[]{1, 36, 38, MON_EarthSlime.class});
        configList.add(new Object[]{1, 32, 30, MON_EarthSlime.class});
        configList.add(new Object[]{1, 20, 20, MON_EarthSlime.class});
        configList.add(new Object[]{1, 12, 38, MON_EarthSlime.class});
        configList.add(new Object[]{1, 2, 28, MON_EarthSlime.class});
        configList.add(new Object[]{1, 23, 24, MON_EarthSlime.class});
        configList.add(new Object[]{1, 6, 24, MON_EarthSlime.class});
        configList.add(new Object[]{1, 8, 31, MON_EarthSlime.class});
        configList.add(new Object[]{1, 34, 35, MON_EarthSlime.class});
        configList.add(new Object[]{1, 33, 23, MON_EarthSlime.class});
        configList.add(new Object[]{1, 11, 14, MON_EarthSlime.class});
        
        // Map 2 (Pyramid first floor)
        configList.add(new Object[]{2, 46, 44, MON_Snake.class});
        configList.add(new Object[]{2, 31, 25, MON_Snake.class});
        configList.add(new Object[]{2, 39, 31, MON_Snake.class});
        configList.add(new Object[]{2, 46, 3, MON_Snake.class});
        configList.add(new Object[]{2, 39, 43, MON_Snake.class});
        configList.add(new Object[]{2, 41, 27, MON_Snake.class});
        configList.add(new Object[]{2, 34, 10, MON_Snake.class});
        configList.add(new Object[]{2, 25, 16, MON_Snake.class});
        configList.add(new Object[]{2, 46, 32, MON_EarthSlime.class});
        configList.add(new Object[]{2, 39, 2, MON_EarthSlime.class});
        configList.add(new Object[]{2, 22, 27, MON_EarthSlime.class});
        configList.add(new Object[]{2, 30, 43, MON_EarthSlime.class});
        configList.add(new Object[]{2, 36, 19, MON_EarthSlime.class});
        configList.add(new Object[]{2, 44, 6, MON_EarthSlime.class});
        configList.add(new Object[]{2, 18, 14, MON_EarthSlime.class});
        configList.add(new Object[]{2, 24, 36, MON_EarthSlime.class});
        configList.add(new Object[]{2, 30, 35, MON_EarthSlime.class});
        configList.add(new Object[]{2, 42, 15, MON_EarthSlime.class});
        configList.add(new Object[]{2, 10, 2, MON_EarthSlime.class});
        configList.add(new Object[]{2, 14, 39, MON_EarthSlime.class});
        configList.add(new Object[]{2, 31, 15, MON_MOMMY.class});
        configList.add(new Object[]{2, 36, 25, MON_MOMMY.class});
        configList.add(new Object[]{2, 46, 22, MON_MOMMY.class});
        configList.add(new Object[]{2, 2, 2, MON_MOMMY.class});
        configList.add(new Object[]{2, 32, 6, MON_MOMMY.class});
        configList.add(new Object[]{2, 46, 11, MON_MOMMY.class});
        configList.add(new Object[]{2, 12, 27, MON_MOMMY.class});
        configList.add(new Object[]{2, 36, 36, MON_MOMMY.class});
        
        // Map 4 (Pyramid basement)
        configList.add(new Object[]{4, 16, 2, MON_EarthSlime.class});
        configList.add(new Object[]{4, 22, 14, MON_EarthSlime.class});
        configList.add(new Object[]{4, 45, 24, MON_EarthSlime.class});
        configList.add(new Object[]{4, 13, 15, MON_EarthSlime.class});
        configList.add(new Object[]{4, 35, 2, MON_EarthSlime.class});
        configList.add(new Object[]{4, 34, 8, MON_EarthSlime.class});
        configList.add(new Object[]{4, 24, 23, MON_EarthSlime.class});
        configList.add(new Object[]{4, 2, 15, MON_EarthSlime.class});
        configList.add(new Object[]{4, 24, 47, MON_EarthSlime.class});
        configList.add(new Object[]{4, 33, 43, MON_EarthSlime.class});
        configList.add(new Object[]{4, 9, 41, MON_EarthSlime.class});
        configList.add(new Object[]{4, 16, 10, MON_Snake.class});
        configList.add(new Object[]{4, 40, 15, MON_Snake.class});
        configList.add(new Object[]{4, 34, 28, MON_Snake.class});
        configList.add(new Object[]{4, 2, 28, MON_Snake.class});
        configList.add(new Object[]{4, 31, 14, MON_Snake.class});
        configList.add(new Object[]{4, 47, 12, MON_Snake.class});
        configList.add(new Object[]{4, 18, 35, MON_Snake.class});
        configList.add(new Object[]{4, 2, 8, MON_Snake.class});
        configList.add(new Object[]{4, 34, 22, MON_Snake.class});
        configList.add(new Object[]{4, 46, 37, MON_Snake.class});
        configList.add(new Object[]{4, 16, 21, MON_Snake.class});
        configList.add(new Object[]{4, 2, 47, MON_Snake.class});
        configList.add(new Object[]{4, 25, 8, MON_MOMMY.class});
        configList.add(new Object[]{4, 25, 21, MON_MOMMY.class});
        configList.add(new Object[]{4, 8, 11, MON_MOMMY.class});
        configList.add(new Object[]{4, 39, 46, MON_MOMMY.class});
        configList.add(new Object[]{4, 44, 4, MON_MOMMY.class});
        configList.add(new Object[]{4, 18, 41, MON_MOMMY.class});
        configList.add(new Object[]{4, 9, 34, MON_MOMMY.class});
        configList.add(new Object[]{4, 45, 42, MON_MOMMY.class});
        configList.add(new Object[]{4, 38, 20, MON_MOMMY.class});
        configList.add(new Object[]{4, 9, 21, MON_MOMMY.class});
        configList.add(new Object[]{4, 14, 47, MON_MOMMY.class});
        configList.add(new Object[]{4, 33, 36, MON_MOMMY.class});
        
        // last stage 
        configList.add(new Object[]{5, 25, 23, MON_anubis.class});
        
        baseMonsterConfigs = configList.toArray(new Object[0][]);
    }

    public void setObject(int currentMap) {
        int index = 0;
        for (Object[] config : baseObjectConfigs) {
            int map = (int) config[0];
            if (map == currentMap) {
                int x = (int) config[1];
                int y = (int) config[2];
                Class<?> itemClass = (Class<?>) config[3];
                boolean isPickup = config.length > 4 ? (boolean) config[4] : true;
                
                // Skip if already picked up
                if (isPickup && index < 20 && itemPickedUp[map][index][0]) {
                    index++;
                    continue;
                }
                
                try {
                    gp.obj[currentMap][index] = (Entity) itemClass.getConstructor(GamePanel.class).newInstance(gp);
                    gp.obj[currentMap][index].worldX = gp.TileSize * x;
                    gp.obj[currentMap][index].worldY = gp.TileSize * y;
                    
                    if (isPickup) {
                        gp.obj[currentMap][index].isPickup = true;
                    }
                    
                    index++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void markItemAsPickedUp(int map, int index) {
        if (map >= 0 && map < gp.maxMap && index >= 0 && index < 20) {
            itemPickedUp[map][index][0] = true;
        }
    }

    public void resetPickedUpItems(int map) {
        if (map >= 0 && map < gp.maxMap) {
            for (int i = 0; i < 20; i++) {
                itemPickedUp[map][i][0] = false;
            }
        }
    }

    public void resetAllPickedUpItems() {
        for (int map = 0; map < gp.maxMap; map++) {
            for (int i = 0; i < 20; i++) {
                itemPickedUp[map][i][0] = false;
            }
        }
    }

    public void setNPC(int currentMap) {
        int index = 0;
        for (Object[] config : baseNPCConfigs) {
            int map = (int) config[0];
            if (map == currentMap) {
                int x = (int) config[1];
                int y = (int) config[2];
                Class<?> npcClass = (Class<?>) config[3];
                
                try {
                    gp.npc[currentMap][index] = (Entity) npcClass.getConstructor(GamePanel.class).newInstance(gp);
                    gp.npc[currentMap][index].worldX = gp.TileSize * x;
                    gp.npc[currentMap][index].worldY = gp.TileSize * y;
                    index++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setMonster(int currentMap) {
        int index = 0;
        
        // First, add base monsters for this map
        for (Object[] config : baseMonsterConfigs) {
            int map = (int) config[0];
            if (map == currentMap) {
                int x = (int) config[1];
                int y = (int) config[2];
                Class<?> monsterClass = (Class<?>) config[3];
                
                try {
                    createMonster(currentMap, index, x, y, monsterClass);
                    index++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        // Then add quest-dependent monsters
        if (currentMap == 0) {
            // Map 0 - Add quest slimes if needed
            if (gp.questProgress >= 1) {
                addQuestMonsters(currentMap, index, new int[][]{
                    {32, 32}, {30, 30}, {34, 28}
                }, MON_EarthSlime.class);
            }
        }
        else if (currentMap == 6) {
            // Map 6 - Add passage snakes if needed
            if (gp.questProgress >= 2) {
                addQuestMonsters(currentMap, index, new int[][]{
                    {12, 15}, {25, 15}, {12, 7}
                }, MON_Snake.class);
            }
        }
    }
    
    private void addQuestMonsters(int currentMap, int startIndex, int[][] locations, Class<?> monsterClass) {
        int index = startIndex;
        for (int[] loc : locations) {
            try {
                createMonster(currentMap, index, loc[0], loc[1], monsterClass);
                index++;
            } catch (Exception e) {
                System.out.println("Failed to spawn quest monster at " + loc[0] + "," + loc[1]);
            }
        }
    }
    
    private void createMonster(int currentMap, int slot, int x, int y, Class<?> monsterClass) throws Exception {
        if (slot >= gp.monster[currentMap].length) {
            System.out.println("Warning: Monster slot " + slot + " out of bounds");
            return;
        }
        
        Entity monster = (Entity) monsterClass.getConstructor(GamePanel.class).newInstance(gp);
        monster.worldX = gp.TileSize * x;
        monster.worldY = gp.TileSize * y;
        
        // Set spawn point for monsters that need it
        if (monster instanceof MON_EarthSlime) {
            ((MON_EarthSlime) monster).setSpawnPoint(monster.worldX, monster.worldY);
        }
        else if (monster instanceof MON_Snake) {
            ((MON_Snake) monster).setSpawnPoint(monster.worldX, monster.worldY);
        }
        else if (monster instanceof MON_MOMMY) {
            ((MON_MOMMY) monster).setSpawnPoint(monster.worldX, monster.worldY);
        }
        
        gp.monster[currentMap][slot] = monster;
    }

    public void setInteractiveTile(int currentMap) {
        int index = 0;
        for (Object[] config : baseTileConfigs) {
            int map = (int) config[0];
            if (map == currentMap) {
                int x = (int) config[1];
                int y = (int) config[2];
                Class<?> tileClass = (Class<?>) config[3];
                
                try {
                    gp.iTile[currentMap][index] = (InteractiveTile) tileClass
                        .getConstructor(GamePanel.class, int.class, int.class, int.class)
                        .newInstance(gp, 0, x, y);
                    index++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Helper method to clear current map assets
    public void clearMapAssets(int map) {
        // Clear objects (but keep pickup tracking)
        if (gp.obj[map] != null) {
            for (int i = 0; i < gp.obj[map].length; i++) {
                gp.obj[map][i] = null;
            }
        }
        
        // Clear NPCs
        if (gp.npc[map] != null) {
            for (int i = 0; i < gp.npc[map].length; i++) {
                gp.npc[map][i] = null;
            }
        }
        
        // Clear monsters
        if (gp.monster[map] != null) {
            for (int i = 0; i < gp.monster[map].length; i++) {
                gp.monster[map][i] = null;
            }
        }
        
        // Clear interactive tiles
        if (gp.iTile[map] != null) {
            for (int i = 0; i < gp.iTile[map].length; i++) {
                gp.iTile[map][i] = null;
            }
        }
    }
    
    // Despawn methods
    public void despawnMonsters(int map) {
        if (gp.monster[map] != null) {
            for (int i = 0; i < gp.monster[map].length; i++) {
                gp.monster[map][i] = null;
            }
        }
    }

    public void despawnNPCs(int map) {
        if (gp.npc[map] != null) {
            for (int i = 0; i < gp.npc[map].length; i++) {
                gp.npc[map][i] = null;
            }
        }
    }

    public void despawnObjects(int map) {
        if (gp.obj[map] != null) {
            for (int i = 0; i < gp.obj[map].length; i++) {
                gp.obj[map][i] = null;
            }
        }
    }

    public void despawnInteractiveTiles(int map) {
        if (gp.iTile[map] != null) {
            for (int i = 0; i < gp.iTile[map].length; i++) {
                gp.iTile[map][i] = null;
            }
        }
    }

    public boolean[][][] getItemPickedUp() {
        return itemPickedUp;
    }

    public void setItemPickedUp(boolean[][][] loadedData) {
        if (loadedData != null) {
            for (int map = 0; map < Math.min(itemPickedUp.length, loadedData.length); map++) {
                for (int i = 0; i < Math.min(itemPickedUp[map].length, loadedData[map].length); i++) {
                    if (loadedData[map][i] != null && loadedData[map][i].length > 0) {
                        itemPickedUp[map][i][0] = loadedData[map][i][0];
                    }
                }
            }
        }
    }

    public boolean isItemPickedUp(int map, int index) {
        if (map >= 0 && map < itemPickedUp.length && 
            index >= 0 && index < itemPickedUp[map].length) {
            return itemPickedUp[map][index][0];
        }
        return false;
    }
    
    public int getTotalPickedUpCount() {
        int count = 0;
        for (int map = 0; map < itemPickedUp.length; map++) {
            for (int i = 0; i < itemPickedUp[map].length; i++) {
                if (itemPickedUp[map][i][0]) {
                    count++;
                }
            }
        }
        return count;
    }
}