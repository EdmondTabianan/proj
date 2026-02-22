package data;

import java.io.Serializable;

public class DataStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Player stats - make these PUBLIC
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
    
    // PICKUP ITEMS STATUS - Make this PUBLIC
    public boolean[][][] itemPickedUp;
}