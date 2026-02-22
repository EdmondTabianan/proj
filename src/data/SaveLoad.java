package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import main.GamePanel;
import main.AssetSetter; // Add this import

public class SaveLoad {
    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
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
            
            // Save pickup items status
            if (gp.aSetter != null) {
                ds.itemPickedUp = gp.aSetter.getItemPickedUp();
            }
            
            // Save timestamp
            ds.saveTime = System.currentTimeMillis();

            oos.writeObject(ds);
            oos.close();
            
            System.out.println("Game saved to slot " + (slot + 1));
            
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
            
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));
            DataStorage ds = (DataStorage) ois.readObject();
            ois.close();

            if (gp.player == null) {
                System.out.println("ERROR: Player is null in SaveLoad.load()");
                return;
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
            
            // Load player position and map
            gp.player.worldX = ds.worldX;
            gp.player.worldY = ds.worldY;
            gp.currentMap = ds.currentMap;
            
            // Load pickup items status
            if (gp.aSetter != null && ds.itemPickedUp != null) {
                gp.aSetter.setItemPickedUp(ds.itemPickedUp);
            }
            
            System.out.println("Game loaded from slot " + (slot + 1));
            
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean saveSlotExists(int slot) {
        String filename = "save_slot_" + (slot + 1) + ".dat";
        return new File(filename).exists();
    }
}