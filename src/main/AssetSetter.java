package main;

import entity.Entity;
import entity.NPC_Beverly;
import entity.NPC_blueboy;
import entity.NPC_merchant;
import entity.NPC_vhong;
import monster.MON_GreenSlime;
import monster.MON_MOMMY;
import monster.MON_Snake;
import object.OBJ_Axe;
import object.OBJ_Doors;
import object.OBJ_Key;
import object.OBJ_Potion_Blue;
import object.OBJ_boat;
import object.OBJ_bow_normal;
import object.OBJ_ice_wand;
import object.OBJ_tablet;
import tile_interactive.IT_Drytree;
import tile_interactive.InteractiveTile;

public class AssetSetter {

    GamePanel gp;
    
    // Track which items have been picked up
    private boolean[][][] itemPickedUp; // [map][index][0] - boolean flag

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
        
        // Initialize the pickup tracking array
        itemPickedUp = new boolean[gp.maxMap][20][1]; // 20 is max objects per map
    }

    public void setObject(int currentMap) {
        // Object configurations: [map, x, y, itemClass, isPickup? (optional)]
        Object[][] objectConfigs = {
            // first map
            {0, 7, 10, OBJ_Key.class, true},           // Key is pickup
            {0, 46, 43, OBJ_boat.class, true},          // Boat is pickup
            {0, 28, 15, OBJ_Doors.class, false},        // Door is NOT pickup (interactable)
            {0, 46, 40, OBJ_Potion_Blue.class, true},   // Potion is pickup
            {0, 47, 40, OBJ_Potion_Blue.class, true},   // Potion is pickup
            {0, 47, 39, OBJ_Potion_Blue.class, true},   // Potion is pickup
            {0, 12, 17, OBJ_bow_normal.class, true},    // Bow is pickup

            {1, 24, 43, OBJ_boat.class, true},          // Boat is pickup
            {1, 10, 8, OBJ_Key.class, true}             // Key is pickup
        };

        int index = 0;
        for (Object[] config : objectConfigs) {
            int map = (int) config[0];
            if (map == currentMap) {
                int x = (int) config[1];
                int y = (int) config[2];
                Class<?> itemClass = (Class<?>) config[3];
                boolean isPickup = config.length > 4 ? (boolean) config[4] : true; // Default to pickup
                
                // Skip if this is a pickup item and it has already been collected
                if (isPickup && itemPickedUp[map][index][0]) {
                    System.out.println("Skipping already collected pickup: " + itemClass.getSimpleName() + " at (" + x + "," + y + ")");
                    index++;
                    continue;
                }
                
                try {
                    gp.obj[currentMap][index] = (Entity) itemClass.getConstructor(GamePanel.class).newInstance(gp);
                    gp.obj[currentMap][index].worldX = gp.TileSize * x;
                    gp.obj[currentMap][index].worldY = gp.TileSize * y;
                    
                    // Mark as pickup item in the entity itself
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
            System.out.println("Marked item as picked up - map: " + map + ", index: " + index);
        }
    }

    // Reset picked up items for a specific map (useful for new game)
    public void resetPickedUpItems(int map) {
        if (map >= 0 && map < gp.maxMap) {
            for (int i = 0; i < 20; i++) {
                itemPickedUp[map][i][0] = false;
            }
            System.out.println("Reset picked up items for map: " + map);
        }
    }

    // Reset all picked up items (for new game)
    public void resetAllPickedUpItems() {
        for (int map = 0; map < gp.maxMap; map++) {
            for (int i = 0; i < 20; i++) {
                itemPickedUp[map][i][0] = false;
            }
        }
        System.out.println("Reset all picked up items");
    }

    public void setNPC(int currentMap) {
        // NPC configurations: [map, x, y, npcClass]
        Object[][] npcConfigs = {
            {0, 39, 30, NPC_vhong.class},
            {0, 8, 10, NPC_Beverly.class},
            {3, 20, 20, NPC_merchant.class}
        };

        int index = 0;
        for (Object[] config : npcConfigs) {
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
        // Monster configurations: [map, x, y, monsterClass]
        Object[][] monsterConfigs = {
            // Map 0
            {0, 32, 32, MON_GreenSlime.class},
            {0, 30, 30, MON_GreenSlime.class},
            {0, 34, 28, MON_GreenSlime.class},
            {0, 35, 6, MON_Snake.class},
            {0, 5, 19, MON_Snake.class},
            
            // Map 1
            {1, 22, 31, MON_Snake.class},
            {1, 10, 29, MON_Snake.class},
            {1, 38, 31, MON_Snake.class},
            {1, 37, 26, MON_Snake.class},
            {1, 7, 42, MON_Snake.class},
            {1, 43, 43, MON_Snake.class},
            {1, 26, 32, MON_Snake.class},
            {1, 30, 19, MON_Snake.class},
            {1, 6, 35, MON_Snake.class},
            {1, 43, 45, MON_Snake.class},
            {1, 28, 28, MON_Snake.class},
            {1, 16, 22, MON_Snake.class},
            {1, 16, 33, MON_GreenSlime.class},
            {1, 36, 38, MON_GreenSlime.class},
            {1, 32, 30, MON_GreenSlime.class},
            {1, 20, 19, MON_GreenSlime.class},
            {1, 12, 38, MON_GreenSlime.class},
            {1, 2, 28, MON_GreenSlime.class},
            {1, 23, 24, MON_GreenSlime.class},
            {1, 6, 24, MON_GreenSlime.class},
            {1, 8, 31, MON_GreenSlime.class},
            {1, 34, 34, MON_GreenSlime.class},
            {1, 33, 23, MON_GreenSlime.class},
            {1, 11, 14, MON_GreenSlime.class},
            
            // Map 2 (Pyramid first floor)
            {2, 46, 44, MON_Snake.class},
            {2, 31, 25, MON_Snake.class},
            {2, 39, 31, MON_Snake.class},
            {2, 46, 3, MON_Snake.class},
            {2, 39, 43, MON_Snake.class},
            {2, 41, 27, MON_Snake.class},
            {2, 34, 10, MON_Snake.class},
            {2, 25, 16, MON_Snake.class},
            {2, 46, 32, MON_GreenSlime.class},
            {2, 39, 2, MON_GreenSlime.class},
            {2, 22, 27, MON_GreenSlime.class},
            {2, 30, 43, MON_GreenSlime.class},
            {2, 36, 19, MON_GreenSlime.class},
            {2, 44, 6, MON_GreenSlime.class},
            {2, 18, 14, MON_GreenSlime.class},
            {2, 24, 36, MON_GreenSlime.class},
            {2, 30, 35, MON_GreenSlime.class},
            {2, 42, 15, MON_GreenSlime.class},
            {2, 10, 2, MON_GreenSlime.class},
            {2, 14, 39, MON_GreenSlime.class},
            {2, 31, 15, MON_MOMMY.class},
            {2, 36, 25, MON_MOMMY.class},
            {2, 46, 22, MON_MOMMY.class},
            {2, 2, 2, MON_MOMMY.class},
            {2, 32, 6, MON_MOMMY.class},
            {2, 46, 11, MON_MOMMY.class},
            {2, 12, 27, MON_MOMMY.class},
            {2, 36, 36, MON_MOMMY.class},
            
            // Map 4 (Pyramid basement)
            {4, 16, 2, MON_GreenSlime.class},
            {4, 22, 14, MON_GreenSlime.class},
            {4, 45, 24, MON_GreenSlime.class},
            {4, 13, 15, MON_GreenSlime.class},
            {4, 35, 2, MON_GreenSlime.class},
            {4, 34, 8, MON_GreenSlime.class},
            {4, 24, 23, MON_GreenSlime.class},
            {4, 2, 15, MON_GreenSlime.class},
            {4, 24, 47, MON_GreenSlime.class},
            {4, 33, 43, MON_GreenSlime.class},
            {4, 9, 41, MON_GreenSlime.class},
            {4, 16, 10, MON_Snake.class},
            {4, 40, 15, MON_Snake.class},
            {4, 34, 28, MON_Snake.class},
            {4, 2, 28, MON_Snake.class},
            {4, 31, 14, MON_Snake.class},
            {4, 47, 12, MON_Snake.class},
            {4, 18, 35, MON_Snake.class},
            {4, 2, 8, MON_Snake.class},
            {4, 34, 22, MON_Snake.class},
            {4, 46, 37, MON_Snake.class},
            {4, 16, 21, MON_Snake.class},
            {4, 2, 47, MON_Snake.class},
            {4, 25, 8, MON_MOMMY.class},
            {4, 25, 21, MON_MOMMY.class},
            {4, 8, 11, MON_MOMMY.class},
            {4, 39, 46, MON_MOMMY.class},
            {4, 44, 4, MON_MOMMY.class},
            {4, 18, 41, MON_MOMMY.class},
            {4, 9, 34, MON_MOMMY.class},
            {4, 45, 42, MON_MOMMY.class},
            {4, 38, 20, MON_MOMMY.class},
            {4, 9, 21, MON_MOMMY.class},
            {4, 14, 47, MON_MOMMY.class},
            {4, 33, 36, MON_MOMMY.class}
        };
    
        int index = 0;
        for (Object[] config : monsterConfigs) {
            int map = (int) config[0];
            if (map == currentMap) {
                int x = (int) config[1];
                int y = (int) config[2];
                Class<?> monsterClass = (Class<?>) config[3];
                
                try {
                    Entity monster = (Entity) monsterClass.getConstructor(GamePanel.class).newInstance(gp);
                    monster.worldX = gp.TileSize * x;
                    monster.worldY = gp.TileSize * y;
                    
                    // Set spawn point for ALL monster types
                    if (monster instanceof MON_GreenSlime) {
                        ((MON_GreenSlime) monster).setSpawnPoint(monster.worldX, monster.worldY);
                    }
                    else if (monster instanceof MON_Snake) {
                        ((MON_Snake) monster).setSpawnPoint(monster.worldX, monster.worldY);
                    }
                    else if (monster instanceof MON_MOMMY) {
                        ((MON_MOMMY) monster).setSpawnPoint(monster.worldX, monster.worldY);
                    }
                    
                    gp.monster[currentMap][index] = monster;
                    index++;
                    
                } catch (Exception e) {
                    System.err.println("Error spawning monster at (" + x + "," + y + "): " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void setInteractiveTile(int currentMap) {
        // Interactive tile configurations: [map, x, y, tileClass]
        Object[][] tileConfigs = {
            {0, 10, 24, IT_Drytree.class},
            {0, 17, 12, IT_Drytree.class},
            {0, 17, 13, IT_Drytree.class},
            {0, 18, 17, IT_Drytree.class}
        };

        int index = 0;
        for (Object[] config : tileConfigs) {
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
                if (gp.monster[map][i] != null) {
                    gp.monster[map][i] = null;
                }
            }
        }
    }

    public void despawnNPCs(int map) {
        if (gp.npc[map] != null) {
            for (int i = 0; i < gp.npc[map].length; i++) {
                if (gp.npc[map][i] != null) {
                    gp.npc[map][i] = null;
                }
            }
        }
    }

    public void despawnObjects(int map) {
        if (gp.obj[map] != null) {
            for (int i = 0; i < gp.obj[map].length; i++) {
                if (gp.obj[map][i] != null) {
                    gp.obj[map][i] = null;
                }
            }
        }
    }

    public void despawnInteractiveTiles(int map) {
        if (gp.iTile[map] != null) {
            for (int i = 0; i < gp.iTile[map].length; i++) {
                if (gp.iTile[map][i] != null) {
                    gp.iTile[map][i] = null;
                }
            }
        }
    }
}