package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import main.GamePanel;
import entity.Entity;
import object.OBJ_Axe;
import object.OBJ_Sword_Normal;
import object.OBJ_Shield_Wood;
import object.OBJ_ice_wand;
import object.OBJ_bow_normal;
import object.OBJ_Key;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;
import object.OBJ_boat;
import object.OBJ_tablet;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Arrows;
import object.OBJ_Doors;
import object.OBJ_ice;

public class SaveLoad {
    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public Entity getObject(String itemName) {
        Entity obj = null;
        
        if (itemName == null) return null;
        
        // Convert to lowercase for case-insensitive matching
        String lowerName = itemName.toLowerCase();
        
        // Axe - handles "Woodcutter axe", "Axe", etc.
        if (lowerName.contains("woodcutter") || lowerName.contains("axe")) {
            obj = new OBJ_Axe(gp);
            System.out.println("  Created: Axe from '" + itemName + "'");
        }
        // Sword - handles "Normal Sword", "Sword", etc.
        else if (lowerName.contains("sword")) {
            obj = new OBJ_Sword_Normal(gp);
            System.out.println("  Created: Sword from '" + itemName + "'");
        }
        // Shield - handles "Wood Shield", "Shield", etc.
        else if (lowerName.contains("shield")) {
            obj = new OBJ_Shield_Wood(gp);
            System.out.println("  Created: Shield from '" + itemName + "'");
        }
        // Wand - handles "Normal wand", "Wand", etc.
        else if (lowerName.contains("wand")) {
            obj = new OBJ_ice_wand(gp);
            System.out.println("  Created: Wand from '" + itemName + "'");
        }
        // Bow
        else if (lowerName.contains("bow")) {
            obj = new OBJ_bow_normal(gp);
            System.out.println("  Created: Bow from '" + itemName + "'");
        }
        // Key
        else if (lowerName.contains("key")) {
            obj = new OBJ_Key(gp);
            System.out.println("  Created: Key from '" + itemName + "'");
        }
        // Potion - handles both blue and red with any case
        else if (lowerName.contains("potion")) {
            if (lowerName.contains("blue")) {
                obj = new OBJ_Potion_Blue(gp);
                System.out.println("  Created: Blue Potion from '" + itemName + "'");
            } else if (lowerName.contains("red")) {
                obj = new OBJ_Potion_Red(gp);
                System.out.println("  Created: Red Potion from '" + itemName + "'");
            }
        }
        // Tablet - handles "Ancient Tablet", "Tablet", etc.
        else if (lowerName.contains("tablet") || lowerName.contains("ancient")) {
            obj = new OBJ_tablet(gp);
            System.out.println("  Created: Tablet from '" + itemName + "'");
        }
        // Boat
        else if (lowerName.contains("boat")) {
            obj = new OBJ_boat(gp);
            System.out.println("  Created: Boat from '" + itemName + "'");
        }
        // Coin
        else if (lowerName.contains("coin")) {
            obj = new OBJ_Coin_Bronze(gp);
            System.out.println("  Created: Coin from '" + itemName + "'");
        }
        // Heart
        else if (lowerName.contains("heart")) {
            obj = new OBJ_Heart(gp);
            System.out.println("  Created: Heart from '" + itemName + "'");
        }
        // Mana Crystal
        else if (lowerName.contains("mana")) {
            obj = new OBJ_ManaCrystal(gp);
            System.out.println("  Created: Mana Crystal from '" + itemName + "'");
        }
        // Arrows
        else if (lowerName.contains("arrow")) {
            obj = new OBJ_Arrows(gp);
            System.out.println("  Created: Arrows from '" + itemName + "'");
        }
        // Door
        else if (lowerName.contains("door")) {
            obj = new OBJ_Doors(gp);
            System.out.println("  Created: Door from '" + itemName + "'");
        }
        // Ice
        else if (lowerName.contains("ice")) {
            obj = new OBJ_ice(gp);
            System.out.println("  Created: Ice from '" + itemName + "'");
        }
        else {
            System.out.println("  UNKNOWN ITEM TYPE: " + itemName);
        }
        
        return obj;
    }

    public void save(int slot) {
        try {
            String filename = "save_slot_" + (slot + 1) + ".dat";
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));

            DataStorage ds = new DataStorage();

            // Player stats
            ds.level = gp.player.level;
            ds.maxHP = gp.player.maxLife;
            ds.currentHP = gp.player.life;
            ds.maxMana = gp.player.maxMana;
            ds.currentMana = gp.player.mana;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;
            
            // Player position and map
            ds.worldX = gp.player.worldX;
            ds.worldY = gp.player.worldY;
            ds.currentMap = gp.currentMap;
            
            // Save character choice
            ds.characterUsed = gp.player.characterused;
            
            // Save pickup items status
            if (gp.aSetter != null) {
                ds.itemPickedUp = gp.aSetter.getItemPickedUp();
            }
            
            // SAVE INVENTORY ITEMS
            ds.itemNames = new ArrayList<>();
            ds.itemAmounts = new ArrayList<>();
            
            // Reset slot indices
            ds.currentWeaponSlot = -1;
            ds.currentShieldSlot = -1;
            ds.currentRangeSlot = -1;
            
            for (int i = 0; i < gp.player.inventory.size(); i++) {
                Entity item = gp.player.inventory.get(i);
                ds.itemNames.add(item.name);
                ds.itemAmounts.add(item.amount);
                
                // Track equipped items by type
                if (item.type == gp.player.type_sword || item.type == gp.player.type_axe) {
                    ds.currentWeaponSlot = i;
                }
                else if (item.type == gp.player.type_shield) {
                    ds.currentShieldSlot = i;
                }
                else if (item.type == gp.player.type_wand || item.type == gp.player.type_bow) {
                    ds.currentRangeSlot = i;
                }
            }
            
            // Save timestamp
            ds.saveTime = System.currentTimeMillis();

            oos.writeObject(ds);
            oos.close();
            
            System.out.println("Game saved to slot " + (slot + 1) + " with " + ds.itemNames.size() + " items");
            System.out.println("  Weapon slot: " + ds.currentWeaponSlot);
            System.out.println("  Shield slot: " + ds.currentShieldSlot);
            System.out.println("  Range slot: " + ds.currentRangeSlot);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(int slot) {
        try {
            String filename = "save_slot_" + (slot + 1) + ".dat";
            File saveFile = new File(filename);
            
            if (!saveFile.exists()) {
                System.out.println("Save file does not exist: " + filename);
                return;
            }
            
            System.out.println("=== LOADING SLOT " + (slot + 1) + " ===");
            
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));
            DataStorage ds = (DataStorage) ois.readObject();
            ois.close();
    
            if (gp.player == null) {
                System.out.println("ERROR: Player is null in SaveLoad.load()");
                return;
            }
    
            // Load character choice
            if (ds.characterUsed != -1) {
                gp.player.characterused = ds.characterUsed;
                System.out.println("Character choice loaded: " + ds.characterUsed);
            }
    
            // Load player stats
            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxHP;
            gp.player.life = ds.currentHP;
            gp.player.maxMana = ds.maxMana;
            gp.player.mana = ds.currentMana;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.coin = ds.coin;
            
            System.out.println("Stats loaded - Level: " + ds.level + ", HP: " + ds.currentHP + "/" + ds.maxHP);
            
            // Load player position and map
            gp.player.worldX = ds.worldX;
            gp.player.worldY = ds.worldY;
            gp.currentMap = ds.currentMap;
            
            System.out.println("Position loaded - Map: " + ds.currentMap + " at (" + ds.worldX + ", " + ds.worldY + ")");
            
            // Load pickup items status
            if (gp.aSetter != null && ds.itemPickedUp != null) {
                gp.aSetter.setItemPickedUp(ds.itemPickedUp);
                System.out.println("Pickup items status loaded");
            }
            
            // LOAD INVENTORY ITEMS - Clear and recreate
            gp.player.inventory.clear();
            
            // Reset equipped items
            gp.player.currentweapon = null;
            gp.player.currentShield = null;
            gp.player.currentRange = null;
            
            if (ds.itemNames != null && ds.itemNames.size() > 0) {
                System.out.println("Loading " + ds.itemNames.size() + " inventory items...");
                
                // Create and add all items
                int successCount = 0;
                for (int i = 0; i < ds.itemNames.size(); i++) {
                    String itemName = ds.itemNames.get(i);
                    int itemAmount = ds.itemAmounts.get(i);
                    
                    Entity item = getObject(itemName);
                    if (item != null) {
                        item.amount = itemAmount;
                        gp.player.inventory.add(item);
                        successCount++;
                        System.out.println("  [" + i + "] Added: " + itemName + " x" + itemAmount + " (Type: " + item.type + ")");
                    } else {
                        System.out.println("  [" + i + "] FAILED to create: " + itemName);
                    }
                }
                
                System.out.println("Successfully loaded " + successCount + " out of " + ds.itemNames.size() + " items");
                
                // Print all items in inventory with their types
                System.out.println("Items in inventory after loading:");
                for (int i = 0; i < gp.player.inventory.size(); i++) {
                    Entity item = gp.player.inventory.get(i);
                    System.out.println("  Slot " + i + ": " + item.name + " (Type: " + item.type + ")");
                }
                
                // Equip based on saved slot indices
                if (ds.currentWeaponSlot >= 0 && ds.currentWeaponSlot < gp.player.inventory.size()) {
                    gp.player.currentweapon = gp.player.inventory.get(ds.currentWeaponSlot);
                    System.out.println("  Equipped weapon: " + gp.player.currentweapon.name);
                }
                
                if (ds.currentShieldSlot >= 0 && ds.currentShieldSlot < gp.player.inventory.size()) {
                    gp.player.currentShield = gp.player.inventory.get(ds.currentShieldSlot);
                    System.out.println("  Equipped shield: " + gp.player.currentShield.name);
                }
                
                if (ds.currentRangeSlot >= 0 && ds.currentRangeSlot < gp.player.inventory.size()) {
                    gp.player.currentRange = gp.player.inventory.get(ds.currentRangeSlot);
                    System.out.println("  Equipped ranged: " + gp.player.currentRange.name);
                }
                
            } else {
                System.out.println("No inventory items found");
            }
            
            // If inventory is empty, add default items
            if (gp.player.inventory.isEmpty()) {
                gp.player.setItems();
                System.out.println("Default items added");
            }
            
            // Recalculate player stats
            gp.player.attack = gp.player.getAttack();
            gp.player.defense = gp.player.getDefense();
            
            // Refresh player images
            gp.player.getImage();
            gp.player.getAttackImage();
            gp.player.getGuardImage();
            
            System.out.println("Game loaded from slot " + (slot + 1) + " with " + gp.player.inventory.size() + " items");
            System.out.println("  Current weapon: " + (gp.player.currentweapon != null ? gp.player.currentweapon.name : "none"));
            System.out.println("  Current shield: " + (gp.player.currentShield != null ? gp.player.currentShield.name : "none"));
            System.out.println("  Current ranged: " + (gp.player.currentRange != null ? gp.player.currentRange.name : "none"));
            System.out.println("=== LOAD COMPLETE ===");
            
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean saveSlotExists(int slot) {
        String filename = "save_slot_" + (slot + 1) + ".dat";
        return new File(filename).exists();
    }
    
    public String getSaveSlotStatus(int slot) {
        String filename = "save_slot_" + (slot + 1) + ".dat";
        File saveFile = new File(filename);
        
        if (saveFile.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));
                DataStorage ds = (DataStorage) ois.readObject();
                ois.close();
                
                // Format the save info
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd HH:mm");
                String date = sdf.format(new java.util.Date(ds.saveTime));
                
                // Count picked up items
                int pickedUpCount = 0;
                if (ds.itemPickedUp != null) {
                    for (int map = 0; map < ds.itemPickedUp.length; map++) {
                        if (ds.itemPickedUp[map] != null) {
                            for (int i = 0; i < ds.itemPickedUp[map].length; i++) {
                                if (ds.itemPickedUp[map][i] != null && ds.itemPickedUp[map][i][0]) {
                                    pickedUpCount++;
                                }
                            }
                        }
                    }
                }
                
                // Count inventory items
                int inventoryCount = (ds.itemNames != null) ? ds.itemNames.size() : 0;
                
                return "Lv." + ds.level + " - " + date + 
                       " (P:" + pickedUpCount + " items, I:" + inventoryCount + " inv)";
                
            } catch (Exception e) {
                System.out.println("Save file corrupted: " + filename);
                return "CORRUPTED";
            }
        } else {
            return "EMPTY";
        }
    }
}