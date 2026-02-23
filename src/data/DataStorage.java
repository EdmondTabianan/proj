package data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Player stats
    public int level;
    public int maxHP;
    public int currentHP;
    public int maxMana;
    public int currentMana;
    public int strength;
    public int dexterity;
    public int exp;
    public int nextLevelExp;
    public int coin;
    
    // Player position and map
    public int worldX;
    public int worldY;
    public int currentMap;
    public long saveTime;
    
    // Character choice
    public int characterUsed = -1; // -1 = not set, 0 = Xylo, 1 = Alexandria
    
    // PICKUP ITEMS STATUS
    public boolean[][][] itemPickedUp;
    
    // INVENTORY ITEMS - FIX: Use consistent naming
    public ArrayList<String> itemNames = new ArrayList<>();   
    public ArrayList<Integer> itemAmounts = new ArrayList<>();
    public int currentWeaponSlot = -1;
    public int currentShieldSlot = -1;
    public int currentRangeSlot = -1;
}